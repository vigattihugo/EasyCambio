package com.portfolio.easycambio;

import com.portfolio.easycambio.client.FrankfurterClient;
import com.portfolio.easycambio.exception.CurrencyException;
import com.portfolio.easycambio.model.Resultado;
import com.portfolio.easycambio.service.ConverterService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Cria as dependencias (Injecao Manual)
        FrankfurterClient client = new FrankfurterClient();
        ConverterService service = new ConverterService(client);
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("   BEM-VINDO AO EASYCAMBIO (PORTFOLIO)  ");
        System.out.println("=========================================");

        boolean rodando = true;

        while (rodando) {
            System.out.println("\nEscolha uma opcao:");
            System.out.println("1 - Converter Moeda");
            System.out.println("2 - Ver Moedas Comuns");
            System.out.println("3 - Sair");
            System.out.print("Sua opcao: ");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1" -> {
                    try {
                        System.out.print("Moeda de origem (ex: USD): ");
                        String origem = scanner.nextLine().trim();

                        System.out.print("Moeda de destino (ex: BRL): ");
                        String destino = scanner.nextLine().trim();

                        System.out.print("Valor para converter: ");
                        String valorStr = scanner.nextLine().trim();

                        // Trata caso o usuario digite virgula como decimal
                        double valor = Double.parseDouble(valorStr.replace(",", "."));

                        // Executa a conversao
                        Resultado res = service.converter(valor, origem, destino);

                        // Imprime o resultado formatado
                        System.out.println("\n=========================================");
                        System.out.println("          RESULTADO DA CONVERSAO         ");
                        System.out.println("=========================================");
                        System.out.printf("Original:   %.2f %s\n", res.valorOriginal(), res.moedaOrigem());
                        System.out.printf("Convertido: %.2f %s\n", res.valorConvertido(), res.moedaDestino());
                        System.out.printf("Taxa usada: %.4f\n", res.taxa());
                        System.out.printf("Data cota:  %s\n", res.data());
                        System.out.println("=========================================");

                    } catch (NumberFormatException e) {
                        System.out.println("\n[Erro] Por favor, digite um valor numerico valido.");
                    } catch (CurrencyException e) {
                        System.out.println("\n[Erro] " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("\n[Erro] Ocorreu um erro inesperado: " + e.getMessage());
                    }
                }
                case "2" -> {
                    System.out.println("\nAlgumas moedas populares suportadas pela API:");
                    System.out.println("- USD (Dolar Americano)");
                    System.out.println("- BRL (Real Brasileiro)");
                    System.out.println("- EUR (Euro)");
                    System.out.println("- GBP (Libra Esterlina)");
                    System.out.println("- CAD (Dolar Canadense)");
                    System.out.println("- JPY (Iene Japones)");
                    System.out.println("- AUD (Dolar Australiano)");
                    System.out.println("Nota: A API suporta mais de 30 moedas no total!");
                }
                case "3" -> {
                    rodando = false;
                    System.out.println("\nObrigado por usar o EasyCambio! Ate mais.");
                }
                default -> System.out.println("\nOpcao invalida. Escolha 1, 2 ou 3.");
            }
        }

        scanner.close();
    }
}
