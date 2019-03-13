package ludwig.samuel.thinn.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
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

    private ArrayList<Meal> dataSet = new ArrayList<Meal>();

    public MealAdapter(ArrayList<Meal> dataSet) {
        this.dataSet.addAll(dataSet);
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
        viewHolder.calories.setText(String.valueOf(meal.getCalories()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stats.getInstance().consume(meal.getCalories());
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Meals.getInstance().getMeals().remove(k);
                refresh();
                return true;
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

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.recyclerview_item_card_name);
            calories = (TextView)itemView.findViewById(R.id.recyclerview_item_card_cal);
        }
    }

    public void refresh() {
        dataSet.clear();
        dataSet.addAll(Meals.getInstance().getMeals());
        notifyDataSetChanged();
    }
}
