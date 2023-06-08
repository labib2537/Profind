package com.example.profind;

public class c_3_Comment_model {
    String user_id, time, comment_text, post_id;

    public c_3_Comment_model(){

    }

    public c_3_Comment_model(String user_id, String time, String comment_text, String post_id) {
        this.user_id = user_id;
        this.time = time;
        this.comment_text = comment_text;
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
}
