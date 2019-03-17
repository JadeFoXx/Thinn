package ludwig.samuel.thinn.util;
import java.util.Calendar;

public class Today {
    public static long get() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.MILLISECOND, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.HOUR_OF_DAY, 0);
        return today.getTimeInMillis();
    }
}
