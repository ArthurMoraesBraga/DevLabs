---

# 🚗 DriveFlow — Plataforma de Aluguel de Carros 🔑

> [!NOTE]
> Plataforma web para **efetuar, modificar, consultar e cancelar pedidos de aluguel de automóveis pela Internet**, com análise financeira feita por agentes (empresas e bancos) e execução de contratos, com ou sem crédito bancário.

<table>
  <tr>
    <td width="800px">
      <div align="justify">
        O <b>DriveFlow</b> foi desenvolvido no <b>Laboratório 02</b> da disciplina <b>Desenvolvimento e Integração de Aplicações Web (DIAW)</b> do curso de <b>Engenharia de Software da PUC Minas</b>, sob orientação do <a href="https://github.com/joaopauloaramuni">Prof. Dr. João Paulo Aramuni</a>. O sistema apoia a gestão de aluguéis de automóveis: <i>clientes</i> cadastrados criam e acompanham pedidos de aluguel, e <i>agentes</i> (empresas e bancos) analisam esses pedidos do ponto de vista financeiro. Com parecer positivo, o agente <b>executa o contrato</b>, define a quem o automóvel fica registrado e, se necessário, associa um <b>contrato de crédito</b> concedido por um banco. O back-end segue a arquitetura <b>MVC</b> com <b>Spring Boot</b> e persistência no <b>Supabase (PostgreSQL)</b> via <b>Spring Data JPA</b>, e o front-end usa <b>React + Vite</b> com <b>modo claro/escuro</b>.
      </div>
    </td>
    <td>
      <div>
        <img src="docs/assets/logo.svg" alt="Logo do DriveFlow" width="120px"/>
      </div>
    </td>
  </tr>
</table>

---

## 🚧 Status do Projeto

