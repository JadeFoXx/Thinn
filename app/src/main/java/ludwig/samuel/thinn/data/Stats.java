package ludwig.samuel.thinn.data;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import ludwig.samuel.thinn.BR;
import ludwig.samuel.thinn.util.Today;

public class Stats extends BaseObservable {
    private static Stats instance;
    private int calorieLimit = 0;
    private Deque<Integer> recentCaloriesConsumed = new ArrayDeque<>();
    private Deque<String> recentMealNames = new ArrayDeque<>();
    private Deque<Integer> recentMealColors = new ArrayDeque<>();
    private long lastDate;

    public static Stats getInstance() {
        if(instance == null){
            instance = new Stats();
        }
        return instance;
    }

    @Bindable
    public int getCaloriesConsumed() {
        int caloriesConsumed = 0;
        for(Integer consumed : recentCaloriesConsumed) {
            caloriesConsumed += consumed;
        }
        return caloriesConsumed;
    }

    public void consume(int amount, String name, int color) {
        if(amount > 0) {
            recentCaloriesConsumed.push(amount);
            recentMealNames.push(name);
            recentMealColors.push(color);
        }
        notifyPropertyChanged(BR.caloriesConsumed);
        notifyPropertyChanged(BR.caloriesLeft);
    }

    public void consume(int amount, String name) {
        consume(amount, name, Meal.DEFAULT_COLOR);
    }

    public void consume(int amount) {
        consume(amount, String.valueOf(amount) + " cal", Meal.DEFAULT_COLOR);
    }

    @Bindable
    public int getCaloriesLeft() {
        int caloriesLeft = calorieLimit;
        for(Integer consumed : recentCaloriesConsumed) {
            caloriesLeft -= consumed;
        }
        return caloriesLeft;
    }

    @Bindable
    public int getCalorieLimit() {
        return calorieLimit;
    }

    public void setCalorieLimit(int calorieLimit) {
        this.calorieLimit = calorieLimit;
        notifyPropertyChanged(BR.caloriesConsumed);
        notifyPropertyChanged(BR.caloriesLeft);
    }

    public long getLastDate() {
        return lastDate;
    }

    public void setLastDate(long lastDate) {
        this.lastDate = lastDate;
    }

    public List<Integer> getConsumedSegments() {
        List<Integer> list = new ArrayList<>(recentCaloriesConsumed);
        Collections.reverse(list);
        return list;
    }

    public List<String> getConsumedNames() {
        List<String> list = new ArrayList<>(recentMealNames);
        Collections.reverse(list);
        return list;
    }

    public List<Integer> getConsumedColors() {
        List<Integer> list = new ArrayList<>(recentMealColors);
        Collections.reverse(list);
        return list;
    }

    public void undo() {
        if(!recentCaloriesConsumed.isEmpty()) {
            recentMealNames.pop();
            recentMealColors.pop();
            consume(-1* recentCaloriesConsumed.pop());
        }
    }

    public void save(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("thinn_stats", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("calorieLimit", calorieLimit);
        editor.putString("recentCaloriesConsumed", new Gson().toJson(recentCaloriesConsumed));
        editor.putString("recentMealNames", new Gson().toJson(recentMealNames));
        editor.putString("recentMealColors", new Gson().toJson(recentMealColors));
        editor.putLong("lastDate", lastDate);
        editor.commit();
    }

    public void restore(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("thinn_stats", Context.MODE_PRIVATE);
        calorieLimit = User.getInstance().getDailyCalories();
        recentCaloriesConsumed = jsonToInts(sharedPreferences.getString("recentCaloriesConsumed", null));
        recentMealNames = jsonToStrings(sharedPreferences.getString("recentMealNames", null));
        recentMealColors = jsonToInts(sharedPreferences.getString("recentMealColors", null));
        lastDate = sharedPreferences.getLong("lastDate", -1);
        if(lastDate == -1 || lastDate != Today.get()) {
            purge(context);
        }
    }

    private Deque<Integer> jsonToInts(String json) {
        Gson gson = new Gson();
        Deque<Integer> deque = new ArrayDeque<>();
        if(json != null) {
            List<Integer> list = gson.fromJson(json, new TypeToken<List<Integer>>() {}.getType());
            if(list != null) {
                deque.addAll(list);
            }
        }
        return deque;
    }

    private Deque<String> jsonToStrings(String json) {
        Gson gson = new Gson();
        Deque<String> deque = new ArrayDeque<>();
        if(json != null) {
            List<String> list = gson.fromJson(json, new TypeToken<List<String>>() {}.getType());
            if(list != null) {
                deque.addAll(list);
            }
        }
        return deque;
    }

    private void purge(Context context) {
        lastDate = Today.get();
        setCalorieLimit(User.getInstance().getDailyCalories());
        recentCaloriesConsumed.clear();
        recentMealNames.clear();
        recentMealColors.clear();
        save(context);
    }

}
