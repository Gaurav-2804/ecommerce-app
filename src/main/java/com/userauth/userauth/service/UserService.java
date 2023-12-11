package com.userauth.userauth.service;

import java.util.List;
import com.userauth.userauth.entity.UserProducts;
import com.userauth.userauth.entity.ProfileDetails;
import com.userauth.userauth.entity.User;

public interface UserService {
    User getUser(Long id);

    User getUser(String userName);

    User saveUser(User user);

    User getUserFromDatabase(String userName);

    void saveUserProfile(ProfileDetails profileDetails);

    ProfileDetails getUserProfile(String userId);

    List<UserProducts> getAllUserProducts(String userId);
}
