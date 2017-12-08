package com.hci.project.cerebro;

/**
 * Created by Malavika Ramprasad on 12/3/2017.
 */

class DeviceToken {
    String token;
    int user_id;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public DeviceToken(int user_id, String token){
        this.user_id = user_id;
        this.token = token;
    }
}
