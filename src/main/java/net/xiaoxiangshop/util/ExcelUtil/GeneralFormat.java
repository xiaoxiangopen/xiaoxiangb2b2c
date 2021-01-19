package net.xiaoxiangshop.util.ExcelUtil;

import org.apache.poi.ss.usermodel.DataFormatter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.*;
import java.util.Locale;

/**
 * @Auther: han
 * @Date: 2018/10/8 10:52
 * @Description:
 * @Version 1.0
 */
public class GeneralFormat extends Format {
    private static final long serialVersionUID = 1L;
    private static final MathContext TO_10_SF;
    private final DecimalFormatSymbols decimalSymbols;
    private final DecimalFormat integerFormat;
    private final DecimalFormat decimalFormat;
    private final DecimalFormat scientificFormat;

    public GeneralFormat(Locale locale) {
        this.decimalSymbols = DecimalFormatSymbols.getInstance(locale);
        this.scientificFormat = new DecimalFormat("#", this.decimalSymbols);
        DataFormatter.setExcelStyleRoundingMode(this.scientificFormat);
        this.integerFormat = new DecimalFormat("#", this.decimalSymbols);
        DataFormatter.setExcelStyleRoundingMode(this.integerFormat);
        this.decimalFormat = new DecimalFormat("#.##########", this.decimalSymbols);
        DataFormatter.setExcelStyleRoundingMode(this.decimalFormat);
    }

    public StringBuffer format(Object number, StringBuffer toAppendTo, FieldPosition pos) {
        if (!(number instanceof Number)) {
            return this.integerFormat.format(number, toAppendTo, pos);
        } else {
            double value = new BigDecimal(number+"").doubleValue();
            if (!Double.isInfinite(value) && !Double.isNaN(value)) {
                double abs = Math.abs(value);
                if (abs >= 1.0E11D || abs <= 1.0E-10D && abs > 0.0D) {
                    return this.scientificFormat.format(number, toAppendTo, pos);
                } else if (Math.floor(value) != value && abs < 1.0E10D) {
                    double rounded = (new BigDecimal(value)).round(TO_10_SF).doubleValue();
                    return this.decimalFormat.format(rounded, toAppendTo, pos);
                } else {
                    return this.integerFormat.format(number, toAppendTo, pos);
                }
            } else {
                return this.integerFormat.format(number, toAppendTo, pos);
            }
        }
    }

    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }

    static {
        TO_10_SF = new MathContext(10, RoundingMode.HALF_UP);
    }
}
