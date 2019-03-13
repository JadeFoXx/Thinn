package ludwig.samuel.thinn.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import ludwig.samuel.thinn.R;
import ludwig.samuel.thinn.data.Meal;
import ludwig.samuel.thinn.data.Meals;
import ludwig.samuel.thinn.data.Stats;
import ludwig.samuel.thinn.data.User;
import ludwig.samuel.thinn.databinding.FragmentMainBinding;
import ludwig.samuel.thinn.util.MealAdapter;

public class MainFragment extends Fragment {

    private int page;
    private String title;
    private Activity parentActivty;
    private RecyclerView recyclerViewMeals;
    private MealAdapter mealAdapter;
    private EditText editTextMealName;
    private EditText editTextMealCalories;
    private View buttonAddMeal;
    private EditText editTextCaloriesToConsume;
    private View buttonConsume;
    private View buttonUndo;

    public static MainFragment newInstance(int page, String title) {
        MainFragment mainFragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putString("title", title);
        mainFragment.setArguments(args);
        return mainFragment;
    }

    public void setParentActivity(Activity activity) {
        parentActivty = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("page");
        title = getArguments().getString("title");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMainBinding binding = FragmentMainBinding.inflate(inflater, container, false);
        binding.setUser(User.getInstance());
        binding.setStats(Stats.getInstance());
        recyclerViewMeals = (RecyclerView)binding.getRoot().findViewById(R.id.fragment_main_recylcerview_common_meals);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMeals.setLayoutManager(layoutManager);
        mealAdapter = new MealAdapter(Meals.getInstance().getMeals());
        recyclerViewMeals.setAdapter(mealAdapter);
        editTextMealName = (EditText)binding.getRoot().findViewById(R.id.fragment_main_meal_name);
        editTextMealCalories = (EditText)binding.getRoot().findViewById(R.id.fragment_main_meal_cal);
        buttonAddMeal = binding.getRoot().findViewById(R.id.fragment_main_button_add_meal);
        buttonAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddMealClick();
            }
        });
        editTextCaloriesToConsume = (EditText)binding.getRoot().findViewById(R.id.fragment_main_cal_to_consume);
        buttonConsume = binding.getRoot().findViewById(R.id.fragment_main_button_consume);
        buttonUndo = binding.getRoot().findViewById(R.id.fragment_main_button_undo);

        buttonConsume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConsumeClick();
            }
        });

        buttonUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUndoClick();
            }
        });
        return binding.getRoot();
    }

    private void onAddMealClick() {
        String name = editTextMealName.getText().toString();
        String calories = editTextMealCalories.getText().toString();
        if(!name.isEmpty() && !calories.isEmpty()) {
            Meals.getInstance().getMeals().add(new Meal(name, Integer.valueOf(calories)));
            mealAdapter.refresh();
        }
    }

    private void onConsumeClick() {
        String calories = editTextCaloriesToConsume.getText().toString();
        if(!calories.isEmpty()){
            Stats.getInstance().consume(Integer.valueOf(calories));
            editTextCaloriesToConsume.setText("");
        }
    }

    private void onUndoClick() {
        Stats.getInstance().undo();
    }
}
