package top.weidaboy.controller;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import top.weidaboy.entity.User;
import top.weidaboy.entity.Weekinfo;
import top.weidaboy.service.UserService;
import top.weidaboy.service.WeekinfoService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


/*
    org.springframework.beans.factory.BeanCreationException: Error creating bean with name
    对应的bean没有添加注解
    对应bean添加注解错误，例如将spring的@Service错选成dubbo的包
    选择错误的自动注入办法。
 */
@Controller
@RequestMapping("")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private WeekinfoService weekinfoService;

    private Enumeration enumeration;

    public WeekinfoService getWeekinfoService() {
        return weekinfoService;
    }

    public void setWeekinfoService(WeekinfoService weekinfoService) {
        this.weekinfoService = weekinfoService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping("/UserByID")
    //根据工号获得学员信息
    public ModelAndView queryUserByID(@Param("id") Integer id) {
        User user = this.userService.queryUserByID(id);
        ModelAndView mv = new ModelAndView("success");
        mv.addObject(user);
        return mv;
    }

    /*
    * org.springframework.web.util.NestedServletException:
    * Request processing failed; nested exception is java.lang.NullPointerException
    * 错误出现的原因：处理该请求的方法所需要的部分参数在请求时没有值，导致空指针错误；
        解决方法一：在第一次访问该页的请求，或者说访问出错的页请求链接上加上处理该请求的方法所需的参数；
        解决方法二：配置两个处理请求的方法，一个不需要参数（用于第一次访问），一个不需要参数，用于处理有参数的请求。
    * */
    @RequestMapping("/UserLogin")
    //根据工号获得学员信息
    public String queryUserByIDandPassword(@Param("id") Integer id,
                                           @Param("password") String password,
                                           Model map,
                                           HttpSession session,
                                           Map<String, Object> error) {
        if( id==null || password ==null){
            map.addAttribute("errors", "账号或者密码不能为空");
           return "../../newlogin";
        }

        System.out.println("id:" + id + " Password:" + password);
        User user = userService.queryUserByIDandPassword(id, password);
        System.out.println(user);
        if (user != null) {
            //如果是管理员
            if(user.getId() == 20200001 ){
                //转发到管理员界面
                List<Integer> weeks = weekinfoService.queryWeeks();
                if(weeks != null){
                    map.addAttribute("weeks",weeks);
                }
                return "Administrator";
            }

            map.addAttribute("user", user);
            //添加到session
            session.setAttribute("user", user);
            //设置活动周期 1小时
            session.setMaxInactiveInterval(50 * 60);
            return "forward:/refresh";
        } else {
            map.addAttribute("errors", "账号或者密码错误");
            return "../../newlogin";
        }
    }

    /**
     * 修改密码
     */
    @RequestMapping("/changePassword")
    public String changePassword(@Param("id") Integer id, @Param("password") String password, Model map,
                                 HttpSession session, Map<String, Object> error){
        //1、修改密码
        userService.changePassword(id, password);
        //2、
        error.put("changePassword","ok");
        return "UserInterface";
    }



    /**
     *  刷新界面
     */
    @RequestMapping("/refresh")
    public String refreshFace(HttpSession session, HttpServletRequest request){
        enumeration = session.getAttributeNames();//获取session中所有的键值对
        String FileName="";
        //方法二：通过for循环进行遍历
//        while(enumeration.hasMoreElements()){
//            String AddFileName=enumeration.nextElement().toString();//获取session中的键值
//            System.out.println(AddFileName);
//        }
        // 获取session传过来的值
        User user = (User)session.getAttribute("user");


        //所有周数数组
        List<Integer> integers = weekinfoService.allWeeks(user.getId());
        if(integers != null){
            //把周数插入到要返回的周报内容
            List<Weekinfo> weekinfos = new ArrayList<Weekinfo>();
//          for (Integer integer : integers) {
//            Weekinfo weekinfo = weekinfoService.findWeeklyByIdAndWeek(user.getId(), integer);
//            weekinfos.add(weekinfo);
//          }
            //遍历八周
            for(int i = 0 ; i<5 ; i++) {
                Weekinfo weekinfo = weekinfoService.findWeeklyByIdAndWeek(user.getId(), integers.get(i));
                weekinfos.add(weekinfo);
            }

            session.setAttribute("weekinfos", weekinfos);
            session.setMaxInactiveInterval(50 * 60);
            //最大周数
            int maxWeek = weekinfoService.MaxWeek();
            request.setAttribute("maxWeek",maxWeek);
        }
        return "UserInterface";
    }


    /**
     *  刷新界面
     */
    @RequestMapping("/refreshALL")
    public String refreshFaceAll(HttpSession session, HttpServletRequest request){
        enumeration =session.getAttributeNames();//获取session中所有的键值对
        // 获取session传过来的值
        User user = (User)session.getAttribute("user");
        //所有周数数组
        List<Integer> integers = weekinfoService.allWeeks(user.getId());
        //把周数插入到要返回的周报内容
        List<Weekinfo> weekinfos = new ArrayList<Weekinfo>();
        for (Integer integer : integers) {
            Weekinfo weekinfo = weekinfoService.findWeeklyByIdAndWeek(user.getId(), integer);
            weekinfos.add(weekinfo);
        }
        session.setAttribute("weekinfos", weekinfos);
        session.setMaxInactiveInterval(50 * 60);
        //最大周数
        int maxWeek = weekinfoService.MaxWeek();
        request.setAttribute("maxWeek",maxWeek);
        return "UserInterface";
    }



    /**
     * 删除
     * @param id
     * @param week
     */
    @RequestMapping("/deleteWeekly")
    public String deleteWeekly(@Param("id") Integer id, @Param("week")Integer week){
         weekinfoService.deleteWeekly(id,week);
        return "forward:/refresh";
    }


    /**
     * 新建周报
     */
    @RequestMapping("/newWeekly")
    public String newWeekly(HttpServletRequest request, HttpServletResponse response,Model map,
                          @Param("id") Integer id, @Param("week")Integer week,
                          Map<String, Object> error){
        Weekinfo weekinfo = weekinfoService.findWeeklyByIdAndWeek(id, week);
        //1、判断是否存在该周报
        if(weekinfo == null){
            weekinfo = new Weekinfo();
            //2.获取当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String addtime = df.format(new Date()).toString();
            weekinfo.setTime(addtime);
            weekinfo.setId(id);
            weekinfo.setWeek(week);
            weekinfoService.addWeekly(weekinfo);
            map.addAttribute("weekinfo", weekinfo);
            //3、转发到ChangeWeekly.jsp
            return "showWeekly";
        }else {
            error.put("newmsg","no");
            return "forward:/refresh";
        }
    }

    /**
     * 返回首页
     * @return
     */
    @RequestMapping("/Back")
    public String Back(){
        return "forward:/refresh";
    }

    @RequestMapping("Exit")
    public String Exit(HttpServletRequest request, HttpServletResponse response){
        request.getSession().invalidate();//清除 session 中的所有信息
        return "../../newlogin"; //登录页面
    }
    /**
     * 查看周报
     * @param request
     * @param response
     * @param map
     * @param id
     * @param week
     * @return
     */
    @RequestMapping("/showWeekly")
    public String showWeekly(HttpServletRequest request, HttpServletResponse response,
                             Model map,@Param("id") Integer id,
                             @Param("week")Integer week){
        //1、获得ID 和 周数 得到用户对应周数信息
        Weekinfo weekinfo = weekinfoService.findWeeklyByIdAndWeek(id, week);
        int maxWeek = weekinfoService.MaxWeek();
        map.addAttribute("weekinfo", weekinfo);
        //最大周数
        map.addAttribute("maxWeek", maxWeek);
        //2、转发到ChangeWeekly.jsp
        return "showWeekly";
    }

    /**
     * 文件下载
     * @param fileName
     * @param request
     * @param response
     * @param session
     * @throws Exception
     */
    @RequestMapping("/FileDownload")
    public void allWeeklyDownload(@RequestParam(value = "fileName", required = false) String fileName,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    HttpSession session) throws Exception {
        //获得文件名字，确定存在系统路径
        String path = request.getSession().getServletContext().getRealPath("")+fileName;
        //生成Excel文件
        excelOutput(path);

        //String path = "src/main/resources/exceldownload/百色学院蓝桥四班周报.xlsx";
        System.out.println("下载文件路径："+path);

        //得到要下载的文件
        File file = new File(path);
        if (!file.exists()) {
            response.setContentType("text/html; charset=UTF-8");//注意text/html，和application/html
            response.getWriter().print("<html><body>" +
                    "<script type='text/javascript'>" +
                    "swal('您要下载的资源已被删除！');" +
                    "</script>" +
                    "</body>" +
                    "</html>");
            response.getWriter().close();
            System.out.println("您要下载的资源已被删除！！");
            return;
        }

        //转码，免得文件名中文乱码
        fileName = URLEncoder.encode(fileName, "UTF-8");
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        // 读取要下载的文件，保存到文件输入流
        FileInputStream in = new FileInputStream(path);
        // 创建输出流
        OutputStream out = response.getOutputStream();
        // 创建缓冲区
        byte buffer[] = new byte[1024]; // 缓冲区的大小设置是个迷  我也没搞明白
        int len = 0;
        //循环将输入流中的内容读取到缓冲区当中
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        //关闭文件输入流
        in.close();
        // 关闭输出流
        out.close();
    }

    /**
     * 文件下载
     * @param fileName
     * @param request
     * @param response
     * @param session
     * @throws Exception
     */
    @RequestMapping("/FileWeekDownload")
    public void WeeklyDownload(@RequestParam(value = "fileName", required = false) String fileName,
                               @RequestParam(value = "week", required = false) Integer week,
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  HttpSession session) throws Exception {
        //获得文件名字，确定存在系统路径
        String path = request.getSession().getServletContext().getRealPath("")+fileName;
        //生成单周Excel文件
        excelOutputWeekly(path,week);

        System.out.println("下载文件路径："+path);
        //得到要下载的文件
        File file = new File(path);
        if (!file.exists()) {
            response.setContentType("text/html; charset=UTF-8");//注意text/html，和application/html
            response.getWriter().print("<html><body>" +
                    "<script type='text/javascript'>" +
                    "swal('您要下载的资源已被删除！');" +
                    "</script>" +
                    "</body>" +
                    "</html>");
            response.getWriter().close();
            System.out.println("您要下载的资源已被删除！！");
            return;
        }

        //转码，免得文件名中文乱码
        fileName = URLEncoder.encode(fileName, "UTF-8");
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        // 读取要下载的文件，保存到文件输入流
        FileInputStream in = new FileInputStream(path);
        // 创建输出流
        OutputStream out = response.getOutputStream();
        // 创建缓冲区
        byte buffer[] = new byte[1024]; // 缓冲区的大小设置是个迷  我也没搞明白
        int len = 0;
        //循环将输入流中的内容读取到缓冲区当中
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        //关闭文件输入流
        in.close();
        // 关闭输出流
        out.close();
    }


    /*
    * 0 - BLACK 黑 深色0 - 7
    1 - BLUE 兰 2 - GREEN 绿
    3 - CYAN 青 4 - RED 红
    5 - MAGENTA 洋红 6 - BROWN 棕bai
    7 - LIGHTGRAY 淡灰 8 - DARKGRAY 深灰 淡色du8 - 15
    9 - LIGHTBLUE 淡兰 10 - LIGHTGREEN 淡绿
    11 - LIGHTCYAN 淡青 12 - LIGHTRED 淡红
    13 - LIGHTMAGENTA 淡洋红 4 - YELLOW 黄
    15 - WHITE 白
    * */
    public void excelOutputWeekly(String path,Integer week){
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
        weeklyfont.setFontHeightInPoints((short) 14);
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
        cellFont.setColor((short)53);
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

//      目录页分割

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
                    if(weekinfos.get(j).getWeek() == week ) {
                        //如果id相同，那么就是同一个人，就往表里添加数据
                        //记得Integer这个东西，127比较就不对了！！！
                        if (users.get(i).getId().intValue() == weekinfos.get(j).getId().intValue()) {
                            //添加新行
                            Row rowly = sheet.createRow(sheet.getPhysicalNumberOfRows());
                            //设置行高  6400 / 320 = 20   180 * 20 = 3600
                            rowly.setHeight((short) 3600);
                            //第一列:周次信息
                            if (weekinfos.get(j).getLimits() != null) {
                                rowly.createCell(0).setCellValue(
                                        "第" + weekinfos.get(j).getWeek() + "周小组周报" + "(" + weekinfos.get(j).getLimits() + ")");
                            } else {
                                rowly.createCell(0).setCellValue(
                                        "第" + weekinfos.get(j).getWeek() + "周小组周报");
                            }

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
                }

                //设置单元格列宽：
                //设置列宽   (参数一:列下标，参数二:列宽    )
                sheet.setColumnWidth(0, 25 * 256);
                sheet.setColumnWidth(1, 125 * 256);
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
                if(weekinfos.get(j).getWeek() == week) {
                    //如果id相同，那么就是同一个人，就往表里添加数据
                    if (users.get(i).getId().intValue() == weekinfos.get(j).getId().intValue()) {
                        //添加新行
                        Row row2 = sheet.createRow(sheet.getPhysicalNumberOfRows());
                        //设置行高  6400 / 320 = 20   180 * 20 = 3600
                        row2.setHeight((short) 3600);
                        //第一列:周次信息
                        if (weekinfos.get(j).getLimits() != null) {
                            row2.createCell(0).setCellValue(
                                    "第" + weekinfos.get(j).getWeek() + "周个人周报" + "(" + weekinfos.get(j).getLimits() + ")");
                        } else {
                            row2.createCell(0).setCellValue(
                                    "第" + weekinfos.get(j).getWeek() + "周个人周报");
                        }
                        //第二列：周报内容
                        row2.createCell(1).setCellValue(weekinfos.get(j).getContent());
                        //设置整行样式
                        row2.getCell(0).setCellStyle(anthorStyle);  //其他
                        row2.getCell(1).setCellStyle(weeklyStyle);  //内容

                    }
                }

            }

            //设置列宽   (参数一:列下标，参数二:列宽)
            sheet.setColumnWidth(0, 25 * 256);
            sheet.setColumnWidth(1, 125 * 256);
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
        //导出单元格
        try {
            workbook.write(
                    new FileOutputStream(new File(path)));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*导出全部*/
    public void excelOutput(String path){
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
        weeklyfont.setFontName("仿宋");
        weeklyfont.setFontHeightInPoints((short) 14);
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
        cellFont.setColor((short)53);
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

//      目录页分割

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
                        if(weekinfos.get(j).getLimits()!=null){
                            rowly.createCell(0).setCellValue(
                                    "小组周报"+"("+weekinfos.get(j).getLimits()+")");
                        }
                        else{
                            rowly.createCell(0).setCellValue(
                                    "第"+weekinfos.get(j).getWeek()+"周小组周报");
                        }

                        //第二列：小组周报内容
                        rowly.createCell(1).setCellValue(weekinfos.get(j).getTcontent());
                        //第三列：返回目录
                        //设置单元格样式
                        rowly.getCell(0).setCellStyle(anthorStyle);  //其他
                        rowly.getCell(1).setCellStyle(weeklyStyle);  //内容
                        //设置行高
                        rowly.setHeight((short) 4000);
                    }
                }

                //设置单元格列宽：
                //设置列宽   (参数一:列下标，参数二:列宽    )
                sheet.setColumnWidth(0, 25 * 256);
                sheet.setColumnWidth(1, 125 * 256);
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
                    row2.setHeight((short) 4000);
                    //第一列:周次信息
                    if(weekinfos.get(j).getLimits() != null){
                        row2.createCell(0).setCellValue(
                                "个人周报"+"("+weekinfos.get(j).getLimits()+")");
                    }
                    else{
                        row2.createCell(0).setCellValue(
                                "第"+weekinfos.get(j).getWeek()+"周个人周报");
                    }
                    //第二列：周报内容
                    row2.createCell(1).setCellValue(weekinfos.get(j).getContent());
                    //设置整行样式
                    row2.getCell(0).setCellStyle(anthorStyle);  //其他
                    row2.getCell(1).setCellStyle(weeklyStyle);  //内容

                }

            }

            //设置列宽   (参数一:列下标，参数二:列宽)
            sheet.setColumnWidth(0, 25 * 256);
            sheet.setColumnWidth(1, 125 * 256);
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
        //导出单元格
        try {
            workbook.write(
                    new FileOutputStream(new File(path)));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 管理员代码
     */
    @RequestMapping("/administrator")
    public String administrator(HttpServletRequest request, Model model) {
        //获取选择小组编号和某一周数
        String team = (String) request.getParameter("team");
        String week = (String) request.getParameter("week");

        //定义所有用户和周信息
        List<User> users = null;
        List<Weekinfo> weekInfos = null;
        if(team != null || week != null){ //如果不为空
            //小组和周数选择的索引
            String gt = (String) request.getParameter("gt");
            String gw = (String) request.getParameter("gw");
            //通过map来进行动态sql select
            if(team.equals("全部"))   team = "";
            if(week.equals("全部"))   week = "";
            //开始获取所有信息
            users = userService.queryUserAll(team);
            weekInfos = weekinfoService.queryWeekInfoAll(week);

            model.addAttribute("users", users);
            model.addAttribute("weekInfos", weekInfos);
            model.addAttribute("gT",gt);
            model.addAttribute("gW",gw);
        }

        model.addAttribute("users", users);
        model.addAttribute("weekInfos", weekInfos);
        //获取数据库中的weeks周数信息
        List<Integer> weeks = weekinfoService.queryWeeks();
        model.addAttribute("weeks", weeks);
        return "Administrator";
    }
}




