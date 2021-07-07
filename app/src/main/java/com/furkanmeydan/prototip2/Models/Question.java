package com.furkanmeydan.prototip2.Models;

import java.io.Serializable;

public class Question implements Serializable {
    private String postID,
                    senderID,
                    questionText,
                    postOwnerID,
                    answerText,
                    postHeaderText,
                    senderNameText,
                    questionID;

    private long postTimestamp;

    private int status;

    public Question() {
    }

    public Question(String postID, String postHeader, String senderID, String senderNameText, String questionText, String postOwnerID, String answerText, String questionID, int status, long postTimestamp) {
        this.postID = postID;
        this.senderID = senderID;
        this.questionText = questionText;
        this.postOwnerID = postOwnerID;
        this.answerText = answerText;
        this.questionID = questionID;
        this.status = status;
        this.postHeaderText = postHeader;
        this.senderNameText = senderNameText;
        this.postTimestamp = postTimestamp;
    }

    public String getPostID() {
        return postID;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getPostOwnerID() {
        return postOwnerID;
    }

    public String getAnswerText() {
        return answerText;
    }

    public String getQuestionID() {
        return questionID;
    }

    public int getStatus() {
        return status;
    }

    public String getPostHeaderText() {
        return postHeaderText;
    }

    public String getSenderNameText() {
        return senderNameText;
    }

    public long getPostTimestamp() {
        return postTimestamp;
    }
}
