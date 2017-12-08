package com.hci.project.cerebro;

import java.util.List;

/**
 * Created by Malavika Ramprasad on 12/2/2017.
 */

public class TutorRequests {

    public List<SubmitQuestion> accepted;
    public List<SubmitQuestion> pending;


    public void setPending(List<SubmitQuestion> pending) {
        this.pending = pending;
    }

    public void setAccepted(List<SubmitQuestion> accepted) {
        this.accepted = accepted;
    }


    public List<SubmitQuestion> getAccepted() {
        return accepted;
    }

    public List<SubmitQuestion> getPending() {
        return pending;
    }

    public TutorRequests(List<SubmitQuestion> accepted, List<SubmitQuestion> pending)
    {
        this.accepted = accepted;
        this.pending = pending;
    }
}
