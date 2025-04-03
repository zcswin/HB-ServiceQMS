package com.ww.boengongye.entity;

import java.util.List;

public class FlowBlockUser {
    public String blockId;
    public List<User> userIds;

    public FlowBlockUser() {
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public List<User> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<User> userIds) {
        this.userIds = userIds;
    }
}
