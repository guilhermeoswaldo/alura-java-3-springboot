# Voll.med API

API REST para cadastro de médicos e pacientes, autenticação com JWT, agendamento e cancelamento de consultas.

## Tecnologias

- Java 25
- Spring Boot 4
- Spring Security
- Spring Data JPA
- Flyway
- MySQL
- Springdoc OpenAPI

## Banco de dados

Suba o MySQL com Docker Compose:

```powershell
docker compose up -d
```

O compose cria os bancos:

- `vollmed`: usado pela aplicação.
- `vollmed_test`: usado pelos testes com o profile `test`.

## Executando

```powershell
.\mvnw.cmd spring-boot:run
```

A documentação da API fica disponível em:

```text
http://localhost:8080/swagger-ui.html
```

## Testes

Para rodar os testes:

```powershell
.\mvnw.cmd test
```

Para rodar testes usando o profile `test`:

```powershell
.\mvnw.cmd test -Dspring.profiles.active=test
```
