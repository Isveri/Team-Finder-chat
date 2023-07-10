package com.evi.teamfinderchat.exception;

import lombok.Getter;

@Getter
public class GroupNotFoundException extends RuntimeException {

    private final String code = "2";

    public GroupNotFoundException(String message) {
        super(message);
    }
}
