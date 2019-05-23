package com.github.durazno.secretsharing.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.durazno.secretsharing.R;
import com.github.durazno.secretsharing.entities.Project;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder> {
    List<Project> projects;

    public ProjectAdapter(List<Project> projects) {
        this.projects = projects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Project project = projects.get(position);
        holder.cvDescription.setText(project.getDescription());
        holder.cvTitle.setText(project.getName());
        holder.cvDate.setText(project.getCreated_at());
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cv_title)
        TextView cvTitle;
        @BindView(R.id.cv_description)
        TextView cvDescription;
        @BindView(R.id.cv_date)
        TextView cvDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
