package com.example.netdepression.base;

public class DiscoverIc {

    private String name;
    private int imageId;

    public DiscoverIc(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
