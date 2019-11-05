import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Frode Fan
 * @version 1.0
 * @since 2019/9/20
 */
public class VoteBack {
    private static final String userDir = System.getProperty("user.dir");
    private static final List<FileDTO> oneResourcesList;
    private static final List<FileDTO> twoResourcesList;
    private static final List<FileDTO> threeResourcesList;
    private static final List<FileDTO> fourResourcesList;

    static {
        File[] oneFiles = new File(userDir + File.separator + "resources" + File.separator + "1").listFiles();
        File[] twoFiles = new File(userDir + File.separator + "resources" + File.separator + "2").listFiles();
        File[] threeFiles = new File(userDir + File.separator + "resources" + File.separator + "3").listFiles();
        File[] fourFiles = new File(userDir + File.separator + "resources" + File.separator + "4").listFiles();
        oneResourcesList = new ArrayList<>(oneFiles.length);
        twoResourcesList = new ArrayList<>(twoFiles.length);
        threeResourcesList = new ArrayList<>(threeFiles.length);
        fourResourcesList = new ArrayList<>(fourFiles.length);
        try {
            for (File file : oneFiles) {
                oneResourcesList.add(FileUtils.getFileDTO(file));
            }
            for (File file : twoFiles) {
                twoResourcesList.add(FileUtils.getFileDTO(file));
            }
            for (File file : threeFiles) {
                threeResourcesList.add(FileUtils.getFileDTO(file));
            }
            for (File file : fourFiles) {
                fourResourcesList.add(FileUtils.getFileDTO(file));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
//        for (int i = 0; i < 100; i++) {
            Website.generateCookie();
            vote();
//            Thread.sleep(5000);
//        }
    }

    public static void vote() throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        String outputDir = userDir + File.separator + "match-log" + File.separator + currentTimeMillis + File.separator;
        byte[] gifBytes = Website.getImageBytes();
        List<byte[]> pngBytesList = Gif.getPngBytesListFromGif(gifBytes);
        FileUtils.saveToJpg(pngBytesList.get(1), outputDir + "code.png");
        byte[][] cropBytesArr = new byte[4][];
        for (int i = 0; i < pngBytesList.size(); i++) {
            switch (i) {
                case 1:
                    cropBytesArr[3] = Crop.cropJpg(pngBytesList.get(i), 125, 6, 20, 22);
                    break;
                case 2:
                    cropBytesArr[2] = Crop.cropJpg(pngBytesList.get(i), 90, 6, 20, 22);
                    break;
                case 3:
                    cropBytesArr[0] = Crop.cropJpg(pngBytesList.get(i), 20, 6, 20, 22);
                    cropBytesArr[1] = Crop.cropJpg(pngBytesList.get(i), 55, 6, 20, 22);
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
            switch (i) {
                case 0:
                    for (int j = 0; j < oneResourcesList.size(); j++) {
                        matchResult = ImageMatch.match(pngBytes, oneResourcesList.get(j).getBytes());
                        if (matchResult > max) {
                            max = matchResult;
                            result = j;
                        }
                    }
                    FileUtils.saveToJpg(pngBytes, outputDir + "1.png");
                    if (result == -1) {
                        System.out.println("匹配失败");
                        //保存错误文件
                    }
                    System.out.println("匹配结果：" + oneResourcesList.get(result).getCode());
                    stringBuilder.append(oneResourcesList.get(result).getCode());
                    break;
                case 1:
                    for (int j = 0; j < twoResourcesList.size(); j++) {
                        matchResult = ImageMatch.match(pngBytes, twoResourcesList.get(j).getBytes());
                        if (matchResult > max) {
                            max = matchResult;
                            result = j;
                        }
                    }
                    FileUtils.saveToJpg(pngBytes, outputDir + "2.png");
                    if (result == -1) {
                        System.out.println("匹配失败");
                        //保存错误文件
                    }
                    System.out.println("匹配结果：" + twoResourcesList.get(result).getCode());
                    stringBuilder.append(twoResourcesList.get(result).getCode());
                    break;
                case 2:
                    for (int j = 0; j < threeResourcesList.size(); j++) {
                        matchResult = ImageMatch.match(pngBytes, threeResourcesList.get(j).getBytes());
                        if (matchResult > max) {
                            max = matchResult;
                            result = j;
                        }
                    }
                    FileUtils.saveToJpg(pngBytes, outputDir + "3.png");
                    if (result == -1) {
                        System.out.println("匹配失败");
                        //保存错误文件
                    }
                    System.out.println("匹配结果：" + threeResourcesList.get(result).getCode());
                    stringBuilder.append(threeResourcesList.get(result).getCode());
                    break;
                case 3:
                    for (int j = 0; j < fourResourcesList.size(); j++) {
                        matchResult = ImageMatch.match(pngBytes, fourResourcesList.get(j).getBytes());
                        if (matchResult > max) {
                            max = matchResult;
                            result = j;
                        }
                    }
                    FileUtils.saveToJpg(pngBytes, outputDir + "4.png");
                    if (result == -1) {
                        System.out.println("匹配失败");
                        //保存错误文件
                    }
                    System.out.println("匹配结果：" + fourResourcesList.get(result).getCode());
                    stringBuilder.append(fourResourcesList.get(result).getCode());
                    break;
                default:
                    ;
            }
        }
        FileUtils.saveToFile(stringBuilder.toString(), outputDir + "code.txt");
//        Website.vote(stringBuilder.toString());
    }
}
