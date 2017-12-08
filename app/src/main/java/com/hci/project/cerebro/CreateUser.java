package com.hci.project.cerebro;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Malavika Ramprasad on 11/29/2017.
 */

public class CreateUser {
    @SerializedName("first_name")
    @Expose
    String first_name;
    @SerializedName("last_name")
    @Expose
    String last_name;
    @SerializedName("email")
    @Expose
    String email;
    @SerializedName("password")
    @Expose
    String password;
    @SerializedName("password_confirmation")
    @Expose
    String password_confirmation;

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirm_password(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }
    public CreateUser(String first_name, String last_name, String email, String password, String password_confirmation)
    {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }
}
