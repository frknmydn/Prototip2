package com.furkanmeydan.prototip2.Models;

public class Block {


    // UserblockerID > Engelleyen
    // UserblockedID > Engellenen

   private  String  userBlockerID,
                    userBlockedID,
                    blockReason;

    public Block(String userBlockerID, String userBlockedID,String blockReason) {
        this.userBlockerID = userBlockerID;
        this.userBlockedID = userBlockedID;
        this.blockReason = blockReason;
    }

    public String getUserBlockerID() {
        return userBlockerID;
    }

    public String getUserBlockedID() {
        return userBlockedID;
    }

    public String getBlockReason() {
        return blockReason;
    }

    public void setBlockReason(String blockReason) {
        this.blockReason = blockReason;
    }
}
