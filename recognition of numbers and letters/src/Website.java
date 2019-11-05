import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.UUID;

/**
 * @author Frode Fan
 * @version 1.0
 * @since 2019/9/20
 */
public class Website {
    private static String cookie = "57838640";

    public static void main(String[] args) throws IOException {
        get();
    }

    public static String get() throws IOException {
        InputStream in = null;
        BufferedOutputStream out = null;
        String uri = "http://dxpvote.svccloud.cn/vote/get/imgCode";
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
            con.setRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate");
            con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("Cookie", "insert_cookie=57838640");
            con.setRequestProperty("Host", "dxpvote.svccloud.cn");
            con.setRequestProperty("Referer", "http://vweb.svccloud.cn/");
            con.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            con.connect();// 获取连接
            in = con.getInputStream();
            String filename = "d:\\imageTotal\\imageCode.gif";
            File file = new File(filename);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            in = new BufferedInputStream(in);
            out = new BufferedOutputStream(fileOutputStream);
            int len;
            byte[] b = new byte[1024];
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }

//            url = new URL("http://dxpvote.svccloud.cn/vote/save?choice_id=19&poll_id=2&uuid="+ UUID.randomUUID() +"&img_code=n44m");
//            con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("POST");
//            con.setRequestProperty("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
//            con.setRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
//            con.setRequestProperty("Accept-Encoding", "gzip, deflate");
//            con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
//            con.setRequestProperty("Connection", "keep-alive");
//            con.setRequestProperty("Cookie", "insert_cookie=57838640");
//            con.setRequestProperty("Host", "dxpvote.svccloud.cn");
//            con.setRequestProperty("Referer", "http://vweb.svccloud.cn/");
//            con.setRequestProperty("User-Agent",
//                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//            con.connect();// 获取连接
//
//            in = con.getInputStream();
//            byte[] buf = new byte[1024];
//
//            String str = "";
//            while (in.read(buf) != -1) {
//                str += new String(buf);
//            }
//            System.out.println(str);

            return filename;
        } finally {
            if (out != null) {
                out.close();
            }
            if (null != in)
                in.close();
        }
    }

    /**
     * 返回图片byte数组
     *
     * @return
     * @throws IOException
     */
    public static byte[] getImageBytes() throws IOException {
        System.out.println("获取验证码");
        String uri = "http://vweb.svccloud.cn/#/mainIndex";
        URL url = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "*/*;");
        con.setRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
        con.setRequestProperty("Accept-Encoding", "gzip, deflate");
        con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
        con.setRequestProperty("Connection", "keep-alive");
        con.setRequestProperty("Host", "dxpvote.svccloud.cn");
        con.setRequestProperty("Referer", "http://vweb.svccloud.cn/");
        con.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        con.connect();// 获取连接
        while (new BufferedInputStream(con.getInputStream()).read(new byte[1024]) != -1) {
        }
        con.disconnect();

        uri = "http://dxpvote.svccloud.cn/vote/get/imgCode";
        url = new URL(uri);
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
        con.setRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
        con.setRequestProperty("Accept-Encoding", "gzip, deflate");
        con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
        con.setRequestProperty("Connection", "keep-alive");
//        con.setRequestProperty("Cookie", String.format("insert_cookie=%s", cookie));
        con.setRequestProperty("Host", "dxpvote.svccloud.cn");
        con.setRequestProperty("Referer", "http://vweb.svccloud.cn/");
        con.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        con.connect();// 获取连接
        try (InputStream in = new BufferedInputStream(con.getInputStream())) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len;
            byte[] b = new byte[1024];
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            return out.toByteArray();
        }
    }

    public static String vote(String code) {
        //全国
//        String uri = "http://dxpvote.svccloud.cn/vote/save?choice_id=36&poll_id=2&uuid=" + UUID.randomUUID() + "&img_code=" + code;
        //福建
        String uri = "http://dxpvote.svccloud.cn/vote/save?choice_id=19&poll_id=2&uuid=" + UUID.randomUUID() + "&img_code=" + code;
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept", "application/json, text/plain, */*");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate");
            con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("Content-Length", "0");
//        con.setRequestProperty("Cookie", String.format("insert_cookie=%s", cookie));
            con.setRequestProperty("Host", "dxpvote.svccloud.cn");
            con.setRequestProperty("Origin", "http://vweb.svccloud.cn");
            con.setRequestProperty("Referer", "http://vweb.svccloud.cn/");
            con.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            con.connect();// 获取连接
            try (InputStream in = con.getInputStream()) {
                // 1.创建内存输出流,将读到的数据写到内存输出流中
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                // 2.创建字节数组
                byte[] arr = new byte[1024];
                int len;
                while (-1 != (len = in.read(arr))) {
                    bos.write(arr, 0, len);
                }
                // 3.将内存输出流的数据全部转换为字符串
                String s = bos.toString("utf-8");
                System.out.println("s = " + s);
    //            System.out.println(cookie);
                if (s.contains("验证码输入错误")) {
                    return "验证码输入错误";
                } else if (s.contains("验证码已失效")) {
                    return "验证码已失效";
                } else if (s.contains("false")) {
                    return String.format("投票失败：%s", s);
                } else {
                    return "投票成功";
                }
            }
        } catch (IOException e) {
            return "投票失败：服务器异常";
        }
    }

    public synchronized static void generateCookie() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (i == 0) {
                str.append(new Random().nextInt(4) + 4);
            } else {
                str.append(new Random().nextInt(10));
            }
        }
        cookie = str.toString();
    }
}
