package com.a700apps.ummelquwain.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed.arafa on 9/11/2017.
 */

public class CommentRequestModel {

    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("ProgramID")
    @Expose
    private Integer programID;
    @SerializedName("CommentText")
    @Expose
    private String commentText;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
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

    public CommentRequestModel(String userID, Integer programID, String commentText) {
        this.userID = userID;
        this.programID = programID;
        this.commentText = commentText;
    }
}
