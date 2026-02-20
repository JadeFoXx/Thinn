package ludwig.samuel.thinn.ui;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ludwig.samuel.thinn.R;
import ludwig.samuel.thinn.data.User;
import ludwig.samuel.thinn.databinding.ActivitySettingsBinding;

public class settingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingsBinding settingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        settingsBinding.setUser(User.getInstance());
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
