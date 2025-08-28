### Gestor de Clientes (CRM Simples)

Este é um **CRM simples** desenvolvido em **Java** com **Spring Boot**,
seguindo a arquitetura **hexagonal** (ports and adapters) para gerenciar
cadastro, consulta e atualização de clientes. A API é documentada com
**Swagger** (Springdoc OpenAPI) para testes interativos.

---

#### 📝 Enunciado do Desafio

**Gestão de Clientes (CRM Simples)**\
**Contexto:** Cadastro de pessoa física com informações de contato e
status.

#### Tabelas

- **cliente**:
  `(id PK, nome, email UNIQUE, telefone, cpf UNIQUE, status VARCHAR(20), criado_em, atualizado_em)`

#### CRUD

- **Create**: `POST /clientes`
- **Read**: `GET /clientes/{id}`,
  `GET /clientes?status=ATIVO&nome=...`
- **Update**: `PUT /clientes/{id}`
- **Delete**: `DELETE /clientes/{id}` (exclusão lógica via
  `status='INATIVO'`)

#### Regras

1.  CPF válido (formato e verificação de dígitos).
2.  Email obrigatório e único.
3.  Status: `ATIVO`, `INATIVO` ou `PROSPECT`.
4.  Nome obrigatório (mínimo 3 caracteres).
5.  Telefone opcional (formato DDI+DDD, ex.: `+55 (11) 98765-4321`).
6.  Exclusão lógica define `status` como `INATIVO`.
7.  Sem duplicação de CPF ou email.
8.  CPF não pode ser atualizado.

#### Características

- Listagem por `id` ascendente em `GET /clientes`.
- Campos `criadoEm` e `atualizadoEm` em `dd/MM/yyyy HH:mm:ss` (ex.:
  `27/08/2025 20:22:00`).
- Validação de nome (mínimo 3 caracteres) com mensagem:
  `"O nome deve ter pelo menos 3 caracteres"`.
- Mensagens de erro claras para outras validações.

---

#### 📂 Estrutura do Projeto

Localizado em `br/com/cbd/gestor_clientes/`, com entidades, ports,
adapters (JDBC), serviços e mapeadores na arquitetura hexagonal.

---

#### 🚀 Funcionalidades

- Cadastro com validação de CPF, email, telefone e nome.
- Consulta por ID ou com filtros (status/nome).
- Atualização (exceto CPF).
- Exclusão lógica (`status=INATIVO`).
- Documentação via **Swagger UI**.
- Mensagens de erro amigáveis.
- Escalabilidade com arquitetura hexagonal.

---

#### 🛠️ Tecnologias

- **Java 21+**
- **Spring Boot**
- **JDBC** (com `JdbcTemplate` e PostgreSQL)
- **PostgreSQL**
- **Springdoc OpenAPI (Swagger)**
- **Maven**

---

#### ▶️ Como Executar

1.  **Pré-requisitos:**
    - Instale **PostgreSQL** e crie o banco `gestor_clientes`.
    - Configure em `application.properties`:

```
    spring.datasource.url=jdbc:postgresql://localhost:5432/gestor_clientes
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    spring.datasource.driver-class-name=org.postgresql.Driver

    # InicializaÃ§Ã£o do schema
    spring.sql.init.mode=always
    spring.sql.init.platform=postgres

    # swagger
    springdoc.api-docs.path=/v3/api-docs
    springdoc.swagger-ui.path=/swagger-ui/index.html
    springdoc.swagger-ui.operationsSorter=alpha
    springdoc.swagger-ui.tagsSorter=alpha
    springdoc.swagger-ui.displayRequestDuration=true
```

2.  **Clonar:**

```bash
git clone https://github.com/emersonpessoa01/gestao-clientes.git
```

3.  **Acessar:**

```bash
cd gestao-clientes
```

4.  **Executar**:

- Aplique o schema em src/main/resources/schema.sql
- Rode:

```bash
mvn clean install
mvn spring-boot:run
```

5.  **Acessar**:

- API: http://localhost:8081
- Swagger UI: http://localhost:8081/swagger-ui/index.html
- Validador CPF: http://localhost:8081/validate-cpf.html

📦 Schema SQL src/main/resources/schema.sql:

```sql
CREATE TABLE cliente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    cpf VARCHAR(14) UNIQUE NOT NULL,
    status VARCHAR(20) NOT NULL,
    criado_em TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP NOT NULL
);
```

Aplique no banco gestor_clientes antes de executar.

📦 Importação da coleção de testes no Insomnia:

[Download da coleção](./api-collections/Insomnia_2025-08-22.yaml)

path:

```
./api-collections/Insomnia_2025-08-22.yaml

```

#### 👤 Endpoints da API Cliente

| Método   | Endpoint                                 | Descrição                                |
| -------- | ---------------------------------------- | ---------------------------------------- |
| `POST`   | `/clientes`                              | Cria um novo cliente                     |
| `GET`    | `/clientes/{id}`                         | Busca um cliente por ID                  |
| `GET`    | `/clientes`                              | Lista clientes com filtros opcionais     |
| `PUT`    | `/clientes/{id}`                         | Atualiza um cliente existente            |
| `DELETE` | `/clientes/{id}`                         | Realiza exclusão lógica (status=INATIVO) |
| `GET`    | `/clientes/validate-cpf?cpf=11144477735` | Valida um CPF e retorna se é válido      |


Exemplo (GET /clientes):

```json
[
  {
    "id": 1,
    "nome": "Ahsoka Tano",
    "email": "ahsokatano@mail.com",
    "telefone": "+55 (11) 9888-8888",
    "cpf": "111.444.777-35",
    "status": "ATIVO",
    "criadoEm": "27/08/2025 18:27:29",
    "atualizadoEm": "27/08/2025 20:10:15"
  }
]
```

#### 💡 Objetivo

Este projeto serve como um exemplo prático de um CRM simples, ideal para estudos de **Java** com **Spring Boot** e conceitos de **CRUD** em aplicações web. Ele também demonstra o uso de **Swagger** para documentação de APIs, validação de dados com mensagens amigáveis e boas práticas de desenvolvimento.

---

#### 📜 Licença

Este projeto é de uso livre para estudos e melhorias.

#### 🧑‍💻 Autor

Desenvolvido por Emerson Pessoa <br>
[Linkedin](https://www.linkedin.com/in/emersonpessoa01/)
