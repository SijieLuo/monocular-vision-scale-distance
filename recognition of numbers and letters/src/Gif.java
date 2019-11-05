import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;
import com.sun.imageio.plugins.png.PNGImageWriter;
import com.sun.imageio.plugins.png.PNGImageWriterSpi;

import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Frode Fan
 * @version 1.0
 * @since 2019/9/19
 */
public class Gif {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 4; i++) {
            getGifOneFrame("d:\\imgCode.gif", "d:\\" + i + ".png", i);
        }
    }

    public static void getGifOneFrame(String src, String target, int frame) throws IOException {
        FileImageInputStream in = null;
        FileImageOutputStream out = null;
        try {
            in = new FileImageInputStream(new File(src));
            ImageReaderSpi readerSpi = new GIFImageReaderSpi();
            GIFImageReader gifReader = (GIFImageReader) readerSpi.createReaderInstance();
            gifReader.setInput(in);
            int num = gifReader.getNumImages(true);
// 要取的帧数要小于总帧数
            if (num > frame) {
                ImageWriterSpi writerSpi = new PNGImageWriterSpi();
                PNGImageWriter writer = (PNGImageWriter) writerSpi.createWriterInstance();

                for (int i = 0; i < num; i++) {
                    if (i == frame) {
                        File newfile = new File(target);
                        out = new FileImageOutputStream(newfile);
                        writer.setOutput(out);
//    读取读取帧的图片
                        writer.write(gifReader.read(i));
                        return;
                    }
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }

    }

    public static List<String> getGifOneFrame(String src) throws IOException {
        FileImageInputStream in = null;
        FileImageOutputStream out = null;
        try {
            in = new FileImageInputStream(new File(src));
            ImageReaderSpi readerSpi = new GIFImageReaderSpi();
            GIFImageReader gifReader = (GIFImageReader) readerSpi.createReaderInstance();
            gifReader.setInput(in);
            int num = gifReader.getNumImages(true);
// 要取的帧数要小于总帧数
            ImageWriterSpi writerSpi = new PNGImageWriterSpi();
            PNGImageWriter writer = (PNGImageWriter) writerSpi.createWriterInstance();
            String prefix = src.substring(0, src.lastIndexOf("."));
            List<String> paths = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                File newfile = new File(prefix + "\\" + num + "crop-" + i + ".png");
                newfile.getParentFile().mkdir();
                paths.add(newfile.getPath());
                out = new FileImageOutputStream(newfile);
                writer.setOutput(out);
//    读取读取帧的图片
                writer.write(gifReader.read(i));
            }
            return paths;
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    public static List<byte[]> getPngBytesListFromGif(byte[] gifBytes) throws IOException {
        ImageReaderSpi readerSpi = new GIFImageReaderSpi();
        GIFImageReader gifReader = (GIFImageReader) readerSpi.createReaderInstance();
        gifReader.setInput(new MemoryCacheImageInputStream(new ByteArrayInputStream(gifBytes)));
        int num = gifReader.getNumImages(true);
        // 要取的帧数要小于总帧数
        ImageWriterSpi writerSpi = new PNGImageWriterSpi();
        PNGImageWriter writer = (PNGImageWriter) writerSpi.createWriterInstance();
        List<byte[]> jpgList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            writer.setOutput(new MemoryCacheImageOutputStream(byteArrayOutputStream));
            //    读取读取帧的图片
            writer.write(gifReader.read(i));
            jpgList.add(byteArrayOutputStream.toByteArray());
        }
        return jpgList;
    }

}