//        /*******************1.接收请求参数***********************************/
//
//        /*******************2.对接收的参数进行编码处理**************************/
//        /**因为使用的是UTF-8的编码形式，所以不需要进行转码**/
//        //获取参数 ，默认会对参数进行编码 ISO8859-1
//        //把乱码转回二进制位
//        // byte[] bytes = name.getBytes("ISO8859-1");
//        //再去使用UTF-8进行编码
//        // name = new String(name.getBytes(),"UTF-8");
//
//        /*******************3.告诉浏览器响应的文件的类型*************************/
//        // 根据文件名来获取mime类型
//        String mimeType = session.getServletContext().getMimeType(fileName);
//        // 设置 mimeType
//        resp.setContentType(mimeType);
//
//        /*******************4.告诉浏览器以附件的形式下载*************************/
//        // 获取客户端信息
//        String agent = req.getHeader("User-Agent");
//        fileName = DownLoadUtils.getFileName(agent, fileName);
//
//        // 告诉浏览器是以附件形式来下载 不要解析
//        resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//
//        /*******************5.输出对应的流*************************/
//        //获取文件的绝对路径,拼接文件的路径
//        String path = session.getServletContext().getRealPath("src/main/resources/exceldownload/百色学院蓝桥四班周报.xlsx");
//
//        System.out.println("下载文件的路径" + path);
//        //写入流中
//        FileInputStream is = new FileInputStream(path);
//        //获取相应的输出流
//        ServletOutputStream os = resp.getOutputStream();
//
//        byte[] b = new byte[1024];
//        int len;
//        //写入浏览器中
//        while ((len = is.read(b)) != -1) {
//            os.write(b, 0, len);
//        }
//        //关闭对应的流
//        os.close();
//        is.close();
//       // return "UserInterface";
//checkPay.apk为需要下载的文件
//String filename = "checkPay.apk";   //我这里使用的是一个固定的文件，方法可以不用写filename参数
//获取文件的绝对路径名称，apk为根目录下的一个文件夹，这个只能获取根目录文件夹的绝对路径
