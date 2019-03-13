package ludwig.samuel.thinn.data;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import ludwig.samuel.thinn.BR;


public class User extends BaseObservable {
    private static User instance;
    private String sex = "male";
    private int age = 25;
    private int height = 197;
    private int weight = 120;
    private double activity = 1.53;
    private int tdee = 0;
    private int delta = -500;
    private int dailyCalories = 0;

    public static User getInstance() {
        if(instance == null) {
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
    public double getActivity() {
        return activity;
    }

    @Bindable
    public int getTdee() {
        tdee = (int)(activity * ((10*weight)+(6.25*height)-(5*age) + (sex.equals("male") ? 5 : -161)) + 0.9);
        return tdee;
    }

    @Bindable
    public int getDelta() {
        return delta;
    }

    @Bindable
    public int getDailyCalories() {
        dailyCalories = getTdee() + delta;
        return dailyCalories;
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

    public void setActivity(double activity) {
        this.activity = activity;
        setTdee();
        notifyPropertyChanged(BR.activity);
    }

    public void setTdee() {
        notifyPropertyChanged(BR.tdee);
    }

    public void setDelta(int delta) {
        this.delta = delta;
        setDailyCalories(tdee+delta);
        notifyPropertyChanged(BR.delta);
    }

    public void setDailyCalories(int dailyCalories) {
        this.dailyCalories = dailyCalories;
        notifyPropertyChanged(BR.dailyCalories);
    }
}
