package com.example.profind;

public class d_0_0_10_posts_model {
    String description, images, img_vdo_indicator, phone, place, posted_at,
            profession, reacts, post_id, post_type, group_id, comments;

    public d_0_0_10_posts_model() {

    }

    public d_0_0_10_posts_model(String description, String phone, String images,
                                String place, String posted_at, String profession,
                                String img_vdo_indicator, String reacts, String post_id,
                                String post_type, String group_id, String comments) {
        this.description = description;
        this.images = images;
        this.img_vdo_indicator = img_vdo_indicator;
        this.phone = phone;
        this.place = place;
        this.posted_at = posted_at;
        this.profession = profession;
        this.reacts = reacts;
        this.post_id = post_id;
        this.post_type = post_type;
        this.group_id = group_id;
        this.comments = comments;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(String posted_at) {
        this.posted_at = posted_at;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getImg_vdo_indicator() {
        return img_vdo_indicator;
    }

    public void setImg_vdo_indicator(String img_vdo_indicator) {
        this.img_vdo_indicator = img_vdo_indicator;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReacts() {
        return reacts;
    }

    public void setReacts(String reacts) {
        this.reacts = reacts;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
