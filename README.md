# Projeto Prático Web Application

![Language](https://img.shields.io/badge/language-Java-blue.svg)
![Framework](https://img.shields.io/badge/framework-Spring%20Boot-green.svg)
![Database](https://img.shields.io/badge/database-H2-orange.svg)

Projeto Prático Web é uma aplicação desenvolvida utilizando Spring Boot, destinada a gerenciar tarefas e projetos de forma eficiente e moderna.

## Configuração do Ambiente

Este projeto está configurado para ser executado com Java 11 e Spring Boot 2.x.

## Estrutura do Projeto

Aqui está uma visão geral da estrutura do projeto e a função de cada pacote:

### 📂 `config`
- **`SecurityConfiguration`**: Configurações de segurança, incluindo autenticação e autorização.
- **`ModelMapperConfig`**: Define as configurações para o mapeamento de modelos.

### 📂 `controller`
- Gerencia as requisições e respostas HTTP.

### 📂 `dto`
- Define os Data Transfer Objects para simplificar e abstrair as entidades da aplicação.

### 📂 `enum_data`
- Contém enums usados em todo o projeto.

### 📂 `exception`
- Classes de exceção personalizadas para um melhor controle de erros.

### 📂 `mapper`
- Facilita a conversão entre entidades e DTOs.

### 📂 `model`
- Entidades do domínio mapeadas para as tabelas do banco de dados.

### 📂 `repository`
- Interfaces para abstração de operações CRUD do banco de dados.

### 📂 `service`
- Camada de serviço que contém a lógica de negócios.

### 📂 `validator`
- Validação de objetos DTO para garantir que os dados sejam válidos antes da persistência.

### 📃 `ProjetoPraticoWebApplication.java`
- Ponto de entrada principal da aplicação.
