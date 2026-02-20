package ludwig.samuel.thinn.ui;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import ludwig.samuel.thinn.R;
import ludwig.samuel.thinn.data.Meal;
import ludwig.samuel.thinn.data.Meals;
import ludwig.samuel.thinn.data.Stats;
import ludwig.samuel.thinn.data.User;
import ludwig.samuel.thinn.databinding.ActivityMainBinding;
import ludwig.samuel.thinn.util.MealAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mealRecyclerView;
    private MealAdapter mealAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.setStats(Stats.getInstance());
        mainBinding.setUser(User.getInstance());
        mealRecyclerView = (RecyclerView)mainBinding.getRoot().findViewById(R.id.main_recylcerview_common_meals);
        Meals.getInstance().restore(this);
        mealAdapter = new MealAdapter(Meals.getInstance().getMeals());
        mealRecyclerView.setAdapter(mealAdapter);
        mealRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mealAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, settingsActivity.class);
            startActivity(settingsIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddMealClick(View view) {

        final AlertDialog addMealDialog = new AlertDialog.Builder(this).create();
        View addMealView = LayoutInflater.from(this).inflate(R.layout.dialog_add_meal, null);
        final EditText addMealName = (EditText)addMealView.findViewById(R.id.dialog_add_meal_name);
        final EditText addMealCalories = (EditText)addMealView.findViewById(R.id.dialog_add_meal_calories);
        View buttonAddMeal = (View)addMealView.findViewById(R.id.dialog_add_meal_add);
        buttonAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = addMealName.getText().toString();
                String calories = addMealCalories.getText().toString();
                if(!name.isEmpty() && !calories.isEmpty()) {
                    Meals.getInstance().getMeals().add(new Meal(name, Integer.valueOf(calories)));
                    mealAdapter.refresh();
                }
                addMealDialog.dismiss();
            }
        });
        addMealDialog.setView(addMealView);
        addMealDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addMealDialog.show();
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        User.getInstance().restore(this);
        Stats.getInstance().restore(this);
        Meals.getInstance().restore(this);
    }
}
