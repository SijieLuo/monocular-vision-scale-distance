/**
 * @author Frode Fan
 * @version 1.0
 * @since 2019/9/20
 */
public class FileDTO {
    private String filename;
    private String code;
    private byte[] bytes;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
        this.code = filename.substring(0, 1);
    }

    public String getCode() {
        return code;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
