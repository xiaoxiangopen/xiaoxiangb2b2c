package net.xiaoxiangshop.util.ExcelUtil;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengding on 2018/4/15.
 */
public class ExportExcelUtil {

       private static Logger logger = LoggerFactory.getLogger(ExportExcelUtil.class);

    /**
     * 设置表头格式
     * @param wb
     * @return
     */
    public static CellStyle setHeadCellStyle(SXSSFWorkbook wb) {
        //创建单元格的 显示样式
        CellStyle style = wb.createCellStyle();

//        style.setAlignment(HorizontalAlignment.CENTER); //水平方向上的对其方式
//        style.setVerticalAlignment(VerticalAlignment.CENTER);  //垂直方向上的对其方式
        //字体格式
        Font font = wb.createFont();
        font.setFontName("仿宋_GB2312");
        font.setBold(true);//粗体显示
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);//选择需要用到的字体格式
        return style;

    }

    /**
     * 设置单元格内容样式
     * @param wb
     * @return
     */
    public static CellStyle setCellStyle(SXSSFWorkbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setWrapText(true);
        // 设置上下左右边框
//        cellStyle.setBorderBottom(BorderStyle.THIN);
//        cellStyle.setBorderLeft(BorderStyle.THIN);
//        cellStyle.setBorderRight(BorderStyle.THIN);
//        cellStyle.setBorderTop(BorderStyle.THIN);
//        //设置左对齐
//        cellStyle.setAlignment(HorizontalAlignment.LEFT);
//        //水平居中
//        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置标题字体
        Font cellFont = wb.createFont();
        // 设置字体大小
        cellFont.setFontHeightInPoints((short) 11);
        // 设置字体
        cellFont.setFontName("等线");
        // 把字体应用到当前的样式
        cellStyle.setFont(cellFont);
        return cellStyle;

    }

    /**
     * 过滤数据
     * @param titles
     * @param fields
     * @param str
     */
    public static void exportColumn(String [] titles,String [] fields,String[] str){
        for (int i = 0; i < str.length; i++) {
            String strs = str[i];
            String[] strss = strs.split(":");
            fields[i] = (strss[0]);
            titles[i] = (strss[1]);
        }
    }


    /**
     * 导出excel
     * @param response
     * @param request
     * @param extfilename 文件名
     * @param sheetName   sheet页名
     * @param headers     表头集合
     * @param fieldNames  表头对应的实体属性集合
     * @param dataList    数据
     * @param flag        第一列是否为序号列
     * @param mergeLine  合并列数，例如合并前 5  列 需要考虑有没有序号
     * @throws IOException
     */
    public static void createExcelForStyle(HttpServletResponse response, HttpServletRequest request, String extfilename, String sheetName, String[] headers, String[] fieldNames, List dataList, boolean flag,Integer mergeLine) throws IOException {
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            SXSSFWorkbook work = new SXSSFWorkbook(100);
            //创建工作表
            Sheet sheet = work.createSheet("a");
            //显示标题
            Row title_row = sheet.createRow(0);
            title_row.setHeight((short) (40 * 20));
            Row header_row = sheet.createRow(0);
            //创建表头 单元格的 显示样式
            CellStyle headStyle = setHeadCellStyle(work);
            int headcell_index=0;
            if(flag){//有序号
                sheet.setColumnWidth(headcell_index, 1600);
                Cell headcell = header_row.createCell(headcell_index);
                //应用样式到  单元格上
                headcell.setCellStyle(headStyle);
                headcell.setCellValue("序号");
                headcell_index=1;
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.setColumnWidth(headcell_index, 5000);
                Cell cell = header_row.createCell(headcell_index);
                cell.setCellStyle(headStyle);
                cell.setCellValue(headers[i]);
                headcell_index++;
            }
            //设置单元格格式
            CellStyle cellStyle = setCellStyle(work);
            //插入需导出的数据
            for (int i = 0; i < dataList.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Object obj = dataList.get(i);
                Class classType = obj.getClass();
                int cell_index=0;
                if(flag){//有序号
                    Cell cell = row.createCell(cell_index);
                    cell.setCellValue(i+1);
                    cell.setCellStyle(cellStyle);
                    cell_index=1;
                }
                for (int j = 0; j < fieldNames.length; j++) {
                    Cell cell = row.createCell(cell_index);
                    cell.setCellStyle(cellStyle);
                    String fieldName = fieldNames[j];
                    String firstLetter = fieldName.substring(0, 1).toUpperCase();
                    String getMethodName = "get" + firstLetter + fieldName.substring(1);
                    Method getMethod = classType.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(obj, new Object[]{});
                    if(value!=null){
                        if (value instanceof Date) {//日期类型
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                            cell.setCellValue(df.format(value));
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                    cell_index++;
                }
            }
            //合并单元格
//            mergeContent1(sheet,flag,mergeLine);



            String userAgent = request.getHeader("user-agent");
            if (userAgent != null && userAgent.indexOf("Firefox") >= 0 || userAgent.indexOf("Chrome") >= 0
                    || userAgent.indexOf("Safari") >= 0) {
                extfilename= new String((extfilename).getBytes(), "ISO8859-1");//IE浏览器
            } else {
                extfilename= URLEncoder.encode(extfilename,"UTF8"); //其他浏览器
            }
            response.setContentType("application/msexcel");
            response.setHeader("Content-disposition", "attachment;filename=\"" + extfilename + "\"");
            work.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    //合并单元格
    public static void mergeContent(Sheet sheet) throws Exception{
        boolean flag = false;
        int index = 0;
        int rownum = -1;
        if (sheet.getLastRowNum() >2) {
            // 循环导出的最大行数
            for(int i=2; i<sheet.getLastRowNum()-1; ++i) {
                Map<Integer, Object> map1 = new HashMap<Integer, Object>();
                Map<Integer, Object> map2 = new HashMap<Integer, Object>();
                String frontRow = "";
                String afterRow = "";
                // 循环前面4列
                for(int j=0; j<4; ++j) {
                    Cell cell2 = sheet.getRow(i).getCell(j);
                    Cell cell3 = sheet.getRow(i+1).getCell(j);

                    frontRow = cell2.getStringCellValue();
                    afterRow = cell3.getStringCellValue();
                    map1.put(j, frontRow);
                    map2.put(j, afterRow);
                }
                if(map1.equals(map2)) {
                    // 相同的时候， 标记改成true
                    flag = true;
                    index++;
                    if(rownum == -1) {
                        rownum = i;
                    }
                    continue;
                }else {
                    // 遇到不同， 且只有2列， 那么就合并。多列在想想
                    if(flag) {
                        for(int k=0; k<4; ++k) {
                            sheet.addMergedRegion(new CellRangeAddress(rownum, rownum+index, k, k));
                        }
                        flag = false;
                        index = 0;
                        rownum = -1;
                    }
                }
            }
        }
    }

    //合并单元格
    public static void mergeContent1(Sheet sheet,boolean f,Integer mergeLine) throws Exception{
        boolean flag = false;
        int index = 0;
        int rownum = -1;
        if (sheet.getLastRowNum() >2) {
            // 循环导出的最大行数
            for(int i=1; i<= sheet.getLastRowNum()-1; ++i) {
                Map<Integer, Object> map1 = new HashMap<Integer, Object>();
                Map<Integer, Object> map2 = new HashMap<Integer, Object>();
                String frontRow = "";
                String afterRow = "";
                // 循环前面5列
                for(int j=1; j< mergeLine; ++j) {
                    Cell cell2 = sheet.getRow(i).getCell(j);
                    Cell cell3 = sheet.getRow(i+1).getCell(j);

                    frontRow = cell2.getStringCellValue();
                    afterRow = cell3.getStringCellValue();
                    map1.put(j, frontRow);
                    map2.put(j, afterRow);
                }
                if(map1.equals(map2)) {
                    // 相同的时候， 标记改成true
                    flag = true;
                    index++;
                    if(rownum == -1) {
                        rownum = i;
                    }
                    continue;
                }else {
                    // 遇到不同，
                    if(flag) {
                        if(f){
                            for(int k=0; k<= mergeLine; ++k) {
                                sheet.addMergedRegion(new CellRangeAddress(rownum, rownum+index, k, k));
                            }
                        }else {
                            for(int k=0; k<= mergeLine -1; ++k) {
                                sheet.addMergedRegion(new CellRangeAddress(rownum, rownum+index, k, k));
                            }
                        }
                        flag = false;
                        index = 0;
                        rownum = -1;
                    }
                }
            }
        }
    }


    /**
     * 导出excel
     * @param extfilename 文件名
     * @param sheetName   sheet页名
     * @param headers     表头集合
     * @param fieldNames  表头对应的实体属性集合
     * @param dataList    数据
     * @param flag        第一列是否为序号列
     * @param mergeLine  合并列数，例如合并前 5  列 需要考虑有没有序号
     * @throws IOException
     */
    public static InputStream createExcel(String extfilename, String sheetName, String[] headers, String[] fieldNames, List dataList, boolean flag,Integer mergeLine) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            SXSSFWorkbook work = new SXSSFWorkbook(100);
            //创建工作表
            Sheet sheet = work.createSheet(sheetName);
            //显示标题
            Row title_row = sheet.createRow(0);
            title_row.setHeight((short) (40 * 20));
            Row header_row = sheet.createRow(0);
            //创建表头 单元格的 显示样式
            CellStyle headStyle = setHeadCellStyle(work);
            int headcell_index=0;
            if(flag){//有序号
                sheet.setColumnWidth(headcell_index, 1600);
                Cell headcell = header_row.createCell(headcell_index);
                //应用样式到  单元格上
                headcell.setCellStyle(headStyle);
                headcell.setCellValue("序号");
                headcell_index=1;
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.setColumnWidth(headcell_index, 5000);
                Cell cell = header_row.createCell(headcell_index);
                cell.setCellStyle(headStyle);
                cell.setCellValue(headers[i]);
                headcell_index++;
            }
            //设置单元格格式
            CellStyle cellStyle = setCellStyle(work);
            //插入需导出的数据
            for (int i = 0; i < dataList.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Object obj = dataList.get(i);
                Class classType = obj.getClass();
                int cell_index=0;
                if(flag){//有序号
                    Cell cell = row.createCell(cell_index);
                    cell.setCellValue(i+1);
                    cell.setCellStyle(cellStyle);
                    cell_index=1;
                }
                for (int j = 0; j < fieldNames.length; j++) {
                    Cell cell = row.createCell(cell_index);
                    cell.setCellStyle(cellStyle);
                    String fieldName = fieldNames[j];
                    String firstLetter = fieldName.substring(0, 1).toUpperCase();
                    String getMethodName = "get" + firstLetter + fieldName.substring(1);
                    Method getMethod = classType.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(obj, new Object[]{});
                    if(value!=null){
                        if (value instanceof Date) {//日期类型
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                            cell.setCellValue(df.format(value));
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                    cell_index++;
                }
            }
            //合并单元格
            mergeContent1(sheet,flag,mergeLine);

            work.write(out);
            InputStream excelStream = new ByteArrayInputStream(out.toByteArray());
            out.close();

            return  excelStream;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
        return null;
    }

    /**
     * 导出excel
     * @param response
     * @param request
     * @param extfilename 文件名
     * @param sheetName   sheet页名
     * @param headers     表头集合
     * @param fieldNames  表头对应的实体属性集合
     * @param dataList    数据
     * @param flag        第一列是否为序号列
     * @throws IOException
     */
    public static void createExcel(HttpServletResponse response, HttpServletRequest request, String extfilename, String sheetName, String[] headers, String[] fieldNames, List dataList, boolean flag) throws IOException {
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            //XSSFWorkbook work = new XSSFWorkbook();
            SXSSFWorkbook work = new SXSSFWorkbook(100);
            //创建工作表
            Sheet sheet = work.createSheet(sheetName);
            //显示标题
            Row title_row = sheet.createRow(0);
            title_row.setHeight((short) (40 * 20));
            Row header_row = sheet.createRow(0);
            //创建单元格的 显示样式
            CellStyle style = work.createCellStyle();
//            style.setAlignment(HorizontalAlignment.CENTER); //水平方向上的对其方式
//            style.setVerticalAlignment(VerticalAlignment.CENTER);  //垂直方向上的对其方式
            //字体格式
            Font font = work.createFont();
            font.setFontName("仿宋_GB2312");
            font.setBold(true);//粗体显示
            font.setFontHeightInPoints((short) 12);
            style.setFont(font);//选择需要用到的字体格式
//            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 0));
            int headcell_index=0;
            if(flag){//有序号
                sheet.setColumnWidth(headcell_index, 1600);
                Cell headcell = header_row.createCell(headcell_index);
                //应用样式到  单元格上
                headcell.setCellStyle(style);
                headcell.setCellValue("序号");
                headcell_index=1;
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.setColumnWidth(headcell_index, 5000);
                Cell cell = header_row.createCell(headcell_index);
                cell.setCellStyle(style);
                cell.setCellValue(headers[i]);
                headcell_index++;
            }
            //插入需导出的数据
            for (int i = 0; i < dataList.size(); i++) {
                Row row = sheet.createRow(i + 1);
                row.setHeight((short) (20 * 20)); //设置行高  基数为20
                Object obj = dataList.get(i);
                Class classType = obj.getClass();
                int cell_index=0;
                if(flag){//有序号
                    Cell cell = row.createCell(cell_index);
                    cell.setCellValue(i+1);
                    cell_index=1;
                }
                for (int j = 0; j < fieldNames.length; j++) {
                    Cell cell = row.createCell(cell_index);
                    String fieldName = fieldNames[j];
                    String firstLetter = fieldName.substring(0, 1).toUpperCase();
                    String getMethodName = "get" + firstLetter + fieldName.substring(1);
                    Method getMethod = classType.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(obj, new Object[]{});
                    if(value!=null){
                        if (value instanceof Date) {//日期类型
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                            cell.setCellValue(df.format(value));
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                    cell_index++;
                }
            }
//            String agent = request.getHeader("USER-AGENT").toLowerCase();
            //根据浏览器类型处理文件名称
//            if (agent.indexOf("msie") > -1) {
//                extfilename = URLEncoder.encode(extfilename, "UTF-8");
//            } else {  //firefox/safari不转码
//                extfilename = new String(extfilename.getBytes("UTF-8"), "ISO8859-1");
//            }
            String userAgent = request.getHeader("user-agent");
            if (userAgent != null && userAgent.indexOf("Firefox") >= 0 || userAgent.indexOf("Chrome") >= 0
                    || userAgent.indexOf("Safari") >= 0) {
                extfilename= new String((extfilename).getBytes(), "ISO8859-1");//IE浏览器
            } else {
                extfilename= URLEncoder.encode(extfilename,"UTF8"); //其他浏览器
            }
            response.setContentType("application/msexcel");
            response.setHeader("Content-disposition", "attachment;filename=\"" + extfilename + "\"");
            work.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }





    /**
     *
     * @param work         工作表
     * @param fieldNames  字段名
     * @param dataList    数据
     * @param line         上次插入行数
     * @param flag         是否有序号
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws ReflectiveOperationException
     */
    public static void insertDataToExcel(SXSSFWorkbook work,  String[] fieldNames, List dataList,int line, boolean flag) throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, ReflectiveOperationException{

        Sheet sheet=work.getSheetAt(0);

        //插入需导出的数据
        for (int i=0; i < dataList.size(); i++) {
            Row row = sheet.createRow(line+i + 1);
            row.setHeight((short) (20 * 20)); //设置行高  基数为20
            Object obj = dataList.get(i);
            Class classType = obj.getClass();
            int cell_index=0;
            if(flag){//有序号
                Cell cell = row.createCell(cell_index);
                cell.setCellValue(line+i+1);
                cell_index=1;
            }
            for (int j = 0; j < fieldNames.length; j++) {
                Cell cell = row.createCell(cell_index);
                String fieldName = fieldNames[j];
                String firstLetter = fieldName.substring(0, 1).toUpperCase();
                String getMethodName = "get" + firstLetter + fieldName.substring(1);
                Method getMethod = classType.getMethod(getMethodName, new Class[]{});
                Object value = getMethod.invoke(obj, new Object[]{});
                if(value!=null){
                    if (value instanceof Date) {//日期类型
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                        cell.setCellValue(df.format(value));
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
                cell_index++;
            }
        }
    }

    /**
     *
     * @param work         Excel 表格
     * @param extfilename  文件名
     * @param docsPath    文件路径
     * @return
     */
    public static String uploadExcel(SXSSFWorkbook work,String extfilename,String docsPath,String modelName){
        OutputStream os = null;
        String path = "temp"+File.separator+"erp_web_templates"+File.separator+modelName+File.separator;
        String filepath="";
        try {
            delAllFile(filepath);
            Date dNow = new Date();   //当前时间
            SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); //格式化当前系统日期
            String dateTime = dateFm.format(dNow);
            String random = System.currentTimeMillis()+"";
            path =path+dateTime+File.separator+"export_"+random+File.separator;
            String folder = docsPath +path;
            filepath = folder+ extfilename;
            System.out.println("path=="+path);
            System.out.println("folder=="+folder);
            System.out.println("filepath=="+filepath);
            boolean b = customBufferStreamCopy(filepath,folder);
            if(b){
                os = new FileOutputStream(filepath);
                work.write(os);
            }
        } catch (UnsupportedEncodingException e) {
            path = "";
            logger.error("数据解析异常",e);
        } catch (IOException e) {
            path = "";
            logger.error("数据解析异常",e);
        } finally {
            if(null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("输出流关闭异常",e);
                }
            }
        }

        return  path;
    }

    private static boolean customBufferStreamCopy( String target, String folder) throws IOException {
        boolean b = false;
        try {
            File fileroot = new File(folder);
            if (!fileroot.exists()) {
                fileroot.mkdirs();
            }
            // 判断目标文件是否存在
            File targetFile = new File(target);
            if (targetFile.exists()) {
                // 如果目标文件存在并允许覆盖
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                new File(target).delete();
                targetFile = new File(target);
            }else{
                targetFile.createNewFile();
            }
            b = true;
        }
        catch (Exception e) {
            b = false;
        }
        return b;
    }

    // param path 文件夹完整绝对路径
    // 删除指定文件夹下所有文件过滤 是不是文件且文件夹是今天之前的
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                if (temp.getPath().indexOf("export_") > -1) {
                    temp.delete();
                }else {
                    continue;
                }
            }
            if (temp.isDirectory()) {
                String dateTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                if (!dateTime.equals(tempList[i])) {
                    delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                    delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                    flag = true;
                }
            }
        }
        return flag;
    }
    // 删除文件夹
    // param folderPath 文件夹完整绝对路径
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //测试
    public static void main(String[] args) throws Exception {
        SXSSFWorkbook work = new SXSSFWorkbook(100);
        //创建工作表
        Sheet sheet = work.createSheet("a");
        //显示标题
        Row title_row = sheet.createRow(0);
        title_row.setHeight((short) (40 * 20));
        Row header_row = sheet.createRow(0);

    }

}
