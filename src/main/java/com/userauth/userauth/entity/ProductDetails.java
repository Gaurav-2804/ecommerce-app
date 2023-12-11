package com.userauth.userauth.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class ProductDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "uuid cannot be blank")
    @NonNull
    @Column(nullable = false, unique = true)
    private String uuid;

    @NotBlank(message = "productName cannot be blank")
    @NonNull
    @Column(nullable = false, unique = true)
    private String productName;

    @NotBlank(message = "brand cannot be blank")
    @NonNull
    @Column(nullable = false, unique = true)
    private String brand;

    @NotNull(message = "Price cannot be blank")
    @Column(name = "price", nullable = false)
    private double price;

    @NotBlank(message = "type cannot be blank")
    @NonNull
    @Column(nullable = false, unique = true)
    private String type;

    @Column(name = "files", nullable = false)
    @ElementCollection(targetClass = String.class)
    private List<String> fileUrls;

}
