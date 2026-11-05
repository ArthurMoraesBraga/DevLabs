// Camada de acesso à API REST do back-end Spring Boot.
const BASE_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api'
const TOKEN_KEY = 'aluguel.token'

export const tokenStorage = {
  get: () => localStorage.getItem(TOKEN_KEY),
  set: (t) => localStorage.setItem(TOKEN_KEY, t),
  clear: () => localStorage.removeItem(TOKEN_KEY),
}

export class ApiError extends Error {
  constructor(status, message, detalhes) {
    super(message)
    this.status = status
    this.detalhes = detalhes
  }
}

async function request(method, path, body) {
  const headers = { 'Content-Type': 'application/json' }
  const token = tokenStorage.get()
  if (token) headers.Authorization = `Bearer ${token}`

  const resp = await fetch(`${BASE_URL}${path}`, {
    method,
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  })

  if (resp.status === 401 && path !== '/auth/login') {
    tokenStorage.clear()
    window.dispatchEvent(new Event('sessao-expirada'))
  }

  const texto = await resp.text()
  const dados = texto ? JSON.parse(texto) : null

  if (!resp.ok) {
    const msg = dados?.erro ?? (resp.status === 401 ? 'Sessão expirada. Faça login novamente.' : `Erro ${resp.status}`)
    throw new ApiError(resp.status, msg, dados?.detalhes)
  }
  return dados
}

export const api = {
  // Autenticação
  login: (login, senha) => request('POST', '/auth/login', { login, senha }),
  logout: () => request('POST', '/auth/logout'),
  me: () => request('GET', '/auth/me'),

  // Clientes (CRUD)
  criarCliente: (c) => request('POST', '/clientes', c),
  listarClientes: () => request('GET', '/clientes'),
  buscarCliente: (id) => request('GET', `/clientes/${id}`),
  atualizarCliente: (id, c) => request('PUT', `/clientes/${id}`, c),
  excluirCliente: (id) => request('DELETE', `/clientes/${id}`),

  // Agentes
  criarAgente: (a) => request('POST', '/agentes', a),
  listarBancos: () => request('GET', '/agentes?tipo=BANCO'),

  // Automóveis
  listarAutomoveis: () => request('GET', '/automoveis'),
  criarAutomovel: (a) => request('POST', '/automoveis', a),
  atualizarAutomovel: (id, a) => request('PUT', `/automoveis/${id}`, a),
  excluirAutomovel: (id) => request('DELETE', `/automoveis/${id}`),

  // Pedidos
  listarPedidos: () => request('GET', '/pedidos'),
  buscarPedido: (id) => request('GET', `/pedidos/${id}`),
  criarPedido: (p) => request('POST', '/pedidos', p),
  atualizarPedido: (id, p) => request('PUT', `/pedidos/${id}`, p),
  cancelarPedido: (id) => request('PATCH', `/pedidos/${id}/cancelar`),
  avaliarPedido: (id, a) => request('PATCH', `/pedidos/${id}/avaliar`, a),
  executarContrato: (id, c) => request('POST', `/pedidos/${id}/contrato`, c),
}
