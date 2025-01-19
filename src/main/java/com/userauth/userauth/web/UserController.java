package com.userauth.userauth.web;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.userauth.userauth.entity.Category;
import com.userauth.userauth.entity.ProductData;
import com.userauth.userauth.entity.ProductRequest;
import com.userauth.userauth.entity.ProfileDetails;
import com.userauth.userauth.entity.User;
import com.userauth.userauth.entity.UserProducts;
import com.userauth.userauth.service.ProductDataService;
import com.userauth.userauth.service.ProductDetailsService;
import com.userauth.userauth.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    UserService userService;
    ProductDetailsService productDetailsService;
    ProductDataService productDataService;

    @GetMapping("/api/user/{id}")
    public ResponseEntity<String> findById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUser(id).getUsername(), HttpStatus.OK);
    }

    @PostMapping("/api/user/register")
    public ResponseEntity<HttpStatus> createUser(@Valid @RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/user/get")
    public ResponseEntity<User> findByUsername(@RequestBody Map<String, String> userPayload) {
        String username = userPayload.get("username");
        User tempUser = userService.getUserFromDatabase(username);
        return new ResponseEntity<User>(tempUser, HttpStatus.OK);
    }

    @PostMapping("/api/user/insertDetails")
    public ResponseEntity<HttpStatus> createProduct(@RequestParam("files") MultipartFile[] files,
            @RequestPart("productData") @JsonDeserialize(as = ProductData.class) ProductData productData) {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setUserId(productData.getUserId());
        productRequest.setProductName(productData.getProductName());
        productRequest.setPrice(productData.getPrice());
        productRequest.setFiles(files);
        productDetailsService.saveProduct(productRequest, productData);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/user/insertProfileDetails")
    public ResponseEntity<HttpStatus> saveUserProfile(@RequestBody ProfileDetails profileDetails) {
        userService.saveUserProfile(profileDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/user/getProfile")
    public ResponseEntity<ProfileDetails> getUserProfile(@RequestBody Map<String, String> userPayload) {
        String userId = userPayload.get("userId");
        ProfileDetails profileDetails = userService.getUserProfile(userId);
        return new ResponseEntity<>(profileDetails, HttpStatus.OK);
    }

    @GetMapping("/api/user/product/{uuid}")
    public ResponseEntity<ProductData> getUserProduct(@PathVariable String uuid) {
        ProductData productData = productDataService.getProductData(uuid);
        return new ResponseEntity<>(productData, HttpStatus.OK);
    }

    @GetMapping("/api/client/product/{uuid}")
    public ResponseEntity<ProductData> getProduct(@PathVariable String uuid) {
        ProductData productData = productDataService.getProductData(uuid);
        return new ResponseEntity<>(productData, HttpStatus.OK);
    }

    @GetMapping("/api//client/getDetails")
    public ResponseEntity<List<UserProducts>> getProducts() {
        List<UserProducts> products = productDetailsService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/api/client/getCategories")
    public ResponseEntity<List<Category>> getCategory() {
        List<Category> categories = productDetailsService.getCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/api/user/getAllProducts")
    public ResponseEntity<List<UserProducts>> getUserProducts(@RequestBody Map<String, String> userPayload) {
        String userId = userPayload.get("userId");
        List<UserProducts> userProducts = userService.getAllUserProducts(userId);
        return new ResponseEntity<>(userProducts, HttpStatus.OK);
    }

    @GetMapping("/api/client/getCarousels")
    public ResponseEntity<List<String>> getCarouselImages() {
        List<String> carouselImages = productDataService.getCarouselImages();
        return new ResponseEntity<>(carouselImages, HttpStatus.OK);
    }

}
