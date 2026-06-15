package com.portfolio.easycambio.client;

import com.portfolio.easycambio.model.ExchangeRates;

public interface CurrencyClient {
    ExchangeRates buscarTaxas(String base);
}
