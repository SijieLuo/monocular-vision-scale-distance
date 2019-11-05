import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Frode Fan
 * @version 1.0
 * @since 2019/9/20
 */
public class FileUtils {
    public static byte[] toBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            return bos.toByteArray();
        }
    }

    public static byte[] toBytes(InputStream inputStream) throws IOException {
        try (InputStream in = inputStream) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            return bos.toByteArray();
        }
    }

    public static FileDTO getFileDTO(File file) throws IOException {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFilename(file.getName());
        fileDTO.setBytes(FileUtils.toBytes(file));
        return fileDTO;
    }

    public static FileDTO getFileDTO(String filename, InputStream inputStream) throws IOException {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFilename(filename);
        fileDTO.setBytes(FileUtils.toBytes(inputStream));
        return fileDTO;
    }

    public static void saveToJpg(byte[] source, String target) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(source));
        File file = new File(target);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        ImageIO.write(bufferedImage, "png", file);
    }

    public static void saveToFile(String content, String target) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(target)) {
            fileOutputStream.write(content.getBytes());
        }
    }
}
