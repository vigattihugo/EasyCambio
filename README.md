# EasyCambio 🪙

O **EasyCambio** é uma aplicação simples e robusta para linha de comando (CLI) desenvolvida em **Java 17** que realiza a conversão de moedas em tempo real consumindo a API pública **Frankfurter**.

Este projeto foi desenvolvido focando em consumo de APIs REST e os principais pilares da **Programação Orientada a Objetos (POO)**.

---

## Tecnologias Utilizadas

- **Java 17**: Versão LTS que permite o uso de recursos modernos (como *Records* e *Switch Expressions*).
- **Maven**: Gerenciamento de dependências e build.
- **Gson (Google)**: Biblioteca leve para mapear respostas JSON da API em objetos Java.
- **JUnit 5**: Framework para testes unitários.
- **HttpClient**: Cliente HTTP nativo do Java para consumo de APIs REST sem dependências externas pesadas.

---

## Conceitos de POO Demonstrados

1. **Abstração (Decoplamento)**:
   - A interface [`CurrencyClient`](src/main/java/com/portfolio/easycambio/client/CurrencyClient.java) define o contrato para buscar as taxas de câmbio. A classe [`ConverterService`](src/main/java/com/portfolio/easycambio/service/ConverterService.java) não sabe *como* as taxas são obtidas (se de uma API, banco de dados ou mock), apenas que pode solicitar taxas a qualquer classe que assine esse contrato.

2. **Polimorfismo**:
   - Graças à abstração do client, no teste unitário [`ConverterServiceTest`](src/test/java/com/portfolio/easycambio/service/ConverterServiceTest.java) criamos um *Mock* manual (classe anônima) do client. Isso permite testar as regras de conversão sem fazer requisições HTTP reais à API, mantendo os testes rápidos e independentes de conexão com a internet.

3. **Encapsulamento & Imutabilidade**:
   - Uso de **Java Records** ([`ExchangeRates`](src/main/java/com/portfolio/easycambio/model/ExchangeRates.java) e [`Resultado`](src/main/java/com/portfolio/easycambio/model/Resultado.java)) para representar os dados trafegados. Records geram dados imutáveis por padrão, com acessores limpos e livres de boilerplate (sem getters/setters repetitivos).

4. **Tratamento Customizado de Erros (Hierarquia de Exceções)**:
   - Criamos uma exceção base ([`CurrencyException`](src/main/java/com/portfolio/easycambio/exception/CurrencyException.java)) e especializações como [`NetworkException`](src/main/java/com/portfolio/easycambio/exception/NetworkException.java) (para falhas de conexão) e [`InvalidCurrencyException`](src/main/java/com/portfolio/easycambio/exception/InvalidCurrencyException.java) (para entradas inválidas), garantindo mensagens de erro limpas para o usuário e prevenindo o travamento da aplicação.

---

## Estrutura de Pastas

```text
easycambio/
│
├── src/
│   ├── main/java/com/portfolio/easycambio/
│   │   ├── Main.java               # Classe de entrada (Menu CLI)
│   │   ├── client/
│   │   │   ├── CurrencyClient.java  # Interface de comunicacao
│   │   │   └── FrankfurterClient.java # Implementacao real HTTP
│   │   ├── exception/
│   │   │   ├── CurrencyException.java
│   │   │   ├── InvalidCurrencyException.java
│   │   │   └── NetworkException.java
│   │   ├── model/
│   │   │   ├── ExchangeRates.java   # Mapeamento do JSON
│   │   │   └── Resultado.java       # DTO de saida da conversao
│   │   └── service/
│   │       └── ConverterService.java # Regras de negocio da conversao
│   │
│   └── test/java/com/portfolio/easycambio/
│       └── service/
│           └── ConverterServiceTest.java # Testes de unidade
│
├── pom.xml                         # Configurações do Maven
└── README.md                       # Documentação do projeto
```

---

## Como Executar o Projeto

> [!NOTE]
> É necessário possuir o **JDK 17 ou superior** e o **Maven** instalados na sua máquina.

1. **Clonar o Repositório**:
   ```bash
   git clone <link-do-seu-repositorio>
   cd easycambio
   ```

2. **Compilar o Projeto**:
   ```bash
   mvn clean compile
   ```

3. **Executar a Aplicação**:
   ```bash
   mvn exec:java
   ```

4. **Executar os Testes Unitários**:
   ```bash
   mvn test
   ```

---

## Como Funciona (Fluxo da Aplicação)

Ao rodar a aplicação, o seguinte menu interativo será exibido no console:

```text
=========================================
   BEM-VINDO AO EASYCAMBIO  
=========================================

Escolha uma opcao:
1 - Converter Moeda
2 - Ver Moedas Comuns
3 - Sair
Sua opcao: 
```

**Exemplo de conversão de BRL para USD:**
1. O usuário escolhe a opção `1`.
2. Digita a moeda de origem (`USD`).
3. Digita a moeda de destino (`BRL`).
4. Insere o valor (`100`).
5. A aplicação faz uma requisição HTTP, calcula o câmbio e retorna formatado:

```text
=========================================
          RESULTADO DA CONVERSAO         
=========================================
Original:   100,00 USD
Convertido: 524,50 BRL
Taxa usada: 5.2450
Data cota:  2026-06-12
=========================================
```
