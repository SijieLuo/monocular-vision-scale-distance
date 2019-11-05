import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Frode Fan
 * @version 1.0
 * @since 2019/9/20
 */
public class Main {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 100; i++) {
            processOneImageCode();
        }
    }

    public static void processOneImageCode() throws IOException {
        //分割gif为jpg
        List<String> paths = Gif.getGifOneFrame(Website.get());

        //切割jpg图片
        List<String> crops = new ArrayList<>();
        String path;
        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < paths.size(); i++) {
            path = paths.get(i);
            String p = path.substring(0, path.lastIndexOf("\\"));
            switch (i) {
                case 0:
                    break;
                case 1:
                    crops.add(p + "\\4\\" + currentTimeMillis + "crop" + (i + 1) + ".png");
                    Crop.cropImg(path, "png", 128, 6, 20, 22, "png", true, p + "\\4\\" + currentTimeMillis + "crop" + (i + 1) + ".png");
                    break;
                case 2:
                    crops.add(p + "\\3\\" + currentTimeMillis + "crop" + (i + 1) + ".png");
                    Crop.cropImg(path, "png", 92, 6, 20, 22, "png", true, p + "\\3\\" + currentTimeMillis + "crop" + (i + 1) + ".png");
                    break;
                case 3:
                    crops.add(p + "\\1\\" + currentTimeMillis + "crop" + (i + 1) + ".png");
                    crops.add(p + "\\2\\" + currentTimeMillis + "crop" + (i + 1) + ".png");
                    Crop.cropImg(path, "png", 20, 6, 20, 22, "png", true, p + "\\1\\" + currentTimeMillis + "crop" + (i + 1) + ".png");
                    Crop.cropImg(path, "png", 56, 6, 20, 22, "png", true, p + "\\2\\" + currentTimeMillis + "crop" + (i + 1) + ".png");
                    break;
                default:
                    ;
            }
//            crops.add(p + "\\1\\crop" + (i + 1) + ".png");
//            crops.add(p + "\\2\\crop" + (i + 1) + ".png");
//            crops.add(p + "\\3\\crop" + (i + 1) + ".png");
//            crops.add(p + "\\4\\crop" + (i + 1) + ".png");
//            Crop.cropImg(path, "png", 20, 6, 20, 18, "png", true, p + "\\1\\crop" + (i + 1) + ".png");
//            Crop.cropImg(path, "png", 55, 6, 20, 18, "png", true, p + "\\2\\crop" + (i + 1) + ".png");
//            Crop.cropImg(path, "png", 90, 6, 20, 18, "png", true, p + "\\3\\crop" + (i + 1) + ".png");
//            Crop.cropImg(path, "png", 125, 6, 21, 18, "png", true, p + "\\4\\crop" + (i + 1) + ".png");
        }

        //灰化图片
        List<String> grays = new ArrayList<>();
        for (String crop : crops) {
            grays.add(ImageUtils.grayImage(crop));
        }

//        File file = new File("D:\\resources\\1");
//        File[] files = file.listFiles();
//        List<Double> list = new ArrayList<>();
//        for (String gray : grays) {
//            double max = 0d;
//            double matchResult;
//            int result = 0;
//            for (int i = 0; i < files.length; i++) {
//                matchResult = ImageMatch.match(gray, files[i].getPath());
//                list.add(matchResult);
//                if (matchResult > max) {
//                    max = matchResult;
//                    result = i;
//                }
//            }
//            System.out.println(gray);
//            System.out.println(files[result].getName() + ":" + list.get(result));
//        }

        //二值化图片
        for (String gray : grays) {
            ImageUtils.binaryImage(gray);
        }
    }
}
