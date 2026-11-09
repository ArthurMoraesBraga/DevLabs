import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../services/api.js'
import { useAuth } from '../context/AuthContext.jsx'
import StatusBadge from '../components/StatusBadge.jsx'
import Mensagem from '../components/Mensagem.jsx'
import FotoCarro from '../components/FotoCarro.jsx'
import { formatarData } from '../utils/formatar.js'

export default function Pedidos() {
  const { isCliente, isAgente } = useAuth()
  const [pedidos, setPedidos] = useState([])
  const [aba, setAba] = useState('ATIVOS')
  const [erro, setErro] = useState(null)

  const carregar = () => api.listarPedidos().then(setPedidos).catch(setErro)
  useEffect(() => { carregar() }, [])

  async function cancelar(id) {
    if (!confirm('Cancelar este pedido?')) return
    try {
      await api.cancelarPedido(id)
      // Ao cancelar, o pedido sai da aba "Ativos" e passa para "Cancelados".
      setAba('CANCELADOS')
      carregar()
    } catch (err) {
      setErro(err)
    }
  }

  const ehCancelado = (p) => p.status === 'CANCELADO'
  const cancelados = pedidos.filter(ehCancelado)
  const ativos = pedidos.filter((p) => !ehCancelado(p))

  const visiveis = aba === 'CANCELADOS' ? cancelados
    : aba === 'TODOS' ? pedidos
    : ativos

  return (
    <section>
      <div className="cabecalho">
        <h1>{isCliente ? 'Meus pedidos' : 'Pedidos de aluguel'}</h1>
        {isCliente && <Link className="btn btn-primary" to="/pedidos/novo">+ Novo pedido</Link>}
      </div>
      <Mensagem erro={erro} />

      <div className="abas abas-pedidos">
        <button type="button" className={aba === 'ATIVOS' ? 'ativa' : ''} onClick={() => setAba('ATIVOS')}>
          Ativos{ativos.length ? ` (${ativos.length})` : ''}
        </button>
        <button type="button" className={aba === 'CANCELADOS' ? 'ativa' : ''} onClick={() => setAba('CANCELADOS')}>
          Cancelados{cancelados.length ? ` (${cancelados.length})` : ''}
        </button>
        <button type="button" className={aba === 'TODOS' ? 'ativa' : ''} onClick={() => setAba('TODOS')}>
          Todos{pedidos.length ? ` (${pedidos.length})` : ''}
        </button>
      </div>

      {visiveis.length === 0 ? (
        <div className="vazio">
          <h3>Nenhum pedido {aba === 'CANCELADOS' ? 'cancelado' : aba === 'ATIVOS' ? 'ativo' : 'encontrado'}</h3>
          <p>{aba === 'CANCELADOS'
            ? 'Pedidos cancelados aparecem aqui.'
            : isCliente ? 'Faça seu primeiro pedido de aluguel.' : 'Quando houver pedidos, eles aparecem aqui.'}</p>
        </div>
      ) : (
        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>#</th>
                {isAgente && <th>Cliente</th>}
                <th>Automóvel</th>
                <th>Período</th>
                <th>Status</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {visiveis.map((p) => (
                <tr key={p.id}>
                  <td>{p.id}</td>
                  {isAgente && <td>{p.cliente.nome}</td>}
                  <td>
                    <div className="celula-carro">
                      <FotoCarro url={p.automovel.imagemUrl} seed={p.automovel.placa} titulo={`${p.automovel.marca} ${p.automovel.modelo}`} />
                      <div>{p.automovel.marca} {p.automovel.modelo}<br /><small>{p.automovel.placa}</small></div>
                    </div>
                  </td>
                  <td>{formatarData(p.dataInicio)} a {formatarData(p.dataFim)}</td>
                  <td><StatusBadge status={p.status} /></td>
                  <td className="acoes-tabela">
                    <Link to={`/pedidos/${p.id}`}>Detalhes</Link>
                    {((isCliente && p.status === 'PENDENTE') ||
                      (isAgente && ['PENDENTE', 'APROVADO'].includes(p.status))) && (
                      <Link to={`/pedidos/${p.id}/editar`}>Modificar</Link>
                    )}
                    {isCliente && ['PENDENTE', 'APROVADO'].includes(p.status) && (
                      <button className="link perigo" onClick={() => cancelar(p.id)}>Cancelar</button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  )
}
