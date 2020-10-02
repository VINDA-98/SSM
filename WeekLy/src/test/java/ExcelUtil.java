import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;

public class ExcelUtil {

    /**
     * 导出excel
     *
     * @param title    导出表的标题
     * @param rowsName 导出表的列名
     * @param dataList 需要导出的数据
     * @param fileName 生成excel文件的文件名
     * @param response
     */
    public void exportExcel(String title, String[] rowsName, List<String[]> dataList, String fileName, HttpServletResponse response) throws Exception {
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition",
                "attachment; filename=" + fileName);
        response.setContentType("application/msexcel");
        this.export(title, rowsName, dataList, fileName, output);
        this.close(output);
    }

    /**
     * 数据导出
     * @param title
     * @param rowName
     * @param dataList
     * @param fileName
     * @param out
     * @throws Exception
     */
    private void export(String title, String[] rowName, List<String[]> dataList, String fileName, OutputStream out) throws Exception {
        //查看poi使用的是什么版本的jar包
        //System.out.println("!!!!!!!!!"+ XSSFCellStyle.class.getProtectionDomain().getCodeSource().getLocation());
        try {
            // 创建工作簿对象
            XSSFWorkbook workbook = new XSSFWorkbook();
            // 创建工作表
            XSSFSheet sheet = workbook.createSheet(title);
            // 产生表格标题行
            XSSFRow rowm = sheet.createRow(0);
            //创建表格标题列
            XSSFCell cellTiltle = rowm.createCell(0);
            // sheet样式定义;    getColumnTopStyle();    getStyle()均为自定义方法 --在下面,可扩展
            // 获取列头样式对象
            XSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);
            // 获取单元格样式对象
            XSSFCellStyle style = this.getStyle(workbook);
            //合并表格标题行，合并列数为列名的长度,第一个0为起始行号，第二个1为终止行号，第三个0为起始列好，第四个参数为终止列号
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.length - 1)));
            //设置标题行样式
            cellTiltle.setCellStyle(columnTopStyle);
            //设置标题行值
            cellTiltle.setCellValue(title);
            // 定义所需列数
            int columnNum = rowName.length;
            // 在索引2的位置创建行(最顶端的行开始的第二行)
            XSSFRow rowRowName = sheet.createRow(2);
            // 将列头设置到sheet的单元格中
            for (int n = 0; n < columnNum; n++) {
                // 创建列头对应个数的单元格
                XSSFCell cellRowName = rowRowName.createCell(n);
                // 设置列头单元格的数据类型
                cellRowName.setCellType(CellType.STRING);
                XSSFRichTextString text = new XSSFRichTextString(rowName[n]);
                // 设置列头单元格的值
                cellRowName.setCellValue(text);
                // 设置列头单元格样式
                cellRowName.setCellStyle(columnTopStyle);
            }

            // 将查询出的数据设置到sheet对应的单元格中
            for (int i = 0; i < dataList.size(); i++) {
                // 遍历每个对象
                Object[] obj = dataList.get(i);
                // 创建所需的行数
                XSSFRow row = sheet.createRow(i + 3);
                for (int j = 0; j < obj.length; j++) {
                    // 设置单元格的数据类型
                    XSSFCell cell = null;
                    if (j == 0) {
                        cell = row.createCell(j, NUMERIC);
                        cell.setCellValue(i + 1);
                    } else {
                        cell = row.createCell(j, CellType.STRING);
                        if (!"".equals(obj[j]) && obj[j] != null) {
                            // 设置单元格的值
                            cell.setCellValue(obj[j].toString());
                        }
                    }
                    cell.setCellStyle(style); // 设置单元格样式
                }
            }

            // 让列宽随着导出的列长自动适应
            for (int colNum = 0; colNum < columnNum; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                    XSSFRow currentRow;
                    // 当前行未被使用过
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null) {
                        XSSFCell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == 1) {
                            int length = currentCell.getStringCellValue()
                                    .getBytes().length;
                            if (columnWidth < length) {
                                columnWidth = length;
                            }
                        }
                    }
                }
                if (colNum == 0) {
                    sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
                } else {
                    sheet.setColumnWidth(colNum, (columnWidth + 8) * 256);
                }
            }
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 列头单元格样式
     */
    private XSSFCellStyle getColumnTopStyle(XSSFWorkbook workbook) {

        // 设置字体
        XSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 16);
        // 字体加粗
        font.setBold(true);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(new XSSFColor(Color.BLACK));
        // 设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(new XSSFColor(Color.BLACK));
        // 设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(new XSSFColor(Color.BLACK));
        // 设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(new XSSFColor(Color.BLACK));
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;

    }

    /*
     * 列数据信息单元格样式
     */
    private XSSFCellStyle getStyle(XSSFWorkbook workbook) {
        // 设置字体
        XSSFFont font = workbook.createFont();
        // 设置字体大小
        // font.setFontHeightInPoints((short)10);
        // 字体加粗
        // font.setBold(true);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色;
        //style.setBottomBorderColor(new XSSFColor(Color.RED));
        // 设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(new XSSFColor(Color.BLACK));
        // 设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(new XSSFColor(Color.BLACK));
        // 设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(new XSSFColor(Color.BLACK));
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 关闭输出流
     *
     * @param os
     */
    private void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
 