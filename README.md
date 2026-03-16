💳 BTG Cards Service - Desafio Técnico DOMVS iT
API REST desenvolvida como parte do desafio técnico para a vaga de Desenvolvedor Java. O microsserviço é responsável por orquestrar o fluxo de solicitação de cartões de crédito, validando regras de negócio complexas, persistindo dados de forma segura e emitindo eventos assíncronos para a criação efetiva das contas.

🏗️ Arquitetura e Fluxo de Dados
Abaixo está o diagrama de sequência do fluxo principal da aplicação, evidenciando o isolamento de responsabilidades e a comunicação assíncrona:

Snippet de código
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
🛡️ Diferenciais Técnicos e Segurança
Design Patterns: Utilização do padrão Strategy (RegraElegibilidade) para o motor de regras das ofertas. Permite a criação de novas ofertas sem modificar o serviço principal (Princípio Open/Closed do SOLID).

Segurança e LGPD (Data Masking): O CPF do cliente, classificado como dado sensível, nunca é salvo em texto pleno (plaintext). Foi implementado um AttributeConverter (JPA) que utiliza criptografia AES para salvar o dado embaralhado no banco e descriptografá-lo apenas em tempo de execução.

Mensageria (Event-Driven): Integração com RabbitMQ. O serviço de propostas não bloqueia a resposta aguardando a criação da conta; um evento é publicado na fila e consumido de forma totalmente assíncrona pelo ContaCartaoListener.

Tratamento Global de Exceções: Uso de @RestControllerAdvice para capturar exceções de validação (@Valid, @CPF) e regras de negócio, padronizando os erros da API (Fail-Fast) e evitando vazamento de Stack Traces.

Testes Automatizados: Cobertura de testes unitários para os serviços e regras de negócio utilizando JUnit 5 e Mockito.

⚙️ Regras de Negócio Implementadas
Critérios de Elegibilidade:

Oferta A: Renda > R$ 1.000,00

Oferta B: Renda > R$ 15.000,00 E Investimentos > R$ 5.000,00

Oferta C: Renda > R$ 50.000,00 E Tempo de Conta Corrente > 2 anos

Restrições de Benefícios:

CASHBACK e PONTOS são mutuamente exclusivos.

SEGURO_VIAGEM exclusivo para a Oferta C.

SALA_VIP exclusivo para as Ofertas B e C.

🚀 Como Executar o Projeto
Pré-requisitos: Java 21, Maven e Docker instalados.

1. Subir a Infraestrutura (Banco de Dados e Mensageria):
Na raiz do projeto, inicie os containers via Docker Compose:

Bash
docker compose up -d
2. Rodar a Aplicação Spring Boot:

Bash
./mvnw clean spring-boot:run
O Hibernate se encarregará de criar as tabelas no PostgreSQL automaticamente.

3. Executar os Testes Unitários:

Bash
./mvnw test
📖 Documentação da API
Como a aplicação possui integração com o springdoc-openapi, a documentação interativa (Swagger UI) pode ser acessada através do link abaixo com a aplicação rodando:

👉 Acessar Swagger UI

Endpoint Principal: POST /api/propostas
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
  "beneficiosSelecionados": ["SALA_VIP", "SEGURO_VIAGEM"]
}
