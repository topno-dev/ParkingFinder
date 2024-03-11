package com.example.parkingfinder.frags;

public class vehicle {
    String name;
    String veh_num;
    int image;

    public vehicle(String name, String veh_num, int image) {
        this.name = name;
        this.veh_num = veh_num;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVeh_num() {
        return veh_num;
    }

    public void setVeh_num(String veh_num) {
        this.veh_num = veh_num;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
