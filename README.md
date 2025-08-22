### Gestor de Clientes (CRM Simples)

Este projeto é um **CRM simples** desenvolvido em **Java** com **Spring Boot**, projetado para oferecer um sistema básico de cadastro, gerenciamento e consulta de clientes. Ele segue boas práticas de arquitetura, com foco em organização, clareza e escalabilidade do código. A API é documentada com **Swagger** (Springdoc OpenAPI), permitindo testes interativos dos endpoints.

---

### 📝 Enunciado do Desafio

**Gestão de Clientes (CRM Simples)**  
**Contexto:** Cadastro de pessoa física com informações de contato e status.  

### Tabelas
- **cliente**: `(id PK, nome, email UNIQUE, telefone, cpf UNIQUE, status VARCHAR(20), criado_em, atualizado_em)`

### CRUD
- **Create**: `POST /clientes`
- **Read**: `GET /clientes/{id}`, `GET /clientes?status=ATIVO&nome=...`
- **Update**: `PUT /clientes/{id}`
- **Delete**: `DELETE /clientes/{id}` (exclusão lógica via `status='INATIVO'`)

### Regras
1. CPF válido (formato e dígitos verificadores).
2. Email obrigatório e único.
3. Status deve ser `ATIVO`, `INATIVO` ou `PROSPECT`.
4. Nome obrigatório (mínimo 3 caracteres).
5. Telefone opcional, mas deve seguir o formato DDI+DDD (ex.: `+55 (11) 98765-4321`).
6. Exclusão lógica altera o status para `INATIVO`.
7. Não permite duplicação de CPF ou email.
8. Atualização de CPF é proibida.

### Características da API
- Os clientes são listados em ordem crescente por `id` no endpoint `GET /clientes`.
- Os campos `criadoEm` e `atualizadoEm` são gerados automaticamente e exibidos no formato `DD/MM/YYYY HH:mm:ss` (ex.: `20/08/2025 18:34:00`).
- Validação explícita no serviço garante que o nome tenha pelo menos 3 caracteres, retornando uma mensagem simples: `"O nome deve ter pelo menos 3 caracteres"`.
- Outras mensagens de erro de validação são claras e legíveis, informando o motivo do erro.

---

### 📂 Estrutura do Projeto

O domínio principal do sistema está localizado em:

```
br/com/cbd/gestor_clientes/
```

Este pacote contém as entidades, repositórios, serviços e controladores que formam o núcleo do sistema de gestão de clientes.

---

### 🚀 Funcionalidades

- Cadastro de novos clientes com validação de CPF, email, telefone e nome (mínimo 3 caracteres).
- Consulta de clientes por ID ou com filtros por status e/ou nome.
- Atualização de informações de clientes existentes (exceto CPF).
- Exclusão lógica de clientes (alteração para status `INATIVO`).
- Documentação interativa da API via **Swagger UI**.
- Mensagens de erro amigáveis para validações, com mensagem simples para nome inválido.
- Estrutura modular e escalável para futuras expansões.

---

### 🛠️ Tecnologias Utilizadas

- **Java 21+**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **Banco de Dados H2** (em memória, para desenvolvimento)
- **Springdoc OpenAPI (Swagger)** (para documentação da API)
- **Maven**

---

### ▶️ Como Executar o Projeto

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/emersonpessoa01/gestao-clientes.git
   ```

2. **Acessar o diretório do projeto:**
   ```bash
   cd gestao-clientes
   ```

3. **Compilar e executar com Maven:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Acessar a aplicação:**
   - **API Base URL**: `http://localhost:8081`
   - **Swagger UI**: Acesse a documentação interativa da API em:
     ```
     http://localhost:8081/swagger-ui/index.html
     ```
     No Swagger UI, você pode visualizar todos os endpoints, testar chamadas diretamente no navegador e verificar exemplos de requisições e respostas.

---

📦 Importação da coleção de testes no Insomnia:

[Download da coleção](./api-collections/Insomnia_2025-08-21.yaml)

path:

```
./api-collections/Insomnia_2025-08-21.yaml

```

### 👤 Endpoints da API Cliente

| Método  | Endpoint                | Descrição                              |
|---------|-------------------------|----------------------------------------|
| `POST`  | `/clientes`             | Cria um novo cliente                   |
| `GET`   | `/clientes/{id}`        | Busca um cliente por ID                |
| `GET`   | `/clientes`             | Lista clientes com filtros opcionais   |
| `PUT`   | `/clientes/{id}`        | Atualiza um cliente existente          |
| `DELETE`| `/clientes/{id}`        | Realiza exclusão lógica (status=INATIVO) |

### Detalhes dos Endpoints
- **GET /clientes**: Retorna clientes ordenados por `id` em ordem ascendente. Suporta filtros opcionais por `status` (ATIVO, INATIVO, PROSPECT) e/ou `nome` (busca parcial, case-insensitive).
- **Formato de Data**: Os campos `criadoEm` e `atualizadoEm` são exibidos no formato `DD/MM/YYYY HH:mm:ss`.
- **Validações**: Erros de validação no serviço (ex.: nome com menos de 3 caracteres) retornam mensagens simples como `"O nome deve ter pelo menos 3 caracteres"`. Outros erros de validação (ex.: email, CPF) são claros e legíveis.
- **Documentação**: Use o **Swagger UI** em `http://localhost:8081/swagger-ui/index.html` para explorar e testar os endpoints interativamente.

**Exemplo de Resposta (GET /clientes):**
```json
[
    {
        "id": 1,
        "nome": "Ahsoka Tano",
        "email": "ahsokatano@mail.com",
        "telefone": "+55 (11) 9888-8888",
        "cpf": "111.444.777-35",
        "status": "ATIVO",
        "criadoEm": "19/08/2025 18:27:29",
        "atualizadoEm": "20/08/2025 10:46:06"
    }
]
```

**Exemplo de Erro de Validação (POST /clientes, nome inválido):**
```json
"O nome deve ter pelo menos 3 caracteres"
```

**Exemplo de Erro de Validação (POST /clientes, outros campos):**
```json
{
    "timestamp": "20/08/2025 21:34:00",
    "status": 400,
    "error": "Bad Request",
    "errors": [
        {
            "field": "email",
            "message": "Email inválido",
            "rejectedValue": "invalid-email"
        }
    ],
    "path": "/clientes"
}
```

---

### 💡 Objetivo

Este projeto serve como um exemplo prático de um CRM simples, ideal para estudos de **Java** com **Spring Boot** e conceitos de **CRUD** em aplicações web. Ele também demonstra o uso de **Swagger** para documentação de APIs, validação de dados com mensagens amigáveis e boas práticas de desenvolvimento.

---

### 📜 Licença

Este projeto é de uso livre para estudos e melhorias.

### 🧑‍💻 Autor
Desenvolvido por Emerson Pessoa <br>
[Linkedin](https://www.linkedin.com/in/emersonpessoa01/)