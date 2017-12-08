package com.hci.project.cerebro;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Malavika Ramprasad on 12/4/2017.
 */

public class RequestTutor {
    int tutor_id;
    Date start_time;
    Date end_time;

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public Date getStart_time() {
        return start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public int getTutor_id() {
        return tutor_id;
    }

    public int setTutor_id(int tutor_id) {
        return this.tutor_id = tutor_id;
    }

    public RequestTutor(int tutor_id, Date start_time, Date end_time){
        this.end_time = end_time;
        this.tutor_id = tutor_id;
        this.start_time = start_time;
    }

}
