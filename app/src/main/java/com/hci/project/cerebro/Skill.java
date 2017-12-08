package com.hci.project.cerebro;

/**
 * Created by Malavika Ramprasad on 11/30/2017.
 */

public class Skill {
    int id;
    String name;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void Skill(int tag_id, String description){
        this.name = description;
        this.id = tag_id;
    }
}
