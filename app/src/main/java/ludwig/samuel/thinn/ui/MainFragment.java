package ludwig.samuel.thinn.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ludwig.samuel.thinn.R;
import ludwig.samuel.thinn.data.Stats;
import ludwig.samuel.thinn.data.User;
import ludwig.samuel.thinn.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {

    private int page;
    private String title;
    private Activity parentActivty;
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
        Stats.getInstance().setCaloriesLeft(User.getInstance().getDailyCalories());
        binding.setUser(User.getInstance());
        binding.setStats(Stats.getInstance());
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

    public void onConsumeClick() {
        Stats.getInstance().consume(Integer.valueOf(editTextCaloriesToConsume.getText().toString()));
        editTextCaloriesToConsume.setText("");
    }

    public void onUndoClick() {
        Stats.getInstance().undo();
    }
}
