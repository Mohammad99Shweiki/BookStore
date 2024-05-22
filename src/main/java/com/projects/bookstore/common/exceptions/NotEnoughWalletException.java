package com.projects.bookstore.common.exceptions;

public class NotEnoughWalletException extends RuntimeException {
    public NotEnoughWalletException() {
        super("You don't have enough in wallet to complete the purchase");
    }
}
