💳 BTG Cards Service - Desafio Técnico DOMVS iT












API REST desenvolvida como parte de um desafio técnico para a vaga de Desenvolvedor Java.
O microsserviço é responsável por orquestrar o fluxo de solicitação de cartões de crédito, aplicando regras de negócio, persistindo dados e emitindo eventos assíncronos para criação da conta do cartão.

🏗️ Arquitetura e Fluxo de Dados

Abaixo está o diagrama de sequência do fluxo principal da aplicação, demonstrando o isolamento de responsabilidades e a comunicação assíncrona entre serviços.

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
        Listener->>DB: Cria e salva ContaCartao
    end
    
    Service-->>API: Retorna DTO com Status Final
    API-->>Cliente: 201 Created
🛡️ Diferenciais Técnicos
🧩 Design Patterns

Uso do Strategy Pattern (RegraElegibilidade) para implementar o motor de regras das ofertas.

Isso permite:

adicionar novas ofertas

alterar regras sem modificar o serviço principal

manter aderência ao Princípio Open/Closed (SOLID)

🔐 Segurança e LGPD

O CPF do cliente nunca é salvo em texto puro.

Foi implementado um AttributeConverter (JPA) que:

criptografa o CPF usando AES

salva apenas a versão criptografada no banco

descriptografa apenas em tempo de execução

Isso evita exposição de dados sensíveis.

📩 Arquitetura Event-Driven

Integração com RabbitMQ para comunicação assíncrona.

Fluxo:

Proposta aprovada

Evento publicado na fila

ContaCartaoListener consome o evento

Conta de cartão criada

Isso garante:

baixo acoplamento

melhor escalabilidade

resposta rápida da API

⚠️ Tratamento Global de Erros

Uso de @RestControllerAdvice para:

capturar exceções

padronizar respostas de erro

aplicar estratégia Fail Fast

🗄️ Arquitetura de Dados

Infraestrutura preparada para múltiplos bancos:

PostgreSQL

MongoDB

Elasticsearch

Todos disponíveis via Docker Compose.

⚙️ Regras de Negócio Implementadas
🎯 Critérios de Elegibilidade
Oferta	Regras
Oferta A	Renda > R$ 1.000
Oferta B	Renda > R$ 15.000 e Investimentos > R$ 5.000
Oferta C	Renda > R$ 50.000 e Tempo de conta > 2 anos
🎁 Restrições de Benefícios
Benefício	Restrição
CASHBACK	Exclusivo (não pode combinar com PONTOS)
PONTOS	Exclusivo (não pode combinar com CASHBACK)
SEGURO_VIAGEM	Apenas Oferta C
SALA_VIP	Apenas Ofertas B e C
🚀 Como Executar o Projeto
Pré-requisitos

Java 21

Maven

Docker

Subir infraestrutura
docker compose up -d
Rodar aplicação
./mvnw clean spring-boot:run
Executar testes
./mvnw test
📖 Documentação da API

Com a aplicação rodando, acesse:

http://localhost:8080/swagger-ui/index.html
📌 Exemplo de Request
POST /api/propostas
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
