package com.example.redzone;

import java.util.List;

public class MsgRequest {
    String location_code;
    String start_date;
    String end_date;
    String disaster_group;
    List<String> disaster_type;
    List<String> disaster_level;

    public String getLocation_code() {
        return location_code;
    }

    public void setLocation_code(String location_code) {
        this.location_code = location_code;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDisaster_group() {
        return disaster_group;
    }

    public void setDisaster_group(String disaster_group) {
        this.disaster_group = disaster_group;
    }

    public List<String> getDisaster_type() {
        return disaster_type;
    }

    public void setDisaster_type(List<String> disaster_type) {
        this.disaster_type = disaster_type;
    }

    public List<String> getDisaster_level() {
        return disaster_level;
    }

    public void setDisaster_level(List<String> disaster_level) {
        this.disaster_level = disaster_level;
    }
}