package com.portfolio.easycambio.model;

public record Resultado(
    double valorOriginal,
    String moedaOrigem,
    String moedaDestino,
    double taxa,
    double valorConvertido,
    String data
) {}
