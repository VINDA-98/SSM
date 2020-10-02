import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
/**
 * 把宿舍每个人的号数和姓名存进集合中，用随机方法获取随机数，随机数用来输出集合中对应key的value，每输出一次就删除该key。
 * @author 肖大宝哥哥
 */
public class StudentNum {

    public static void main(String[] args) throws IOException{
        Random random = new Random();
        int num = 0;
        Map map = new HashMap();

        File file = new File("C:\\Users\\Vinda_Boy\\Desktop\\ThrowTheRubbish.txt");
        if(!file.exists()) file.createNewFile();
        PrintWriter pw = new PrintWriter(new FileWriter(file,true));

        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Vinda_Boy\\Desktop\\ThrowTheRubbish.txt"));
        String msg = null;
        String[] sg = new String[10];

        //map删除其中一个值，不会再次排序
        map.put(1, "劳立德");
        map.put(2, "于翔");
        map.put(3, "谢宝乐");
        map.put(4, "沈振");
        map.put(5, "何定杰");
        map.put(6, "韦达");
        map.put(7, "罗玲彬");
        map.put(8, "吴军传");

        for(int i = 0; i < 1000; i++) {
            num = random.nextInt(8) + 1;
            if(map.get(num) == null) {
                num = random.nextInt(7) + 1;
            }else {
                pw.println(map.get(num));
                map.remove(num);
            }
        }
        pw.close();
    }
}