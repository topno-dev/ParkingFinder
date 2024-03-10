package com.example.parkingfinder.frags;

public class parking_list {
    String Category;
    String Address;
    int Space_Avail;
    String Phone;
    int Image;

    public parking_list(String category, String address, int space_Avail, String phone, int image) {
        Category = category;
        Address = address;
        Space_Avail = space_Avail;
        Phone = phone;
        Image = image;
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
