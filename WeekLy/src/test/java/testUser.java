import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.weidaboy.dao.UserDao;
import top.weidaboy.dao.WeekinfoDao;
import top.weidaboy.entity.User;
import top.weidaboy.entity.Weekinfo;
import top.weidaboy.service.UserService;
import top.weidaboy.service.WeekinfoService;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//org.apache.ibatis.executor.ExecutorException: No constructor found in xxxBean
//需要构造函数和get和set方法

//org.apache.ibatis.reflection.ReflectionException:
// There is no getter for property named 'id' in 'class java.lang.Integer'  #{}  而不是${}
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:springmybatis.xml"})
public class testUser {

    @Autowired
    private UserDao userDao;
    @Resource
    private UserService userService;

    @Autowired
    private WeekinfoDao weekinfoDao;
    @Resource
    private WeekinfoService weekinfoService;


    @Test
    public void test01(){
        //dao接口的动态代理对象在SpringIoc中的id值，就是接口的文件名
       // UserDao userDao = (UserDao) context.getBean("userDao");
        int id = 20207006;
        User user  = userDao.queryUserByID(id);
        System.out.println(user);

    }
    /**
     *  测试登录
     */
    @Test
    public void test02(){
//        ApplicationContext context = new ClassPathXmlApplicationContext("springmybatis.xml");
//        //dao接口的动态代理对象在SpringIoc中的id值，就是接口的文件名
        User user  = userDao.queryUserByIDandPassword(20207006,"123456");
        System.out.println(user);

    }

    /**
     * 测试通过id 和 密码实现登录功能
     */
    @Test
    public void test03(){
       User user = userService.queryUserByIDandPassword(20207006,"123456");
        if(user != null){
//            ModelAndView mv = new ModelAndView("success");
//            mv.addObject(user);
            System.out.println(user);
        }
    }


