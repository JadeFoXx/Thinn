package ludwig.samuel.thinn.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.Deque;
import java.util.List;

import ludwig.samuel.thinn.BR;
import ludwig.samuel.thinn.util.Today;

public class Stats extends BaseObservable {
    private static Stats instance;
    private int caloriesConsumed = 0;
    private int caloriesLeft = 0;
    private Deque<Integer> recentCaloriesConsumed = new ArrayDeque<>();
    private long lastDate;

    public static Stats getInstance() {
        if(instance == null){
            instance = new Stats();
        }
        return instance;
    }

    @Bindable
    public int getCaloriesConsumed() {
        return caloriesConsumed;
    }

    public void setCaloriesConsumed(int consumed) {
        caloriesConsumed = consumed;
        notifyPropertyChanged(BR.caloriesConsumed);
    }

    public void consume(int amount) {
        this.caloriesConsumed += amount;
        this.caloriesLeft -= amount;
        if(amount > 0) {
            recentCaloriesConsumed.push(amount);
        }
        notifyPropertyChanged(BR.caloriesConsumed);
        notifyPropertyChanged(BR.caloriesLeft);
    }

    @Bindable
    public int getCaloriesLeft() {
        return caloriesLeft;
    }

    public void setCaloriesLeft(int caloriesLeft) {
        this.caloriesLeft = caloriesLeft;
        notifyPropertyChanged(BR.caloriesLeft);
    }

    public long getLastDate() {
        return lastDate;
    }

    public void setLastDate(long lastDate) {
        this.lastDate = lastDate;
    }

    public void undo() {
        if(recentCaloriesConsumed.size() > 0) {
            consume(-1* recentCaloriesConsumed.pop());
        }
    }

    public void save(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("thinn_stats", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("caloriesConsumed", caloriesConsumed);
        editor.putInt("caloriesLeft", caloriesLeft);
        editor.putString("recentCaloriesConsumed", dequeToJson(recentCaloriesConsumed));
        editor.putLong("lastDate", lastDate);
        editor.commit();
    }

    public void restore(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("thinn_stats", Context.MODE_PRIVATE);
        caloriesConsumed = sharedPreferences.getInt("caloriesConsumed", 0);
        caloriesLeft = sharedPreferences.getInt("caloriesLeft", User.getInstance().getDailyCalories());
        recentCaloriesConsumed = jsonToString(sharedPreferences.getString("recentCaloriesConsumed", null));
        lastDate = sharedPreferences.getLong("lastDate", -1);
        if(lastDate == -1 || lastDate != Today.get()) {
            lastDate = Today.get();
            setCaloriesLeft(User.getInstance().getDailyCalories());
            setCaloriesConsumed(0);
            recentCaloriesConsumed.clear();
        }
    }

    private String dequeToJson(Deque<Integer> deque) {
        Gson gson = new Gson();
        return gson.toJson(deque);
    }

    private Deque<Integer> jsonToString(String json) {
        Gson gson = new Gson();
        Deque<Integer> deque = new ArrayDeque<>();
        if(json != null) {
            deque.addAll((List<Integer>) gson.fromJson(json, new TypeToken<List<Integer>>() {
            }.getType()));
        }
        return deque;
    }

}
