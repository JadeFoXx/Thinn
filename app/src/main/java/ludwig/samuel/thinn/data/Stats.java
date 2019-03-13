package ludwig.samuel.thinn.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import ludwig.samuel.thinn.BR;

public class Stats extends BaseObservable {
    private static Stats instance;
    private int caloriesConsumed = 0;
    private int caloriesLeft = 0;
    private Deque<Integer> consumed = new ArrayDeque<>();

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

    public void consume(int amount) {
        this.caloriesConsumed += amount;
        this.caloriesLeft -= amount;
        consumed.push(amount);
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

    public void undo() {
        if(consumed.size() > 0) {
            consume(-1*consumed.pop());
        }
    }
}
