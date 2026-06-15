package com.portfolio.easycambio.client;

import com.google.gson.Gson;
import com.portfolio.easycambio.exception.NetworkException;
import com.portfolio.easycambio.exception.InvalidCurrencyException;
import com.portfolio.easycambio.model.ExchangeRates;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class FrankfurterClient implements CurrencyClient {
    private final HttpClient client;
    private final Gson gson;
    private static final String API_URL = "https://api.frankfurter.app/latest?from=";

    public FrankfurterClient() {
        // Inicializa o HttpClient com timeout
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        this.gson = new Gson();
    }

    @Override
    public ExchangeRates buscarTaxas(String base) {
        if (base == null || base.trim().isEmpty()) {
            throw new InvalidCurrencyException("A moeda base nao pode ser vazia.");
        }

        String url = API_URL + base.trim().toUpperCase();

        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Envia a requisicao HTTP
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            // Trata erros especificos da API Frankfurter (retorna 404 para moeda invalida)
            if (res.statusCode() == 404) {
                throw new InvalidCurrencyException("Moeda '" + base + "' nao e suportada ou nao existe.");
            }

            if (res.statusCode() != 200) {
                throw new NetworkException("Erro no servidor da API. Status: " + res.statusCode());
            }

            // Converte o JSON para o Record de taxas
            return gson.fromJson(res.body(), ExchangeRates.class);

        } catch (InvalidCurrencyException | NetworkException e) {
            throw e;
        } catch (Exception e) {
            throw new NetworkException("Nao foi possivel conectar com a API: " + e.getMessage(), e);
        }
    }
}
