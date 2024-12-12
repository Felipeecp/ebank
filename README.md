# Bank API - Sistema Bancário

## Sobre o Projeto
API REST desenvolvida implementando um sistema bancário com funcionalidades de cadastro de clientes, transações financeiras e gerenciamento de contas.

## Funcionalidades

### Autenticação
- Registro de usuário/cliente
- Login com JWT
- Autorização baseada em token

### Operações Bancárias
- Depósitos
- Transferências (débito/crédito)
- Consulta de saldo
- Extrato de transações
- Ajuste de limite de crédito
- Listagem de contas disponíveis para transferência

## Tecnologias Utilizadas
- Java 17
- Spring Boot 3.2
- Spring Security
- JWT
- PostgreSQL
- Hibernate/JPA
- Maven

## Pré-requisitos
- JDK 17
- Maven
- PostgreSQL

## Configuração do Ambiente

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/bank-api.git
```
2. Configure o banco de dados no application.yml:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bank_db
    username: seu_usuario
    password: sua_senha
```
3. Execute a aplicação
```bash
mvn spring-boot:run
```

## Principais Endpoints

### Autenticação
```http
POST /api/auth/register - Registro de novo usuário
POST /api/auth/login - Login de usuário
```

### Conta
```http
GET /api/accounts/{accountNumber}/limits - Consulta limites e saldos
PUT /api/accounts/credit-limit - Ajusta limite de crédito
GET /api/accounts/available - Lista contas disponíveis
```

### Transações
```http
POST /api/transactions/deposit - Realiza depósito
POST /api/transactions/transfer - Realiza transferência
GET /api/transactions/balance/{accountNumber} - Consulta saldo
GET /api/accounts/{accountNumber}/statement - Extrato da conta
```