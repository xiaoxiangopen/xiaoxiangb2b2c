package net.xiaoxiangshop.util.ExcelUtil;

import org.apache.poi.openxml4j.opc.OPCPackage;

import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Auther: han
 * @Date: 2018/10/5 14:52
 * @Description:
 * @Version 1.0
 */
public class ImportBig extends AbstractExcel{


    /**
     * 大批量导入
     * @param is
     * @param clazz
     * @return
     * @throws Exception
     */
    public static LinkedHashMap imp(FileInputStream is, Class clazz) throws Exception{
        Excel annotation = (Excel) clazz.getAnnotation(Excel.class);
        if(annotation == null){
            return null;
        }
        try {
            OPCPackage p = OPCPackage.open(is);
            ReadExcelUtil readExcelUtil = new ReadExcelUtil(p);
            Map<Field,Excel> fieldExcelMap = init(clazz);
            readExcelUtil.setFieldExcelMap(fieldExcelMap);
            readExcelUtil.setClazz(clazz);
            readExcelUtil.process();
            LinkedHashMap<Integer, Object> result = readExcelUtil.getDatas();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 大批量导入
     * @param is
     * @param clazz
     * @return
     * @throws Exception
     */
    public static void imp(FileInputStream is, Class clazz,ReadExcelSevice readExcelSevice) throws Exception{
        Excel annotation = (Excel) clazz.getAnnotation(Excel.class);
        if(annotation == null){
            return;
        }
        try {
            OPCPackage p = OPCPackage.open(is);
            ReadExcelUtil readExcelUtil = new ReadExcelUtil(p);
            Map<Field,Excel> fieldExcelMap = init(clazz);
            readExcelUtil.setFieldExcelMap(fieldExcelMap);
            readExcelUtil.setClazz(clazz);
            readExcelUtil.setReadExcelSevice(readExcelSevice);
            readExcelUtil.process();
            readExcelUtil.getDatas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void  setValue(String cell,Map.Entry<Field,Excel> entry,Object obj) throws Exception {
        Object value = null;
        Field field = entry.getKey();
        Excel excel = entry.getValue();
        if(cell == null){

        } else if(field.getType() == String.class){ //字符串格式
            if(!excel.dateFromat().equals("")){
                value = getDateString(cell,excel.dateFromat());
            } else if(isNumeric(cell) ||isEnumber(cell)) {
                String value1 = new BigDecimal(cell).toPlainString();

                value = value1;
            } else {
                value = cell;
            }
        } else if(field.getType() == Integer.class){//Integer
            if(isNumeric(cell) ||isEnumber(cell)) {
                value = new BigDecimal(cell).intValue();
            }
        } else if(field.getType() == Double.class){//double
            if(isNumeric(cell)||isEnumber(cell)){
                value =  Double.parseDouble(new DecimalFormat(excel.fromat()).format(cell));
            }
        } else if(field.getType() == BigDecimal.class){//BigDecimal
            if(isNumeric(cell)||isEnumber(cell)){
                value = new BigDecimal(new DecimalFormat(excel.fromat()).format( Double.parseDouble(cell)));
            }

        } else if (field.getType()== Short.class){ //short
            if(isNumeric(cell)||isEnumber(cell)){
                value = new BigDecimal(cell).intValue();
            }
        } else if(field.getType() == Long.class){//Long
            if(isNumeric(cell)||isEnumber(cell)){
                value = new BigDecimal(cell).longValue();
            }
        } else {
            value = cell;
        }
        PropertyDescriptor pd = null;
        try {
            pd = new PropertyDescriptor(entry.getKey().getName(), obj.getClass());
            Method rM = pd.getWriteMethod();//获得写方法
            rM.invoke(obj,value);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void main(String[] args) {
        try {
           // FileInputStream is = new FileInputStream("E:\\doc\\主数据\\武汉\\9月\\9月新增模板-湖北bak.xlsx");
            Long sTime = System.currentTimeMillis();
            FileInputStream is = new FileInputStream("E:\\doc\\主数据\\重庆\\9月\\9月销售.xlsx");
//
//            OPCPackage p = OPCPackage.open(is);
//            ReadExcelUtil readExcelUtil = new ReadExcelUtil(p);
//            readExcelUtil.process();
//            LinkedHashMap<Integer,Object>  result = new LinkedHashMap<Integer,Object>();
//            LinkedHashMap<Integer, Map<String, String>> datas = readExcelUtil.getDatas();
//            for(Map.Entry<Integer,Map<String, String>> item:datas.entrySet()){
//                System.out.println(item.getValue().get("AG"));
//            }

//            LinkedHashMap<Integer,ExcelData> imp = ImportBig.imp(is, ExcelData.class);
//            for(Map.Entry<Integer,ExcelData>  item:imp.entrySet()){
//                ExcelData excelData = item.getValue();
//                System.out.print(item.getKey()+"---->");
//                System.out.print(excelData.getBillTypeName()+",");
//                System.out.print(excelData.getBillNo()+",");
//                System.out.print(excelData.getSupplierNo()+",");
//                System.out.print(excelData.getProductCode()+",");
//                System.out.print(excelData.getDate()+",");
//                System.out.print(excelData.getTime()+",");
//                System.out.print(excelData.getPrice()+",");
//                System.out.print(excelData.getBillNum()+",");
//                System.out.print(excelData.getBarcode()+",");
//                System.out.println(excelData.getCreateDate());
//         }

    //        ImportBig.imp(is,ExcelData.class,new DemoReadExcelServiceImpl());
            Long eTime = System.currentTimeMillis();
            System.out.println("执行耗时:"+((eTime-sTime)/1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
