package cn.yanweijia.slimming.utils;

import android.databinding.BindingConversion;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by weijia on 02/10/2017.<br/>
 * <p>
 *     This class is write to convert data that DataBinding could not recognize <br/>
 *     参考:<a href="http://blog.csdn.net/zhuhai__yizhi/article/details/52924196">DataBinding 中 BindingConversion 的使用</a>
 *
 * </p>
 *
 * @author weijia
 */

public class DataBindingConverter {

    /**
     * convert Date to String
     * @param date
     * @return formated string
     */
    @BindingConversion
    public static String convertDateToString(java.util.Date date) {
        if(date!=null)
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
        else
            return null;
    }

    /**
     * convert BigDecimal to String
     * @param bigDecimal
     * @return
     * @author weijia
     */
    @BindingConversion
    public static String convertBigDecimalToString(BigDecimal bigDecimal){
        if(bigDecimal!=null)
            return bigDecimal.toString();
        else
            return null;
    }
}
