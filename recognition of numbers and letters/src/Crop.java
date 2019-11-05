import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author Frode Fan
 * @version 1.0
 * @since 2019/9/19
 */
public class Crop {
    public static void main(String[] args) {
        cropImg("d:\\1.png", "png", 20, 6, 20, 18, "png", true, "d:\\crop1.png");
        cropImg("d:\\1.png", "png", 55, 6, 20, 18, "png", true, "d:\\crop2.png");
        cropImg("d:\\1.png", "png", 90, 6, 20, 18, "png", true, "d:\\crop3.png");
        cropImg("d:\\1.png", "png", 125, 6, 21, 18, "png", true, "d:\\crop4.png");
    }

    /**
     * 裁剪图片
     *
     * @param srcPath          原始图片路径
     * @param readImageFormat  读取图片的格式
     * @param x                裁剪的x坐标
     * @param y                裁剪的y坐标
     * @param width            裁剪后图片宽度
     * @param height           裁剪后图片高度
     * @param writeImageFormat 保存裁剪后图片的格式
     * @param isSave           是否保存裁剪后的图片到本地[不保存会返回裁剪后图片的字节数组]
     * @param toPath           裁剪后的图片保存路径
     * @return byte[] 如果图片不保存在本地,则返回裁剪后图片的字节数组
     */
    public static byte[] cropImg(String srcPath, String readImageFormat, int x, int y,
                                 int width, int height, String writeImageFormat, boolean isSave, String toPath) {
        FileInputStream fis = null;
        ImageInputStream iis = null;
        try {
            //读取图片文件
            fis = new FileInputStream(srcPath);
            Iterator it = ImageIO.getImageReadersByFormatName(readImageFormat);
            ImageReader reader = (ImageReader) it.next();
            //获取图片流
            iis = ImageIO.createImageInputStream(fis);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            //定义一个矩形
            Rectangle rect = new Rectangle(x, y, width, height);
            //提供一个 BufferedImage，将其用作解码像素数据的目标。
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            if (bi.getWidth() < width) {
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(bi.getWidth(), 0, 2, height);
                g.drawImage(bi, 0, 0, bi.getWidth(), height, null);
                bi = tag;
            }
            if (isSave) {
                //保存新图片
                File file = new File(toPath);
                file.getParentFile().mkdirs();
                ImageIO.write(bi, writeImageFormat, file);
                return null;
            } else {
                //返回字节数组
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);
                ImageIO.write(bi, writeImageFormat, byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] cropJpg(byte[] bytes, int x, int y, int width, int height) throws IOException {
        Iterator it = ImageIO.getImageReadersByFormatName("png");
        ImageReader reader = (ImageReader) it.next();
        //获取图片流
        ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes));
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        //定义一个矩形
        Rectangle rect = new Rectangle(x, y, width, height);
        //提供一个 BufferedImage，将其用作解码像素数据的目标。
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        if (bi.getWidth() < width) {
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(bi.getWidth(), 0, 2, height);
            g.drawImage(bi, 0, 0, bi.getWidth(), height, null);
            bi = tag;
        }
        //返回字节数组
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);
        ImageIO.write(bi, "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
