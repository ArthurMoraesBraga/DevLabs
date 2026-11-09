import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../services/api.js'
import Mensagem from '../components/Mensagem.jsx'
import Avatar from '../components/Avatar.jsx'
import { formatarMoeda } from '../utils/formatar.js'

/** Listagem de clientes (CRUD — visão do agente). */
export default function Clientes() {
  const [clientes, setClientes] = useState([])
  const [busca, setBusca] = useState('')
  const [erro, setErro] = useState(null)

  const carregar = () => api.listarClientes().then(setClientes).catch(setErro)
  useEffect(() => { carregar() }, [])

  async function excluir(c) {
    if (!confirm(`Excluir o cliente ${c.nome}?`)) return
    try {
      await api.excluirCliente(c.id)
      carregar()
    } catch (err) {
      setErro(err)
    }
  }

  const termo = busca.toLowerCase()
  const visiveis = clientes.filter((c) => c.nome.toLowerCase().includes(termo) || c.cpf.includes(termo))

  return (
    <section>
      <div className="cabecalho"><h1>Clientes</h1></div>
      <Mensagem erro={erro} />
      <input className="busca" placeholder="Buscar por nome ou CPF" value={busca} onChange={(e) => setBusca(e.target.value)} />
      <div className="table-container">
        <table>
          <thead>
            <tr><th>Nome</th><th>CPF</th><th>RG</th><th>Profissão</th><th>Renda total</th><th>Ações</th></tr>
          </thead>
          <tbody>
            {visiveis.map((c) => (
              <tr key={c.id}>
                <td><div className="celula-pessoa"><Avatar nome={c.nome} />{c.nome}</div></td>
                <td>{c.cpf}</td>
                <td>{c.rg}</td>
                <td>{c.profissao}</td>
                <td><span className="badge badge-success">{formatarMoeda(c.rendaTotal)}</span></td>
                <td className="acoes-tabela">
                  <Link to={`/clientes/${c.id}/editar`}>Editar</Link>
                  <button className="link perigo" onClick={() => excluir(c)}>Excluir</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <p className="dica mt-2">Novos clientes se cadastram pela tela de cadastro.</p>
    </section>
  )
}
