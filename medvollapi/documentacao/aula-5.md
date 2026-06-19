# Aula 5 - Deploy da aplicacao

Nesta aula foi criado o profile de producao da aplicacao. A configuracao separa as credenciais do banco de dados do codigo-fonte e desativa recursos de desenvolvimento que nao devem ser usados no ambiente de producao.

## Alteracoes da aula 5

### Criacao do profile de producao

Arquivo relacionado:

- `src/main/resources/application-prod.properties`

Foi criado o arquivo de configuracao do profile `prod`. As propriedades de conexao com o banco passam a receber seus valores por variaveis de ambiente.

```properties
spring.datasource.url=${DATASOURCE_URL}
spring.datasource.username=${DATASOURCE_USERNAME}
spring.datasource.password=${DATASOURCE_PASSWORD}
```

Comentario: usar variaveis de ambiente evita registrar credenciais de producao no repositorio e permite configurar cada ambiente sem alterar o codigo.

### Desativacao dos logs SQL em producao

Arquivo relacionado:

- `src/main/resources/application-prod.properties`

No profile de producao, a exibicao e a formatacao das consultas SQL foram desativadas.

```properties
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
```

Comentario: essa configuracao reduz o volume de logs e evita expor detalhes desnecessarios das consultas realizadas pela aplicacao.

## Melhorias aplicadas com IA

### Configuracao de segredo JWT e CORS por ambiente

Arquivos relacionados:

- `src/main/resources/application.properties`
- `src/main/resources/application-prod.properties`
- `src/main/java/br/alura/medvollapi/infra/security/SecurityConfiguration.java`

O segredo JWT e as origens permitidas pelo CORS passaram a ser configuraveis por variaveis de ambiente. A configuracao de seguranca tambem aceita mais de uma origem, separada por virgulas.

```properties
api.security.token.secret=${JWT_SECRET}
app.cors.allowed-origins=${CORS_ALLOWED_ORIGINS}
```

```java
return List.of(this.allowedOrigins.split(","))
        .stream()
        .map(String::trim)
        .filter(origin -> !origin.isBlank())
        .toList();
```

Comentario: isso impede que valores sensiveis e a URL do frontend fiquem fixos no codigo, alem de permitir configuracoes diferentes para desenvolvimento e producao.

### Desativacao da integracao automatica com Docker Compose em producao

Arquivo relacionado:

- `src/main/resources/application-prod.properties`

Foi desativada a integracao automatica do Spring Boot com Docker Compose quando o profile `prod` estiver ativo.

```properties
spring.docker.compose.enabled=false
```

Comentario: em producao, a aplicacao nao deve tentar iniciar ou controlar os containers locais do ambiente de desenvolvimento.

### Conteinerizacao da API e do banco de dados

Arquivos relacionados:

- `Dockerfile`
- `docker-compose.prod.yml`
- `.dockerignore`

Foi criado um `Dockerfile` para executar o JAR com o profile `prod` e um Compose de producao que sobe a API junto do MySQL. O banco possui volume persistente e healthcheck antes da inicializacao da API.

```dockerfile
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY target/medvollapi-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
```

Comentario: no Compose, a aplicacao se conecta ao banco usando `jdbc:mysql://db:3306/vollmed`, pois `db` e o nome do servico dentro da rede Docker.

### Protecao de variaveis locais e exemplo de configuracao

Arquivos relacionados:

- `.gitignore`
- `.env.example`

O arquivo `.env` foi ignorado pelo Git e foi adicionado um `.env.example` com as variaveis necessarias para executar o ambiente de producao.

```env
DATASOURCE_PASSWORD=troque-essa-senha
JWT_SECRET=troque-essa-chave-por-uma-chave-grande
CORS_ALLOWED_ORIGINS=https://seu-front.com
```

Comentario: o arquivo de exemplo documenta a configuracao necessaria sem expor valores reais.

### Separacao entre testes comuns e testes de integracao

Arquivos relacionados:

- `pom.xml`
- `src/test/java/br/alura/medvollapi/controller/ConsultaControllerUnitTest.java`
- `src/test/java/br/alura/medvollapi/domain/medico/repository/MedicoRepositoryTest.java`

Os testes usuais passaram a usar o profile `h2`, enquanto testes de integracao com MySQL podem ser identificados pelo sufixo `IT` e executados pelo profile Maven `integration-test`.

```powershell
.\mvnw.cmd test
.\mvnw.cmd verify -Pintegration-test
```

Comentario: essa separacao deixa a execucao padrao dos testes mais rapida e independente de um banco MySQL externo, mantendo uma opcao explicita para testes de integracao.

### Atualizacao das instrucoes de execucao

Arquivo relacionado:

- `README.md`

O README foi atualizado com os fluxos de desenvolvimento local, execucao do JAR com profile de producao, testes e inicializacao do Docker Compose de producao.

Comentario: a documentacao do projeto passou a orientar quais variaveis devem ser definidas e como cada ambiente deve ser executado.
