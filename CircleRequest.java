package com.geovengers.redzone;

import java.io.Serializable;
import java.util.List;

public class CircleRequest implements Serializable {
    private String start_date;
    private String end_date;
    private String disaster_group;
    private List<String> disaster_type;
    private List<String> disaster_level;
    private final static long serialVersionUID = 7560425657774133335L;

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
