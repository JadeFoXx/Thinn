package ludwig.samuel.thinn.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import ludwig.samuel.thinn.BR;


public class User extends BaseObservable {
    private static User instance;
    private String sex = "male";
    private String age = "25";
    private String height = "197";
    private String weight = "120";
    private String neckCircumference = "45";
    private String waistCircumference = "126";
    private String hipCircumference = "140";
    private String activity = "1.2";
    private String delta = "-500";


    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    @Bindable
    public String getSex() {
        return this.sex;
    }

    @Bindable
    public String getAge() {
        return this.age;
    }

    @Bindable
    public String getHeight() {
        return this.height;
    }

    @Bindable
    public String getWeight() {
        return this.weight;
    }

    @Bindable
    public String getNeckCircumference() {
        return neckCircumference;
    }

    @Bindable
    public String getWaistCircumference() {
        return waistCircumference;
    }

    @Bindable
    public String getHipCircumference() {
        return hipCircumference;
    }

    private int sti(String string) {
        if(!string.isEmpty()) {
            return Integer.valueOf(string);
        }
        return 0;
    }

    private double std(String string) {
        if(!string.isEmpty()) {
            return Double.valueOf(string);
        }
        return 0;
    }


    @Bindable
    public int getBodyfatPercentage() {
        if (sex.equals("male")) {
            return (int) (495 / (1.0324 - 0.19077 * Math.log10(sti(waistCircumference) - sti(neckCircumference)) + 0.15456 * Math.log10(sti(height))) - 450 + 0.5);
        } else {
            return (int) (495 / (1.29579 - 0.35004 * Math.log10(sti(waistCircumference) + sti(hipCircumference) - sti(neckCircumference)) + 0.22100 * Math.log10(sti(height))) - 450 + 0.5);
        }
    }

    @Bindable
    public String getActivity() {
        return activity;
    }

    @Bindable
    public int getTdee() {
        double leanBodyMass = sti(weight) * (1 - (getBodyfatPercentage() / 100.0));
        double rdee = 370 + (21.6 * leanBodyMass);
        return (int) (std(activity) * rdee);
    }

    @Bindable
    public String getDelta() {
        return delta;
    }

    @Bindable
    public int getDailyCalories() {
        return getTdee() + sti(delta);
    }


    public void setSex(String sex) {
        this.sex = sex;
        setTdee();
        notifyPropertyChanged(BR.sex);
    }

    public void setAge(String age) {
        this.age = age;
        setTdee();
        notifyPropertyChanged(BR.age);
    }

    public void setHeight(String height) {
        this.height = height;
        setTdee();
        notifyPropertyChanged(BR.height);
    }

    public void setWeight(String weight) {
        this.weight = weight;
        setTdee();
        notifyPropertyChanged(BR.weight);
    }

    public void setNeckCircumference(String circumference) {
        this.neckCircumference = circumference;
        setBodyfatPercentage();
        notifyPropertyChanged(BR.neckCircumference);
    }

    public void setWaistCircumference(String circumference) {
        this.waistCircumference = circumference;
        setBodyfatPercentage();
        notifyPropertyChanged(BR.waistCircumference);
    }

    public void setHipCircumference(String circumference) {
        this.hipCircumference = circumference;
        setBodyfatPercentage();
        notifyPropertyChanged(BR.hipCircumference);
    }

    public void setBodyfatPercentage() {
        setTdee();
        notifyPropertyChanged(BR.bodyfatPercentage);
    }

    public void setActivity(String activity) {
        this.activity = activity;
        setTdee();
        notifyPropertyChanged(BR.activity);
    }

    public void setTdee() {
        setDailyCalories();
        notifyPropertyChanged(BR.tdee);
    }

    public void setDelta(String delta) {
        this.delta = delta;
        setDailyCalories();
        notifyPropertyChanged(BR.delta);
    }

    public void setDailyCalories() {
        notifyPropertyChanged(BR.dailyCalories);
    }

    public void save(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("thinn_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sex", sex);
        editor.putString("age", age);
        editor.putString("height", height);
        editor.putString("weight", weight);
        editor.putString("neckCircumference", neckCircumference);
        editor.putString("waistCircumference", waistCircumference);
        editor.putString("hipCircumference", waistCircumference);
        editor.putString("activity", String.valueOf(activity));
        editor.putString("delta", delta);
        editor.commit();
    }

    public void restore(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("thinn_user", Context.MODE_PRIVATE);
        sex = sharedPreferences.getString("sex", "male");
        age = sharedPreferences.getString("age", "25");
        height = sharedPreferences.getString("height", "197");
        weight = sharedPreferences.getString("weight", "120");
        neckCircumference = sharedPreferences.getString("neckCircumference", "45");
        waistCircumference = sharedPreferences.getString("waistCircumference", "126");
        hipCircumference = sharedPreferences.getString("hipCircumference", "140");
        activity = sharedPreferences.getString("activity", "1.2");
        delta = sharedPreferences.getString("delta", "-500");
    }
}
