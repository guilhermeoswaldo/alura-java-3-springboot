# Aula 4 - Descricao

Nesta aula o projeto passou a contar com testes automatizados para validar comportamentos da API e da camada de persistencia. O foco foi criar testes de repository com banco de dados de teste, testes de controller com MockMvc, uso de mocks para isolar dependencias e configuracao de ambiente proprio para execucao dos testes.

## Alteracoes da aula 4

### Criacao do teste de repository de medicos

Arquivo relacionado:

- `src/test/java/br/alura/medvollapi/domain/medico/repository/MedicoRepositoryTest.java`

Foi criada a classe `MedicoRepositoryTest`, responsavel por testar a query customizada `buscarMedicoAleatorioDisponivelNaData`.

Principais anotacoes utilizadas:

```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
```

Comentario: o `@DataJpaTest` carrega apenas os componentes necessarios para testes de persistencia. A anotacao `@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)` impede que o Spring substitua o banco configurado por um banco em memoria.

### Uso do banco de dados de teste

Arquivo relacionado:

- `src/test/resources/application-test.properties`

Foi configurado um profile de teste apontando para o banco `vollmed_test`.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/vollmed_test
```

Comentario: com essa configuracao, os testes podem usar um banco separado do banco principal da aplicacao, evitando alterar dados do ambiente normal.

### Persistencia de dados no teste com TestEntityManager

Arquivo relacionado:

- `src/test/java/br/alura/medvollapi/domain/medico/repository/MedicoRepositoryTest.java`

Foi usado o `TestEntityManager` para persistir entidades necessarias aos cenarios de teste.

```java
@Autowired
private TestEntityManager em;
```

Exemplo de cadastro de medico no teste:

```java
private Medico cadastrarMedico(String nome, String email, String crm, EspecialidadeMedico especialidade) {
    var medico = new Medico(this.dadosMedico(nome, email, crm, especialidade));
    this.em.persist(medico);
    return medico;
}
```

Comentario: o `TestEntityManager` facilita a preparacao do banco antes de executar a query que esta sendo testada.

### Testes para busca de medico disponivel

Arquivo relacionado:

- `src/test/java/br/alura/medvollapi/domain/medico/repository/MedicoRepositoryTest.java`

Foram criados cenarios para validar a disponibilidade de medicos em consultas:

- Deve devolver `null` quando o unico medico cadastrado ja possui consulta no horario.
- Deve devolver o medico quando ele estiver disponivel na data.
- Deve devolver `null` quando nao existir medico da especialidade informada.
- Deve devolver `null` quando o medico estiver inativo.
- Deve devolver o medico quando a consulta no horario estiver cancelada.

Exemplo de validacao:

```java
var medicoLivre = medicoRepository.buscarMedicoAleatorioDisponivelNaData(
        EspecialidadeMedico.CARDIOLOGIA,
        proximaSegundaAs10
);

assertThat(medicoLivre).isNull();
```

Comentario: esses testes validam a query do repository de forma mais proxima do uso real, incluindo medico ativo, especialidade e consultas canceladas.

### Criacao do teste de controller de consultas

Arquivo relacionado:

- `src/test/java/br/alura/medvollapi/controller/ConsultaControllerUnitTest.java`

Foi criada a classe `ConsultaControllerUnitTest`, responsavel por testar o endpoint de consultas usando `MockMvc`.

Principais anotacoes utilizadas:

```java
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
```

Comentario: o `MockMvc` permite simular requisicoes HTTP sem subir um servidor real, validando status code, corpo da resposta e integracao com validacoes do Spring.

### Uso de mock para isolar o service

Arquivo relacionado:

- `src/test/java/br/alura/medvollapi/controller/ConsultaControllerUnitTest.java`

O `ConsultaService` foi substituido por um mock no contexto do teste.

```java
@MockitoBean
private ConsultaService consultaService;
```

Comentario: no Spring Boot 4, a anotacao usada para mocks no contexto Spring e `@MockitoBean`, substituindo o uso antigo de `@MockBean`.

### Teste do endpoint de agendamento

Arquivo relacionado:

- `src/test/java/br/alura/medvollapi/controller/ConsultaControllerUnitTest.java`

Foram testados os cenarios principais do endpoint `POST /consultas`:

- Deve devolver `400 Bad Request` quando as informacoes estiverem invalidas.
- Deve devolver `201 Created` quando as informacoes estiverem validas.

Exemplo de requisicao com `MockMvc`:

```java
var response = mockMvc.perform(post("/consultas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosAgendamentoConsultaJson.write(
                        new DadosAgendamentoConsulta(2L, 5L, especialidade, data)).getJson()))
        .andReturn().getResponse();
