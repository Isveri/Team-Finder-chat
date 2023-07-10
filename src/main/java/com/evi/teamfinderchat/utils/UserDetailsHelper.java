package com.evi.teamfinderchat.utils;


import com.evi.teamfinderchat.exception.UserNotFoundException;
import com.evi.teamfinderchat.security.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserDetailsHelper {

    public static User getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        throw new UserNotFoundException("Cant load security context");
    }


}
