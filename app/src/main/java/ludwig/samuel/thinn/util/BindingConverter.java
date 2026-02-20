package ludwig.samuel.thinn.util;

import androidx.databinding.InverseMethod;
import android.widget.EditText;

public class BindingConverter {

    @InverseMethod("stringToInt")
    public static String intToString(EditText view, int oldValue, int value) {
        if(value == 0) {
            return "";
        }
        if(value != oldValue) {
            return String.valueOf(value);
        }
        return String.valueOf(oldValue);
    }

    public static int stringToInt(EditText view, int oldValue, String value) {
        if(!value.equals(String.valueOf(oldValue))) {
            if(!value.isEmpty()) {
                try {
                    return Integer.valueOf(value);
                } catch (NumberFormatException nfE) {
                    return 0;
                }

            }
            return 0;
        }
        return oldValue;
    }

    @InverseMethod("stringToDouble")
    public static String doubleToString(EditText view, double oldValue, double value) {
        if(value != oldValue) {
            return String.valueOf(value);
        }
        return String.valueOf(oldValue);
    }

    public static double stringToDouble(EditText view, double oldValue, String value) {
        if(!value.equals(String.valueOf(oldValue))) {
            if(!value.isEmpty()) {
                try {
                    return Double.valueOf(value);
                } catch (NumberFormatException nfE) {
                    return 0;
                }
            }
            return 0;
        }
        return oldValue;
    }
}
