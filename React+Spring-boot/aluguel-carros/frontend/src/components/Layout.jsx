import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'
import Marca from './Marca.jsx'
import Avatar from './Avatar.jsx'
import ThemeToggle from './ThemeToggle.jsx'

export default function Layout() {
  const { usuario, logout, isAgente, isCliente } = useAuth()
  const navigate = useNavigate()

  async function sair() {
    await logout()
    navigate('/login')
  }

  return (
    <>
      <header className="navbar">
        <NavLink to="/" aria-label="Página inicial"><Marca /></NavLink>
        <nav className="nav-links">
          <NavLink to="/" end>Início</NavLink>
          <NavLink to="/pedidos" end>Pedidos</NavLink>
          {isCliente && <NavLink to="/pedidos/novo">Novo pedido</NavLink>}
          <NavLink to="/automoveis">Automóveis</NavLink>
          {isAgente && <NavLink to="/clientes">Clientes</NavLink>}
          {isCliente && <NavLink to={`/clientes/${usuario.id}/editar`}>Meu perfil</NavLink>}
        </nav>
        <div className="usuario">
          <ThemeToggle />
          <Avatar nome={usuario.nome} />
          <div className="usuario-info">
            <strong>{usuario.nome}</strong>
            <small>{usuario.perfil}</small>
          </div>
          <button className="btn btn-outline btn-sm" onClick={sair}>Sair</button>
        </div>
      </header>

      <main className="conteudo">
        <Outlet />
      </main>

      <footer className="footer">
        <Marca />
        <p>DriveFlow · Plataforma de Aluguel de Carros · PUC Minas · DIAW</p>
      </footer>
    </>
  )
}
