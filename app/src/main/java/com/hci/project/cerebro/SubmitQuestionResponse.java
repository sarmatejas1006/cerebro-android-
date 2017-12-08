package com.hci.project.cerebro;

import java.util.List;

/**
 * Created by Malavika Ramprasad on 12/5/2017.
 */

public class SubmitQuestionResponse {
    List<User> tutors;
    SubmitQuestion question;

    public List<User> getTutors() {
        return tutors;
    }

    public SubmitQuestion getQuestion() {
        return question;
    }

    public void setQuestion(SubmitQuestion question) {
        this.question = question;
    }

    public void setTutors(List<User> tutors) {
        this.tutors = tutors;
    }

    public SubmitQuestionResponse(List<User> userList, SubmitQuestion qstn) {
        this.tutors = userList;
        this.question = qstn;
    }
}
