import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { api } from '../services/api.js'
import { useAuth } from '../context/AuthContext.jsx'
import StatusBadge from '../components/StatusBadge.jsx'
import Mensagem from '../components/Mensagem.jsx'
import CarArt from '../components/CarArt.jsx'
import { formatarData, formatarMoeda } from '../utils/formatar.js'

/** Consulta do pedido + ações do agente (avaliar e executar contrato). */
export default function PedidoDetalhe() {
  const { id } = useParams()
  const { isAgente, isCliente } = useAuth()
  const [pedido, setPedido] = useState(null)
  const [erro, setErro] = useState(null)
  const [sucesso, setSucesso] = useState(null)

  useEffect(() => {
    api.buscarPedido(id).then(setPedido).catch(setErro)
  }, [id])

  async function executar(acao, mensagem) {
    setErro(null)
    setSucesso(null)
    try {
      setPedido(await acao())
      setSucesso(mensagem)
    } catch (err) {
      setErro(err)
    }
  }

  if (!pedido) return <Mensagem erro={erro} />

  return (
    <section className="estreito">
      <div className="cabecalho">
        <h1>Pedido #{pedido.id}</h1>
        <StatusBadge status={pedido.status} />
      </div>
      <Mensagem erro={erro} sucesso={sucesso} />

      <div className="banner-carro">
        <CarArt seed={pedido.automovel.placa} titulo={`${pedido.automovel.marca} ${pedido.automovel.modelo}`} />
      </div>

      <div className="cartao">
        <h3>Dados do pedido</h3>
        <p><b>Automóvel:</b> {pedido.automovel.marca} {pedido.automovel.modelo} {pedido.automovel.ano} — placa {pedido.automovel.placa}</p>
        <p><b>Período:</b> {formatarData(pedido.dataInicio)} a {formatarData(pedido.dataFim)}</p>
        <p><b>Criado em:</b> {formatarData(pedido.dataCriacao)}</p>
        {pedido.parecer && <p><b>Parecer de {pedido.agenteAvaliador?.nome}:</b> {pedido.parecer}</p>}
      </div>

      {isAgente && (
        <div className="cartao analise">
          <h3>Análise financeira do cliente</h3>
          <p><b>Nome:</b> {pedido.cliente.nome} — CPF {pedido.cliente.cpf}</p>
          <p><b>Profissão:</b> {pedido.cliente.profissao}</p>
          <p><b>Renda total declarada:</b> {formatarMoeda(pedido.cliente.rendaTotal)}</p>
        </div>
      )}

      {pedido.contrato && (
        <div className="cartao contrato">
          <h3>Contrato</h3>
          <p><b>Assinado em:</b> {formatarData(pedido.contrato.dataAssinatura)}</p>
          <p><b>Automóvel registrado como propriedade de:</b> {pedido.contrato.tipoProprietario}</p>
          {pedido.contrato.credito ? (
            <p><b>Crédito:</b> {formatarMoeda(pedido.contrato.credito.valor)} concedido por {pedido.contrato.credito.bancoNome},
              {' '}{pedido.contrato.credito.prazoMeses}x a {pedido.contrato.credito.taxaJurosMensal}% a.m.</p>
          ) : <p>Sem contrato de crédito.</p>}
        </div>
      )}

      {isAgente && pedido.status === 'PENDENTE' && (
        <FormAvaliacao onEnviar={(dados) => executar(() => api.avaliarPedido(id, dados), 'Avaliação registrada.')} />
      )}
      {isAgente && pedido.status === 'APROVADO' && (
        <FormContrato onEnviar={(dados) => executar(() => api.executarContrato(id, dados), 'Contrato executado.')} />
      )}

      <div className="acoes">
        <Link className="btn btn-outline" to="/pedidos">Voltar</Link>
        {isCliente && pedido.status === 'PENDENTE' && <Link className="btn btn-primary" to={`/pedidos/${id}/editar`}>Modificar</Link>}
      </div>
    </section>
  )
}

function FormAvaliacao({ onEnviar }) {
  const [parecer, setParecer] = useState('')
  const enviar = (aprovado) => (e) => {
    e.preventDefault()
    if (!parecer.trim()) return alert('Escreva o parecer antes de avaliar.')
    onEnviar({ aprovado, parecer })
  }
  return (
    <form className="cartao formulario parecer">
      <h3>Avaliar pedido</h3>
      <label>Parecer financeiro
        <textarea rows={3} value={parecer} onChange={(e) => setParecer(e.target.value)} />
      </label>
      <div className="acoes">
        <button className="btn btn-success" onClick={enviar(true)}>Aprovar</button>
        <button className="btn btn-danger" onClick={enviar(false)}>Reprovar</button>
      </div>
    </form>
  )
}

function FormContrato({ onEnviar }) {
  const [tipoProprietario, setTipoProprietario] = useState('CLIENTE')
  const [comCredito, setComCredito] = useState(false)
  const [bancos, setBancos] = useState([])
  const [credito, setCredito] = useState({ bancoId: '', valor: '', taxaJurosMensal: '', prazoMeses: '' })

  useEffect(() => { api.listarBancos().then(setBancos).catch(() => setBancos([])) }, [])

  const alterar = (campo) => (e) => setCredito({ ...credito, [campo]: e.target.value })

  function enviar(e) {
    e.preventDefault()
    onEnviar({
      tipoProprietario,
      credito: comCredito ? {
        bancoId: Number(credito.bancoId),
        valor: Number(credito.valor),
        taxaJurosMensal: Number(credito.taxaJurosMensal),
        prazoMeses: Number(credito.prazoMeses),
      } : null,
    })
  }

  return (
    <form className="cartao formulario" onSubmit={enviar}>
      <h3>Executar contrato</h3>
      <label>Automóvel será registrado como propriedade de
        <select value={tipoProprietario} onChange={(e) => setTipoProprietario(e.target.value)}>
          <option value="CLIENTE">Cliente</option>
          <option value="EMPRESA">Empresa</option>
          <option value="BANCO">Banco</option>
        </select>
      </label>
      <label className="checkbox">
        <input type="checkbox" checked={comCredito} onChange={(e) => setComCredito(e.target.checked)} />
        Associar contrato de crédito
      </label>
      {comCredito && (
        <>
          <label>Banco concedente
            <select value={credito.bancoId} onChange={alterar('bancoId')} required>
              <option value="">Selecione...</option>
              {bancos.map((b) => <option key={b.id} value={b.id}>{b.nome}</option>)}
            </select>
          </label>
          <div className="linha">
            <label>Valor (R$)<input type="number" min="0.01" step="0.01" value={credito.valor} onChange={alterar('valor')} required /></label>
            <label>Juros (% a.m.)<input type="number" min="0" step="0.01" value={credito.taxaJurosMensal} onChange={alterar('taxaJurosMensal')} required /></label>
            <label>Prazo (meses)<input type="number" min="1" value={credito.prazoMeses} onChange={alterar('prazoMeses')} required /></label>
          </div>
        </>
      )}
      <button className="btn btn-primary">Executar contrato</button>
    </form>
  )
}