```

Comentario: o teste usa `JacksonTester` para gerar o JSON da requisicao e comparar a resposta esperada.

### Teste do endpoint de cancelamento

Arquivo relacionado:

- `src/test/java/br/alura/medvollapi/controller/ConsultaControllerUnitTest.java`

Foi adicionado teste para o endpoint `POST /consultas/cancelar`.

```java
var response = mockMvc.perform(post("/consultas/cancelar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "idConsulta": 1,
                          "motivo": "PACIENTE_DESISTIU"
                        }
                        """))
        .andReturn().getResponse();

assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
```

Comentario: esse teste valida que uma requisicao de cancelamento com corpo valido retorna `204 No Content`.

## Melhorias aplicadas com IA

### Criacao do banco de teste no Docker Compose

Arquivos relacionados:

- `docker-compose.yml`
- `docker/mysql/init/01-create-test-database.sql`

O Docker Compose passou a montar scripts de inicializacao do MySQL.

```yaml
volumes:
  - ./docker/mysql/init:/docker-entrypoint-initdb.d
```

Foi criado um script para criar o banco `vollmed_test`.

```sql
create database if not exists vollmed_test;
```

Comentario: a variavel `MYSQL_DATABASE` da imagem oficial do MySQL cria apenas um banco inicial. Para criar tambem o banco de teste, foi usado um script em `/docker-entrypoint-initdb.d`.

### Separacao das configuracoes de teste

Arquivos relacionados:

- `src/test/resources/application-test.properties`
- `src/test/resources/application-h2.properties`

As configuracoes especificas de teste foram mantidas em `src/test/resources`, separadas das configuracoes principais da aplicacao.

Comentario: essa organizacao evita levar configuracoes de teste para o artefato principal da aplicacao.

### Configuracao opcional de H2 em memoria

Arquivo relacionado:

- `src/test/resources/application-h2.properties`

Foi criada uma configuracao opcional para executar testes com H2 em memoria.

```properties
spring.datasource.url=jdbc:h2:mem:medvollapi_test;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.docker.compose.enabled=false
```

Comentario: o modo `MODE=MySQL` reduz incompatibilidades entre o H2 e as migrations escritas para MySQL, mas os testes principais passaram a usar o banco MySQL de teste para manter o mesmo dialeto da aplicacao.

### Ajustes adicionais nos testes

Arquivos relacionados:

- `src/test/java/br/alura/medvollapi/domain/medico/repository/MedicoRepositoryTest.java`
- `src/test/java/br/alura/medvollapi/controller/ConsultaControllerUnitTest.java`

Foram adicionados cenarios extras para aumentar a cobertura dos comportamentos ja existentes:

- Medico de especialidade diferente nao deve ser selecionado.
- Medico inativo nao deve ser selecionado.
- Consulta cancelada nao deve bloquear a disponibilidade do medico.
- Cancelamento de consulta valido deve retornar `204 No Content`.

Comentario: essas melhorias aumentam a confianca nas regras ja implementadas sem alterar o fluxo da aplicacao nem antecipar mudancas das proximas aulas.
