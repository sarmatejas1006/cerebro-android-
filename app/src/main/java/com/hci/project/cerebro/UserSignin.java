package com.hci.project.cerebro;

/**
 * Created by Malavika Ramprasad on 11/30/2017.
 */

public class UserSignin {
    String email;
    String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserSignin(String email, String password)
    {
        this.email = email;
        this.password = password;
    }
}
