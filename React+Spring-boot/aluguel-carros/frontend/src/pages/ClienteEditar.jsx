import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { api } from '../services/api.js'
import { useAuth } from '../context/AuthContext.jsx'
import ClienteForm from '../components/ClienteForm.jsx'
import Mensagem from '../components/Mensagem.jsx'

/** Edição de cliente: o próprio cliente ("Meu perfil") ou um agente. */
export default function ClienteEditar() {
  const { id } = useParams()
  const { usuario, isCliente, logout } = useAuth()
  const navigate = useNavigate()
  const [cliente, setCliente] = useState(null)
  const [erro, setErro] = useState(null)
  const [sucesso, setSucesso] = useState(null)

  useEffect(() => {
    api.buscarCliente(id).then(setCliente).catch(setErro)
  }, [id])

  async function salvar(dados) {
    setErro(null)
    setSucesso(null)
    try {
      setCliente(await api.atualizarCliente(id, dados))
      setSucesso('Dados atualizados.')
    } catch (err) {
      setErro(err)
    }
  }

  async function excluirConta() {
    if (!confirm('Tem certeza? Esta ação não pode ser desfeita.')) return
    try {
      await api.excluirCliente(id)
      if (isCliente && usuario.id === Number(id)) {
        await logout()
        navigate('/login')
      } else {
        navigate('/clientes')
      }
    } catch (err) {
      setErro(err)
    }
  }

  return (
    <section className="estreito">
      <div className="cabecalho"><h1>{isCliente ? 'Meu perfil' : 'Editar cliente'}</h1></div>
      <Mensagem erro={erro} sucesso={sucesso} />
      {cliente && (
        <div className="cartao">
          <ClienteForm key={cliente.id} inicial={cliente} edicao onSubmit={salvar} textoBotao="Salvar alterações" />
          <hr />
          <button className="btn btn-danger" onClick={excluirConta}>Excluir {isCliente ? 'minha conta' : 'cliente'}</button>
        </div>
      )}
    </section>
  )
}
