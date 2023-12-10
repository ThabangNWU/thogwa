package com.thogwa.thogwa.backend.service;

import com.thogwa.thogwa.backend.model.UserProfile;
import com.thogwa.thogwa.backend.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService {
    @Autowired
    private UserProfileRepository userRepo;


    public void addProfile(UserProfile userProfile) {
        userRepo.save(userProfile);
    }

    public List<UserProfile> listProfiles(){
        return userRepo.findAll();
    }
}
