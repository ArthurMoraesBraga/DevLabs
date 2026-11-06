import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../services/api.js'
import { useAuth } from '../context/AuthContext.jsx'
import Mensagem from '../components/Mensagem.jsx'
import FotoCarro from '../components/FotoCarro.jsx'

const STATUS = ['PENDENTE', 'APROVADO', 'REPROVADO', 'CONTRATADO', 'CANCELADO']

/** Painel com o resumo dos pedidos por status e a frota em destaque. */
export default function Inicio() {
  const { usuario, isCliente } = useAuth()
  const [pedidos, setPedidos] = useState([])
  const [frota, setFrota] = useState([])
  const [erro, setErro] = useState(null)

  useEffect(() => {
    api.listarPedidos().then(setPedidos).catch(setErro)
    api.listarAutomoveis().then(setFrota).catch(() => setFrota([]))
  }, [])

  const contar = (s) => pedidos.filter((p) => p.status === s).length

  return (
    <section>
      <div className="hero inicio">
        <div>
          <h1>Olá, {usuario.nome}</h1>
          <p>{isCliente
            ? 'Acompanhe aqui o status dos seus pedidos de aluguel.'
            : 'Pedidos aguardando análise financeira e execução de contrato.'}</p>
          <div className="acoes">
            <Link className="btn btn-primary" to="/pedidos">Ver pedidos</Link>
            {isCliente && <Link className="btn btn-outline" to="/pedidos/novo">Fazer novo pedido</Link>}
          </div>
        </div>
        {pedidos.length === 0 ? (
          <Link to={isCliente ? '/pedidos/novo' : '/automoveis'} className="hero-cta hero-cta-vazio" aria-label="Alugue seu carro">
            <span className="hero-cta-texto">
              <strong>Alugue seu carro</strong>
              <small>{isCliente ? 'Faça seu primeiro pedido agora' : 'Veja a frota disponível'}</small>
            </span>
          </Link>
        ) : (
          <FotoCarro url={frota[0]?.imagemUrl} seed={frota[0]?.placa ?? 'hero-inicio'} titulo="Carro em destaque" className="hero-carro" />
        )}
      </div>

      <Mensagem erro={erro} />

      <div className="cards">
        {STATUS.map((s) => (
          <div key={s} className={`card-status ${s.toLowerCase()}`}>
            <span>{s.charAt(0) + s.slice(1).toLowerCase()}</span>
            <strong>{contar(s)}</strong>
          </div>
        ))}
      </div>

      {frota.length > 0 && (
        <>
          <div className="secao-titulo">
            <h2>Frota em destaque</h2>
            <Link to="/automoveis">Ver todos os automóveis</Link>
          </div>
          <div className="grid">
            {frota.slice(0, 3).map((a) => (
              <article key={a.id} className="car-card">
                <FotoCarro url={a.imagemUrl} seed={a.placa} titulo={`${a.marca} ${a.modelo}`} />
                <div className="car-card-content">
                  <h3 className="car-card-title">{a.marca} {a.modelo}</h3>
                  <div className="car-card-footer">
                    <span className="text-muted">{a.ano}</span>
                    <span className="placa">{a.placa}</span>
                  </div>
                </div>
              </article>
            ))}
          </div>
        </>
      )}
    </section>
  )
}
