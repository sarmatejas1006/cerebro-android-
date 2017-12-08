package com.hci.project.cerebro;

/**
 * Created by Malavika Ramprasad on 12/5/2017.
 */

public class TutorReview {
    int question_id;
    String review;
    Float rating;

    public int getQuestion_id() {
        return question_id;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Float getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public TutorReview(int question_id, String review, float rating){
        this.question_id = question_id;
        this.rating = rating;
        this.review = review;
    }
}
