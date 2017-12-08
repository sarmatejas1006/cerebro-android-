package com.hci.project.cerebro;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Malavika Ramprasad on 11/29/2017.
 */

public class SubmitQuestion {
    @SerializedName("tag_id")
    @Expose
    int tag_id;
    @SerializedName("learner_id")
    @Expose
    int learner_id;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("tutor_accepted")
    @Expose
    Boolean tutor_accepted;
    @SerializedName("start_time")
    @Expose
    Date start_time;
    @SerializedName("end_time")
    @Expose
    Date end_time;

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public int getLearner_id() {
        return learner_id;
    }

    public void setLearner_id(int learner_id) {
        this.learner_id = learner_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuestion_id() { return id; }

    public void setQuestion_id(int id) { this.id = id; }

    public Date getEnd_time() {
        return end_time;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setEnd_time(Time end_time) {
        this.end_time = end_time;
    }

    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Boolean getTutor_accepted() {
        return tutor_accepted;
    }

    public void setTutor_accepted(Boolean tutor_accepted) {
        this.tutor_accepted = tutor_accepted;
    }

    public SubmitQuestion(int tag_id, String description, int learner_id, int id) {
        this.learner_id = learner_id;
        this.tag_id = tag_id;
        this.description = description;
        this.id = id;
    }
}
