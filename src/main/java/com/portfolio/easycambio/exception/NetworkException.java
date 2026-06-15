package com.portfolio.easycambio.exception;

public class NetworkException extends CurrencyException {
    public NetworkException(String msg) {
        super(msg);
    }

    public NetworkException(String msg, Throwable causa) {
        super(msg, causa);
    }
}
