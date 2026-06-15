package com.portfolio.easycambio.service;

import com.portfolio.easycambio.client.CurrencyClient;
import com.portfolio.easycambio.exception.CurrencyException;
import com.portfolio.easycambio.exception.InvalidCurrencyException;
import com.portfolio.easycambio.model.ExchangeRates;
import com.portfolio.easycambio.model.Resultado;

public class ConverterService {
    private final CurrencyClient client;

    // Injecao de Dependencia no construtor
    public ConverterService(CurrencyClient client) {
        this.client = client;
    }

    public Resultado converter(double valor, String origem, String destino) {
        // Validacao basica de valores
        if (valor <= 0) {
            throw new CurrencyException("O valor para conversao deve ser maior que zero.");
        }
        if (origem == null || destino == null || origem.trim().isEmpty() || destino.trim().isEmpty()) {
            throw new InvalidCurrencyException("As moedas de origem e destino nao podem ser vazias.");
        }

        String de = origem.trim().toUpperCase();
        String para = destino.trim().toUpperCase();

        // Se forem a mesma moeda, nao precisa gastar chamada de API
        if (de.equals(para)) {
            return new Resultado(valor, de, para, 1.0, valor, "N/A");
        }

        // Busca as taxas na API usando o client
        ExchangeRates taxas = client.buscarTaxas(de);

        // Verifica se a moeda destino existe no retorno da API
        if (taxas.rates() == null || !taxas.rates().containsKey(para)) {
            throw new InvalidCurrencyException("A moeda de destino '" + para + "' nao esta disponivel para conversao a partir de " + de + ".");
        }

        // Calcula a conversao
        double taxa = taxas.rates().get(para);
        double valorConvertido = valor * taxa;

        return new Resultado(valor, de, para, taxa, valorConvertido, taxas.date());
    }
}
