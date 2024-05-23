package ru.suleyman.bank.exception;

import lombok.extern.slf4j.Slf4j;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Slf4j
public class UserAlreadyExistAuthenticationException extends RuntimeException{
    public UserAlreadyExistAuthenticationException(String message) {
        super(message);
        log.warn(message);
    }
}
