package net.xiaoxiangshop.util.ExcelUtil;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.math.RoundingMode;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Auther wyz
 * @Date 2019年9月17日15:35:01
 * @Description
 */
@Target({FIELD,TYPE})
@Retention(RUNTIME)
@Documented
public @interface Excel {

    /**
     * 名称，字段名
     * @return
     */
    String name() default "";

    /**
     * 标题
     * @return
     */
    String title() default "";


    /**
     * 所在行标识，类使用决定起始行的位置
     * @return
     */
    int row() default -1;

    /**
     * 列标记
     * @return
     */
    int column() default 0;

    String cell() default "";

    /**
     * 格式化参数
     * @return
     */
    String fromat() default "";

    /**
     * 日期格式化函数，读取EXCEL时使用在String变量上
     * @return
     */
    String dateFromat() default  "";


    /**
     * 是否求和
     * @return
     */
    boolean sum() default false;

    /**
     * sheet
     * @return
     */
    String sheet() default "sheet1";

    /**
     * 大数据处理时对应的解析格式
     * @return
     */
    String dataFormatter() default "";
    /**
     * 宽度
     * @return
     */
    int width() default 3500;

    /**
     * 设置单元格颜色
     * @return
     */
    short color() default -1;

    /**
     * 宽度
     * @return
     */
    boolean page() default false;


    RoundingMode roundingMode() default  RoundingMode.HALF_UP;





}
