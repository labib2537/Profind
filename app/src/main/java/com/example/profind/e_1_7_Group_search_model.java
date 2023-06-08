package com.example.profind;

public class e_1_7_Group_search_model {
    String banner,group_name,phone, total_members,time;

    public e_1_7_Group_search_model() {

    }

    public e_1_7_Group_search_model(String banner, String group_name, String phone, String total_members, String time) {
        this.banner = banner;
        this.group_name = group_name;
        this.phone = phone;
        this.total_members = total_members;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTotal_members() {
        return total_members;
    }

    public void setTotal_members(String total_members) {
        this.total_members = total_members;
    }
}
