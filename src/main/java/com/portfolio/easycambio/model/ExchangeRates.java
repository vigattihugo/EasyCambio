package com.portfolio.easycambio.model;

import java.util.Map;

public record ExchangeRates(
    double amount,
    String base,
    String date,
    Map<String, Double> rates
) {}
