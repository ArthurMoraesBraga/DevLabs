import { useState } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'
import Mensagem from '../components/Mensagem.jsx'
import AuthShell from '../components/AuthShell.jsx'

export default function Login() {
  const { usuario, login } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ login: '', senha: '' })
  const [erro, setErro] = useState(null)

  if (usuario) return <Navigate to="/" replace />

  async function entrar(e) {
    e.preventDefault()
    setErro(null)
    try {
      await login(form.login, form.senha)
      navigate('/')
    } catch (err) {
      setErro(err)
    }
  }

  return (
    <AuthShell titulo="Bem-vindo de volta" subtitulo="Entre com seu usuário e senha para continuar.">
      <form className="formulario" onSubmit={entrar}>
        <Mensagem erro={erro} />
        <label>Usuário
          <input value={form.login} onChange={(e) => setForm({ ...form, login: e.target.value })} required autoFocus />
        </label>
        <label>Senha
          <input type="password" value={form.senha} onChange={(e) => setForm({ ...form, senha: e.target.value })} required />
        </label>
        <button className="btn btn-primary">Entrar</button>
      </form>
      <p className="auth-rodape">Não tem conta? <Link to="/cadastro">Cadastre-se</Link></p>
    </AuthShell>
  )
}
