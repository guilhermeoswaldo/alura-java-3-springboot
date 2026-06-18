# Aula 3 - Descricao

Nesta aula o projeto passou a contar com documentacao automatica da API usando SpringDoc/OpenAPI. O foco foi adicionar o Swagger UI ao projeto, configurar as informacoes gerais da API, integrar a documentacao com o uso de JWT e ajustar a seguranca para permitir acesso publico aos endpoints da documentacao.

## Alteracoes da aula 3

### Adicao da dependencia do SpringDoc

Arquivo relacionado:

- `pom.xml`

Foi adicionada a dependencia `springdoc-openapi-starter-webmvc-ui`, responsavel por gerar a documentacao OpenAPI e disponibilizar a interface do Swagger UI.

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>3.0.3</version>
</dependency>
```

Comentario: com essa dependencia, a aplicacao passa a expor a documentacao da API em rotas como `/v3/api-docs` e `/swagger-ui.html`.

### Configuracao do OpenAPI

Arquivo relacionado:

- `src/main/java/br/alura/medvollapi/infra/springdoc/SpringDocConfiguration.java`

Foi criada a classe `SpringDocConfiguration`, responsavel por customizar a documentacao gerada pelo SpringDoc.

Principais pontos configurados:

- Titulo da API como `Voll.med API`.
- Descricao das funcionalidades principais da aplicacao.
- Informacoes de contato do time backend.
- Licenca da API.
- Esquema de seguranca para autenticacao com token JWT.

Exemplo da configuracao de seguranca:

```java
new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
```

Comentario: essa configuracao faz com que o Swagger UI saiba que os endpoints protegidos usam token `Bearer` no header `Authorization`.

### Liberacao das rotas do Swagger na seguranca

Arquivo relacionado:

- `src/main/java/br/alura/medvollapi/infra/security/SecurityConfiguration.java`

As rotas da documentacao foram liberadas no Spring Security para que o Swagger UI possa ser acessado sem exigir autenticacao.

```java
authorizeRequests.requestMatchers(
        "/v3/api-docs/**",
        "/v3/api-docs.yaml",
        "/swagger-ui/**",
        "/swagger-ui.html"
).permitAll();
```

Comentario: sem essa liberacao, a propria tela de documentacao ficaria bloqueada pelo filtro de seguranca da API.

### Indicacao de autenticacao JWT nos controllers

Arquivos relacionados:

- `src/main/java/br/alura/medvollapi/controller/ConsultaController.java`
- `src/main/java/br/alura/medvollapi/controller/MedicoController.java`
- `src/main/java/br/alura/medvollapi/controller/PacienteController.java`

Os controllers passaram a usar a anotacao `@SecurityRequirement(name = "bearer-key")`, indicando na documentacao que os endpoints precisam de autenticacao via token JWT.

```java
@RestController
@RequestMapping("consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaController {
}
```

Comentario: o nome `bearer-key` precisa ser igual ao nome definido na configuracao do OpenAPI.

## Melhorias aplicadas com IA

### Ajuste do status HTTP no agendamento de consultas

Arquivo relacionado:

- `src/main/java/br/alura/medvollapi/controller/ConsultaController.java`

O endpoint de agendamento passou a retornar `201 Created` em vez de `200 OK`, deixando a resposta mais adequada para uma operacao de criacao.

```java
return ResponseEntity.status(HttpStatus.CREATED).body(dto);
```

### Reforco das regras de permissao por perfil

Arquivos relacionados:

- `src/main/java/br/alura/medvollapi/controller/MedicoController.java`
- `src/main/java/br/alura/medvollapi/controller/PacienteController.java`
- `src/main/java/br/alura/medvollapi/infra/security/SecurityConfiguration.java`

As restricoes de exclusao foram movidas para os metodos dos controllers com `@Secured(PerfilPermissao.ADMIN)`.

```java
@DeleteMapping("/{id}")
@Secured(PerfilPermissao.ADMIN)
@Transactional
public ResponseEntity<Void> excluir(@PathVariable Long id) {
}
```

Comentario: como a classe `SecurityConfiguration` ja esta com `@EnableMethodSecurity(securedEnabled = true)`, a regra fica mais proxima do endpoint protegido e evita depender apenas da configuracao central de URLs.

### Melhor tratamento de mensagens de erro

Arquivo relacionado:

- `src/main/java/br/alura/medvollapi/infra/exception/GerenciadorDeErros.java`

As mensagens retornadas em erros de JSON malformado e erros internos gerais foram ajustadas para evitar expor detalhes tecnicos da aplicacao.

```java
return ResponseEntity.badRequest().body("JSON invalido ou campo com formato incorreto");
```

```java
return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Erro interno inesperado");
```

Comentario: mensagens mais controladas melhoram a experiencia de quem consome a API e reduzem a exposicao de informacoes internas.

