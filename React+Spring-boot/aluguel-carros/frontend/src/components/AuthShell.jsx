import Marca from './Marca.jsx'
import CarArt from './CarArt.jsx'
import ThemeToggle from './ThemeToggle.jsx'

/** Moldura das telas de login e cadastro: painel visual + formulário. */
export default function AuthShell({ titulo, subtitulo, largo = false, children }) {
  return (
    <div className="auth">
      <ThemeToggle className="tema-toggle-flutuante" />
      <aside className="auth-visual">
        <Marca />
        <div>
          <h2>Seu próximo carro, a poucos cliques.</h2>
          <p>Faça o pedido, acompanhe a análise financeira e assine o contrato sem sair de casa.</p>
          <CarArt seed="hero-auth" titulo="Carro em destaque" />
        </div>
        <ul className="vantagens">
          <li>Pedidos acompanhados em tempo real</li>
          <li>Análise financeira por empresas e bancos</li>
          <li>Contrato com crédito associado, quando houver</li>
        </ul>
      </aside>
      <main className="auth-painel">
        <div className={`auth-form ${largo ? 'largo' : ''}`}>
          <h1>{titulo}</h1>
          <p className="sub">{subtitulo}</p>
          {children}
        </div>
      </main>
    </div>
  )
}
