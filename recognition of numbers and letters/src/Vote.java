import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 投票
 *
 * @author Frode Fan
 * @version 1.0
 * @since 2019/9/20
 */
public class Vote {
    private static final String userDir = System.getProperty("user.dir");
    private static final List<FileDTO> resourcesList;

    static {
        resourcesList = new ArrayList<>();
        //判断是否运行jar
        URL url = Vote.class.getProtectionDomain().getCodeSource().getLocation();
        if (url.getPath().endsWith(".jar")) {
            try {
                JarFile jarFile = new JarFile(url.getFile());
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    if (jarEntry.getName().startsWith("resources") && jarEntry.getName().endsWith(".png")) {
                        // jar内的文件只能通过流处理
                        InputStream inputStream = Vote.class.getClassLoader().getResourceAsStream(jarEntry.getName());
                        resourcesList.add(FileUtils.getFileDTO(jarEntry.getName().substring(jarEntry.getName().lastIndexOf("/") + 1)
                                , inputStream));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File[] files = new File("/resources").listFiles();
            try {
                for (File file : files) {
                    resourcesList.add(FileUtils.getFileDTO(file));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        boolean isLogFail = args.length > 0 ? Boolean.valueOf(args[0]) : false;
        for (int i = 0; i < 1000; i++) {
//            Website.generateCookie();
            try {
                if (vote(isLogFail)) {
                    rest(2, 5);
                } else {
                    rest(1, 2);
                }
            } catch (Exception e) {
                e.printStackTrace();
                rest(1, 2);
            }
        }
    }

    public static boolean vote(boolean isLogFail) throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(String.format("当前时间：%s", simpleDateFormat.format(currentTimeMillis)));

        final String outputDir = userDir + File.separator + "match-log" + File.separator + currentTimeMillis + File.separator;
        byte[] gifBytes = Website.getImageBytes();
        List<byte[]> pngBytesList = Gif.getPngBytesListFromGif(gifBytes);
        byte[][] cropBytesArr = new byte[4][];
        for (int i = 0; i < pngBytesList.size(); i++) {
            switch (i) {
                case 1:
                    cropBytesArr[3] = Crop.cropJpg(pngBytesList.get(i), 128, 6, 20, 22);
                    break;
                case 2:
                    cropBytesArr[2] = Crop.cropJpg(pngBytesList.get(i), 92, 6, 20, 22);
                    break;
                case 3:
                    cropBytesArr[0] = Crop.cropJpg(pngBytesList.get(i), 20, 6, 20, 22);
                    cropBytesArr[1] = Crop.cropJpg(pngBytesList.get(i), 56, 6, 20, 22);
                    break;
                default:
                    ;
            }
        }
        List<byte[]> grayPngBytesList = new ArrayList<>(4);
        for (byte[] cropBytes : cropBytesArr) {
            grayPngBytesList.add(ImageUtils.grayImage(cropBytes));
        }

        byte[] pngBytes;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < grayPngBytesList.size(); i++) {
            pngBytes = grayPngBytesList.get(i);
            double max = 0d;
            double matchResult;
            int result = -1;
            for (int j = 0; j < resourcesList.size(); j++) {
                matchResult = ImageUtils.match(pngBytes, resourcesList.get(j).getBytes());
                if (matchResult > max) {
                    max = matchResult;
                    result = j;
                    if (matchResult == 1) {
                        break;
                    }
                }
            }
            if (result == -1) {
                System.out.println("匹配失败");
                isLogFail = true;
                break;
            }
            System.out.println("匹配结果：" + resourcesList.get(result).getCode() + "  --" + max);
            stringBuilder.append(resourcesList.get(result).getCode());
        }
        boolean isSuccess = false;
        if (stringBuilder.length() == 4) {
            System.out.print("开始投票...");
            String voteResult = Website.vote(stringBuilder.toString());
            isSuccess = "投票成功".equals(voteResult);
            System.out.println(voteResult);
            if (isLogFail) {
                isLogFail = "验证码输入错误".equals(voteResult);
            }
        }
        if (isLogFail) {
            FileUtils.saveToJpg(pngBytesList.get(1), outputDir + "code.png");
            FileUtils.saveToFile(stringBuilder.toString(), outputDir + "code.txt");
            for (int i = 0; i < grayPngBytesList.size(); i++) {
                FileUtils.saveToJpg(grayPngBytesList.get(i), outputDir + (i + 1) + ".png");
            }
        }
        return isSuccess;
    }

    private static void rest(int start, int end) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                str.append(new Random().nextInt(end - start) + start);
            } else {
                str.append(new Random().nextInt(10));
            }
        }
        System.out.println(String.format("休息：%s", str));
        System.out.println("");
        try {
            Thread.sleep(Integer.valueOf(str.toString()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
