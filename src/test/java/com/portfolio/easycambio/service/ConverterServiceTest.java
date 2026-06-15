package com.portfolio.easycambio.service;

import com.portfolio.easycambio.client.CurrencyClient;
import com.portfolio.easycambio.exception.CurrencyException;
import com.portfolio.easycambio.exception.InvalidCurrencyException;
import com.portfolio.easycambio.model.ExchangeRates;
import com.portfolio.easycambio.model.Resultado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ConverterServiceTest {
    private ConverterService service;
    private CurrencyClient mockClient;

    @BeforeEach
    public void setup() {
        // Criacao de um Mock Manual do CurrencyClient usando classe anonima
        mockClient = new CurrencyClient() {
            @Override
            public ExchangeRates buscarTaxas(String base) {
                if ("USD".equals(base)) {
                    return new ExchangeRates(1.0, "USD", "2026-06-12", Map.of(
                            "BRL", 5.0,
                            "EUR", 0.9
                    ));
                }
                throw new InvalidCurrencyException("Moeda mock nao encontrada.");
            }
        };

        service = new ConverterService(mockClient);
    }

    @Test
    public void testConversaoComSucesso() {
        Resultado resultado = service.converter(100.0, "USD", "BRL");

        assertNotNull(resultado);
        assertEquals(100.0, resultado.valorOriginal());
        assertEquals("USD", resultado.moedaOrigem());
        assertEquals("BRL", resultado.moedaDestino());
        assertEquals(5.0, resultado.taxa());
        assertEquals(500.0, resultado.valorConvertido());
        assertEquals("2026-06-12", resultado.data());
    }

    @Test
    public void testConversaoMesmaMoeda() {
        Resultado resultado = service.converter(50.0, "EUR", "EUR");

        assertNotNull(resultado);
        assertEquals(50.0, resultado.valorOriginal());
        assertEquals("EUR", resultado.moedaOrigem());
        assertEquals("EUR", resultado.moedaDestino());
        assertEquals(1.0, resultado.taxa());
        assertEquals(50.0, resultado.valorConvertido());
        assertEquals("N/A", resultado.data());
    }

    @Test
    public void testValorInvalidoMenorOuIgualAZero() {
        assertThrows(CurrencyException.class, () -> {
            service.converter(-10.0, "USD", "BRL");
        });

        assertThrows(CurrencyException.class, () -> {
            service.converter(0.0, "USD", "BRL");
        });
    }

    @Test
    public void testMoedaDestinoNaoSuportada() {
        assertThrows(InvalidCurrencyException.class, () -> {
            service.converter(100.0, "USD", "GBP");
        });
    }
}
