package com.kelompokmcs.tournal.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Agenda implements Parcelable {
    private int agendaId, groupId;
    private String groupName, agendaTitle, agendaDesc, startTime, endTime;
    private double startLat, startLng, startAlt, endLat, endLng, endAlt;

    public Agenda(int agendaId, int groupId, String groupName, String agendaTitle, String agendaDesc, double startLat, double startLng, double startAlt, double endLat, double endLng, double endAlt, String startTime, String endTime) {
        this.agendaId = agendaId;
        this.groupId = groupId;
        this.groupName = groupName;
        this.agendaTitle = agendaTitle;
        this.agendaDesc = agendaDesc;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startLat = startLat;
        this.startLng = startLng;
        this.startAlt = startAlt;
        this.endLat = endLat;
        this.endLng = endLng;
        this.endAlt = endAlt;
    }

    protected Agenda(Parcel in) {
        agendaId = in.readInt();
        groupId = in.readInt();
        groupName = in.readString();
        agendaTitle = in.readString();
        agendaDesc = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        startLat = in.readDouble();
        startLng = in.readDouble();
        startAlt = in.readDouble();
        endLat = in.readDouble();
        endLng = in.readDouble();
        endAlt = in.readDouble();
    }

    public static final Creator<Agenda> CREATOR = new Creator<Agenda>() {
        @Override
        public Agenda createFromParcel(Parcel in) {
            return new Agenda(in);
        }

        @Override
        public Agenda[] newArray(int size) {
            return new Agenda[size];
        }
    };

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLng() {
        return startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }

    public double getStartAlt() {
        return startAlt;
    }

    public void setStartAlt(double startAlt) {
        this.startAlt = startAlt;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLng() {
        return endLng;
    }

    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }

    public double getEndAlt() {
        return endAlt;
    }

    public void setEndAlt(double endAlt) {
        this.endAlt = endAlt;
    }

    public int getAgendaId() {
        return agendaId;
    }

    public void setAgendaId(int agendaId) {
        this.agendaId = agendaId;
    }

    public String getAgendaTitle() {
        return agendaTitle;
    }

    public void setAgendaTitle(String agendaTitle) {
        this.agendaTitle = agendaTitle;
    }

    public String getAgendaDesc() {
        return agendaDesc;
    }

    public void setAgendaDesc(String agendaDesc) {
        this.agendaDesc = agendaDesc;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(agendaId);
        parcel.writeInt(groupId);
        parcel.writeString(groupName);
        parcel.writeString(agendaTitle);
        parcel.writeString(agendaDesc);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeDouble(startLat);
        parcel.writeDouble(startLng);
        parcel.writeDouble(startAlt);
        parcel.writeDouble(endLat);
        parcel.writeDouble(endLng);
        parcel.writeDouble(endAlt);
    }
}
