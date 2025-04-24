# Ciclo Comercial

Este projeto implementa um sistema para gerenciar territórios e usuários em um ambiente comercial. Ele permite o cadastro, edição, listagem e alteração de estados de territórios e usuários, além de funcionalidades de autenticação e autorização.

## Funcionalidades

### Gerenciamento de Territórios
- Cadastro, edição e listagem de territórios.
- Alteração do estado (ativo/inativo) de territórios.
- Atualização de informações como status, email e modificações.

### Gerenciamento de Usuários
- Cadastro, edição e listagem de usuários.
- Alteração do estado (ativo/inativo) de usuários.
- Apenas usuários já cadastrados podem adicionar novos usuários.

### Segurança
- Login com autenticação baseada em email e senha.
- Controle de acesso por grupos de usuários.

## Tecnologias

- **Java 21**
- **Maven**
- **Spring Boot 3.3.4**
  - Spring Security
  - Spring Data JPA
  - Thymeleaf
- **Microsoft SQL Server**
- **Lombok**
- **JUnit 5** (Testes)

## Pré-requisitos do Projeto

- **Java 21** instalado.
- **Maven** instalado.
- Banco de dados **Microsoft SQL Server** configurado.

## Estrutura de Pacotes

- **com.inteligencia.ciclo_comercial**: Pacote principal com as classes de serviço, controladores e repositórios.
  - **controller**: Camada de controle da aplicação (APIs e rotas).
  - **service**: Lógica de negócios para as operações.
  - **repository**: Interfaces para persistência no banco de dados.
  - **model**: Entidades JPA que representam os objetos de negócio (Território, Usuário).

## Endpoints

### Usuários

- **Listar Usuários**  
  `GET /usuarios/lista-usuarios`  
  Retorna a lista de todos os usuários cadastrados.

### Territórios

- **Listar Territórios**  
  `GET /territorio/lista-territorio`  
  Retorna a lista de todas as estruturas cadastradas.

## Configuração do Banco de Dados

No arquivo `src/main/resources/application.properties`, configure as credenciais do banco de dados:

```ini
spring.datasource.url=jdbc:sqlserver://<host>:<port>;databaseName=<database>;encrypt=true;trustServerCertificate=true
spring.datasource.username=<seu-usuario>
spring.datasource.password=<sua-senha>
