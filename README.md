# 💳 BTG Cards Service - Desafio Técnico DOMVS iT

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.3-brightgreen?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Message_Broker-ff6600?style=for-the-badge&logo=rabbitmq)
![Docker](https://img.shields.io/badge/Docker-Containers-2496ED?style=for-the-badge&logo=docker)
![JUnit5](https://img.shields.io/badge/JUnit5-Testing-25A162?style=for-the-badge&logo=junit5)

API REST desenvolvida como parte do desafio técnico para a vaga de Desenvolvedor Java. O microsserviço é responsável por orquestrar o fluxo de solicitação de cartões de crédito, validando regras de negócio complexas, persistindo dados de forma segura e emitindo eventos assíncronos para a criação efetiva das contas.

---

## 🏗️ Arquitetura e Fluxo de Dados

Abaixo está o diagrama de sequência do fluxo principal da aplicação, evidenciando o isolamento de responsabilidades e a comunicação assíncrona:

``mermaid
sequenceDiagram
    actor Cliente
    participant API as PropostaController
    participant Service as PropostaService
    participant Regras as Motor (Strategy)
    participant DB as PostgreSQL
    participant Fila as RabbitMQ (Aprovadas)
    participant Listener as ContaCartaoListener

    Cliente->>API: POST /api/propostas
    API->>Service: processarNovaProposta()
    Service->>Regras: Validar Elegibilidade
    Regras-->>Service: Retorna Aprovada/Rejeitada
    Service->>DB: Salvar Proposta (CPF Criptografado)
    
    alt Se Proposta Aprovada
        Service->>Fila: Publicar Evento (proposta_id)
        Fila-->>Listener: Consome Evento Assincronamente
        Listener->>DB: Cria e salva "ContaCartao"
    end
    
    Service-->>API: Retorna DTO com Status Final
    API-->>Cliente: 201 Created (Status e Benefícios)
 # 🛡️ Diferenciais Técnicos e Segurança
Design Patterns: Utilização do padrão Strategy (RegraElegibilidade) para o motor de regras das ofertas. Permite a criação de novas ofertas sem modificar o serviço principal (Princípio Open/Closed do SOLID).

Segurança e LGPD (Data Masking): O CPF do cliente nunca é salvo em texto pleno. Foi implementado um AttributeConverter (JPA) que utiliza criptografia AES para salvar o dado embaralhado no banco e descriptografá-lo apenas em tempo de execução.

Mensageria (Event-Driven): Integração com RabbitMQ. O serviço de propostas não bloqueia a resposta; um evento é publicado e consumido de forma assíncrona pelo ContaCartaoListener.

Tratamento Global de Exceções: Uso de @RestControllerAdvice para capturar exceções de validação e regras de negócio, padronizando os erros da API (Fail-Fast).

Arquitetura de Dados: Infraestrutura robusta com suporte pronto para PostgreSQL, MongoDB e Elasticsearch via Docker.

# ⚙️ Regras de Negócio Implementadas
Critérios de Elegibilidade:
Oferta A: Renda > R$ 1.000,00.

Oferta B: Renda > R$ 15.000,00 E Investimentos > R$ 5.000,00.

Oferta C: Renda > R$ 50.000,00 E Tempo de Conta Corrente > 2 anos.

Restrições de Benefícios:
CASHBACK e PONTOS são mutuamente exclusivos.

SEGURO_VIAGEM exclusivo para a Oferta C.

SALA_VIP exclusivo para as Ofertas B e C.

# 🚀 Como Executar o Projeto
Pré-requisitos: Java 21, Maven e Docker instalados.

Subir a Infraestrutura:
Bash
docker compose up -d

Rodar a Aplicação:
Bash
./mvnw clean spring-boot:run

Executar os Testes:
Bash
./mvnw test

# 📖 Documentação da API
A documentação interativa (Swagger UI) pode ser acessada com a aplicação rodando em:
👉 http://localhost:8080/swagger-ui/index.html

Exemplo de Request (POST /api/propostas):
JSON
{
  "cpf": "06236683056",
  "nome": "Matheus Kormann",
  "renda": 55000.00,
  "investimentos": 10000.00,
  "tempoContaCorrenteAnos": 3,
  "ofertaSelecionada": "OFERTA_C",
  "beneficiosSelecionados": ["SALA_VIP", "SEGURO_VIAGEM"]
}
