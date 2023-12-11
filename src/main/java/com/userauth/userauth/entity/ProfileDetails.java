package com.userauth.userauth.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
public class ProfileDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String userId;

    private String fullName;

    private String address;

    private String pinCode;

    private String city;

    private String state;

    private String mobileNumber;
}
