package com.portfolio.easycambio.exception;

public class CurrencyException extends RuntimeException {
    public CurrencyException(String msg) {
        super(msg);
    }

    public CurrencyException(String msg, Throwable causa) {
        super(msg, causa);
    }
}
