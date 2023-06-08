package com.example.profind;

import java.util.List;

public class e_1_4_0_Group_create_model{
    String banner,group_name,group_name_lower,phone,time,total_members;
    List<String> members,invited;

    public e_1_4_0_Group_create_model() {
    }

    public e_1_4_0_Group_create_model(String banner, String group_name,
                                      String group_name_lower, String phone,
                                      String time, String total_members,
                                      List<String> members,List<String> invited) {
        this.banner = banner;
        this.group_name = group_name;
        this.group_name_lower = group_name_lower;
        this.phone = phone;
        this.time = time;
        this.total_members = total_members;
        this.members = members;
        this.invited=invited;
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

    public String getGroup_name_lower() {
        return group_name_lower;
    }

    public void setGroup_name_lower(String group_name_lower) {
        this.group_name_lower = group_name_lower;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotal_members() {
        return total_members;
    }

    public void setTotal_members(String total_members) {
        this.total_members = total_members;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<String> getInvited() {
        return invited;
    }

    public void setInvited(List<String> invited) {
        this.invited = invited;
    }
}