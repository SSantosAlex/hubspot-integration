# HubSpot Integration API

## Descrição
Este projeto é uma API REST desenvolvida em Java 21 utilizando Spring Boot, que integra com a API do HubSpot. Ele implementa autenticação via OAuth 2.0 (authorization code flow), cria contatos no CRM e processa webhooks de criação de contatos.

## Tecnologias Utilizadas
- Java 21
- Spring Boot
- Spring Cloud OpenFeign
- Spring Cache
- Lombok
- OAuth 2.0

## Configuração

### 1. Clonar o Repositório
```bash
git clone https://github.com/SSantosAlex/hubspot-integration.git)
cd hubspot-integration-api
```

### 2. Configurar as Variáveis de Ambiente
No arquivo `application.yml`, defina as credenciais do HubSpot:
```yaml
hubspot:
  client:
    id: id do cliente
    secret: senha do cliente
  api:
    base-url: https://api.hubapi.com
  redirect:
    uri: http://localhost:8080/auth/callback
  scope: crm.objects.contacts.write auth

spring:
  cache:
    type: simple

feign:
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 5000
        loggerLevel: basic
```

### 3. Executar a Aplicação
```bash
./gradlew bootRun
```

## Endpoints

### 1. Obter a URL de Autenticação
**Requisição:**
```bash
curl --request GET "http://localhost:8080/hubspot/oauth/authorize"
```
**Resposta:** URL para autenticação no HubSpot.

### 2. Callback de OAuth (Trocar código por token)
**Requisição:**
```bash
curl --request GET "http://localhost:8080/hubspot/oauth/callback?code=SEU_CODIGO"
```

### 3. Criar um Contato
**Requisição:**
```bash
curl --request POST "http://localhost:8080/hubspot/contacts" \
  --header "Content-Type: application/json" \
  --data '{
    "properties": {
      "email": "teste@email.com",
      "firstname": "Nome",
      "lastname": "Sobrenome"
    }
  }'
```

### 4. Webhook de Criação de Contatos
O webhook recebe eventos de criação de contatos do HubSpot.
**Requisição de Exemplo:**
```
curl -X POST http://localhost:8080/webhooks \
  -H "Content-Type: application/json" \
  -d '{
    "objectId": 98765,
    "eventType": "contact.creation",
    "properties": {
      "email": {"value": "teste2@example.com"}
    }
  }'
```

## Documentação Técnica

### Decisões Tomadas
- **Spring Boot** foi escolhido para facilitar a configuração e desenvolvimento rápido da API.
- **OAuth 2.0** foi implementado para garantir um fluxo seguro de autenticação.
- **FeignClient** foi utilizado para integração com o HubSpot devido à sua simplicidade e suporte nativo a balanceamento de carga e interceptação de requisições.
- **Spring Cache** armazena o token OAuth 2.0, reduzindo chamadas desnecessárias ao servidor do HubSpot.
- **Lombok** reduz o código boilerplate, melhorando a legibilidade.

### Considerações e Possíveis Melhorias Futuras
- Implementação dos demais testes para maior cobertura e confiabilidade.
- Uso de um banco de dados para armazenar tokens e logs de eventos recebidos.
- Melhorias na segurança, incluindo encriptação de tokens ou o devido armazenamento das credenciais em um vault de chaves.
- Implementação de retentativas automáticas para chamadas que falhem devido a rate limits do HubSpot.
- A API utiliza cache para armazenar o token OAuth 2.0 e evitar requisições desnecessárias.


