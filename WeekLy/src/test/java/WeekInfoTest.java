import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.weidaboy.dao.WeekinfoDao;
import top.weidaboy.entity.User;
import top.weidaboy.entity.Weekinfo;
import top.weidaboy.service.UserService;
import top.weidaboy.service.WeekinfoService;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:springmybatis.xml"})
public class WeekInfoTest {
    @Resource
    private WeekinfoService weekinfoService;

    @Resource
    private WeekinfoDao weekinfoDao;

    @Resource
    private UserService userService;

    /**
     * 测试获取所有weekinfo表里的用户数据
     */
    @Test
    public void test01() {
        List<Weekinfo> weekinfos = weekinfoService.allWeekly();
        for (top.weidaboy.entity.Weekinfo weekinfo : weekinfos) {
            System.out.println(weekinfo);
        }
    }


    /*
     * isExiste attempted to return null from a method with a primitive return type (boolean).
     * 返回类型出错
     * */
    @Test
    public void test02() {
        Weekinfo weeklyByIdAndWeek = weekinfoService.findWeeklyByIdAndWeek(20207006, 6);
        System.out.println(weeklyByIdAndWeek);
        boolean existe = weekinfoService.isExiste(20207006, 20);

        System.out.println(existe);

//        Weekinfo weekinfo = new Weekinfo();
//        //2.获取当前时间
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String addtime = df.format(new Date()).toString();
//        weekinfo.setTime(addtime);
//        weekinfo.setId(20207006);
//        weekinfo.setWeek(13);
//        weekinfoService.addWeekly(weekinfo);
    }


    @Test
    public void test03() {
        Weekinfo weeklyByIdAndWeek = weekinfoService.findWeeklyByIdAndWeek(20207006, 6);
        System.out.println(weeklyByIdAndWeek);
        boolean existe = weekinfoService.isExiste(20207006, 20);

        System.out.println(existe);
    }

    @Test
    public  void maxWeek(){
        System.out.println(weekinfoService.MaxWeek());
    }

    @Test
    public void testPoiInput() {
        try {

            XSSFWorkbook workbook = new XSSFWorkbook(
                    new FileInputStream(
                            new File("C:\\Users\\Vinda_Boy\\Desktop\\百色学院蓝桥4班第八周周报.xlsx")));
            //获取所有用户
            List<User> users = userService.allUser();

            Weekinfo weekinfo = null;

            //获取所有的表
            for (int z = 0; z < workbook.getNumberOfSheets(); z++) {
                //获得遍历到的表名
                String sheetname = workbook.getSheetName(z);
                for (int i = 1; i < users.size(); i++) {
                    //如果比较到的名称一样
                    //设置合并单元格距离
                    //初始化周数在这里
                    int week = 1;
                    if (users.get(i).getUsername().equals(sheetname)) {
                        for (int line = 2; line < 108; line += 15) {
                            XSSFSheet sheet = workbook.getSheet(sheetname);
                            XSSFRow row = sheet.getRow(line);
                            weekinfo = new Weekinfo();
                            //设置ID
                            weekinfo.setId(users.get(i).getId());
                            //设置周数
                            weekinfo.setWeek(week);

                            //设置内容
                            weekinfo.setContent(row.getCell(4).getStringCellValue());
                            //设置区间
                            weekinfo.setLimits(row.getCell(0).getStringCellValue());
                            weekinfo.setTime("");
                            weekinfo.setTcontent("");
                            //给组长周报添加内容：
                            if (users.get(i).getId().toString().endsWith("001") && week >= 3) {
                                switch (users.get(i).getTeam()) {
                                    case 1:
                                        sheet = workbook.getSheet("一组");
                                        row = sheet.getRow(line-30);
                                        weekinfo.setTcontent(row.getCell(4).getStringCellValue());
                                        break;
                                    case 2:
                                        sheet = workbook.getSheet("二组");
                                        row = sheet.getRow(line-30);
                                        weekinfo.setTcontent(row.getCell(4).getStringCellValue());
                                        break;
                                    case 3:
                                        sheet = workbook.getSheet("三组");
                                        row = sheet.getRow(line-30);
                                        weekinfo.setTcontent(row.getCell(4).getStringCellValue());
                                        break;
                                    case 4:
                                        sheet = workbook.getSheet("四组");
                                        row = sheet.getRow(line-30);
                                        weekinfo.setTcontent(row.getCell(4).getStringCellValue());
                                        break;
                                    case 5:
                                        sheet = workbook.getSheet("五组");
                                        row = sheet.getRow(line-30);
                                        weekinfo.setTcontent(row.getCell(4).getStringCellValue());
                                        break;
                                    case 6:
                                        sheet = workbook.getSheet("六组");
                                        row = sheet.getRow(line-30);
                                        weekinfo.setTcontent(row.getCell(4).getStringCellValue());
                                        break;
                                    case 7:
                                        sheet = workbook.getSheet("七组");
                                        row = sheet.getRow(line-30);
                                        weekinfo.setTcontent(row.getCell(4).getStringCellValue());
                                        break;
                                    case 8:
                                        sheet = workbook.getSheet("八组");
                                        row = sheet.getRow(line-30);
                                        weekinfo.setTcontent(row.getCell(4).getStringCellValue());
                                        break;
                                }
                            }
                            week += 1;
                            weekinfoDao.insertWeeklyTest(weekinfo);
                        }
                    }
                }
            }
            workbook.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }


}
