package com.example.profind;

public class e_2_0_Notification_model {
    String commenter_liker_appointer_phone,poster_phone,time,type,clicked,total_notifications;

    public e_2_0_Notification_model() {
    }

    public e_2_0_Notification_model(String commenter_liker_appointer_phone,
                                    String poster_phone, String time, String type,
                                    String clicked, String total_notifications) {
        this.commenter_liker_appointer_phone = commenter_liker_appointer_phone;
        this.poster_phone = poster_phone;
        this.time = time;
        this.type = type;
        this.clicked = clicked;
        this.total_notifications = total_notifications;
    }

    public String getCommenter_liker_appointer_phone() {
        return commenter_liker_appointer_phone;
    }

    public void setCommenter_liker_appointer_phone(String commenter_liker_appointer_phone) {
        this.commenter_liker_appointer_phone = commenter_liker_appointer_phone;
    }

    public String getPoster_phone() {
        return poster_phone;
    }

    public void setPoster_phone(String poster_phone) {
        this.poster_phone = poster_phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClicked() {
        return clicked;
    }

    public void setClicked(String clicked) {
        this.clicked = clicked;
    }

    public String getTotal_notifications() {
        return total_notifications;
    }

    public void setTotal_notifications(String total_notifications) {
        this.total_notifications = total_notifications;
    }
}
