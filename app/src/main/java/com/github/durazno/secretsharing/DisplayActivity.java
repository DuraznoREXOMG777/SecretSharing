package com.github.durazno.secretsharing;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.durazno.secretsharing.entities.Project;
import com.github.durazno.secretsharing.models.KeyResponse;
import com.github.durazno.secretsharing.utils.Constants;
import com.github.durazno.secretsharing.utils.RetrofitClient;
import com.github.durazno.secretsharing.utils.SessionManagement;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayActivity extends AppCompatActivity {

    private static final String TAG = "DisplayActivity";

    @BindView(R.id.dp_description)
    TextView description;

    @BindView(R.id.send)
    MaterialButton sender;

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
        Log.d(TAG, "onCreate: "+project.getId());

        actionBar = getSupportActionBar();
        description.setText(management.getKey());
    }

    @OnClick(R.id.send)
    public void sender(View view){
        SessionManagement sessionManagement = new SessionManagement(getApplicationContext(), SessionManagement.AUTHORIZATION);
        String authorization = "Bearer "+sessionManagement.getAuthorization();
        SessionManagement sessionManagement1 = new SessionManagement(getApplicationContext(), "" + project.getId());
        String key = sessionManagement1.getKey();
        if (!authorization.equals("") && !(key.equals(""))) {
            Call<ResponseBody> call = RetrofitClient.getInstance().getApi().sendKey(authorization, project.getId(), key);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Log.d(TAG, "onResponse: "+response.body().string());
                        ResponseBody ResponseBody = response.body();
                        if (response.code() == 200) {
                            Toast.makeText(getApplicationContext(), "Key sent", Toast.LENGTH_SHORT);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}
