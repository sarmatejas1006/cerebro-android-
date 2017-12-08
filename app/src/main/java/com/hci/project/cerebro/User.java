package com.hci.project.cerebro;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * Created by Malavika Ramprasad on 11/29/2017.
 */

public class User implements Parcelable {
    int id;
    String first_name;
    String last_name;
    String email;
    float rating;
    float x_coordinate;
    float y_coordinate;
    Date start_time;
    Date end_time;
    List<BookedSlot> booked_slots;

    protected User(Parcel in) {
        id = in.readInt();
        first_name = in.readString();
        last_name = in.readString();
        email = in.readString();
        rating = in.readFloat();
        x_coordinate = in.readFloat();
        y_coordinate = in.readFloat();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public float getRating() {
        return rating;
    }

    public float getX_coordinate() {
        return x_coordinate;
    }

    public float getY_coordinate() {
        return y_coordinate;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setEnd_time(Time end_time) {
        this.end_time = end_time;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }

    public void setX_coordinate(float x_coordinate) {
        this.x_coordinate = x_coordinate;
    }

    public void setY_coordinate(float y_coordinate) {
        this.y_coordinate = y_coordinate;
    }

    public List<BookedSlot> getSlots() {
        return booked_slots;
    }

    public void setSlots(List<BookedSlot> booked_slots) {
        this.booked_slots = booked_slots;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeString(email);
        parcel.writeFloat(rating);
        parcel.writeFloat(x_coordinate);
        parcel.writeFloat(y_coordinate);
    }
}
