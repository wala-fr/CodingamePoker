package com.codingame.model.object;

public class InvalidMoveException extends Exception {
    private static final long serialVersionUID = 696969696969L;

    public InvalidMoveException(String message) {
        super(message);
    }

}
