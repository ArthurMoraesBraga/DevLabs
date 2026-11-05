import { useEffect, useState } from 'react'
import { api } from '../services/api.js'
import { useAuth } from '../context/AuthContext.jsx'
import Mensagem from '../components/Mensagem.jsx'
import CarArt from '../components/CarArt.jsx'

const VAZIO = { matricula: '', ano: '', marca: '', modelo: '', placa: '' }

/** Frota: todos consultam; agentes cadastram, editam e excluem. */
export default function Automoveis() {
  const { isAgente } = useAuth()
  const [automoveis, setAutomoveis] = useState([])
  const [form, setForm] = useState(VAZIO)
  const [editandoId, setEditandoId] = useState(null)
  const [erro, setErro] = useState(null)
  const [busca, setBusca] = useState('')

  const carregar = () => api.listarAutomoveis().then(setAutomoveis).catch(setErro)
  useEffect(() => { carregar() }, [])

  const alterar = (campo) => (e) => setForm({ ...form, [campo]: e.target.value })

  async function salvar(e) {
    e.preventDefault()
    setErro(null)
    const atual = automoveis.find((a) => a.id === editandoId)
    const dados = {
      ...form,
      ano: Number(form.ano),
      // Propriedade é definida pelo contrato; na edição mantém a atual
      tipoProprietario: atual?.tipoProprietario ?? null,
      proprietarioId: atual?.proprietarioId ?? null,
    }
    try {
      if (editandoId) await api.atualizarAutomovel(editandoId, dados)
      else await api.criarAutomovel(dados)
      setForm(VAZIO)
      setEditandoId(null)
      carregar()
    } catch (err) {
      setErro(err)
    }
  }

  function editar(a) {
    setEditandoId(a.id)
    setForm({ matricula: a.matricula, ano: a.ano, marca: a.marca, modelo: a.modelo, placa: a.placa })
  }

  async function excluir(a) {
    if (!confirm(`Excluir ${a.marca} ${a.modelo}?`)) return
    try {
      await api.excluirAutomovel(a.id)
      carregar()
    } catch (err) {
      setErro(err)
    }
  }

  const termo = busca.toLowerCase()
  const visiveis = automoveis.filter((a) =>
    `${a.marca} ${a.modelo} ${a.placa}`.toLowerCase().includes(termo))

  return (
    <section>
      <div className="cabecalho"><h1>Automóveis</h1></div>
      <Mensagem erro={erro} />

      {isAgente && (
        <form className="cartao formulario" onSubmit={salvar}>
          <h3>{editandoId ? 'Editar automóvel' : 'Cadastrar automóvel'}</h3>
          <div className="linha">
            <label>Matrícula<input value={form.matricula} onChange={alterar('matricula')} required /></label>
            <label>Placa<input value={form.placa} onChange={alterar('placa')} required /></label>
            <label>Ano<input type="number" min="1950" max="2100" value={form.ano} onChange={alterar('ano')} required /></label>
          </div>
          <div className="linha">
            <label>Marca<input value={form.marca} onChange={alterar('marca')} required /></label>
            <label>Modelo<input value={form.modelo} onChange={alterar('modelo')} required /></label>
          </div>
          <div className="acoes">
            <button className="btn btn-primary">{editandoId ? 'Salvar' : 'Cadastrar'}</button>
            {editandoId && <button type="button" className="btn btn-outline"
                                   onClick={() => { setEditandoId(null); setForm(VAZIO) }}>Cancelar</button>}
          </div>
        </form>
      )}

      <input className="busca" placeholder="Buscar por marca, modelo ou placa" value={busca}
             onChange={(e) => setBusca(e.target.value)} />

      {visiveis.length === 0 ? (
        <div className="vazio"><h3>Nenhum automóvel encontrado</h3><p>Ajuste a busca ou cadastre um novo veículo.</p></div>
      ) : (
        <div className="grid">
          {visiveis.map((a) => (
            <article key={a.id} className="car-card">
              <CarArt seed={a.placa} titulo={`${a.marca} ${a.modelo}`} />
              <div className="car-card-content">
                <h3 className="car-card-title">{a.marca} {a.modelo}</h3>
                <div className="car-card-info">
                  <span><i />Ano {a.ano}</span>
                  <span><i />Matrícula {a.matricula}</span>
                </div>
                <div className="car-card-footer">
                  <span className="placa">{a.placa}</span>
                  {a.tipoProprietario
                    ? <span className="badge badge-highlight">{a.tipoProprietario}</span>
                    : <span className="badge badge-success">Locadora</span>}
                </div>
                {isAgente && (
                  <div className="acoes">
                    <button className="btn btn-dark btn-sm" onClick={() => editar(a)}>Editar</button>
                    <button className="btn btn-outline btn-sm" onClick={() => excluir(a)}>Excluir</button>
                  </div>
                )}
              </div>
            </article>
          ))}
        </div>
      )}
    </section>
  )
}
