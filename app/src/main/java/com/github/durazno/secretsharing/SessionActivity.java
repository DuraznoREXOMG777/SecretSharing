package com.github.durazno.secretsharing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.durazno.secretsharing.adapters.TabAdapter;
import com.github.durazno.secretsharing.entities.Project;
import com.github.durazno.secretsharing.entities.User;
import com.github.durazno.secretsharing.fragments.ProjectList;
import com.github.durazno.secretsharing.models.UserResponse;
import com.github.durazno.secretsharing.utils.Constants;
import com.github.durazno.secretsharing.adapters.ProjectAdapter;
import com.github.durazno.secretsharing.utils.RecyclerTouchListener;
import com.github.durazno.secretsharing.utils.RetrofitClient;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionActivity extends AppCompatActivity {
    private static final String TAG = "SessionActivity";

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private TabAdapter tabAdapter;

    private FirebaseAuth mAuth;
    private String tokenFb;
    private String token;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        token = intent.getStringExtra(Constants.TOKEN_TYPE) + " " + intent.getStringExtra(Constants.ACCESS_TOKEN);
        String email = intent.getStringExtra(Constants.EMAIL);

        Gson gson = new Gson();

        tabAdapter = new TabAdapter(getSupportFragmentManager());

        tabAdapter.addFragment(createFragment(1), "Leader");
        tabAdapter.addFragment(createFragment(2), "Second");
        tabAdapter.addFragment(createFragment(3), "Researcher");

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabAdapter.notifyDataSetChanged();

        mAuth = FirebaseAuth.getInstance();
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "onCreate: "+task.getResult().getToken());
            }
        });


        /**
         * Call for the user.
         */
        Call<UserResponse> userResponseCall = RetrofitClient.getInstance().getApi().user(token);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                user = new User(email, userResponse.getName(), token);
                Log.d(TAG, "onResponse: " + user.getFullname());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
            }
        });
    }

    private Fragment createFragment(int i){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, token);
        bundle.putInt(Constants.TYPE, i);
        ProjectList projectList = new ProjectList();
        projectList.setArguments(bundle);
        return projectList;
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*if(mAuth.getCurrentUser() == null){
            Intent intent = new Intent(this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }*/
    }

    private void updateToken(String token) {
        String email = mAuth.getCurrentUser().getEmail();
        User user = new User();
        user.setEmail(email);
        user.setToken(token);
        user.setFullname("Antonio Lopez Higuera");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("User")
                .document(token)
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Actualizado", Toast.LENGTH_SHORT).show();
                    }
                    task.getResult();
                });
    }

}
