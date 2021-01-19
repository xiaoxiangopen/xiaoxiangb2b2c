package net.xiaoxiangshop.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatesUtil {

    /**
     * 指定时间之前天或之后n天
     * @param date
     *@param num   正数：之后 负数：之前
     * @return
     */
    public static Date addDate(Date date,int num){
        Calendar calc =Calendar.getInstance();
        calc.setTime(new Date());
        calc.add(calc.DATE, num);
         date=calc.getTime();
         return date;
    }

    /**
     * 比较日期大小
     * @param beginDate
     * @param endDate
     * @return -1:beginDate<endDate  0:beginDate=endDate  1:beginDate>endDate
     */
    public static Integer compareToDate(Date beginDate,Date endDate){
        Integer compareTo = beginDate.compareTo(endDate);
        return compareTo;
    }
    /**
     * 比较日期大小
     * @param beginDate
     * @param endDate
     * @return -1:beginDate<endDate  0:beginDate=endDate  1:beginDate>endDate
     */
    public static Integer compareToDate(String beginDate,String  endDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = format.parse(beginDate);
            Date date2 = format.parse(endDate);
            int compareTo = date1.compareTo(date2);
            return compareTo;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStringDate(Date date,String format) {
            if(date==null){
                return null;
            }
          SimpleDateFormat formatter = new SimpleDateFormat(format);
           String dateString = formatter.format(date);
           return dateString;
         }
    public static void main(String[] arg){
        Date  ss=new Date();
        Date beginDate = DatesUtil.addDate(ss,-16);
        System.out.println(beginDate);
    }
}
