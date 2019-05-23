package com.github.durazno.secretsharing;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.github.durazno.secretsharing.entities.Project;
import com.github.durazno.secretsharing.utils.Constants;
import com.github.durazno.secretsharing.utils.SessionManagement;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayActivity extends AppCompatActivity {

    private static final String TAG = "DisplayActivity";

    @BindView(R.id.dp_description)
    TextView description;

    ActionBar actionBar;

    private Project project;
    private SessionManagement management;

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle(project.getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        ButterKnife.bind(this);

        project = new Gson().fromJson(getIntent().getStringExtra(Constants.PROJECT), Project.class);
        management = new SessionManagement(getApplicationContext(), ""+project.getId());

        actionBar = getSupportActionBar();
        description.setText(management.getKey());
    }
}
