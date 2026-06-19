# Voll.med API

API REST para cadastro de medicos e pacientes, autenticacao com JWT, agendamento e cancelamento de consultas.

## Tecnologias

- Java 25
- Spring Boot 4
- Spring Security
- Spring Data JPA
- Flyway
- MySQL
- H2 para testes locais
- Springdoc OpenAPI
- Docker Compose

## Ambiente local

O arquivo `docker-compose.yml` e usado apenas para desenvolvimento local e sobe somente o MySQL.

```powershell
docker compose up -d
```

Depois disso, a aplicacao pode ser executada pelo Play do IntelliJ. O DevTools continua funcionando porque a API roda fora do Docker.

Com `spring.docker.compose.enabled=true` no `application.properties`, o Spring Boot tambem pode detectar o Compose local e subir o banco ao iniciar a aplicacao.

Banco local:

- `vollmed`: usado pela aplicacao.
- `vollmed_test`: reservado para testes de integracao com MySQL.

Swagger:

```text
http://localhost:8080/swagger-ui.html
```

## Testes

O build normal usa H2 nos testes de repositorio para nao depender do MySQL em execucoes comuns.

```powershell
.\mvnw.cmd test
```

Para gerar o pacote:

```powershell
.\mvnw.cmd clean package
```

Testes de integracao com MySQL devem seguir a convencao `*IT.java` e podem ser executados com o profile Maven especifico:

```powershell
.\mvnw.cmd verify -Pintegration-test
```

Nesse caso, o MySQL de teste precisa estar disponivel.

## Execucao do JAR em producao

Primeiro gere o pacote:

```powershell
.\mvnw.cmd clean package
```

Depois execute informando o profile e as variaveis obrigatorias:

```powershell
java "-Dspring.profiles.active=prod" "-DDATASOURCE_URL=jdbc:mysql://localhost:3306/vollmed" "-DDATASOURCE_USERNAME=root" "-DDATASOURCE_PASSWORD=root" "-DJWT_SECRET=uma-chave-segura" "-DCORS_ALLOWED_ORIGINS=https://seu-front.com" -jar .\target\medvollapi-0.0.1-SNAPSHOT.jar
```

No profile `prod`, a integracao automatica do Spring Boot com Docker Compose fica desativada. Isso nao impede o uso de Docker Compose por fora; apenas evita que a propria aplicacao tente controlar o Docker.

## Docker de producao

O arquivo `docker-compose.prod.yml` sobe API e MySQL juntos e nao interfere no fluxo local do IntelliJ.

Antes de construir a imagem da API, gere o JAR:

```powershell
.\mvnw.cmd package
```

Crie um arquivo `.env` baseado no `.env.example`:

```env
DATASOURCE_PASSWORD=troque-essa-senha
JWT_SECRET=troque-essa-chave-por-uma-chave-grande
CORS_ALLOWED_ORIGINS=https://seu-front.com
```

Suba o ambiente de producao:

```powershell
docker compose -f docker-compose.prod.yml up -d --build
```

Dentro da rede do Docker Compose, a API acessa o banco pelo hostname do servico:

```text
jdbc:mysql://db:3306/vollmed
```

Por isso, no Compose de producao nao se usa `localhost` para a conexao entre API e banco.
