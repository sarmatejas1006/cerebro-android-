package com.hci.project.cerebro;

import java.util.List;

/**
 * Created by Malavika Ramprasad on 12/2/2017.
 */

public class PostSkills {
    public List<String> skill;

    public void setSkill(List<String> skill) {
        this.skill = skill;
    }

    public List<String> getSkill() {
        return skill;
    }

    public PostSkills(List<String> skills)
    {
        this.skill = skills;
    }
}
