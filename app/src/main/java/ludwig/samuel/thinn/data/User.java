package ludwig.samuel.thinn.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import ludwig.samuel.thinn.BR;


public class User extends BaseObservable {
    private static User instance;
    private String sex = "male";
    private int age = 25;
    private int height = 197;
    private int weight = 120;
    private int neckCircumference = 45;
    private int waistCircumference = 126;
    private int hipCircumference = 140;
    private double activity = 1.2;
    private int delta = -500;


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
    public int getAge() {
        return this.age;
    }

    @Bindable
    public int getHeight() {
        return this.height;
    }

    @Bindable
    public int getWeight() {
        return this.weight;
    }

    @Bindable
    public int getNeckCircumference() {
        return neckCircumference;
    }

    @Bindable
    public int getWaistCircumference() {
        return waistCircumference;
    }

    @Bindable
    public int getHipCircumference() {
        return hipCircumference;
    }


    @Bindable
    public int getBodyfatPercentage() {
        if (sex.equals("male")) {
            return (int) (495 / (1.0324 - 0.19077 * Math.log10(waistCircumference - neckCircumference) + 0.15456 * Math.log10(height)) - 450 + 0.5);
        } else {
            return (int) (495 / (1.29579 - 0.35004 * Math.log10(waistCircumference + hipCircumference - neckCircumference) + 0.22100 * Math.log10(height)) - 450 + 0.5);
        }
    }

    @Bindable
    public double getActivity() {
        return activity;
    }

    @Bindable
    public int getTdee() {
        double leanBodyMass = weight * (1 - (getBodyfatPercentage() / 100.0));
        double rdee = 370 + (21.6 * leanBodyMass);
        return (int) (activity * rdee);
    }

    @Bindable
    public int getDelta() {
        return delta;
    }

    @Bindable
    public int getDailyCalories() {
        return getTdee() - delta;
    }


    public void setSex(String sex) {
        this.sex = sex;
        setTdee();
        notifyPropertyChanged(BR.sex);
    }

    public void setAge(int age) {
        this.age = age;
        setTdee();
        notifyPropertyChanged(BR.age);
    }

    public void setHeight(int height) {
        this.height = height;
        setTdee();
        notifyPropertyChanged(BR.height);
    }

    public void setWeight(int weight) {
        this.weight = weight;
        setTdee();
        notifyPropertyChanged(BR.weight);
    }

    public void setNeckCircumference(int circumference) {
        this.neckCircumference = circumference;
        setBodyfatPercentage();
        notifyPropertyChanged(BR.neckCircumference);
    }

    public void setWaistCircumference(int circumference) {
        this.waistCircumference = circumference;
        setBodyfatPercentage();
        notifyPropertyChanged(BR.waistCircumference);
    }

    public void setHipCircumference(int circumference) {
        this.hipCircumference = circumference;
        setBodyfatPercentage();
        notifyPropertyChanged(BR.hipCircumference);
    }

    public void setBodyfatPercentage() {
        setTdee();
        notifyPropertyChanged(BR.bodyfatPercentage);
    }

    public void setActivity(double activity) {
        this.activity = activity;
        setTdee();
        notifyPropertyChanged(BR.activity);
    }

    public void setTdee() {
        setDailyCalories();
        notifyPropertyChanged(BR.tdee);
    }

    public void setDelta(int delta) {
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
        editor.putInt("age", age);
        editor.putInt("height", height);
        editor.putInt("weight", weight);
        editor.putInt("neckCircumference", neckCircumference);
        editor.putInt("waistCircumference", waistCircumference);
        editor.putInt("hipCircumference", waistCircumference);
        editor.putString("activity", String.valueOf(activity));
        editor.putInt("delta", delta);
        editor.commit();
    }

    public void restore(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("thinn_user", Context.MODE_PRIVATE);
        sex = sharedPreferences.getString("sex", "male");
        age = sharedPreferences.getInt("age", 25);
        height = sharedPreferences.getInt("height", 197);
        weight = sharedPreferences.getInt("weight", 120);
        neckCircumference = sharedPreferences.getInt("neckCircumference", 45);
        waistCircumference = sharedPreferences.getInt("waistCircumference", 126);
        hipCircumference = sharedPreferences.getInt("hipCircumference", 140);
        activity = Double.valueOf(sharedPreferences.getString("activity", "1.2"));
        delta = sharedPreferences.getInt("delta", -500);
    }
}
