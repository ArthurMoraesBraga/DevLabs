import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { api } from '../services/api.js'
import ClienteForm from '../components/ClienteForm.jsx'
import Mensagem from '../components/Mensagem.jsx'
import AuthShell from '../components/AuthShell.jsx'

const AGENTE_VAZIO = { login: '', senha: '', nome: '', endereco: '', cnpj: '', tipo: 'EMPRESA' }

/** Cadastro prévio obrigatório: cliente (pessoa física) ou agente (empresa/banco). */
export default function Cadastro() {
  const navigate = useNavigate()
  const [tipo, setTipo] = useState('CLIENTE')
  const [erro, setErro] = useState(null)
  const [agente, setAgente] = useState(AGENTE_VAZIO)

  async function concluir(promessa) {
    setErro(null)
    try {
      await promessa
      alert('Cadastro realizado! Faça login para continuar.')
      navigate('/login')
    } catch (err) {
      setErro(err)
    }
  }

  const alterarAgente = (campo) => (e) => setAgente({ ...agente, [campo]: e.target.value })

  return (
    <AuthShell largo titulo="Criar conta" subtitulo="O cadastro é necessário para fazer pedidos e acompanhar contratos.">
      <div>
        <div className="abas">
          <button type="button" className={tipo === 'CLIENTE' ? 'ativa' : ''} onClick={() => setTipo('CLIENTE')}>Sou cliente</button>
          <button type="button" className={tipo === 'AGENTE' ? 'ativa' : ''} onClick={() => setTipo('AGENTE')}>Sou agente</button>
        </div>
        <Mensagem erro={erro} />

        {tipo === 'CLIENTE' ? (
          <ClienteForm textoBotao="Cadastrar" onSubmit={(dados) => concluir(api.criarCliente(dados))} />
        ) : (
          <form className="formulario" onSubmit={(e) => {
            e.preventDefault()
            concluir(api.criarAgente({ ...agente, cnpj: agente.cnpj.replace(/\D/g, '') }))
          }}>
            <label>Tipo
              <select value={agente.tipo} onChange={alterarAgente('tipo')}>
                <option value="EMPRESA">Empresa</option>
                <option value="BANCO">Banco</option>
              </select>
            </label>
            <label>Usuário<input value={agente.login} onChange={alterarAgente('login')} required minLength={3} /></label>
            <label>Senha<input type="password" value={agente.senha} onChange={alterarAgente('senha')} required minLength={6} /></label>
            <label>Razão social<input value={agente.nome} onChange={alterarAgente('nome')} required /></label>
            <label>Endereço<input value={agente.endereco} onChange={alterarAgente('endereco')} required /></label>
            <label>CNPJ<input value={agente.cnpj} onChange={alterarAgente('cnpj')} required placeholder="Somente números" /></label>
            <button className="btn btn-primary">Cadastrar</button>
          </form>
        )}
        <p className="auth-rodape">Já tem conta? <Link to="/login">Entrar</Link></p>
      </div>
    </AuthShell>
  )
}
