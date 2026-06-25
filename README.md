# Tools Java Challenge

API REST desenvolvida em Java com Spring Boot para processamento e consulta de pagamentos.

Este projeto foi desenvolvido como parte de um desafio técnico com foco em boas práticas de arquitetura e modelagem de domínio.

## Funcionalidades

- Criação de pagamento com validação de request
- Suporte a tipos de pagamento:
  - `AVISTA`
  - `PARCELADO LOJA`
  - `PARCELADO EMISSOR`
- Listagem de pagamentos cadastrados
- Busca de pagamento por identificador
- Cancelamento de pagamento
- Consulta de pagamento cancelado
- Tratamento centralizado de exceções com respostas padronizadas

## Tecnologias utilizadas

- Java 17
- Spring Boot 3.4
- Spring Web
- Spring Validation
- Gradle
- JUnit 5
- Mockito
- MockMvc

## Estrutura e padrões utilizados

- **Controller**: expõe os endpoints REST
- **Service**: concentra as regras de negócio
- **Repository**: persiste os dados em memória
- **Factory**: cria pagamentos conforme o tipo informado
- **Strategy**: define o comportamento de processamento para cada tipo de pagamento
- **Mapper**: converte o modelo interno para resposta da API
- **DTOs de request/response**: separam entrada e saída da API
- **Exception Handler global**: centraliza o tratamento de erros

## Como rodar o projeto

### Pré-requisitos

- Java 17
- Gradle

### Executar a aplicação

```bash
./gradlew bootRun
```

A API ficará disponível, por padrão, em:

```text
http://localhost:8080
```

## Como rodar os testes

### Executar todos os testes

```bash
./gradlew test
```

### Executar um teste específico

```bash
./gradlew test --tests PaymentServiceTest
```

## Endpoints principais

- `POST /pagamentos`
- `GET /pagamentos`
- `GET /pagamentos/{id}`
- `PATCH /pagamentos/estorno/{id}`
- `GET /pagamentos/estorno/{id}`

## Observações

- O repositório atual é em memória, então os dados são perdidos ao reiniciar a aplicação.
- O projeto possui testes unitários, de controller e de integração cobrindo cenários de sucesso e erro.

