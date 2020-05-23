package com.example.redzone;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CircleResponse {

    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("disaster_group")
    @Expose
    private String disasterGroup;
    @SerializedName("disaster_type")
    @Expose
    private List<String> disasterType = null;
    @SerializedName("disaster_level")
    @Expose
    private List<String> disasterLevel = null;
    @SerializedName("count")
    @Expose
    private List<Count> count = null;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDisasterGroup() {
        return disasterGroup;
    }

    public void setDisasterGroup(String disasterGroup) {
        this.disasterGroup = disasterGroup;
    }

    public List<String> getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(List<String> disasterType) {
        this.disasterType = disasterType;
    }

    public List<String> getDisasterLevel() {
        return disasterLevel;
    }

    public void setDisasterLevel(List<String> disasterLevel) {
        this.disasterLevel = disasterLevel;
    }

    public List<Count> getCount() {
        return count;
    }

    public void setCount(List<Count> count) {
        this.count = count;
    }

}