import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class KnnUtil {
    public static void main(String[] args) throws Exception {
        byte[][] in={fileToByte("d:\\1\\1.png"), fileToByte("d:\\1\\1.png"),  fileToByte("d:\\1\\1.png"), fileToByte("d:\\1\\2.png"), fileToByte("d:\\1\\3.png")};
        byte[] b= fileToByte("d:\\1\\3.png");
        System.out.println(    basePnn(in,5,b));
    }

    public static byte[] fileToByte(String src) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            BufferedImage bi;
            bi = ImageIO.read(new File(src));
            ImageIO.write(bi, "png", baos);
            return baos.toByteArray();
        } finally {
            baos.close();
        }
    }


    public static List<Map> basePnn(byte[][] input,int k,byte[] base){
        /*float[][] output=new float[k][];*/
        List<Map> list=new ArrayList();
        Map map=new HashMap();
        float maxX=0;
        float maxY=0;
        float x;
        float y;
        int xlen;
        int ylen;
        for(int i=0;i<input.length;i++){
            if(maxX<input[i][0]){
                maxX=input[i][0];
            }
        }
        for(int i=0;i<input.length;i++){
            if(maxY<input[i][1]){
                maxY=input[i][1];
            }
        }
        int bx=(int) ((base[0]/maxX)*100);
        int by=(int) ((base[1]/maxX)*100);
        for(int i=0;i<input.length;i++){
            Map maps=new HashMap();
            x=input[i][0];
            y=input[i][1];
            xlen=(int) ((x/maxX)*100);
            ylen=(int) ((y/maxX)*100);
            xlen=xlen-bx;
            ylen=ylen-by;
            maps.put("index", i);
            maps.put("len", Math.sqrt(xlen*xlen+ylen*ylen));
            list.add(maps);
        }
        List link=new LinkedList();
        double now;
        double old;
        for(int i=0;i<list.size();i++){
            for(int j=0;j<(list.size()-i);j++){
                if(j>0){
                    now=(Double) list.get(j).get("len");
                    old=(Double) list.get(j-1).get("len");
                    if(now<old){
                        Map m=list.get(j-1);
                        list.set(j-1, list.get(j));
                        list.set(j, m);
                    }
                }
            }

        }
        return list.subList(0, k);
    }


}