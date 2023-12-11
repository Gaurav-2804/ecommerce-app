package com.userauth.userauth.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
public class ProductData implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Id
    @GeneratedValue
    private String uuid = UUID.randomUUID().toString();

    private String userId;

    private String productName;

    private String brand;

    private double price;

    private String category;

    private String type;

    private List<String> fileUrls;

    private Map<String, String> specifications;

    private Map<String, String> description;

}
