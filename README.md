# Projeto Mail – Envio de Email e Redefinição de Senha com Autenticação via Cookie

Este projeto tem como objetivo gerenciar usuários, autenticar sessões via JWT armazenado em cookie e enviar e-mails para ações como redefinição e confirmação de alteração de senha. Implementado em Java com Quarkus, o sistema adota os princípios da Arquitetura Limpa para promover uma separação clara entre as camadas de domínio, aplicação e infraestrutura.

---

## Sumário

- [Introdução](#introdução)
- [Arquitetura e Tecnologias](#arquitetura-e-tecnologias)
- [Estrutura do Projeto](#estrutura-do-projeto)
  - [Domínio](#domínio)
  - [Aplicação](#aplicação)
  - [Infraestrutura](#infraestrutura)
- [Funcionalidades Principais](#funcionalidades-principais)
  - [Cadastro e Gerenciamento de Usuários](#cadastro-e-gerenciamento-de-usuários)
  - [Autenticação e Autorização](#autenticação-e-autorização)
  - [Envio de E-mail](#envio-de-e-mail)
  - [Redefinição de Senha](#redefinição-de-senha)
- [Detalhamento dos Métodos e Casos de Uso](#detalhamento-dos-métodos-e-casos-de-uso)
- [Configurações e Ambiente](#configurações-e-ambiente)
- [Considerações Finais](#considerações-finais)

---

## Introdução

Este sistema foi desenvolvido para fornecer um serviço robusto de autenticação e gerenciamento de usuários, com a possibilidade de redefinição de senha por meio do envio de e-mails. A autenticação é realizada através de JWT (JSON Web Token) armazenado em cookies, e as operações são organizadas em camadas, seguindo os princípios da Arquitetura Limpa, garantindo maior manutenibilidade, testabilidade e escalabilidade do código.

---

## Arquitetura e Tecnologias

### Arquitetura Limpa

O projeto está organizado de acordo com a Arquitetura Limpa, onde cada camada possui responsabilidades bem definidas:

- **Domínio:** Contém as entidades (como `Usuario` e `Perfil`), regras de negócio e interfaces dos repositórios.
- **Aplicação:** Abriga casos de uso (use cases), serviços e mappers que convertem dados entre os modelos de domínio e os DTOs (Data Transfer Objects).
- **Infraestrutura:** Implementa detalhes técnicos, como acesso a banco de dados, envio de e-mails, autenticação via JWT, filtros de requisição e configurações do Quarkus.

### Tecnologias Utilizadas

- **Java 21:** Linguagem de programação para implementar a lógica do sistema.
- **Quarkus:** Framework que otimiza a execução em tempo de execução e em ambientes cloud/nativos.
- **PostgreSQL:** Banco de dados relacional utilizado para persistência.
- **JWT (JSON Web Token):** Para autenticação e autorização via cookies.
- **Quarkus Mailer:** Para envio de e-mails com templates HTML.
- **Maven:** Gerenciador de dependências e build do projeto.

---

## Estrutura do Projeto

### Domínio

- **Modelos e Entidades:**  
  - `Usuario` – Representa o usuário do sistema com propriedades como username, nome, CPF, email, senha e perfil.
  - `Perfil` – Enum que define os papéis dos usuários (ex.: GERENTE, DESENVOLVEDOR).

- **Repositórios:**  
  - `UsuarioRepository` – Interface com métodos para operações CRUD e consultas específicas (ex.: busca por username e senha).

### Aplicação

- **Use Cases:**  
  - **AutenticarUsuarioUseCase:** Realiza a autenticação do usuário com base no username e senha.
  - **CreateUsuarioUseCase, UpdateUsuarioUseCase, DeleteUsuarioUseCase, GetUsuarioByIdUseCase, GetAllUsuarioUseCase:** Casos de uso para gerenciamento de usuários.
  - **ResetPasswordUseCase:** Implementa a lógica para redefinição de senha, incluindo validação da senha antiga, confirmação da nova senha e disparo de e-mail.

- **Serviços:**  
  - **HashService:** Interface para gerar e validar hashes de senha (utilizando PBKDF2 com salt).
  - **JwtService:** Interface para geração de JWT com as claims necessárias (perfil, username, etc.).

- **Mappers:**  
  - `UsuarioMapper` e `UsuarioEntityMapper` – Responsáveis por converter entre modelos de domínio, DTOs e entidades de persistência.

- **Filtros e Anotações:**  
  - `NotAuthenticate` – Anotação para marcar endpoints que não exigem autenticação.

### Infraestrutura

- **Recursos REST (Endpoints):**  
  - **AuthResource:** Endpoints para login, logout e verificação do usuário autenticado (via cookie e JWT).
  - **UsuarioResource:** Endpoints para operações CRUD e alteração de senha.
  - **EmailResource:** Endpoints para teste e envio de e-mails.

- **Serviços de E-mail:**  
  - **EmailServiceImpl:** Implementa o envio de e-mails utilizando o Quarkus Mailer e carrega templates HTML para mensagens.

- **Implementações de Repositórios:**  
  - **UsuarioRepositoryImpl:** Implementa a interface de repositório utilizando JPA para acesso ao banco de dados.

- **Filtros de Requisição e Resposta:**  
  - **TokenRequestFilter:** Intercepta as requisições para validar o token JWT presente no cookie.
  - **TokenResponseFilter:** Insere um novo token no cookie da resposta, caso o token tenha sido renovado.

- **Configurações Externas:**  
  - **application.properties:** Arquivo com configurações do Quarkus, conexão com o banco, SMTP, logs, CORS, entre outros.
  - **Templates HTML:** Arquivos para formatação dos e-mails de redefinição e confirmação de senha.

---

## Funcionalidades Principais

### Cadastro e Gerenciamento de Usuários

- **Criação, atualização, remoção e consulta de usuários:**  
  São implementados casos de uso que permitem criar, atualizar, excluir e buscar usuários. As operações garantem a validação dos dados e a aplicação de hash nas senhas, assegurando a integridade e segurança dos dados.

### Autenticação e Autorização

- **Login e Logout:**  
  O endpoint `/auth` permite que um usuário efetue login. Após a validação das credenciais, um JWT é gerado e enviado como cookie para o cliente, contendo informações como username e perfil, com expiração de 15 minutos.
- **Proteção de Endpoints:**  
  Os filtros `TokenRequestFilter` e `TokenResponseFilter` garantem que apenas usuários autenticados possam acessar recursos protegidos. Endpoints anotados com `@NotAuthenticate` são isentos dessa verificação.
- **Renovação do Token:**  
  O sistema verifica a idade do token (através da claim `iat`) e, se o tempo de uso ultrapassar o limite definido, gera um novo token e o inclui na resposta.

### Envio de E-mail

- **Envio de E-mails Transacionais:**  
  Utilizando a interface `EmailServicePort` e sua implementação `EmailServiceImpl`, o sistema envia e-mails com conteúdo HTML de forma reativa, garantindo uma comunicação eficaz com o usuário.
- **Templates de E-mail:**  
  Os templates HTML (ex.: `Reset-senha.html` e `Senha-Alterada.html`) são carregados do classpath e processados para incluir informações dinâmicas como nome do usuário, link de redefinição e tempo de expiração.

### Redefinição de Senha

- **Validação e Alteração de Senha:**  
  No `ResetPasswordUseCase`, o sistema valida se a senha antiga fornecida corresponde ao hash armazenado e se a nova senha foi confirmada corretamente.
- **Confirmação via E-mail:**  
  Após a alteração da senha, é disparado um e-mail de confirmação utilizando o método `sendPasswordChangedEmail`.

---

## Detalhamento dos Métodos e Casos de Uso

### 1. Autenticação

- **`AuthResource.login(AuthRequestDTO authDTO)`**  
  *Objetivo:* Validar credenciais, gerar JWT e enviá-lo via cookie.  
  *Justificativa:* Utilizar JWT via cookie facilita o gerenciamento da sessão e permite a renovação automática do token.

- **`AuthResource.logout()`**  
  *Objetivo:* Remover o token do cookie, efetuando o logout do usuário.

- **`AuthResource.whoAmI()` e `whoAmI-cookie()`**  
  *Objetivo:* Exibir informações do usuário autenticado, seja pelo token no header ou pelo cookie.

### 2. Gerenciamento de Usuários

- **`UsuarioResource.createUsuario(UsuarioRequestDTO usuarioRequestDTO)`**  
  *Objetivo:* Criar um novo usuário utilizando o caso de uso `CreateUsuarioUseCase` que valida os dados, aplica hash na senha e converte o DTO para modelo de domínio.

- **`UsuarioResource.updateUsuario(Long id, UsuarioRequestDTO usuarioRequestDTO)`**  
  *Objetivo:* Atualizar os dados do usuário, garantindo que as validações sejam realizadas e que a senha seja re-hasheada se alterada.

- **`UsuarioResource.findUsuarioById(Long id)`**  
  *Objetivo:* Buscar um usuário pelo ID e retornar os dados no formato DTO.

- **`UsuarioResource.deleteUsuario(Long id)`**  
  *Objetivo:* Excluir um usuário do sistema.

- **`UsuarioResource.alterarSenha(...)`**  
  *Objetivo:* Permitir a alteração da senha após validação da senha antiga e confirmação da nova senha, utilizando o `ResetPasswordUseCase` que também dispara o e-mail de confirmação.

### 3. Casos de Uso Internos (Application Layer)

- **`CreateUsuarioUseCase.execute(UsuarioRequestDTO)`**  
  *Objetivo:* Criar um usuário após validar os dados e converter o DTO para o modelo de domínio, aplicando o hash na senha.

- **`ResetPasswordUseCase.alterarSenha(Long userId, ResetPasswordResponseDTO dto)`**  
  *Objetivo:* Alterar a senha do usuário, validando a senha antiga e confirmando a nova senha, além de notificar o usuário via e-mail.

- **`HashServiceImpl.getHashSenha(String senha)` e `verificandoHash(...)`**  
  *Objetivo:* Gerar e validar hashes de senha utilizando PBKDF2 com salt, garantindo a segurança dos dados.

- **`JwtServiceImpl.generateJwt(UsuarioLoginResponseDTO dto)`**  
  *Objetivo:* Gerar um token JWT com as claims necessárias e definir um tempo de expiração curto, reforçando a segurança da sessão.

### 4. Envio de E-mail

- **`EmailServiceImpl.sendEmail(String to, String subject, String content)`**  
  *Objetivo:* Enviar e-mails com conteúdo HTML utilizando o Quarkus Mailer de forma reativa, tratando casos de sucesso e falha.
  
- **`EmailServiceImpl.loadTemplate(String templateName)`**  
  *Objetivo:* Carregar e processar os templates de e-mail, permitindo a personalização das mensagens.

- **`EmailServiceImpl.sendResetPasswordEmail(...)` e `sendPasswordChangedEmail(...)`**  
  *Objetivo:* Enviar e-mails específicos para redefinição e confirmação de alteração de senha, com informações dinâmicas inseridas nos templates.

### 5. Filtros de Segurança

- **`TokenRequestFilter.filter(ContainerRequestContext requestContext)`**  
  *Objetivo:* Validar a presença e a validade do token JWT em cada requisição protegida. Caso o token esteja próximo do vencimento, um novo token é gerado.
  
- **`TokenResponseFilter.filter(...)`**  
  *Objetivo:* Inserir o novo token no cookie da resposta, garantindo que o cliente sempre receba um token válido.

---

## Configurações e Ambiente

### Arquivo de Propriedades (application.properties)

Define configurações essenciais como:
- **Conexão com o Banco de Dados:** PostgreSQL, com estratégias de geração de schema específicas para ambientes de desenvolvimento e produção.
- **Configurações do Hibernate:** Estratégia de criação/atualização do banco.
- **SMTP:** Dados para envio de e-mails (host, porta, usuário e senha).
- **JWT:** Localização das chaves, issuer e expiração.
- **Logs e CORS:** Configurações detalhadas para gerenciamento de logs e permissões de acesso entre domínios.

### Dependências (pom.xml)

Gerenciadas via Maven, incluem:
- Quarkus BOM, Mailer, RESTEasy, Hibernate ORM, SmallRye JWT e outros.
- Plugins para compilação, testes e geração de imagens nativas.

---

## Considerações Finais

- **Arquitetura Limpa:**  
  A separação entre camadas facilita futuras alterações, manutenção e testes unitários.

- **JWT via Cookies:**  
  Armazenar o token em cookies permite controle eficiente da sessão e renovação automática do token.

- **Envio de E-mail:**  
  O uso de templates HTML e a abordagem reativa do Quarkus Mailer garantem e-mails consistentes sem bloquear o fluxo principal do sistema.

- **Segurança das Senhas:**  
  A utilização do algoritmo PBKDF2 com salt reforça a segurança, prevenindo ataques de força bruta e mantendo a confidencialidade dos dados sensíveis.

