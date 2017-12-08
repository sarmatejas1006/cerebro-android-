package com.hci.project.cerebro;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Malavika Ramprasad on 12/3/2017.
 */

public class BookedSlot {

    Date start_time;
    Date end_time;

    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(Time end_time) {
        this.end_time = end_time;
    }

    public Date getStart_time() {
        return start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }
}
