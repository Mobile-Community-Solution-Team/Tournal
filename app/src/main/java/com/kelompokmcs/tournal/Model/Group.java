package com.kelompokmcs.tournal.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Group{
    private int groupId;
    private String groupCode,groupName,groupLocation, startDate, endDate, groupPass;

    public Group(int groupId, String groupCode, String groupName, String groupLocation, String startDate, String endDate, String groupPass) {
        this.groupId = groupId;
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.groupLocation = groupLocation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.groupPass = groupPass;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupLocation() {
        return groupLocation;
    }

    public void setGroupLocation(String groupLocation) {
        this.groupLocation = groupLocation;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getGroupPass() {
        return groupPass;
    }

    public void setGroupPass(String groupPass) {
        this.groupPass = groupPass;
    }
}
