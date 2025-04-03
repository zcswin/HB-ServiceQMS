package com.ww.boengongye.entity;

import java.util.List;

public class SaveURS {
    public String id;
    public List<UserRelationStation> userRelationStation;

    public SaveURS() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<UserRelationStation> getUserRelationStation() {
        return userRelationStation;
    }

    public void setUserRelationStation(List<UserRelationStation> userRelationStation) {
        this.userRelationStation = userRelationStation;
    }
}
