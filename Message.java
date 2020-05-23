package com.example.redzone;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message implements Serializable
{

    @SerializedName("msg_create_dt")
    @Expose
    private String msgCreateDt;
    @SerializedName("location_name")
    @Expose
    private String locationName;
    @SerializedName("location_code")
    @Expose
    private String locationCode;
    @SerializedName("disaster_group")
    @Expose
    private String disasterGroup;
    @SerializedName("disaster_type")
    @Expose
    private String disasterType;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("disaster_level")
    @Expose
    private String disasterLevel;
    private final static long serialVersionUID = 7560425658844137655L;

    public String getMsgCreateDt() {
        return msgCreateDt;
    }

    public void setMsgCreateDt(String msgCreateDt) {
        this.msgCreateDt = msgCreateDt;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getDisasterGroup() {
        return disasterGroup;
    }

    public void setDisasterGroup(String disasterGroup) {
        this.disasterGroup = disasterGroup;
    }

    public String getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(String disasterType) {
        this.disasterType = disasterType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDisasterLevel() {
        return disasterLevel;
    }

    public void setDisasterLevel(String disasterLevel) {
        this.disasterLevel = disasterLevel;
    }

}