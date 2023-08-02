package com.evi.teamfinderchat.exception.handler;


import com.evi.teamfinderchat.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorCodeMsg> userNotFound(UserNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorCodeMsg.builder().code(e.getCode()).build());
    }

    @ExceptionHandler({ChatNotFoundException.class})
    public ResponseEntity<ErrorCodeMsg> alreadyBannedException(ChatNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorCodeMsg.builder().code(e.getCode()).build());
    }

    @ExceptionHandler({EmptyMessageException.class})
    public ResponseEntity<ErrorCodeMsg> alreadyInvitedException(EmptyMessageException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorCodeMsg.builder().code(e.getCode()).build());
    }

    @ExceptionHandler({GroupNotFoundException.class})
    public ResponseEntity<ErrorCodeMsg> alreadyFriendException(GroupNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorCodeMsg.builder().code(e.getCode()).build());
    }

    @ExceptionHandler({NotGroupLeaderException.class})
    public ResponseEntity<ErrorCodeMsg> alreadyReportedException(NotGroupLeaderException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorCodeMsg.builder().code(e.getCode()).build());
    }

}
