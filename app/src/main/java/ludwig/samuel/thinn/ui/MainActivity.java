package ludwig.samuel.thinn.ui;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ludwig.samuel.thinn.R;
import ludwig.samuel.thinn.data.Meal;
import ludwig.samuel.thinn.data.Meals;
import ludwig.samuel.thinn.data.Stats;
import ludwig.samuel.thinn.data.User;
import ludwig.samuel.thinn.databinding.ActivityMainBinding;
import ludwig.samuel.thinn.util.MealAdapter;
import ludwig.samuel.thinn.widget.ThinnWidgetProvider;

public class MainActivity extends AppCompatActivity implements MealAdapter.OnMealEditListener {

    private RecyclerView foodRecyclerView;
    private RecyclerView drinkRecyclerView;
    private MealAdapter foodAdapter;
    private MealAdapter drinkAdapter;
    private SegmentedProgressBar progressBar;
    private Observable.OnPropertyChangedCallback statsCallback;
    private GestureDetector gestureDetector;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.setStats(Stats.getInstance());
        mainBinding.setUser(User.getInstance());
        foodRecyclerView = (RecyclerView)mainBinding.getRoot().findViewById(R.id.main_recyclerview_food);
        drinkRecyclerView = (RecyclerView)mainBinding.getRoot().findViewById(R.id.main_recyclerview_drinks);
        progressBar = (SegmentedProgressBar)mainBinding.getRoot().findViewById(R.id.main_progressbar);
        Meals.getInstance().restore(this);
        foodAdapter = new MealAdapter(Meals.getInstance().getFoods(), this);
        drinkAdapter = new MealAdapter(Meals.getInstance().getDrinks(), this);
        foodRecyclerView.setAdapter(foodAdapter);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        drinkRecyclerView.setAdapter(drinkAdapter);
        drinkRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        statsCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                updateProgressBar();
            }
        };
        Stats.getInstance().addOnPropertyChangedCallback(statsCallback);
        updateProgressBar();

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY = 100;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 == null || e2 == null) return false;
                float dx = e2.getX() - e1.getX();
                float dy = e2.getY() - e1.getY();
                if (Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY) {
                    if (dx < 0) {
                        // Swipe left → settings
                        openSettings();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void openSettings() {
        Intent settingsIntent = new Intent(this, settingsActivity.class);
        startActivity(settingsIntent);
    }

    private void updateProgressBar() {
        progressBar.setData(
            Stats.getInstance().getConsumedSegments(),
            Stats.getInstance().getConsumedNames(),
            Stats.getInstance().getConsumedColors(),
            Stats.getInstance().getCalorieLimit()
        );
    }

    private void showMealDialog(final Meal meal, final String type) {
        final boolean isEdit = meal != null;

        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_meal, null);
        final EditText nameInput = (EditText)dialogView.findViewById(R.id.dialog_add_meal_name);
        final EditText caloriesInput = (EditText)dialogView.findViewById(R.id.dialog_add_meal_calories);
        TextView title = (TextView)dialogView.findViewById(R.id.dialog_add_meal_title);
        TextView buttonText = (TextView)dialogView.findViewById(R.id.dialog_add_meal_button_text);
        View button = dialogView.findViewById(R.id.dialog_add_meal_add);
        final ColorPickerBar colorPicker = (ColorPickerBar)dialogView.findViewById(R.id.dialog_color_picker);
        final View colorPreview = dialogView.findViewById(R.id.dialog_color_preview);

        title.setText(isEdit ? R.string.dialog_title_edit_meal : R.string.dialog_title_add_meal);
        buttonText.setText(isEdit ? R.string.dialog_button_save : R.string.main_button_add_meal);

        if (isEdit) {
            nameInput.setText(meal.getName());
            caloriesInput.setText(String.valueOf(meal.getCalories()));
            colorPicker.setColor(meal.getColor());
        }

        updateColorPreview(colorPreview, colorPicker.getSelectedColor());

        colorPicker.setOnColorChangedListener(new ColorPickerBar.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                updateColorPreview(colorPreview, color);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String calories = caloriesInput.getText().toString();
                if (!name.isEmpty() && !calories.isEmpty()) {
                    int color = colorPicker.getSelectedColor();
                    if (isEdit) {
                        meal.setName(name);
                        meal.setCalories(Integer.valueOf(calories));
                        meal.setColor(color);
                    } else {
                        Meals.getInstance().getMeals().add(new Meal(name, Integer.valueOf(calories), color, type));
                    }
                    refreshAdapters();
                }
                dialog.dismiss();
            }
        });

        dialog.setView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void updateColorPreview(View preview, int color) {
        GradientDrawable dot = new GradientDrawable();
        dot.setShape(GradientDrawable.OVAL);
        dot.setColor(color);
        preview.setBackground(dot);
    }

    public void onAddFoodClick(View view) {
        showMealDialog(null, "food");
    }

    public void onAddDrinkClick(View view) {
        showMealDialog(null, "drink");
    }

    @Override
    public void onEditMeal(Meal meal) {
        showMealDialog(meal, meal.getType());
    }

    @Override
    public void onMealDeleted() {
        refreshAdapters();
    }

    private void refreshAdapters() {
        foodAdapter.refresh(Meals.getInstance().getFoods());
        drinkAdapter.refresh(Meals.getInstance().getDrinks());
    }

    public void onConsumeClick(View view) {
        final AlertDialog consumeDialog = new AlertDialog.Builder(this).create();
        View consumeView = LayoutInflater.from(this).inflate(R.layout.dialog_consume, null);
        final EditText consumeConsume = (EditText)consumeView.findViewById(R.id.dialog_consume_calories);
        View buttonConsume = (View)consumeView.findViewById(R.id.dialog_consume_consume);
        buttonConsume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String calories = consumeConsume.getText().toString();
                if(!calories.isEmpty()) {
                    Stats.getInstance().consume(Integer.valueOf(calories));
                }
                consumeDialog.dismiss();
            }
        });
        consumeDialog.setView(consumeView);
        consumeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        consumeDialog.show();
    }

    public void onUndoClick(View view) {
        Stats.getInstance().undo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        User.getInstance().save(this);
        Stats.getInstance().save(this);
        Meals.getInstance().save(this);
        ThinnWidgetProvider.updateWidget(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        User.getInstance().restore(this);
        Stats.getInstance().restore(this);
        Meals.getInstance().restore(this);
        refreshAdapters();
        updateProgressBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Stats.getInstance().removeOnPropertyChangedCallback(statsCallback);
    }
}
