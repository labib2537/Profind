package com.example.profind;

public class f_0_0_0_ids_model {
    String Address, Name, Profession, Profile_Photo, Rating, Phone_Number, Followed_by, Following;

    public f_0_0_0_ids_model() {
    }

    public f_0_0_0_ids_model(String address, String name, String profession,
                             String profile_Photo, String rating, String phone_Number,
                             String followed_by, String following) {
        Address = address;
        Name = name;
        Profession = profession;
        Profile_Photo = profile_Photo;
        Rating = rating;
        Phone_Number = phone_Number;
        Followed_by = followed_by;
        Following = following;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfession() {
        return Profession;
    }

    public void setProfession(String profession) {
        Profession = profession;
    }

    public String getProfile_Photo() {
        return Profile_Photo;
    }

    public void setProfile_Photo(String profile_Photo) {
        Profile_Photo = profile_Photo;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        Phone_Number = phone_Number;
    }

    public String getFollowed_by() {
        return Followed_by;
    }

    public void setFollowed_by(String followed_by) {
        Followed_by = followed_by;
    }

    public String getFollowing() {
        return Following;
    }

    public void setFollowing(String following) {
        Following = following;
    }
}
