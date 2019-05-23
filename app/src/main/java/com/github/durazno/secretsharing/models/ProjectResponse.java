package com.github.durazno.secretsharing.models;

import com.github.durazno.secretsharing.entities.Project;

import java.util.List;

public class ProjectResponse {
    
    private List<Project> projects_f_leader;

    public List<Project> getProjects() {
        return projects_f_leader;
    }

    public ProjectResponse(List<Project> projects_f_leader) {
        this.projects_f_leader = projects_f_leader;
    }
}

