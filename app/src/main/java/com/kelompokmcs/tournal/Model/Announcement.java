package com.kelompokmcs.tournal.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Announcement implements Parcelable {
    private int announcementId, groupId;
    private String announcementTitle, announcementDesc, dateAndTime, userName, userEmail, userPhoto;

    public Announcement(int announcementId, int groupId, String announcementTitle, String announcementDesc, String dateAndTime, String userName, String userEmail, String userPhoto) {
        this.announcementId = announcementId;
        this.groupId = groupId;
        this.announcementTitle = announcementTitle;
        this.announcementDesc = announcementDesc;
        this.dateAndTime = dateAndTime;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhoto = userPhoto;
    }

    protected Announcement(Parcel in) {
        announcementId = in.readInt();
        groupId = in.readInt();
        announcementTitle = in.readString();
        announcementDesc = in.readString();
        dateAndTime = in.readString();
        userName = in.readString();
        userEmail = in.readString();
        userPhoto = in.readString();
    }

    public int getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(int announcementId) {
        this.announcementId = announcementId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public void setAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }

    public String getAnnouncementDesc() {
        return announcementDesc;
    }

    public void setAnnouncementDesc(String announcementDesc) {
        this.announcementDesc = announcementDesc;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(announcementId);
        dest.writeInt(groupId);
        dest.writeString(announcementTitle);
        dest.writeString(announcementDesc);
        dest.writeString(dateAndTime);
        dest.writeString(userName);
        dest.writeString(userEmail);
        dest.writeString(userPhoto);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Announcement> CREATOR = new Creator<Announcement>() {
        @Override
        public Announcement createFromParcel(Parcel in) {
            return new Announcement(in);
        }

        @Override
        public Announcement[] newArray(int size) {
            return new Announcement[size];
        }
    };
}
