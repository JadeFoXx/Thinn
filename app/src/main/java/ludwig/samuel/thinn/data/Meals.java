package ludwig.samuel.thinn.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Meals {
    private static Meals instance;
    private ArrayList<Meal> meals = new ArrayList<Meal>();

    public static Meals getInstance() {
        if(instance == null){
            instance = new Meals();
        }
        return instance;
    }

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<Meal> meals) {
        this.meals = meals;
    }

    public ArrayList<Meal> getFoods() {
        ArrayList<Meal> foods = new ArrayList<>();
        for (Meal meal : meals) {
            if ("food".equals(meal.getType())) {
                foods.add(meal);
            }
        }
        return foods;
    }

    public ArrayList<Meal> getDrinks() {
        ArrayList<Meal> drinks = new ArrayList<>();
        for (Meal meal : meals) {
            if ("drink".equals(meal.getType())) {
                drinks.add(meal);
            }
        }
        return drinks;
    }

    public void save(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("thinn_stats", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("meals", mealsToJson(meals));
        editor.commit();
    }

    public void restore(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("thinn_stats", Context.MODE_PRIVATE);
        meals = jsonToMeals(sharedPreferences.getString("meals", null));
    }

    private String mealsToJson(ArrayList<Meal> meals) {
        Gson gson = new Gson();
        return gson.toJson(meals);
    }

    private ArrayList<Meal> jsonToMeals(String json) {
        if(json == null) {
            return new ArrayList<Meal>();
        }
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<ArrayList<Meal>>(){}.getType());
    }
}
