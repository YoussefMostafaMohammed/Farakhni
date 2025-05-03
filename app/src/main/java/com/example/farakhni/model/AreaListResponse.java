package com.example.farakhni.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AreaListResponse {
    @SerializedName("meals")
    private List<Area> areas;
    public List<Area>  getAllAreas() {
        return areas;
    }
}
