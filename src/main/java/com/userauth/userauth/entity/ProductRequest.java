package com.userauth.userauth.entity;

import org.springframework.web.multipart.MultipartFile;

public class ProductRequest {
    private String productName;
    private double price;
    // private MultipartFile file;
    private MultipartFile[] files;
    private String userId;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // public MultipartFile getFile() {
    // return this.file;
    // }

    // public void setFile(MultipartFile file) {
    // this.file = file;
    // }

    public MultipartFile[] getFiles() {
        return this.files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

}
