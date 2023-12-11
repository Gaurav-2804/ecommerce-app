package com.userauth.userauth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.userauth.userauth.entity.ProductDetails;
import com.userauth.userauth.entity.ProfileDetails;
import com.userauth.userauth.entity.UserProducts;
import com.userauth.userauth.entity.User;
import com.userauth.userauth.exception.EntityNotFoundException;
import com.userauth.userauth.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Firestore firestore;

    private CollectionReference getUserCollection() {
        return firestore.collection("users");
    }

    private CollectionReference getUserProductCollection(String userId) {
        return firestore.collection("users").document(userId).collection("products");
    }

    private CollectionReference getProfileDetailsCollection(String userId) {
        return firestore.collection("users").document(userId).collection("profile");
    }

    @Override
    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return unwrapUser(user, id);
    }

    @Override
    public User getUser(String userName) {
        Optional<User> user = userRepository.findByUsername(userName);
        return unwrapUser(user, 404L);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        getUserCollection().document(user.getUsername()).set(user);
        return userRepository.save(user);
    }

    static User unwrapUser(Optional<User> enity, Long id) {
        if (enity.isPresent())
            return enity.get();
        else
            throw new EntityNotFoundException(id, User.class);
    }

    @Override
    public User getUserFromDatabase(String userName) {
        DocumentReference docRef = getUserCollection().document(userName);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                User tempUser = document.toObject(User.class);
                return tempUser;
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new EntityNotFoundException(null, User.class);
        }
        return null;
    }

    @Override
    public List<UserProducts> getAllUserProducts(String userId) {
        ApiFuture<QuerySnapshot> querySnapshot = getUserProductCollection(userId).get();
        try {
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
            List<UserProducts> allProductsList = new ArrayList<>();
            Gson gson = new Gson();
            for (QueryDocumentSnapshot document : documents) {
                UserProducts tempProduct = new UserProducts();
                tempProduct.setCategory(document.getId());
                List<ProductDetails> productDataList = gson.fromJson(gson.toJson(document.getData().get("products")),
                        new TypeToken<List<ProductDetails>>() {
                        }.getType());
                tempProduct.setProductsList(productDataList);
                allProductsList.add(tempProduct);
            }
            return allProductsList;
        } catch (Exception e) {
            throw new EntityNotFoundException(null, ProductDetails.class);
        }
    }

    @Override
    public void saveUserProfile(ProfileDetails profileDetails) {
        String userId = profileDetails.getUserId();
        DocumentReference docRef = getProfileDetailsCollection(userId)
                .document(userId);
        docRef.set(profileDetails);
    }

    @Override
    public ProfileDetails getUserProfile(String userId) {
        DocumentReference docRef = getProfileDetailsCollection(userId)
                .document(userId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                ProfileDetails profileDetails = document.toObject(ProfileDetails.class);
                return profileDetails;
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new EntityNotFoundException(null, User.class);
        }
        return null;
    }
}
