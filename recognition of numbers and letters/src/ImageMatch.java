import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Frode Fan
 * @version 1.0
 * @since 2019/9/20
 */
public class ImageMatch {

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\resources\\1");
        File[] files = file.listFiles();
        double max = 0d;
        double matchResult;
        int result = 0;
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            matchResult = match("d:\\1\\code.png", files[i].getPath());
            list.add(matchResult);
            if (matchResult > max) {
                max = matchResult;
                result = i;
            }
        }
        System.out.println(files[result].getName() + ":" + list.get(result));
    }

    public static double match(byte[] source, byte[] target) throws IOException {
        BufferedImage bufferedImageSource = ImageIO.read(new ByteArrayInputStream(source));
        BufferedImage bufferedImageTarget = ImageIO.read(new ByteArrayInputStream(target));
        return compareImage(bufferedImageSource, bufferedImageTarget);
    }

    public static double match(String source, String target) throws IOException {
        BufferedImage img01 = ImageIO.read(new File(target));

        //用64位读取图片，获取数据
        //byte[] decodeBuffer2 = base64Decoder.decodeBuffer(new FileInputStream("E:/img02.png"));
        //装配到字节数组流中
        //InputStream stream2 = new ByteArrayInputStream(decodeBuffer2);
        /**
         * 对比图片的时候，必须保证图片像素相同，否则无法匹配，20K以内，大概2miao
         * 当图片太大的时候，比较时间越长，5M图片比较，需要一分多钟
         */
        BufferedImage img02 = ImageIO.read(new File(source));
        long time = new Date().getTime();
//        System.out.println("开始匹配");
        double result = compareImage(img01, img02);
//        System.out.println(result);
//        System.out.println("匹配耗时" + (new Date().getTime() - time));
        return result;
    }

    // 改变成二进制码， //这里表示的像素，每一像素取点，返回颜色的值
    private static String[][] getPX(BufferedImage image) {
        int[] rgb = new int[3];
        int width = image.getWidth();
//这里表示的像素，每一像素取点，返回颜色的值
        int height = image.getHeight();
        int minx = image.getMinX();

        int miny = image.getMinY();
        String[][] list = new String[width][height];

        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                //这里当流读取对象的时候，把一个点的三原色封装成一个整数，这里进行反向取值，光色分解成三原色，rgb,就是红绿蓝，三原色可以组成任何颜色
                int pixel = image.getRGB(i, j);
                //红色值反向解析
                rgb[0] = (pixel & 0xff0000) >> 16;
                //绿色值反向解析
                rgb[1] = (pixel & 0xff00) >> 8;
                //蓝色值反向解析
                rgb[2] = (pixel & 0xff);
                list[i][j] = rgb[0] + "," + rgb[1] + "," + rgb[2];

            }
        }

        return list;
    }

    public static double compareImage(BufferedImage image1, BufferedImage image2) {
        boolean result = false;
// 分析图片相似度 begin
        String[][] list1 = getPX(image1);
        String[][] list2 = getPX(image2);
//这里double预防溢出，内存,记录比较次数
        double count = 0;
        int xiangsi = 0;
        int busi = 0;
        int length = list1.length;
        int lengthin = list1[1].length;
        for (int i = 0; i < length; i++) {
            //这里没有看明白要来干嘛，好像永远不成立
            if ((i + 1) == list1.length) {
                continue;
            }
            for (int j = 0; j < lengthin; j++) {
                try {
                    String[] value1 = list1[i][j].toString().split(",");
                    String[] value2 = list2[i][j].toString().split(",");

                    for (int n = 0; n < value2.length; n++) {
                        count++;
                        //相似度设置，根据实际需求设定图片相似程度，这里设置为5相似度极高像素的±偏差允许范围
                        if (Math.abs(Integer.parseInt(value1[n]) - Integer.parseInt(value2[n])) < 5) {
                            xiangsi++;
                        } else {
                            busi++;
                        }
                    }
                } catch (RuntimeException e) {
                    continue;
                }

            }

        }
        //这里采用正反比较两次
        list1 = getPX(image2);
        list2 = getPX(image1);
        for (int i = 0; i < length; i++) {
            //这里没有看明白要来干嘛，好像永远不成立
            if ((i + 1) == list1.length) {
                continue;
            }
            for (int j = 0; j < lengthin; j++) {
                try {
                    String[] value1 = list1[i][j].toString().split(",");
                    String[] value2 = list2[i][j].toString().split(",");

                    for (int n = 0; n < value2.length; n++) {
                        count++;
                        //相似度设置，根据实际需求设定图片相似程度，这里设置为5相似度极高,像素的±偏差允许范围
                        if (Math.abs(Integer.parseInt(value1[n]) - Integer.parseInt(value2[n])) < 5) {
                            xiangsi++;
                        } else {
                            busi++;
                        }
                    }
                } catch (RuntimeException e) {
                    continue;
                }

            }
        }
//        double redio = busi / count;
        double radio = xiangsi / count;
//        System.out.println("不相似的次数" + busi + "=========不相似率" + redio * 100 + "%");
//        System.out.println("相似的次数" + xiangsi + "=========相似率" + radio * 100 + "%");
//        if (radio >= 0.68) {
        return radio;
//        } else {
//            return 0;
//        }
//        return result;
    }


}
