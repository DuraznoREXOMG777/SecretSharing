package com.github.durazno.secretsharing.entities;

public class Project {
    private int id;
    private int f_leader;
    private int s_leader;
    private String name, description, finished_at, created_at, updated_at;

    public Project(int id, int f_leader, int s_leader, String name, String description, String finished_at, String created_at, String updated_at) {
        this.id = id;
        this.f_leader = f_leader;
        this.s_leader = s_leader;
        this.name = name;
        this.description = description;
        this.finished_at = finished_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Project(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getF_leader() {
        return f_leader;
    }

    public int getS_leader() {
        return s_leader;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFinished_at() {
        return finished_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