[![Versão](https://img.shields.io/badge/Versão-v1.0.0-blue?style=for-the-badge)](#) ![Status](https://img.shields.io/badge/Status-Funcional-269B65?style=for-the-badge) ![Java](https://img.shields.io/badge/Java-25-007ec6?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.x-007ec6?style=for-the-badge&logo=springboot&logoColor=white) ![Maven](https://img.shields.io/badge/Maven-3.9-007ec6?style=for-the-badge&logo=apachemaven&logoColor=white) ![React](https://img.shields.io/badge/React-19-007ec6?style=for-the-badge&logo=react&logoColor=white) ![Vite](https://img.shields.io/badge/Vite-7-007ec6?style=for-the-badge&logo=vite&logoColor=white) ![Supabase](https://img.shields.io/badge/Supabase-PostgreSQL-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white) ![Licença](https://img.shields.io/badge/Licença-MIT-007ec6?style=for-the-badge&logo=opensourceinitiative)

| Etapa do laboratório | Situação |
| :--- | :---: |
| Modelagem (Casos de Uso, Histórias de Usuário, Classes, Pacotes) | ✅ Concluída |
| Autenticação (login de clientes e agentes) | ✅ Concluída |
| CRUD de cliente | ✅ Concluído |
| Protótipo: criar e visualizar status de pedido | ✅ Concluído |
| Persistência no Supabase (PostgreSQL) via JPA/Hibernate | ✅ Concluída |
| CRUD de automóveis com edição e imagem por link | ✅ Concluído |
| Modo escuro (dark mode) no front-end | ✅ Concluído |
| Revisão dos diagramas conforme feedback | 🔄 Contínua |

---

## 📚 Índice
- [Links Úteis](#-links-úteis)
- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades Principais](#-funcionalidades-principais)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura)
  - [Diagramas](#diagramas)
- [Instalação e Execução](#-instalação-e-execução)
  - [Pré-requisitos](#pré-requisitos)
  - [Variáveis de Ambiente](#-variáveis-de-ambiente)
    - [1 Back-end (Spring Boot)](#1-back-end-spring-boot)
    - [2 Front-end (React, Vite)](#2-front-end-react-vite)
  - [Instalação de Dependências](#-instalação-de-dependências)
    - [Front-end (React)](#front-end-react)
    - [Back-end (Spring Boot)](#back-end-spring-boot)
  - [Inicialização do Banco de Dados (PostgreSQL)](#-inicialização-do-banco-de-dados-postgresql)
  - [Como Executar a Aplicação](#-como-executar-a-aplicação)
    - [Terminal 1: Back-end (Spring Boot)](#terminal-1-back-end-spring-boot)
    - [Terminal 2: Front-end (React, Vite)](#terminal-2-front-end-react-vite)
- [Deploy](#-deploy)
- [Estrutura de Pastas](#-estrutura-de-pastas)
- [Demonstração](#-demonstração)
  - [Aplicação Web](#-aplicação-web)
  - [Exemplo de saída no Terminal (API)](#-exemplo-de-saída-no-terminal-api)
- [Testes](#-testes)
- [Documentações utilizadas](#-documentações-utilizadas)
- [Autores](#-autores)
- [Contribuição](#-contribuição)
- [Agradecimentos](#-agradecimentos)
- [Licença](#-licença)

---

## 🔗 Links Úteis
* 🌐 **Demo Online:** ainda não publicada (execução local, veja [Como Executar](#-como-executar-a-aplicação)).
* 📖 **Documentação:**
  * [Histórias de Usuário](docs/historias-de-usuario.md)
  * [Diagramas UML (PlantUML)](docs/diagramas)
  * [Endpoints da API](#-endpoints-da-api)

---

## 📝 Sobre o Projeto

**Por que existe:** locadoras e instituições financeiras precisam de um canal único em que clientes solicitem aluguéis e agentes analisem esses pedidos antes de fechar o contrato.

**Problema que resolve:** sem um sistema, pedidos, análises de crédito e contratos ficam espalhados entre e-mail, telefone e planilhas. O DriveFlow centraliza o ciclo completo, do pedido ao contrato, e deixa o status visível para o cliente o tempo todo.

**Contexto:** projeto acadêmico do LAB02 (DIAW – PUC Minas), seguindo a especificação do professor. O sistema é dividido em dois subsistemas:
1. **Gestão de pedidos e contratos** → back-end Spring Boot.
2. **Construção dinâmica das páginas web** → front-end React + Vite.

**Regras principais do domínio:**
- O sistema só pode ser usado após **cadastro prévio e login**.
- **Clientes** introduzem, modificam, consultam e cancelam pedidos.
- **Agentes** (empresas e bancos) modificam e avaliam pedidos.
- Do contratante armazenam-se **RG, CPF, nome, endereço, profissão** e até **3 rendimentos** (entidade empregadora + valor).
- Do automóvel registram-se **matrícula, ano, marca, modelo e placa** (e um link de imagem opcional).
- Conforme o contrato, o automóvel fica registrado como propriedade do **cliente, da empresa ou do banco**.
- O aluguel pode ter um **contrato de crédito** concedido por um banco agente.

Ciclo de vida do pedido:

```
PENDENTE ──avaliar (aprovado)──▶ APROVADO ──executar contrato──▶ CONTRATADO
    │                               │
    ├──avaliar (reprovado)──▶ REPROVADO
    └──────cancelar─────────┴──────▶ CANCELADO
```

---

## ✨ Funcionalidades Principais

- 🔐 **Autenticação:** cadastro de clientes e agentes, login com usuário e senha (hash BCrypt), logout e rotas protegidas no back-end e no front-end.
- 👤 **CRUD de Cliente:** criar, listar, consultar, editar e excluir, com até 3 rendimentos, validação de CPF e unicidade de login/CPF.
- 📝 **Pedidos de Aluguel (cliente):** introduzir, consultar, modificar (enquanto PENDENTE) e cancelar (PENDENTE ou APROVADO). Ao cancelar, o pedido sai da aba **Ativos** e vai para a aba **Cancelados**.
- 🏦 **Análise financeira (agente):** visualizar a renda do cliente e aprovar ou reprovar o pedido com parecer.
- 📄 **Execução de contrato (agente):** definir o proprietário do automóvel (cliente, empresa ou banco) e associar um contrato de crédito (banco, valor, juros, prazo).
- 🚘 **Automóveis:** consulta da frota por todos; cadastro, **edição** e exclusão por agentes, com placa e matrícula únicas. Cada veículo aceita um **link de imagem**: ao colar a URL da foto, a imagem aparece em pré-visualização e nos cards (sem link, o sistema gera uma ilustração SVG).
- 🌗 **Modo escuro:** tema claro/escuro alternável em toda a aplicação (inclusive login e cadastro), com a preferência salva no navegador e respeitando o tema do sistema operacional.
- 📊 **Painel inicial:** resumo da quantidade de pedidos por status.
- 🛡️ **Controle de acesso por perfil:** cliente só vê os próprios dados e pedidos; agente vê todos.
- ⚠️ **Erros padronizados:** respostas JSON com mensagem e detalhes por campo, exibidas no front-end.

| Requisito do enunciado | Onde está implementado |
| :--- | :--- |
| Login obrigatório | `AuthController`, `TokenAuthenticationFilter`, `SecurityConfig`, tela `Login` |
| Cadastro prévio | `POST /api/clientes`, `POST /api/agentes`, tela `Cadastro` |
| Dados do contratante + 3 rendimentos | `Cliente`, `Rendimento`, `ClienteService` |
| Cliente: introduzir, modificar, consultar e cancelar | `PedidoAluguelController`, telas `Pedidos`, `PedidoForm`, `PedidoDetalhe` |
| Agente: modificar e avaliar | `PATCH /api/pedidos/{id}/avaliar`, `PUT /api/pedidos/{id}` |
| Execução do contrato após parecer positivo | `ContratoService`, `PedidoAluguel.executarContrato()` |
| Propriedade do automóvel (cliente/empresa/banco) | `TipoProprietario`, `Contrato`, `Automovel` |
| Contrato de crédito concedido por banco | `ContratoCredito` → `Banco` |

---

## 🛠 Tecnologias Utilizadas

As ferramentas, frameworks e bibliotecas abaixo foram utilizados na construção do projeto. Recomenda-se o uso das versões listadas (ou superiores) para garantir a compatibilidade.

### 💻 Front-end

* **Framework/Biblioteca:** React 19
* **Linguagem:** JavaScript (ES2022+) com JSX
* **Roteamento:** React Router 7
* **Estilização:** CSS puro (`src/index.css`) com tokens de cor e **modo escuro** (tema claro/escuro alternável)
* **Gerenciamento de Estado:** Context API (`AuthContext`, `ThemeContext`) + `useState`
* **Build Tool:** Vite 7

### 🖥️ Back-end

* **Linguagem/Runtime:** Java 25 (JDK)
* **Framework:** Spring Boot 4.0.x (Spring Web MVC, Spring Security, Bean Validation)
* **Build e dependências:** Apache Maven
* **Banco de Dados:** Supabase (PostgreSQL)
* **ORM:** Spring Data JPA / Hibernate
* **Autenticação:** Spring Security + token Bearer + BCrypt

### ⚙️ Infraestrutura & DevOps

* **Controle de versão:** Git + GitHub
* **Modelagem:** PlantUML

---

## 🏗 Arquitetura

O DriveFlow é um **monólito em duas aplicações** (back-end e front-end) que se comunicam por **HTTP/JSON (REST)**.

**Back-end — MVC em camadas**

| Camada | Pacote | Responsabilidade |
| :--- | :--- | :--- |
| **Model** | `model` | Entidades de domínio e regras de negócio (ex.: transições de status em `PedidoAluguel`, máximo de 3 rendimentos em `Cliente`) |
| **View** | front-end React | Construção dinâmica das páginas web a partir do JSON da API |
| **Controller** | `controller` | Endpoints REST, validação de entrada (`@Valid`) e autorização por perfil (`@PreAuthorize`) |
| Service | `service` | Orquestração dos casos de uso e regras que envolvem várias entidades |
| Repository | `repository` | Acesso a dados via Spring Data JPA (interfaces `JpaRepository`) sobre o PostgreSQL do Supabase |
| DTO | `dto` | Objetos de entrada e saída da API (a senha nunca é exposta) |
| Security | `security` | Emissão e validação de token; filtro de autenticação |
| Config | `config` | Regras de acesso, CORS, BCrypt e dados de demonstração |
| Exception | `exception` | Exceções de negócio e handler global de erros |

**Padrões adotados:** MVC, Service Layer, Repository, DTO, modelo de domínio rico (as regras de status ficam na entidade) e tratamento centralizado de exceções.

**Fluxo de uma requisição:**
```
React (pages) → services/api.js → [HTTP + Bearer token]
  → TokenAuthenticationFilter → Controller → Service → Model / Repository (JPA)
  → Supabase (PostgreSQL) → DTO de resposta → JSON → React atualiza a tela
```

**Decisões e trade-offs:**
- **Persistência com Spring Data JPA + Supabase (PostgreSQL):** os repositórios são interfaces `JpaRepository` e as entidades do `model` são mapeadas com anotações JPA (`@Entity`, herança `SINGLE_TABLE`, `@ElementCollection` para os rendimentos). O schema é gerado por `ddl-auto=update` e os dados persistem entre reinicializações.
- **Token opaco em memória** em vez de JWT: mais simples de entender e permite logout real (o token é apagado no servidor). Pode evoluir para JWT ou Supabase Auth.
- **Herança `Usuario` → `Cliente` / `Agente` → `Empresa` / `Banco`:** um único login para todos os perfis, com regras específicas por subclasse.

### Diagramas

Os diagramas estão em PlantUML na pasta [`docs/diagramas`](docs/diagramas). Para visualizar, use o [PlantUML Online](https://www.plantuml.com/plantuml) ou a extensão PlantUML do VS Code.

| Diagrama | Arquivo |
| :---: | :---: |
| **Casos de Uso** | [`casos-de-uso.puml`](docs/diagramas/casos-de-uso.puml) |
| **Classes (Modelo)** | [`classes.puml`](docs/diagramas/classes.puml) |
| **Pacotes (Visão Lógica)** | [`pacotes.puml`](docs/diagramas/pacotes.puml) |
| **Histórias de Usuário** | [`historias-de-usuario.md`](docs/historias-de-usuario.md) |

> [!TIP]
> Exporte cada diagrama como PNG para `docs/diagramas/img/` e substitua os links da tabela acima por `<img src="docs/diagramas/img/classes.png" width="400px">` para que apareçam direto no GitHub.

---

## 🔧 Instalação e Execução

### Pré-requisitos

* **Java JDK:** versão **25** (back-end Spring Boot)
* **Apache Maven:** versão **3.9** ou superior
* **Node.js:** versão LTS (**v20** ou superior), para o front-end React
* **Gerenciador de Pacotes:** npm
* **Conta no Supabase:** um projeto PostgreSQL para a conexão do back-end

---

### 🔑 Variáveis de Ambiente

Configure as variáveis de ambiente (ou arquivos de configuração) de cada parte da aplicação.

#### 1 Back-end (Spring Boot)

As credenciais do banco **não ficam** em `application.properties` (versionado). Elas vêm das variáveis de ambiente abaixo ou de `application-local.properties` (ignorado pelo Git — veja [Inicialização do Banco de Dados](#-inicialização-do-banco-de-dados-postgresql)).

| Variável | Descrição | Exemplo |
| :--- | :--- | :--- |
| `server.port` | Porta do back-end | `8080` |
| `app.cors.allowed-origin` | Origem do front-end liberada no CORS | `http://localhost:5173` |
| `SUPABASE_DB_URL` | URL JDBC do banco | `jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:5432/postgres` |
| `SUPABASE_DB_USER` | Usuário do banco | `postgres.seu_project_ref` |
| `SUPABASE_DB_PASSWORD` | Senha do banco | `senha-segura-123` |

#### 2 Front-end (React, Vite)

Crie o arquivo `frontend/.env` a partir de `frontend/.env.example` (opcional, pois o valor padrão já aponta para o back-end local). Em projetos **Vite**, as variáveis precisam do prefixo `VITE_` para ficarem disponíveis no código do cliente.

| Variável | Descrição | Exemplo |
| :--- | :--- | :--- |
| `VITE_API_URL` | URL base da API Spring Boot | `http://localhost:8080/api` |

---

### 📦 Instalação de Dependências

1. **Clone o repositório:**

```bash
git clone <URL_DO_REPOSITÓRIO>
cd aluguel-carros
```

#### Front-end (React)

```bash
cd frontend
npm install
cd ..
```

#### Back-end (Spring Boot)

```bash
cd backend
mvn clean install
cd ..
```

---

### 💾 Inicialização do Banco de Dados (PostgreSQL)

A persistência usa **Supabase (PostgreSQL)** com **Spring Data JPA / Hibernate**. O schema é criado/atualizado automaticamente na primeira execução (`spring.jpa.hibernate.ddl-auto=update`) e o `DataSeeder` popula usuários e automóveis de demonstração **apenas se ainda não existirem** (é idempotente).

**Configure as credenciais (sem expô-las no Git):**

1. Copie o modelo para um arquivo local ignorado pelo Git:

```bash
cd backend/src/main/resources
copy application-local.properties.example application-local.properties   # Windows
# cp application-local.properties.example application-local.properties   # Linux/macOS
```

2. Edite `application-local.properties` com a URL, o usuário e a senha do seu projeto Supabase (painel do Supabase → *Project Settings → Database → Connection string*, modo *pooler*).

> [!IMPORTANT]
> `application-local.properties` está no `.gitignore` e nunca deve ir para o repositório. Em produção, prefira as variáveis de ambiente `SUPABASE_DB_URL`, `SUPABASE_DB_USER` e `SUPABASE_DB_PASSWORD`, que têm prioridade sobre o arquivo local.

---

### ⚡ Como Executar a Aplicação

Execute a aplicação em modo de desenvolvimento em **dois terminais separados**.

#### Terminal 1: Back-end (Spring Boot)

```bash
cd backend
mvn spring-boot:run
```
🚀 *O back-end estará disponível em **http://localhost:8080**.*

#### Terminal 2: Front-end (React, Vite)

```bash
cd frontend
npm run dev
```
🎨 *O front-end estará disponível em **http://localhost:5173**.*

#### 👤 Usuários de demonstração

| Perfil | Usuário | Senha |
| :--- | :--- | :--- |
| Cliente | `cliente` | `123456` |
| Empresa (agente) | `empresa` | `123456` |
| Banco (agente) | `banco` | `123456` |

**Roteiro de teste:**
1. Entre como `cliente` e crie um pedido (status **PENDENTE**).
2. Saia e entre como `banco`. Abra o pedido, veja a renda do cliente e **aprove** com um parecer.
3. Clique em **Executar contrato**, escolha o proprietário e associe um crédito.
4. Volte como `cliente` e veja o status **CONTRATADO** com os dados do contrato.

---

## 🚀 Deploy

1. **Build do projeto:**

```bash
# Front-end: gera a pasta frontend/dist
cd frontend
npm run build

# Back-end: gera o .jar em backend/target
cd ../backend
mvn clean package
```

2. **Configuração do ambiente de produção:** defina as variáveis de ambiente no seu provedor (Vercel, Railway, Render etc.). Configure as de **conexão com o banco** (`SUPABASE_DB_URL`, `SUPABASE_DB_USER`, `SUPABASE_DB_PASSWORD`) para o back-end e a **URL da API** (`VITE_API_URL`) para o front-end.

3. **Execução em produção:**

```bash
java -jar backend/target/aluguel-carros-0.0.1-SNAPSHOT.jar
npx serve -s frontend/dist
```

> 🔑 Em produção, configure `VITE_API_URL` com a URL pública da API e `app.cors.allowed-origin` com a URL pública do front-end.

---

## 📂 Estrutura de Pastas

```
aluguel-carros/
├── .gitignore                   # 🧹 Ignora target, node_modules, .env etc.
├── LICENSE                      # ⚖️ Licença MIT.
├── README.md                    # 📘 Documentação principal.
│
├── backend/                     # 📁 Aplicação Spring Boot (MVC)
│   ├── pom.xml                  # 🛠️ Build e dependências (Maven).
│   └── src/main/
│       ├── java/br/pucminas/aluguelcarros/
│       │   ├── AluguelCarrosApplication.java  # ▶️ Classe principal.
│       │   ├── config/          # 🔧 SecurityConfig (acesso, CORS, BCrypt) e DataSeeder.
│       │   ├── controller/      # 🎮 Endpoints REST (Auth, Cliente, Agente, Automovel, Pedido).
│       │   ├── dto/             # ✉️ Requests e responses da API.
│       │   ├── exception/       # 💥 Exceções de negócio e GlobalExceptionHandler.
│       │   ├── model/           # 🧬 Entidades JPA e regras de negócio.
│       │   ├── repository/      # 🗄️ Repositórios Spring Data JPA (JpaRepository) sobre o PostgreSQL.
│       │   ├── security/        # 🛡️ TokenService e filtro de autenticação.
│       │   └── service/         # ⚙️ Casos de uso e regras de negócio.
│       └── resources/
│           ├── application.properties             # ⚙️ Configuração (sem segredos; usa env vars).
│           └── application-local.properties.example  # 🧩 Modelo das credenciais locais do Supabase.
│
├── frontend/                    # 📁 Aplicação React + Vite
│   ├── .env.example             # 🧩 Variáveis de ambiente do front-end.
│   ├── index.html
│   ├── package.json             # 📦 Dependências e scripts.
│   ├── vite.config.js
│   └── src/
│       ├── components/          # 🧱 Layout, RotaProtegida, FotoCarro, ThemeToggle, StatusBadge, Mensagem.
│       ├── context/             # 🔐 AuthContext (sessão) e ThemeContext (tema claro/escuro).
│       ├── pages/               # 📄 Login, Cadastro, Início, Pedidos, Clientes, Automóveis...
│       ├── services/            # 🔌 api.js (chamadas HTTP).
│       ├── utils/               # 🛠️ Formatação de datas e moeda.
│       ├── App.jsx              # 🧭 Rotas.
│       ├── main.jsx             # ▶️ Ponto de entrada.
│       └── index.css            # 🎨 Estilos globais (tokens + modo escuro).
│
└── docs/                        # 📚 Documentação
    ├── assets/logo.svg          # 🖼️ Logo do projeto.
    ├── diagramas/               # 📐 Casos de uso, classes e pacotes (PlantUML).
    └── historias-de-usuario.md  # 📝 Histórias de usuário com critérios de aceitação.
```

---

## 🎥 Demonstração

> [!WARNING]
> Substitua as imagens abaixo por capturas reais do sistema (salve em `docs/assets/` ou no GitHub Pages).

### 🌐 Aplicação Web

Para melhor visualização, as telas principais estão organizadas lado a lado.

| Tela | Captura de Tela |
| :---: | :---: |
| **Login** | **Cadastro (cliente / agente)** |
| <img src="https://joaopauloaramuni.github.io/image/aramunilogo.png" alt="Tela de Login" width="120px" height="120px"> | <img src="https://joaopauloaramuni.github.io/image/aramunilogo.png" alt="Tela de Cadastro" width="120px" height="120px"> |
| **Início (resumo por status)** | **Lista de Pedidos** |
| <img src="https://joaopauloaramuni.github.io/image/aramunilogo.png" alt="Tela Inicial" width="120px" height="120px"> | <img src="https://joaopauloaramuni.github.io/image/aramunilogo.png" alt="Lista de Pedidos" width="120px" height="120px"> |
| **Detalhe do Pedido (avaliação e contrato)** | **Automóveis (CRUD + imagem)** |
| <img src="https://joaopauloaramuni.github.io/image/aramunilogo.png" alt="Detalhe do Pedido" width="120px" height="120px"> | <img src="https://joaopauloaramuni.github.io/image/aramunilogo.png" alt="Automóveis" width="120px" height="120px"> |

### 💻 Exemplo de Saída no Terminal (API)

#### 1. Login

```bash
curl -X POST 'http://localhost:8080/api/auth/login' \
     -H 'Content-Type: application/json' \
     -d '{"login": "cliente", "senha": "123456"}'
```

**Saída esperada:**
```json
{
  "token": "q3V9...xYz",
  "usuario": { "id": 1, "login": "cliente", "nome": "Maria Silva", "perfil": "CLIENTE" }
}
```

#### 2. Criar pedido de aluguel

```bash
curl -X POST 'http://localhost:8080/api/pedidos' \
     -H 'Authorization: Bearer <token>' \
     -H 'Content-Type: application/json' \
     -d '{"automovelId": 1, "dataInicio": "2026-12-01", "dataFim": "2026-12-10"}'
```

**Saída esperada (resumida):**
```json
{
  "id": 1,
  "status": "PENDENTE",
  "dataInicio": "2026-12-01",
  "dataFim": "2026-12-10",
  "cliente": { "id": 1, "nome": "Maria Silva", "rendaTotal": 7700.00 },
  "automovel": { "id": 1, "marca": "Fiat", "modelo": "Argo", "placa": "ABC1D23" },
  "contrato": null
}
```

#### 3. Erro de validação

```json
{
  "status": 400,
  "erro": "Dados inválidos.",
  "detalhes": { "cpf": "CPF deve ter 11 dígitos (apenas números)" },
  "timestamp": "2026-11-05T14:30:00"
}
```

#### 🔌 Endpoints da API

Todas as rotas, exceto as públicas, exigem o header `Authorization: Bearer <token>`.

| Método | Rota | Acesso | Descrição |
| :--- | :--- | :--- | :--- |
| POST | `/api/auth/login` | público | Login, retorna token |
| POST | `/api/auth/logout` | logado | Invalida o token |
| GET | `/api/auth/me` | logado | Usuário atual |
| POST | `/api/clientes` | público | Cadastrar cliente |
| GET | `/api/clientes` | agente | Listar clientes |
| GET/PUT/DELETE | `/api/clientes/{id}` | o próprio cliente ou agente | Consultar / alterar / excluir |
| POST | `/api/agentes` | público | Cadastrar empresa ou banco |
| GET | `/api/agentes?tipo=BANCO` | logado | Listar agentes |
| GET | `/api/automoveis` | logado | Listar automóveis |
| POST/PUT/DELETE | `/api/automoveis[/{id}]` | agente | Manter automóveis |
| POST | `/api/pedidos` | cliente | Introduzir pedido |
| GET | `/api/pedidos` | logado | Cliente: os seus; agente: todos |
| GET/PUT | `/api/pedidos/{id}` | dono ou agente | Consultar / modificar |
| PATCH | `/api/pedidos/{id}/cancelar` | cliente dono | Cancelar |
| PATCH | `/api/pedidos/{id}/avaliar` | agente | Aprovar ou reprovar com parecer |
| POST | `/api/pedidos/{id}/contrato` | agente | Executar contrato (crédito opcional) |

---

## 🧪 Testes

Ainda não há testes automatizados. A validação foi feita manualmente pelo [roteiro de teste](#-usuários-de-demonstração) e pelos exemplos de cURL acima.

Quando forem adicionados, os testes do back-end rodam com:

```bash
cd backend
mvn test
```
*Ferramentas previstas: JUnit 5 e Spring Boot Test.*

---

## 🔗 Documentações utilizadas

* 📖 **Front-end:** [Documentação oficial do **React**](https://react.dev/reference/react)
* 📖 **Build Tool:** [Guia de configuração do **Vite**](https://vitejs.dev/config/)
* 📖 **Roteamento:** [Documentação do **React Router**](https://reactrouter.com/)
* 📖 **Back-end:** [Documentação oficial do **Spring Boot**](https://docs.spring.io/spring-boot/index.html)
* 📖 **Segurança:** [Documentação do **Spring Security**](https://docs.spring.io/spring-security/reference/)
* 📖 **Banco de Dados:** [Documentação do **Supabase**](https://supabase.com/docs)
* 📖 **Modelagem:** [Documentação do **PlantUML**](https://plantuml.com/)
* 📖 **Guia de Estilo:** [**Conventional Commits**](https://www.conventionalcommits.org/en/v1.0.0/)

---

## 👥 Autores

| 👤 Nome | 🖼️ Foto | :octocat: GitHub | 💼 LinkedIn | 📤 Gmail |
|---------|----------|-----------------|-------------|-----------|
| Nome 1  | <div align="center"><img src="https://joaopauloaramuni.github.io/image/aramunilogo.png" width="70px" height="70px"></div> | <div align="center"><a href="https://github.com/user1"><img src="https://joaopauloaramuni.github.io/image/github6.png" width="50px" height="50px"></a></div> | <div align="center"><a href="https://www.linkedin.com/in/user1"><img src="https://joaopauloaramuni.github.io/image/linkedin2.png" width="50px" height="50px"></a></div> | <div align="center"><a href="mailto:user1@gmail.com"><img src="https://joaopauloaramuni.github.io/image/gmail3.png" width="50px" height="50px"></a></div> |
| Nome 2  | <div align="center"><img src="https://joaopauloaramuni.github.io/image/aramunilogo.png" width="70px" height="70px"></div> | <div align="center"><a href="https://github.com/user2"><img src="https://joaopauloaramuni.github.io/image/github6.png" width="50px" height="50px"></a></div> | <div align="center"><a href="https://www.linkedin.com/in/user2"><img src="https://joaopauloaramuni.github.io/image/linkedin2.png" width="50px" height="50px"></a></div> | <div align="center"><a href="mailto:user2@gmail.com"><img src="https://joaopauloaramuni.github.io/image/gmail3.png" width="50px" height="50px"></a></div> |

> [!TIP]
> 💡 Troque os nomes, fotos e links pelos dados dos integrantes do grupo.

---

## 🤝 Contribuição

1. Faça um `fork` do projeto.
2. Crie uma branch para sua feature (`git checkout -b feature/minha-feature`).
3. Faça commit das mudanças (`git commit -m 'feat: adiciona nova funcionalidade X'`), seguindo [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/).
4. Faça o `push` para a branch (`git push origin feature/minha-feature`).
5. Abra um **Pull Request (PR)**.

---

## 🙏 Agradecimentos

* [**Engenharia de Software PUC Minas**](https://www.instagram.com/engsoftwarepucminas/) - Pelo apoio institucional e pela estrutura acadêmica.
* [**Prof. Dr. João Paulo Aramuni**](https://github.com/joaopauloaramuni) - Pela orientação na disciplina de Desenvolvimento e Integração de Aplicações Web e pelo material de Spring Boot.

---

## 📄 Licença

Este projeto é distribuído sob a **[Licença MIT](./LICENSE)**.

---

## Identidade visual (front-end)

O front-end usa um CSS global em `frontend/src/index.css` com tokens de cor, tipografia (DM Sans + Manrope), raios e sombras. O **modo escuro** é implementado remapeando esses tokens em `:root[data-theme="dark"]`, então todos os componentes se adaptam automaticamente; o tema é controlado por `context/ThemeContext.jsx` e alternado pelo botão `components/ThemeToggle.jsx`.

As imagens dos automóveis podem vir de um **link informado pelo agente** (campo "Link da imagem" no cadastro/edição). Quando não há link — ou o link falha ao carregar — o sistema exibe uma ilustração SVG gerada em código (`components/CarArt.jsx`), cuja cor e carroceria variam conforme a placa, sem depender de arquivos externos.
