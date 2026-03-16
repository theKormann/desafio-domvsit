# 💳 BTG Cards Service - Desafio Técnico DOMVS iT

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3-brightgreen?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Message_Broker-ff6600?style=for-the-badge&logo=rabbitmq)
![Docker](https://img.shields.io/badge/Docker-Containers-2496ED?style=for-the-badge&logo=docker)

API REST desenvolvida como parte do desafio técnico para a vaga de Desenvolvedor Java. O microsserviço é responsável por orquestrar o fluxo de solicitação de cartões de crédito, validando regras de negócio complexas, persistindo os dados de forma relacional e emitindo eventos assíncronos para processamento posterior.

## 🎯 Diferenciais Técnicos da Arquitetura

* **Design Patterns:** Utilização do padrão **Strategy** (através da interface `RegraElegibilidade`) para o motor de regras das ofertas. Isso permite que novas ofertas sejam criadas sem modificar o serviço principal, respeitando o Princípio Aberto/Fechado (Open/Closed) do SOLID.
* **Validação Robusta na Entrada:** Uso de Bean Validation (JSR 303) com DTOs, incluindo a validação matemática real de documentos com a anotação `@CPF` do Hibernate Validator.
* **Mensageria (Event-Driven):** Integração com RabbitMQ. Propostas aprovadas não bloqueiam a resposta ao cliente; um evento é publicado na fila `propostas.aprovadas.queue` para processamento assíncrono (ex: emissão física do cartão).
* **Isolamento de Domínio:** Separação clara entre Entidades de Domínio (`Proposta`) e Objetos de Transferência (`RequestDTO` e `ResponseDTO`).

## ⚙️ Regras de Negócio Implementadas

1. **Critérios de Elegibilidade:**
   * **Oferta A:** Renda > R$ 1.000,00
   * **Oferta B:** Renda > R$ 15.000,00 E Investimentos > R$ 5.000,00
   * **Oferta C:** Renda > R$ 50.000,00 E Tempo de Conta Corrente > 2 anos
2. **Restrições de Benefícios:**
   * `CASHBACK` e `PONTOS` não podem ser selecionados simultaneamente.
   * `SEGURO_VIAGEM` está disponível exclusivamente para a Oferta C.
   * `SALA_VIP` está disponível apenas para as Ofertas B e C.

## 🚀 Como Executar o Projeto

**Pré-requisitos:** Ter o Java 21, Maven e Docker (ou Docker Compose) instalados.

1. **Subir a Infraestrutura (Banco de Dados e Mensageria):**
   Na raiz do projeto, inicie os containers via Docker Compose:
   ```bash
   docker compose up -d
Rodar a Aplicação Spring Boot:

Bash
./mvnw spring-boot:run
A API estará disponível na porta 8080. O Hibernate criará as tabelas no banco de dados automaticamente.

📖 Documentação da API
POST /api/propostas
Cria e analisa uma nova proposta de cartão de crédito.

Exemplo de Request (Cenário de Aprovação - Oferta C):

JSON
{
  "cpf": "06236683056",
  "nome": "Matheus Kormann",
  "renda": 55000.00,
  "investimentos": 10000.00,
  "tempoContaCorrenteAnos": 3,
  "ofertaSelecionada": "OFERTA_C",
  "beneficiosSelecionados": [
    "SALA_VIP",
    "SEGURO_VIAGEM"
  ]
}


Exemplo de Response (201 Created):

JSON
{
  "id": "84e8ef21-e52a-4873-b776-55e3c63e4c90",
  "ofertaSelecionada": "OFERTA_C",
  "beneficiosAtivos": [
    "SALA_VIP",
    "SEGURO_VIAGEM"
  ],
  "status": "APROVADA"
}


Nota: Se uma regra de benefício for violada ou o CPF for matematicamente inválido, a API retornará 400 Bad Request.