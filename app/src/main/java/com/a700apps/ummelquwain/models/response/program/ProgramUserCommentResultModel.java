package com.a700apps.ummelquwain.models.response.program;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mohamed.arafa on 9/10/2017.
 */

public class ProgramUserCommentResultModel extends RealmObject{
    @PrimaryKey
    @SerializedName("CommentID")
    @Expose
    private Integer commentID;
    @SerializedName("UserID")
    @Expose
    private Integer userID;
    @SerializedName("ProgramID")
    @Expose
    private Integer programID;
    @SerializedName("CommentText")
    @Expose
    private String commentText;
    @SerializedName("IsApproved")
    @Expose
    private Boolean isApproved;
    @SerializedName("CreateDate")
    @Expose
    private String createDate;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("UserImage")
    @Expose
    private String userImage;

    public Integer getCommentID() {
        return commentID;
    }

    public void setCommentID(Integer commentID) {
        this.commentID = commentID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getProgramID() {
        return programID;
    }

    public void setProgramID(Integer programID) {
        this.programID = programID;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

}
