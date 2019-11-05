import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageUtils {

    public static String binaryImage(String src) throws IOException {
        File file = new File(src);
        BufferedImage image = ImageIO.read(file);

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_BINARY
        Set<Integer> s = new HashSet<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                s.add(rgb);
                grayImage.setRGB(i, j, rgb);
            }
        }//9276814
        System.out.println(s);
        File newFile = new File(src + "-binary.png");
        ImageIO.write(grayImage, "png", newFile);
        return newFile.getPath();
    }

    public static byte[] binaryImage(byte[] bytes) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_BINARY
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                grayImage.setRGB(i, j, rgb);
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(grayImage, "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static String grayImage(String src) throws IOException {
        File file = new File(src);
        BufferedImage image = ImageIO.read(file);

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_GRAY
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                grayImage.setRGB(i, j, rgb);
            }
        }
        File newFile = new File(src + "-gray.png");
        ImageIO.write(grayImage, "png", newFile);
        return newFile.getPath();
    }

    public static byte[] grayImage(byte[] bytes) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_GRAY
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                grayImage.setRGB(i, j, rgb);
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(grayImage, "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static double match(byte[] source, byte[] target) throws IOException {
        BufferedImage sourceImage = ImageIO.read(new ByteArrayInputStream(source));
        BufferedImage targetImage = ImageIO.read(new ByteArrayInputStream(target));
        return match(sourceImage, targetImage);
    }

    public static double match(String source, String target) throws IOException {
        BufferedImage sourceImage = ImageIO.read(new File(source));
        BufferedImage targetImage = ImageIO.read(new File(target));
        return match(sourceImage, targetImage);
    }

    private static double match(BufferedImage source, BufferedImage target) {
        int width = source.getWidth();
        int height = target.getHeight();

        int matchCounter = 0;
        int total = width * height;
        int rgb1;
        int rgb2;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rgb1 = source.getRGB(i, j);
                rgb2 = target.getRGB(i, j);
                if (rgb1 == rgb2 || (rgb1 != -1 && rgb2 != -1)) {
                    matchCounter++;
                }
            }
        }
        return matchCounter * 1.0 / total;
    }

    public static void main(String[] args) throws IOException {
        if (true) {
            double match = ImageUtils.match("d:\\3.png", "D:\\resources\\b.png");
//            double match = ImageUtils.match("d:\\3.png", "D:\\resources\\d (2).png");
            System.out.println("match = " + match);
            return;
        }
        File[] files = new File("d:\\resources").listFiles();
        List<FileDTO> resourcesList = new ArrayList<>(files.length);
        for (File file : files) {
            if (file.isFile())
                resourcesList.add(FileUtils.getFileDTO(file));
        }

        double max = 0d;
        double matchResult;
        int result = -1;
        for (int j = 0; j < resourcesList.size(); j++) {
            matchResult = ImageUtils.match(FileUtils.toBytes(new File("d:\\3.png")), resourcesList.get(j).getBytes());
            if (matchResult > max) {
                max = matchResult;
                result = j;
            }
        }
        System.out.println("匹配结果：" + resourcesList.get(result).getCode() + "          --" + max);
    }
}