package net.xiaoxiangshop.util.ExcelUtil;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Auther wyz
 * @Date 2018/8/8 13:54
 * @Description
 */
public class Export extends AbstractExcel {


    /**
     * 导出 并返回对象
     *
     * @return
     */
    public static SXSSFWorkbook exportFormap(Map<String, List<Map<String, Object>>> data) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        if (data == null || data.size() == 0) {
            return null;
        }
        SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet(data.keySet().iterator().next());
        //设定列标题
        int rowNum = sheet.getLastRowNum() == 0 ? 0 : sheet.getLastRowNum() ;
        SXSSFRow row = (SXSSFRow) sheet.createRow(rowNum);
        SXSSFCell cell = null;
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        //取第第一条数据
        Map<String, Object> stringObjectMap = data.values().iterator().next().get(0);
        int cellNum = 0;
        for (String columnName : stringObjectMap.keySet()) {
            int width =3000;
            if(stringObjectMap.get(columnName)!=null){
                if(columnName.length()<String.valueOf(stringObjectMap.get(columnName)).length()){
                  width =  String.valueOf(stringObjectMap.get(columnName)).length()*300;
                }
            }
            sheet.setColumnWidth(cellNum, width);
            cell = (SXSSFCell) row.createCell(cellNum);
            cellNum++;
            cell.setCellValue(columnName);
        }

