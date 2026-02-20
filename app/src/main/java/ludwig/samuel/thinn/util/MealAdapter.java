package ludwig.samuel.thinn.util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ludwig.samuel.thinn.R;
import ludwig.samuel.thinn.data.Meal;
import ludwig.samuel.thinn.data.Meals;
import ludwig.samuel.thinn.data.Stats;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    public interface OnMealEditListener {
        void onEditMeal(Meal meal);
        void onMealDeleted();
    }

    private ArrayList<Meal> dataSet = new ArrayList<Meal>();
    private OnMealEditListener editListener;

    public MealAdapter(ArrayList<Meal> dataSet, OnMealEditListener editListener) {
        this.dataSet.addAll(dataSet);
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item_card, viewGroup, false);
        return new MealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder viewHolder, int i) {
        final int k = i;
        final Meal meal = dataSet.get(i);
        viewHolder.meal = meal;
        viewHolder.name.setText(meal.getName());
        viewHolder.calories.setText(String.valueOf(meal.getCalories()) + " cal");

        GradientDrawable dot = new GradientDrawable();
        dot.setShape(GradientDrawable.OVAL);
        dot.setColor(meal.getColor());
        viewHolder.colorDot.setBackground(dot);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stats.getInstance().consume(meal.getCalories(), meal.getName(), meal.getColor());
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (editListener != null) {
                    editListener.onEditMeal(meal);
                }
                return true;
            }
        });
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Meals.getInstance().getMeals().remove(meal);
                if (editListener != null) {
                    editListener.onMealDeleted();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        public Meal meal;
        public TextView name;
        public TextView calories;
        public TextView deleteButton;
        public View colorDot;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.recyclerview_item_card_name);
            calories = (TextView)itemView.findViewById(R.id.recyclerview_item_card_cal);
            deleteButton = (TextView)itemView.findViewById(R.id.recyclerview_item_card_delete);
            colorDot = itemView.findViewById(R.id.recyclerview_item_card_color);
        }
    }

    public void refresh(ArrayList<Meal> meals) {
        dataSet.clear();
        dataSet.addAll(meals);
        notifyDataSetChanged();
    }
}
