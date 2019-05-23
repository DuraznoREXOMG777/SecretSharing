package com.github.durazno.secretsharing.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.durazno.secretsharing.DisplayActivity;
import com.github.durazno.secretsharing.R;
import com.github.durazno.secretsharing.SessionActivity;
import com.github.durazno.secretsharing.adapters.ProjectAdapter;
import com.github.durazno.secretsharing.entities.Project;
import com.github.durazno.secretsharing.utils.Constants;
import com.github.durazno.secretsharing.utils.RecyclerTouchListener;
import com.github.durazno.secretsharing.utils.RetrofitClient;
import com.github.durazno.secretsharing.utils.UtilMethod;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectList extends Fragment {

    @BindView(R.id.rv_project)
    RecyclerView recyclerView;

    private ProjectAdapter projectAdapter;
    private List<Project> projectList;
    private Gson gson = new Gson();
    private String token;
    private int type;

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        token = getArguments().getString(Constants.ACCESS_TOKEN);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        projectList = new ArrayList<>();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        projectAdapter = new ProjectAdapter(projectList);
        recyclerView.setAdapter(projectAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), (v, position) -> {
            Intent intent2 = new Intent(getActivity(), DisplayActivity.class);
            intent2.putExtra(Constants.PROJECT, gson.toJson(projectList.get(position)));
            startActivity(intent2);
        }));

        populate();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            populate();
        }
    }

    private void populate(){
        type = getArguments().getInt(Constants.TYPE);
        Call<ResponseBody> callLeader = null;
        switch(type){
            case 1:
                callLeader = RetrofitClient.getInstance().getApi().projectsLeader(token);
                break;
            case 2:
                callLeader = RetrofitClient.getInstance().getApi().projectsSecond(token);
                break;
            case 3:
                callLeader = RetrofitClient.getInstance().getApi().projectsResearcher(token);
                break;
        }

        callLeader.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.body() != null){
                        if (projectList.size() == 0){
                            projectList = UtilMethod.populateList(response.body().string());
                        }
                        projectAdapter = new ProjectAdapter(projectList);
                        recyclerView.setAdapter(projectAdapter);
                        projectAdapter.notifyDataSetChanged();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        populate();
    }
}
