package net.xiaoxiangshop.util.ExcelUtil;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
//import org.apache.poi.ss.usermodel.CellType;
//import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wyz
 * @Date: 2018/9/22 19:43
 * @Description: excel 导入
 * @Version 1.0
 */
public class Import extends AbstractExcel {


    public static <T> List<T> imp(XSSFWorkbook workbook, Class<T> clazz) throws Exception {

        Excel annotation = (Excel) clazz.getAnnotation(Excel.class);
        if (annotation == null) {
            return null;
        }
        XSSFSheet sheet = workbook.getSheet(annotation.sheet());
        //设定起始行
        int start = annotation.row();
        Map<Field, Excel> fieldExcelMap = init(clazz);
        List<T> list = new ArrayList<>();
        if(sheet!=null) {
            for (; start <= sheet.getLastRowNum(); start++) {
                XSSFRow row = sheet.getRow(start);
                T object = clazz.newInstance();
                for (Map.Entry<Field, Excel> entry : fieldExcelMap.entrySet()) {
                    XSSFCell cell = row.getCell(entry.getValue().column());
                    setValue(cell, entry, object);
                }
                list.add(object);
            }
        }
        return list;
    }

    public static <T> List<T> imp(InputStream is, Class<T> clazz) throws Exception {

        Excel annotation = (Excel) clazz.getAnnotation(Excel.class);
        if (annotation == null) {
            return null;
        }
        XSSFWorkbook workBook = new XSSFWorkbook(is);
        List<T> list = new ArrayList<T>();
        XSSFSheet sheet = workBook.getSheet(annotation.sheet());
        //设定起始行
        int start = 1;
        if(null == sheet){
            return null;
        }
        Map<Field, Excel> fieldExcelMap = init(clazz);
        //List<Object> list = new ArrayList<>();
        for (; start <= sheet.getLastRowNum(); start++) {
            XSSFRow row = sheet.getRow(start);
            T object = clazz.newInstance();
            for (Map.Entry<Field, Excel> entry : fieldExcelMap.entrySet()) {
                XSSFCell cell = row.getCell(entry.getValue().column());
                setValue(cell, entry, object);
            }
            list.add(object);
        }
        return list;
    }

    private static void setValue(XSSFCell cell, Map.Entry<Field, Excel> entry, Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException, ParseException {
//        Object value = null;
//        Field field = entry.getKey();
//        Excel excel = entry.getValue();
//        if (cell == null) {
//
//        } else if (field.getType() == String.class) { //字符串格式
//            if (!excel.dateFromat().equals("")) {
//                if (cell.getCellTypeEnum() == CellType.NUMERIC && HSSFDateUtil.isCellDateFormatted(cell)) {
//                    Date temp = cell.getDateCellValue();
//                    SimpleDateFormat sf = new SimpleDateFormat(excel.dateFromat());
//                    value = sf.format(temp);
//                } else {
//                    value = cell.getStringCellValue();
//                }
//            } else {
//                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
//                    value = bigNumber(cell.getNumericCellValue());
//                } else {
//                    value = cell.getStringCellValue();
//                }
//
//            }
//        } else if (isInteger(field)) {//Integer
//            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
//                value = new BigDecimal(cell.getNumericCellValue()).intValue();
//            } else {
//                String temp = cell.getStringCellValue();
//                if (!StringUtils.isEmpty(temp) && isNumeric(temp)) {
//                    value = new BigDecimal(temp).intValue();
//                } else {
//                    value = null;
//                }
//
//            }
//        } else if (isDouble(field)) {//double
//            if (!excel.fromat().equals("")) {
//                value = Double.parseDouble(new DecimalFormat(excel.fromat()).format(cell.getNumericCellValue()));
//            } else {
//                value = cell.getNumericCellValue();
//            }
//        } else if (field.getType() == Date.class) {//date
//            if (cell.getCellTypeEnum() == CellType.NUMERIC && HSSFDateUtil.isCellDateFormatted(cell)) {
//                Date temp = cell.getDateCellValue();
//                value = temp;
//            } else {
//                if(!StringUtils.isEmpty(cell.getStringCellValue())) {
//                    SimpleDateFormat sf = new SimpleDateFormat(excel.dateFromat());
//                    value = sf.parse(cell.getStringCellValue());
//                }
//            }
//        } else if (field.getType() == BigDecimal.class) {//BigDecimal
//            if (cell.getCellTypeEnum() == CellType.FORMULA || cell.getCellTypeEnum() == CellType.NUMERIC) {
//                if (!excel.fromat().equals("")) {
//                    value = new BigDecimal(new DecimalFormat(excel.fromat()).format(cell.getNumericCellValue()));
//                } else {
//                    value = new BigDecimal(cell.getNumericCellValue());
//                }
//            } else {
//                String temp = cell.getStringCellValue();
//                if (!StringUtils.isEmpty(temp) && isNumeric(temp)) {
//                    value = new BigDecimal(cell.getStringCellValue());
//                } else {
//                    value = null;
//                }
//            }
//        } else if (isByte(field)) { //byte
//            BigDecimal v1 = null;
//            if (cell.getCellTypeEnum() == CellType.FORMULA || cell.getCellTypeEnum() == CellType.NUMERIC) {
//                v1 = new BigDecimal(cell.getNumericCellValue());
//            } else {
//                if( null != cell.getStringCellValue())
//                v1 = new BigDecimal(cell.getStringCellValue());
//            }
//            if(v1!=null){
//                value = new BigDecimal(cell.getNumericCellValue()).byteValue();
//            }
//
//        } else if (isShort(field)) { //short
//            BigDecimal v1 = null;
//            if (cell.getCellTypeEnum() == CellType.FORMULA || cell.getCellTypeEnum() == CellType.NUMERIC) {
//                v1 = new BigDecimal(cell.getNumericCellValue());
//            } else {
//                if( null != cell.getStringCellValue())
//                    v1 = new BigDecimal(cell.getStringCellValue());
//            }
//            if(v1!=null){
//                value= new BigDecimal(cell.getNumericCellValue()).shortValue();
//            }
//        } else if (isLong(field)) {//Long
//            BigDecimal v1 = null;
//            if (cell.getCellTypeEnum() == CellType.FORMULA || cell.getCellTypeEnum() == CellType.NUMERIC) {
//                v1 = new BigDecimal(cell.getNumericCellValue());
//            } else {
//                if( null != cell.getStringCellValue()&&cell.getStringCellValue()!="")
//                    v1 = new BigDecimal(cell.getStringCellValue());
//            }
//            if(v1!=null){
//                value = new BigDecimal(cell.getNumericCellValue()).longValue();
//            }
//        } else {
//            System.out.println(field.getType());
//            value = cell.getStringCellValue();
//        }
//        if(value != null){
//            PropertyDescriptor pd = null;
//            try {
//                pd = new PropertyDescriptor(entry.getKey().getName(), obj.getClass());
//                Method rM = pd.getWriteMethod();//获得写方法
//                rM.invoke(obj, value);
//            } catch (Exception e) {
//                throw e;
//            }
//        }
    }


    public static void main(String[] args) {
        Integer i = 0;
        Class integerClass = Integer.class;

        System.out.println((integerClass.isPrimitive()));

    }
}
