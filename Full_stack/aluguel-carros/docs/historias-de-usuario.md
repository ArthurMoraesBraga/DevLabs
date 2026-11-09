# Histórias de Usuário

Formato: **Como** <ator>, **quero** <ação>, **para** <benefício>. Cada história tem critérios de aceitação verificáveis no protótipo.

## Autenticação

### HU01 – Cadastro prévio
**Como** visitante, **quero** me cadastrar como cliente ou agente (empresa/banco), **para** poder usar o sistema.
- O login deve ser único no sistema.
- A senha deve ter ao menos 6 caracteres e é armazenada com hash (BCrypt).
- Cliente informa RG, CPF (11 dígitos), nome, endereço, profissão e até 3 rendimentos.
- Agente informa razão social, endereço, CNPJ (14 dígitos) e o tipo (EMPRESA ou BANCO).

### HU02 – Login
**Como** usuário cadastrado, **quero** entrar com usuário e senha, **para** acessar as funções do meu perfil.
- Credenciais inválidas exibem "Usuário ou senha inválidos." sem indicar qual campo errou.
- Sem login, qualquer página interna redireciona para a tela de login e a API responde 401.

### HU03 – Logout
**Como** usuário logado, **quero** sair do sistema, **para** encerrar minha sessão com segurança.
- O token é invalidado no servidor.

## Cliente (CRUD)

### HU04 – Consultar e editar meu cadastro
**Como** cliente, **quero** ver e alterar meus dados e rendimentos, **para** mantê-los atualizados para a análise financeira.
- O sistema impede mais de 3 rendimentos.
- Um cliente não acessa o cadastro de outro cliente (403).

### HU05 – Excluir minha conta
**Como** cliente, **quero** excluir meu cadastro, **para** deixar de usar o sistema.
- Não é permitido excluir com pedidos ativos (PENDENTE, APROVADO ou CONTRATADO).

### HU06 – Gerenciar clientes
**Como** agente, **quero** listar, buscar, editar e excluir clientes, **para** dar suporte à análise dos pedidos.

## Pedidos

### HU07 – Introduzir pedido
**Como** cliente, **quero** criar um pedido escolhendo automóvel e período, **para** solicitar um aluguel.
- O pedido nasce com status PENDENTE.
- A data de fim não pode ser anterior à de início; a de início não pode estar no passado.
- O automóvel não pode ter outro pedido ativo no mesmo período.

### HU08 – Consultar pedidos e status
**Como** cliente, **quero** ver meus pedidos e seus status, **para** acompanhar a solicitação.
- O cliente vê só os próprios pedidos; o agente vê todos.

### HU09 – Modificar pedido
**Como** cliente, **quero** alterar automóvel ou datas de um pedido PENDENTE, **para** ajustar minha solicitação.
**Como** agente, **quero** modificar pedidos PENDENTES ou APROVADOS, **para** ajustar condições negociadas.

### HU10 – Cancelar pedido
**Como** cliente, **quero** cancelar um pedido PENDENTE ou APROVADO, **para** desistir do aluguel.

### HU11 – Avaliar pedido
**Como** agente, **quero** ver a renda do cliente e aprovar ou reprovar o pedido com um parecer, **para** realizar a análise financeira.
- Só pedidos PENDENTES podem ser avaliados. O parecer é obrigatório.

### HU12 – Executar contrato
**Como** agente, **quero** executar o contrato de um pedido APROVADO, **para** formalizar o aluguel.
- Define se o automóvel fica registrado como propriedade do cliente, da empresa ou do banco.
- O status passa para CONTRATADO.

### HU13 – Contrato de crédito
**Como** agente, **quero** associar ao contrato um crédito concedido por um banco (valor, juros, prazo), **para** financiar o aluguel.

## Automóveis

### HU14 – Manter automóveis
**Como** agente, **quero** cadastrar, editar e excluir automóveis (matrícula, ano, marca, modelo, placa), **para** manter a frota.
- Placa e matrícula são únicas. Automóvel com pedidos não pode ser excluído.

### HU15 – Consultar automóveis
**Como** usuário logado, **quero** ver a lista de automóveis, **para** escolher um ao fazer o pedido.