    @Test
    public void test04(){
        Weekinfo Weekinfo = weekinfoService.findWeeklyByIdAndWeek(20207006,6);
        if(Weekinfo != null){
//            ModelAndView mv = new ModelAndView("success");
//            mv.addObject(user);
            System.out.println(Weekinfo);

            //所有周数数组
            List<Integer> integers = weekinfoService.allWeeks(20207006);
            //把周数插入到要返回的周报内容
            List<Weekinfo> weekinfos = new ArrayList<Weekinfo>();

            for (Integer integer : integers) {
                Weekinfo weekinfo = weekinfoService.findWeeklyByIdAndWeek(20207006, integer);
                weekinfos.add(weekinfo);
            }

            for (Weekinfo weekinfo : weekinfos) {
                System.out.println(weekinfo);
            }

        }
    }






//    Excel文档			HSSFWorkbook
//    Excel工作表		HSSFSheet
//    Excel 的行		HSSFRow
//    Excel 中的单元格	HSSFCell
//    Excel 字体		HSSFFont
//    Excel 单元格样式	HSSFCellStyle
//    Excel 颜色		HSSFColor
//    合并单元格		    CellRangeAddress
    /*
    TOP,
    CENTER,
    BOTTOM,
    JUSTIFY,
    DISTRIBUTED;
    *
    * */
    @Test
    public void testTitleExcel(){

        //创建一个Excel文档
        XSSFWorkbook  workbook = new XSSFWorkbook();
        //创建一个工作薄   参数是工作薄的名字（sheet1,sheet2....）
        XSSFSheet sheet = null;

        /* 设置为超链接的样式*/
        XSSFCellStyle linkStyle = workbook.createCellStyle();
        XSSFFont cellFont = workbook.createFont();
        cellFont.setBold(true);  //确认加粗
        cellFont.setFontName("楷体");
        cellFont.setUnderline((byte) 1);
        cellFont.setFontHeightInPoints((short)16);
        cellFont.setColor((short)17);
        linkStyle.setFont(cellFont);
        linkStyle.setAlignment(HorizontalAlignment.CENTER);     //居中对齐
        linkStyle.setVerticalAlignment(VerticalAlignment.CENTER);  //居中对齐

        //获取所有用户
        List<User> users = userService.allUser();

        //为目录表头内容设置样式：
        CellStyle titleStyle = workbook.createCellStyle();
        //设置内容字体
        //创建字体样式对象
        Font mulufont = workbook.createFont();
        mulufont.setBold(true);
        mulufont.setFontName("楷体");
        mulufont.setFontHeightInPoints((short) 20);
        mulufont.setColor((short) 26);
        //设置字体样式
        titleStyle.setFont(mulufont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);



        int teamafter = 0;
        Cell likeCell = null;
        //小组周报名称：
        sheet = workbook.createSheet("目录") ;
        Row row = sheet.createRow(3);
        for(int i=1; i< users.size() ; i++ ) {

            //获得上一个用户的组数
            if (i < users.size()) teamafter = users.get(i - 1).getTeam();

            //如果组数不同，新建小组周报
            if (users.get(i).getTeam() != teamafter) {
                switch (users.get(i).getTeam()){
                    //根据小组数来确定位置
                    case 1 : likeCell = row.createCell((short) 0); break;
                    case 2 : likeCell = row.createCell((short) 2); break;
                    case 3 : likeCell = row.createCell((short) 4); break;
                    case 4 : likeCell = row.createCell((short) 6); break;
                    case 5 : likeCell = row.createCell((short) 8); break;
                    case 6 : likeCell = row.createCell((short) 10); break;
                    case 7 : likeCell = row.createCell((short) 12); break;
                    case 8 : likeCell = row.createCell((short) 14); break;
                }

                //XSSFHyperlink link = new XSSFHyperlink(XSSFHyperlink.LINK_URL);// 无法实例化XSSFHyperlink对象
                CreationHelper createHelper = workbook.getCreationHelper();
                XSSFHyperlink hyperlink = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.URL);
                //"#"表示本文档    "明细页面"表示sheet页名称  "A10"表示第几列第几行
                hyperlink.setAddress("#第" + users.get(i).getTeam() + "组A1");
                likeCell.setHyperlink(hyperlink);
                // 点击进行跳转
                likeCell.setCellValue("第" + users.get(i).getTeam() + "组");
                likeCell.setCellStyle(linkStyle);
            }

            int key = users.get(i).getId() % 10 ;//id
            //插入行数
            for (int j = 0; j < 8; j++) {
                Row row1 = sheet.createRow(4+i);
            }

            switch(users.get(i).getTeam()){
                //第一组第一列
                case 1:likeCell = sheet.getRow(4+key).createCell((short) 0);break;
                //第二组第二列
                case 2:likeCell = sheet.getRow(4+key).createCell((short) 2);break;
                case 3:likeCell = sheet.getRow(4+key).createCell((short) 4);break;
                case 4:likeCell = sheet.getRow(4+key).createCell((short) 6);break;
                case 5:likeCell = sheet.getRow(4+key).createCell((short) 8);break;
                case 6:likeCell = sheet.getRow(4+key).createCell((short) 10);break;
                case 7:likeCell = sheet.getRow(4+key).createCell((short) 12);break;
                case 8:likeCell = sheet.getRow(4+key).createCell((short) 14);break;
            }

            //XSSFHyperlink link = new XSSFHyperlink(XSSFHyperlink.LINK_URL);// 无法实例化XSSFHyperlink对象
            CreationHelper createHelper = workbook.getCreationHelper();
            XSSFHyperlink hyperlink = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.URL);
            //"#"表示本文档    "明细页面"表示sheet页名称  "A10"表示第几列第几行
            hyperlink.setAddress("#" + users.get(i).getUsername() + "A1");
            likeCell.setHyperlink(hyperlink);
            // 点击进行跳转
            likeCell.setCellValue(users.get(i).getUsername());
            likeCell.setCellStyle(linkStyle);

        }

        //合并行  参数：起始行，结束行，起始列，结束列
        CellRangeAddress addresses = new CellRangeAddress(0,2 , 0, 14);
        sheet.addMergedRegion(addresses);
        sheet.createRow(0).createCell((short) 0).setCellValue("百色学院蓝桥4班 周报系统");
        sheet.getRow(0).getCell(0).setCellStyle(titleStyle);


        //设置颜色：
        sheet.setTabColor(new XSSFColor(new java.awt.Color(0xFB00FB)));
            //导出单元格
            try {
                workbook.write(
                        new FileOutputStream(new File("C:\\Users\\Vinda_Boy\\Desktop\\TestTitleExcel.xlsx")));
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }


    @Test
    public void testPoiOutput() {
        //获取所有用户
        List<User> users = userService.allUser();
        //获取用户所有的周报数据
        List<Weekinfo> weekinfos = weekinfoService.allWeekly();

        //创建一个Excel文档
        XSSFWorkbook  workbook = new XSSFWorkbook();
        //创建一个工作薄   参数是工作薄的名字（sheet1,sheet2....）
        XSSFSheet sheet = null;

        //为单元格内容设置样式：
        CellStyle weeklyStyle = workbook.createCellStyle();
        //设置内容字体
        //创建字体样式对象
        Font weeklyfont = workbook.createFont();
        //font.setBold(true);
        weeklyfont.setFontName("宋体");
        weeklyfont.setFontHeightInPoints((short) 12);
        //设置字体样式
        weeklyStyle.setFont(weeklyfont);
        weeklyStyle.setAlignment(HorizontalAlignment.LEFT);     //靠左对齐
        weeklyStyle.setVerticalAlignment(VerticalAlignment.TOP);  //靠上对齐
        weeklyStyle.setWrapText(true);//自动换行
        weeklyStyle.setIndention((short)2);


        //设置表头样式：
        //创建字体样式对象
        Font font = workbook.createFont();
        //设置字体样式
        CellStyle cellStyle = workbook.createCellStyle();
        //给字体设置样式
        cellStyle.setFont(font);
        font.setBold(true);  //确认加粗
        font.setColor((short)53);
        font.setFontName("楷体");
        font.setFontHeightInPoints((short) 16);
        //设置水平垂直居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);


        //为目录表头内容设置样式：
        CellStyle titleStyle = workbook.createCellStyle();
        //设置内容字体
        //创建字体样式对象
        Font mulufont = workbook.createFont();
        mulufont.setBold(true);
        mulufont.setFontName("楷体");
        mulufont.setFontHeightInPoints((short) 20);
        mulufont.setColor((short) 24);
        //设置字体样式
        titleStyle.setFont(mulufont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);



        //创建其他单元格内容：为其他单元格内容设置样式
        CellStyle anthorStyle = workbook.createCellStyle();
        Font anthorFont = workbook.createFont();
        anthorFont.setBold(true);  //确认加粗
        anthorFont.setColor((short)53);  //设置颜色
        anthorFont.setFontName("楷体");
        anthorFont.setFontHeightInPoints((short)12);
        //设置字体
        anthorStyle.setFont(anthorFont);
        anthorStyle.setAlignment(HorizontalAlignment.CENTER);     //居中对齐
        anthorStyle.setVerticalAlignment(VerticalAlignment.CENTER);  //居中对齐
        anthorStyle.setWrapText(true);//自动换行


        /* 设置为超链接的样式*/
        XSSFCellStyle linkStyle = workbook.createCellStyle();
        XSSFFont cellFont = workbook.createFont();
        cellFont.setBold(true);  //确认加粗
        cellFont.setFontName("楷体");
        cellFont.setUnderline((byte) 1);
        cellFont.setFontHeightInPoints((short)16);
        cellFont.setColor((short)17);
        linkStyle.setFont(cellFont);
        linkStyle.setAlignment(HorizontalAlignment.CENTER);     //居中对齐
        linkStyle.setVerticalAlignment(VerticalAlignment.CENTER);  //居中对齐


        int teamafter = 0;
        Cell likeCell = null;
        //小组周报名称：
        sheet = workbook.createSheet("目录") ;
        Row row = sheet.createRow(3);
        for(int i=1; i< users.size() ; i++ ) {

            //获得上一个用户的组数
            if (i < users.size()) teamafter = users.get(i - 1).getTeam();

            //如果组数不同，新建小组周报
            if (users.get(i).getTeam() != teamafter) {
                switch (users.get(i).getTeam()){
                    //根据小组数来确定位置
                    case 1 : likeCell = row.createCell((short) 0); break;
                    case 2 : likeCell = row.createCell((short) 2); break;
                    case 3 : likeCell = row.createCell((short) 4); break;
                    case 4 : likeCell = row.createCell((short) 6); break;
                    case 5 : likeCell = row.createCell((short) 8); break;
                    case 6 : likeCell = row.createCell((short) 10); break;
                    case 7 : likeCell = row.createCell((short) 12); break;
                    case 8 : likeCell = row.createCell((short) 14); break;
                }

                //XSSFHyperlink link = new XSSFHyperlink(XSSFHyperlink.LINK_URL);// 无法实例化XSSFHyperlink对象
                CreationHelper createHelper = workbook.getCreationHelper();
                XSSFHyperlink hyperlink = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.URL);
                //"#"表示本文档    "明细页面"表示sheet页名称  "A10"表示第几列第几行
                hyperlink.setAddress("#第" + users.get(i).getTeam() + "组!A1");
                likeCell.setHyperlink(hyperlink);
                // 点击进行跳转
                likeCell.setCellValue("第" + users.get(i).getTeam() + "组");
                likeCell.setCellStyle(linkStyle);
            }

            int key = users.get(i).getId() % 10 ;//id
            //插入行数
            for (int j = 0; j < 8; j++) {
                Row row1 = sheet.createRow(4+i);
            }

            switch(users.get(i).getTeam()){
                //第一组第一列
                case 1:likeCell = sheet.getRow(4+key).createCell((short) 0);break;
                //第二组第二列
                case 2:likeCell = sheet.getRow(4+key).createCell((short) 2);break;
                case 3:likeCell = sheet.getRow(4+key).createCell((short) 4);break;
                case 4:likeCell = sheet.getRow(4+key).createCell((short) 6);break;
                case 5:likeCell = sheet.getRow(4+key).createCell((short) 8);break;
                case 6:likeCell = sheet.getRow(4+key).createCell((short) 10);break;
                case 7:likeCell = sheet.getRow(4+key).createCell((short) 12);break;
                case 8:likeCell = sheet.getRow(4+key).createCell((short) 14);break;
            }

            //XSSFHyperlink link = new XSSFHyperlink(XSSFHyperlink.LINK_URL);// 无法实例化XSSFHyperlink对象
            CreationHelper createHelper = workbook.getCreationHelper();
            XSSFHyperlink hyperlink = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.URL);
            //"#"表示本文档    "明细页面"表示sheet页名称  "A10"表示第几列第几行
            hyperlink.setAddress("#" + users.get(i).getUsername() + "!A1");
            likeCell.setHyperlink(hyperlink);
            // 点击进行跳转
            likeCell.setCellValue(users.get(i).getUsername());
            likeCell.setCellStyle(linkStyle);

        }

        //合并行  参数：起始行，结束行，起始列，结束列
        CellRangeAddress addresses = new CellRangeAddress(0,2 , 0, 14);
        sheet.addMergedRegion(addresses);
        sheet.createRow(0).createCell((short) 0).setCellValue("百色学院蓝桥4班 周报系统");
        sheet.getRow(0).getCell(0).setCellStyle(titleStyle);


        //设置颜色：
        sheet.setTabColor(new XSSFColor(new java.awt.Color(0xFB00FB)));
