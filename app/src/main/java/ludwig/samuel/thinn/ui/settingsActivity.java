package ludwig.samuel.thinn.ui;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import ludwig.samuel.thinn.R;
import ludwig.samuel.thinn.data.User;
import ludwig.samuel.thinn.databinding.ActivitySettingsBinding;

public class settingsActivity extends AppCompatActivity {

    private static final double[] ACTIVITY_VALUES = { 1.2, 1.375, 1.55, 1.725, 1.9 };
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingsBinding settingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        settingsBinding.setUser(User.getInstance());

        // Sex radio buttons
        RadioGroup sexGroup = findViewById(R.id.settings_sex_group);
        RadioButton maleButton = findViewById(R.id.settings_radio_male);
        RadioButton femaleButton = findViewById(R.id.settings_radio_female);

        if ("female".equals(User.getInstance().getSex())) {
            femaleButton.setChecked(true);
        } else {
            maleButton.setChecked(true);
        }

        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.settings_radio_female) {
                    User.getInstance().setSex("female");
                } else {
                    User.getInstance().setSex("male");
                }
            }
        });

        // Activity spinner
        Spinner activitySpinner = findViewById(R.id.settings_activity);
        activitySpinner.setSelection(activityToIndex(User.getInstance().getActivity()));

        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                User.getInstance().setActivity(ACTIVITY_VALUES[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY = 100;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 == null || e2 == null) return false;
                float dx = e2.getX() - e1.getX();
                float dy = e2.getY() - e1.getY();
                if (Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY) {
                    if (dx > 0) {
                        // Swipe right → go back
                        finish();
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

    private int activityToIndex(double activity) {
        int closest = 0;
        double minDiff = Double.MAX_VALUE;
        for (int i = 0; i < ACTIVITY_VALUES.length; i++) {
            double diff = Math.abs(ACTIVITY_VALUES[i] - activity);
            if (diff < minDiff) {
                minDiff = diff;
                closest = i;
            }
        }
        return closest;
    }

    @Override
    protected void onPause() {
        User.getInstance().save(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        User.getInstance().restore(this);
        super.onResume();
    }
}
