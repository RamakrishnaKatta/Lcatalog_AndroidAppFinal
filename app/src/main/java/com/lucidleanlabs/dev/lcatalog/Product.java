package com.lucidleanlabs.dev.lcatalog;


public class Product {

    private int imageId;
    private String title;
    private String description;

    Product(int imageId, String title, String description) {
        this.imageId = imageId;
        this.title = title;
        this.description = description;
    }

    int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