//        目录页分割

        //重新遍历
        teamafter = 0;
        for(int i=1; i< users.size() ; i++ ){

            if( i < users.size() )
             //获得上一个用户的组数
             teamafter = users.get(i - 1).getTeam() ;

            //如果组数不同，新建小组周报
            if(users.get(i).getTeam() != teamafter){
                //小组周报名称：
                sheet = workbook.createSheet("第"+users.get(i).getTeam()+"组") ;
                //创建标题行  (下标从0开始)
                row = sheet.createRow(0);
                //设置内容
                Cell cellTitle = row.createCell(0);

                //设置行高   参数行高（注意单位:是20分之一   short类型）
                row.setHeight((short) 800);

                //给小组周报内容设置表头
                String[] title = {"周次信息", "周报内容","返回目录"};

                for (int x = 0; x < title.length; x++) {
                    //创建单元格
                    Cell cell = row.createCell(x);
                    //创建的单元内容
                    cell.setCellValue(title[x]);
                    cell.setCellStyle(cellStyle);

                }

                //创建超链接
                /* 连接跳转*/
                likeCell = row.createCell((short)2);
                //  XSSFHyperlink link = new XSSFHyperlink(XSSFHyperlink.LINK_URL);// 无法实例化XSSFHyperlink对象
                CreationHelper createHelper = workbook.getCreationHelper();
                XSSFHyperlink  hyperlink = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.URL);
                // "#"表示本文档    "明细页面"表示sheet页名称  "A10"表示第几列第几行
                hyperlink.setAddress("#目录!A1");
                likeCell.setHyperlink(hyperlink);
                // 点击进行跳转
                likeCell.setCellValue("返回目录");
                likeCell.setCellStyle(linkStyle);


                //添加内容
                //总行数加1  sheet.getPhysicalNumberOfRows() 获取当前表中总条数,新增内容
                //设置单元格的内容
                for (int j = 0; j < weekinfos.size() ; j++) {
                    //如果id相同，那么就是同一个人，就往表里添加数据
                    //记得Integer这个东西，127比较就不对了！！！
                    if (users.get(i).getId().intValue() == weekinfos.get(j).getId().intValue()) {
                        //添加新行
                        Row rowly = sheet.createRow(sheet.getPhysicalNumberOfRows());
                        //设置行高  6400 / 320 = 20   180 * 20 = 3600
                        rowly.setHeight((short) 3600);
                        //第一列:周次信息
                        rowly.createCell(0).setCellValue("第"+weekinfos.get(j).getWeek()+"周小组周报");
                        //第二列：小组周报内容
                        rowly.createCell(1).setCellValue(weekinfos.get(j).getTcontent());
                        //第三列：返回目录
                        //设置单元格样式
                        rowly.getCell(0).setCellStyle(anthorStyle);  //其他
                        rowly.getCell(1).setCellStyle(weeklyStyle);  //内容
                        //设置行高
                        rowly.setHeight((short) 3600);
                    }
                }

                //设置单元格列宽：
                //设置列宽   (参数一:列下标，参数二:列宽    )
                sheet.setColumnWidth(0, 25 * 256);
                sheet.setColumnWidth(1, 110 * 256);
                sheet.setColumnWidth(2,  25* 256);

                //设置颜色：
                sheet.setTabColor(new XSSFColor(new java.awt.Color(0xFB1C1A)));
            }

            //新建个人内容
            sheet = workbook.createSheet(users.get(i).getUsername());
            //创建标题行  (下标从0开始)
            row = sheet.createRow(0);
            //设置内容
            Cell cellTitle = row.createCell(0);

            //创建表头  参数是行下标（下标是1）
            Row row0 = sheet.createRow(0);
            //设置行高   参数行高（注意单位:是20分之一   short类型）
            row0.setHeight((short) 800);

            //给目录行设置数据
            String[] title = {"周次信息", "周报内容"};
            for (int x = 0; x < title.length; x++) {
                //创建单元格
                Cell cell = row0.createCell(x);
                //创建的单元内容
                cell.setCellValue(title[x]);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                cellStyle.setWrapText(true);//自动换行
                cell.setCellStyle(cellStyle);
            }

            //创建超链接
            /* 连接跳转*/
            likeCell = row0.createCell((short)2);
            //  XSSFHyperlink link = new XSSFHyperlink(XSSFHyperlink.LINK_URL);// 无法实例化XSSFHyperlink对象
            CreationHelper createHelper = workbook.getCreationHelper();
            XSSFHyperlink  hyperlink = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.URL);
            // "#"表示本文档    "明细页面"表示sheet页名称  "A10"表示第几列第几行
            hyperlink.setAddress("#目录!A1");
            likeCell.setHyperlink(hyperlink);
            // 点击进行跳转
            likeCell.setCellValue("返回目录");
            likeCell.setCellStyle(linkStyle);


            //设置单元格的内容
            for (int j = 0; j < weekinfos.size() ; j++) {
                //记得Integer这个东西，127比较就不对了！！！
                //如果id相同，那么就是同一个人，就往表里添加数据
                if (users.get(i).getId().intValue() == weekinfos.get(j).getId().intValue()) {
                    //添加新行
                    Row row2 = sheet.createRow(sheet.getPhysicalNumberOfRows());
                    //设置行高  6400 / 320 = 20   180 * 20 = 3600
                    row2.setHeight((short) 3600);
                    //第一列:周次信息
                    row2.createCell(0).setCellValue("第"+weekinfos.get(j).getWeek()+"周个人周报");
                    //第二列：周报内容
                    row2.createCell(1).setCellValue(weekinfos.get(j).getContent());
                    //设置整行样式
                    row2.getCell(0).setCellStyle(anthorStyle);  //其他
                    row2.getCell(1).setCellStyle(weeklyStyle);  //内容

                }

            }

            //设置列宽   (参数一:列下标，参数二:列宽)
            sheet.setColumnWidth(0, 25 * 256);
            sheet.setColumnWidth(1, 110 * 256);
            sheet.setColumnWidth(2, 20* 256);

            //设置颜色，每个小组颜色不统一：  分支控制颜色
            switch (users.get(i).getTeam()){
                case 1 : sheet.setTabColor(new XSSFColor(new java.awt.Color(49, 230, 28)));break;
                case 2 : sheet.setTabColor(new XSSFColor(new java.awt.Color(230, 140, 63)));break;
                case 3 : sheet.setTabColor(new XSSFColor(new java.awt.Color(96, 105, 230)));break;
                case 4 : sheet.setTabColor(new XSSFColor(new java.awt.Color(230, 58, 203)));break;
                case 5 : sheet.setTabColor(new XSSFColor(new java.awt.Color(176, 0, 230)));break;
                case 6 : sheet.setTabColor(new XSSFColor(new java.awt.Color(108, 230, 228)));break;
                case 7 : sheet.setTabColor(new XSSFColor(new java.awt.Color(230, 144, 217)));break;
                case 8 : sheet.setTabColor(new XSSFColor(new java.awt.Color(20, 20, 230)));break;
            }

        }

        /*
        *  Class.getResource()的资源获取如果以 / 开头，则从根路径开始搜索资源。
            Class.getResource()的资源获取如果不以 / 开头，则从当前类所在的路径开始搜索资源。
            ClassLoader.getResource()的资源获取不能以 / 开头，统一从根路径开始搜索资源。
        * */
        //导出单元格
        try {
            //2.1找到文件服务器路径
            String path = Class.class.getClass().getResource("/").getPath();
            System.out.println(path);
            workbook.write(
            new FileOutputStream(new File("C:\\Users\\Vinda_Boy\\Desktop\\百色学院蓝桥四班周报.xlsx")));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
