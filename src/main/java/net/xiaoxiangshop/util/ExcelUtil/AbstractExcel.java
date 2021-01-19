package net.xiaoxiangshop.util.ExcelUtil;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: wyz
 * @Date: 2018/9/22 20:01
 * @Description:
 * @Version 1.0
 */
public class AbstractExcel {


    /**
     * 初始化
     * @param
     * @return
     * @throws Exception
     */
    protected static Map<Field,Excel> init(Class clazz) throws Exception{
        Map<Field,Excel> fieldExcelMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field :fields){
            if(field.isAnnotationPresent(Excel.class)){
                Excel filedAnnot = field.getAnnotation(Excel.class);
                fieldExcelMap.put(field,filedAnnot);
            }
        }
        return fieldExcelMap;
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    protected static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    protected  static boolean isEnumber(String str){
        Pattern pattern = Pattern.compile("^[+-]?[\\d]+([.][\\d]*)?([Ee][+-]?[\\d]+)?$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;

    }

    /**
     * 大数值处理
     */
    protected static String bigNumber(Double d){
        return new DecimalFormat("#").format(d);
    }



    /**
     * 判断日期字符串
     * @return
     */
    protected static String getDateString(String val,String format) throws Exception{
        //匹配
        String pattern = "((^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(10|12|0?[13578])([-\\/\\._])(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(11|0?[469])([-\\/\\._])(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(0?2)([-\\/\\._])(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([3579][26]00)([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][0][48])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][0][48])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][2468][048])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][2468][048])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][13579][26])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][13579][26])([-\\/\\._])(0?2)([-\\/\\._])(29)$))";
        boolean isMatch = Pattern.matches(pattern, val);
        SimpleDateFormat wishFormat = new SimpleDateFormat(format); //期望得到的值
        SimpleDateFormat oldFormat = null;
        Date parse = null;
        String wishText = val;
        if(isMatch){
            pattern = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
            isMatch = Pattern.matches(pattern, val);
            if(isMatch){
                oldFormat  = new SimpleDateFormat("yyyy-MM-dd");
            } else {
                oldFormat  = new SimpleDateFormat("yyyy/MM/dd");
            }
            oldFormat.setLenient(true);
            parse = oldFormat.parse(val);
            wishText = wishFormat.format(parse);
        }
        return wishText;
    }

    /**
     * 判断是否是int
     * @param field
     * @return
     */
   protected static boolean isInteger(Field field){
        if(field.getType() == Integer.class || field.getType().getName().equals("int")){
            return true;
        }
        return false;
   }

    /**
     * 判断是否double
     * @param field
     * @return
     */
   protected static boolean isDouble(Field field){
       if(field.getType() == Double.class || field.getType().getName().equals("double")){
           return true;
       }
       return false;
   }

    /**
     * 判断是否Long
     * @param field
     * @return
     */
    protected  static boolean isLong(Field field){
        if(field.getType() == Long.class || field.getType().getName().equals("long")){
            return true;
        }
        return false;
    }

    /**
     * 判断是否Byte
     * @param field
     * @return
     */
    protected  static boolean isByte(Field field){
        if(field.getType() == Byte.class || field.getType().getName().equals("byte")){
            return true;
        }
        return false;
    }

    /**
     * 判断是否Short
     * @param field
     * @return
     */
    protected  static boolean isShort(Field field){
        if(field.getType() == Short.class || field.getType().getName().equals("short")){
            return true;
        }
        return false;
    }





}
