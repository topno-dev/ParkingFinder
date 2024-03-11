package com.example.parkingfinder.frags;

public class parking_list {
    String Category;
    String Address;
    int Space_Avail;
    String Phone;
    int Image;
    int Auto_id;
    double Latitude;
    double Longitude;
    String Url;

    public int getAuto_id() {
        return Auto_id;
    }

    public void setAuto_id(int auto_id) {
        Auto_id = auto_id;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public parking_list(String category, String address, int space_Avail, String phone, int image, int auto_id, double latitude, double longitude, String url) {
        Category = category;
        Address = address;
        Space_Avail = space_Avail;
        Phone = phone;
        Image = image;
        Auto_id = auto_id;
        Latitude = latitude;
        Longitude = longitude;
        Url = url;

    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getSpace_Avail() {
        return Space_Avail;
    }

    public void setSpace_Avail(int space_Avail) {
        Space_Avail = space_Avail;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}
