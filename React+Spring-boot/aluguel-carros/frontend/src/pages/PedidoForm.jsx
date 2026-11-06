import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { api } from '../services/api.js'
import Mensagem from '../components/Mensagem.jsx'
import FotoCarro from '../components/FotoCarro.jsx'
import { hojeISO } from '../utils/formatar.js'

/** Introduzir (sem id) ou modificar (com id) um pedido de aluguel. */
export default function PedidoForm() {
  const { id } = useParams()
  const edicao = Boolean(id)
  const navigate = useNavigate()
  const [automoveis, setAutomoveis] = useState([])
  const [form, setForm] = useState({ automovelId: '', dataInicio: '', dataFim: '' })
  const [erro, setErro] = useState(null)

  useEffect(() => {
    api.listarAutomoveis().then(setAutomoveis).catch(setErro)
    if (edicao) {
      api.buscarPedido(id)
        .then((p) => setForm({ automovelId: p.automovel.id, dataInicio: p.dataInicio, dataFim: p.dataFim }))
        .catch(setErro)
    }
  }, [id, edicao])

  const escolhido = automoveis.find((a) => a.id === Number(form.automovelId))

  async function salvar(e) {
    e.preventDefault()
    setErro(null)
    const dados = { ...form, automovelId: Number(form.automovelId) }
    try {
      const pedido = edicao ? await api.atualizarPedido(id, dados) : await api.criarPedido(dados)
      navigate(`/pedidos/${pedido.id}`)
    } catch (err) {
      setErro(err)
    }
  }

  return (
    <section className="estreito">
      <h1>{edicao ? `Modificar pedido #${id}` : 'Novo pedido de aluguel'}</h1>
      <Mensagem erro={erro} />
      {escolhido && (
        <div className="pre-visualizacao">
          <FotoCarro url={escolhido.imagemUrl} seed={escolhido.placa} titulo={`${escolhido.marca} ${escolhido.modelo}`} />
        </div>
      )}
      <form className="formulario cartao" onSubmit={salvar}>
        <label>Automóvel
          <select value={form.automovelId} onChange={(e) => setForm({ ...form, automovelId: e.target.value })} required>
            <option value="">Selecione...</option>
            {automoveis.map((a) => (
              <option key={a.id} value={a.id}>{a.marca} {a.modelo} {a.ano} - {a.placa}</option>
            ))}
          </select>
        </label>
        <div className="linha">
          <label>Início
            <input type="date" min={hojeISO()} value={form.dataInicio}
                   onChange={(e) => setForm({ ...form, dataInicio: e.target.value })} required />
          </label>
          <label>Fim
            <input type="date" min={form.dataInicio || hojeISO()} value={form.dataFim}
                   onChange={(e) => setForm({ ...form, dataFim: e.target.value })} required />
          </label>
        </div>
        <div className="acoes">
          <button className="btn btn-primary">{edicao ? 'Salvar alterações' : 'Enviar pedido'}</button>
          <button type="button" className="btn btn-outline" onClick={() => navigate(-1)}>Voltar</button>
        </div>
      </form>
    </section>
  )
}