        //存放值
        List<Map<String, Object>> next = data.values().iterator().next();
        for (Map<String, Object> objectMap : next) {
            SXSSFCell cell2 = null;
            SXSSFRow row1 = (SXSSFRow) sheet.createRow(sheet.getLastRowNum() + 1);
            int num = 0;
            for (String s : objectMap.keySet()) {
                cell2 = (SXSSFCell) row1.createCell(num);
                if(objectMap.get(s)!=null){
                    if(isDouble(objectMap.get(s).toString())){
                        setValueForBigDecimal(cell2,objectMap.get(s),null);
                    }else{
                        cell2.setCellValue(objectMap.get(s).toString());
                    }

                }
                num++;
            }
        }
        return workbook;
    }

    public static boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    /*
     * 导出 并返回对象
     *
     * @return
     */
    public static SXSSFWorkbook export(List<?> data) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        return export(workbook, data);
    }

    /**
     * 导出 并返回对象
     *
     * @return
     */
    public static SXSSFWorkbook export(List<?> data, String sheet) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        return export(workbook, data, sheet);
    }

    /**
     * 导出 并返回对象
     *
     * @return
     */
    public static SXSSFWorkbook export(SXSSFWorkbook workbook, List<?> data) throws Exception {
        if (data == null || data.size() == 0) {
            return null;
        }
        set(workbook, data);
        return workbook;
    }

    public static SXSSFWorkbook exportSplit(Map<String, List<?>> data) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        return exportSplit(workbook, data);
    }

    public static SXSSFWorkbook exportSplit(SXSSFWorkbook workbook, Map<String, List<?>> data) throws Exception {
        setSplit(workbook, data);
        return workbook;
    }

    /**
     * 导出单个实例模板
     *
     * @return
     */
    public static SXSSFWorkbook exportExample(Object object) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        Excel annotation = object.getClass().getAnnotation(Excel.class);
        if (annotation == null) {
            return null;
        }
        setExample(workbook, object, annotation.sheet());
        return workbook;
    }


    private static Map<Field, Excel> setExample(SXSSFWorkbook workbook, Object data, String sheetName) throws Exception {

        SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet(sheetName);
        Map<Field, Excel> fieldExcelMap = init(data.getClass());
        writeDataItem(sheet, fieldExcelMap, data);
        return fieldExcelMap;
    }


    /**
     * 导出 并返回对象
     *
     * @return
     */
    public static SXSSFWorkbook export(SXSSFWorkbook workbook, List<?> data, String sheetName) throws Exception {
        if (data == null || data.size() == 0) {
            return null;
        }
        set(workbook, data, sheetName);
        return workbook;
    }


    private static Map<Field, Excel> set(SXSSFWorkbook workbook, List<?> data) throws Exception {

        Object obj = data.get(0);
        Excel annotation = obj.getClass().getAnnotation(Excel.class);
        if (annotation == null) {
            return null;
        }
        return set(workbook, data, annotation.sheet());
    }


    private static Map<Field, Excel> set(SXSSFWorkbook workbook, List<?> data, String sheetName) throws Exception {

        Object obj = data.get(0);
        Excel annotation = obj.getClass().getAnnotation(Excel.class);
        if (annotation == null) {
            return null;
        }
        SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet(sheetName);

        Map<Field, Excel> fieldExcelMap = init(obj.getClass());
        setWidth(sheet, fieldExcelMap);
//        if(annotation.row() != -1){//初始化标题列一般的这种标题是横跨所有字段的
//            SXSSFRow row = sheet.createRow(annotation.row());
//            //合并列
//            for(int i = 0;i < getMaxIndex(fieldExcelMap);i++){
//                row.createCell(i);
//            }
//            sheet.addMergedRegion(new CellRangeAddress(annotation.row(),annotation.row()+1,getMinIndex(fieldExcelMap),getMaxIndex(fieldExcelMap)));
//        }
        //设定列标题
        setCellTitle(sheet, fieldExcelMap);
        writeData(sheet, fieldExcelMap, data);
        return fieldExcelMap;
    }

    private static Map<Field, Excel> setSplit(SXSSFWorkbook workbook, Map<String, List<?>> data) throws Exception {
        for (Map.Entry<String, List<?>> entry : data.entrySet()) {
            set(workbook, entry.getValue(), entry.getKey());
        }
        return new HashMap<>();
    }

    private static void writeDataItem(SXSSFSheet sheet, Map<Field, Excel> fieldExcelMap, Object o) throws Exception {
        SXSSFRow row = (SXSSFRow) sheet.createRow(sheet.getLastRowNum() + 1);
        SXSSFCell cell = null;
        for (Map.Entry<Field, Excel> entry : fieldExcelMap.entrySet()) {
            cell = (SXSSFCell) row.createCell(entry.getValue().column());
            Object value = null;
            PropertyDescriptor pd = new PropertyDescriptor(entry.getKey().getName(), o.getClass());
            Method rM = pd.getReadMethod();//获得读方法
            value = rM.invoke(o);//
            if (value instanceof String) { //字符串格式
                setValueForString(cell, value, entry.getValue());
            } else if (value instanceof Integer) {//Integer
                setValueForInt(cell, value, entry.getValue());
            } else if (value instanceof Double) {//double
                setValueForDouble(cell, value, entry.getValue());
            } else if (value instanceof Long) { //long
                setValueForLong(cell, value, entry.getValue());
            } else if (value instanceof Date) { //date
                setValueForDate(cell, value, entry.getValue());
            } else if (value instanceof Short) { //short
                setValueForShort(cell, value, entry.getValue());
            } else if (value instanceof BigDecimal) { //BigDecimal
                setValueForBigDecimal(cell, value, entry.getValue());
            } else if (entry.getValue().page()) { //如果是sheet页对象
                set((SXSSFWorkbook) sheet.getWorkbook(), (List) value, entry.getValue().name());
            } else {
                setValueForString(cell, value, entry.getValue());
            }
        }

    }

    /**
     * 写入数据
     *
     * @param sheet
     * @param fieldExcelMap
     * @param data
     */
    private static void writeData(SXSSFSheet sheet, Map<Field, Excel> fieldExcelMap, List<?> data) throws Exception {
        for (Object o : data) {
            writeDataItem(sheet, fieldExcelMap, o);
        }
    }

    /**
     * 设置String类型
     *
     * @param cell
     * @param value
     */
    private static void setValueForString(SXSSFCell cell, Object value, Excel excel) {
        String val = null;
        if (null == value) {
            val = "";
        } else {
            val = String.valueOf(value);
        }
        cell.setCellValue(val);
    }

    /**
     * 设置Int类型
     *
     * @param cell
     * @param value
     */
    private static void setValueForInt(SXSSFCell cell, Object value, Excel excel) {
        Integer val = null;
        if (null == value) {
            setValueForString(cell, null, null);
        } else {
            val = Integer.parseInt(String.valueOf(value));
            cell.setCellValue(val);
        }
    }

    /**
     * 设置double类型
     *
     * @param cell
     * @param value
     */
    private static void setValueForDouble(SXSSFCell cell, Object value, Excel excel) {
        Double val = null;
        if (null == value) {
            setValueForString(cell, null, null);
        } else {
            val = Double.parseDouble(String.valueOf(value));
            parseFloatingDecimal(cell, excel, val, Double.class);
        }
    }


    /**
     * 设置double类型
     *
     * @param cell
     * @param value
     */
    private static void setValueForFloat(SXSSFCell cell, Object value, Excel excel) {
        Float val = null;
        if (null == value) {
            setValueForString(cell, null, null);
        } else {
            val = Float.parseFloat(String.valueOf(value));
            parseFloatingDecimal(cell, excel, val, Float.class);
        }
    }

    /**
     * 格式化数字
     *
     * @param cell
     * @param excel
     * @param val
     * @param clzz
     */
    private static void parseFloatingDecimal(SXSSFCell cell, Excel excel, Number val, Class clzz) {
        if (!excel.fromat().equals("")) { //格式化数字
            DecimalFormat formater = new DecimalFormat(excel.fromat());
            formater.setRoundingMode(excel.roundingMode());
            String valStr = formater.format(val);
            setValueForString(cell, valStr, null);
        } else {
            if (clzz == Double.class) {
                cell.setCellValue(val.doubleValue());
            } else if (clzz == Float.class) {
                cell.setCellValue(val.floatValue());
            } else if (clzz == Short.class) {
                cell.setCellValue(val.shortValue());
            } else {
                setValueForString(cell, val, null);
            }

        }
    }


    /**
     * 设置Long
     *
     * @param cell
     * @param value
     * @param excel
     */
    private static void setValueForLong(SXSSFCell cell, Object value, Excel excel) {
        Long val = null;
        if (null == value) {
            setValueForString(cell, null, null);
        } else {
            val = Long.parseLong(String.valueOf(value));
            cell.setCellValue(val);
        }
    }

    /**
     * 设置date
     *
     * @param cell
     * @param value
     * @param excel
     */
    private static void setValueForDate(SXSSFCell cell, Object value, Excel excel) {
        Date val = null;
        if (null == value) {
            setValueForString(cell, null, null);
        } else {
            val = (Date) value;
            SimpleDateFormat sf = null;
            if (!excel.fromat().equals("")) {
                sf = new SimpleDateFormat(excel.fromat());
            } else {
                sf = new SimpleDateFormat("yyyy-MM-dd");
            }
            try {
                String valStr = sf.format(val);
                cell.setCellValue(valStr);
            } catch (Exception e) {
                setValueForString(cell, null, null);
            }

        }
    }

    /**
     * 设置BigDecimal
     *
     * @param cell
     * @param value
     * @param excel
     */
    private static void setValueForBigDecimal(SXSSFCell cell, Object value, Excel excel) {
        BigDecimal val = null;
        if (null == value) {
            setValueForString(cell, null, null);
        } else {
            val = new BigDecimal(String.valueOf(value));
            cell.setCellValue(val.doubleValue());
        }
    }


    /**
     * 设置Long
     *
     * @param cell
     * @param value
     * @param excel
     */
    private static void setValueForShort(SXSSFCell cell, Object value, Excel excel) {
        Short val = null;
        if (null == value) {
            setValueForString(cell, null, null);
        } else {
            val = Short.parseShort(String.valueOf(value));
            cell.setCellValue(val);
        }
    }


    /**
     * 设置列宽
     *
     * @param sheet
     * @param fieldExcelMap
     */
    private static void setWidth(SXSSFSheet sheet, Map<Field, Excel> fieldExcelMap) {
        for (Map.Entry<Field, Excel> entry : fieldExcelMap.entrySet()) {
            sheet.setColumnWidth(entry.getValue().column(), entry.getValue().width());
        }
    }


    /**
     * 设置列标题
     *
     * @param sheet
     * @param fieldExcelMap
     */
    private static void setCellTitle(SXSSFSheet sheet, Map<Field, Excel> fieldExcelMap) {
        int rowNum = sheet.getLastRowNum() == 0 ? 0 : sheet.getLastRowNum() + 1;
        SXSSFRow row = (SXSSFRow) sheet.createRow(rowNum);
        SXSSFCell cell = null;

        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();

        for (Map.Entry<Field, Excel> entry : fieldExcelMap.entrySet()) {
            cell = (SXSSFCell) row.createCell(entry.getValue().column());
            cell.setCellValue(entry.getValue().name());
            if (entry.getValue().color() != -1) {
                cellStyle = sheet.getWorkbook().createCellStyle();
//                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setFillBackgroundColor(entry.getValue().color());
                cellStyle.setFillForegroundColor(entry.getValue().color());
                cell.setCellStyle(cellStyle);
            }
        }

    }


    /**
     * 获取最小的下标
     *
     * @param fieldExcelMap
     * @return
     */
    private static int getMinIndex(Map<Field, Excel> fieldExcelMap) {
        int temp = 10000;
        for (Map.Entry<Field, Excel> entry : fieldExcelMap.entrySet()) {
            if (temp > entry.getValue().column()) {
                temp = entry.getValue().column();
            }
        }
        return temp;
    }


    /**
     * 获取最大的下标
     *
     * @param fieldExcelMap
     * @return
     */
    private static int getMaxIndex(Map<Field, Excel> fieldExcelMap) {
        int temp = 0;
        for (Map.Entry<Field, Excel> entry : fieldExcelMap.entrySet()) {
            if (temp < entry.getValue().column()) {
                temp = entry.getValue().column();
            }
        }
        return temp;
    }


}
