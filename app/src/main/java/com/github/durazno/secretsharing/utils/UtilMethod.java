package com.github.durazno.secretsharing.utils;

import com.github.durazno.secretsharing.entities.Project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UtilMethod {

    public static List<Project> populateList(String array) {
        List<Project> projectList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(array);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Project project = new Project(
                        jsonObject.getInt("id"),
                        jsonObject.getInt("f_leader"),
                        jsonObject.getInt("s_leader"),
                        jsonObject.getString("name"),
                        jsonObject.getString("description"),
                        jsonObject.getString("finished_at"),
                        jsonObject.getString("created_at"),
                        jsonObject.getString("updated_at")
                );
                projectList.add(project);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return projectList;
    }
}
