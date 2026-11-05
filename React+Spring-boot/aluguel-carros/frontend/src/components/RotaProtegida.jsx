import { Navigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'

/** Bloqueia rotas para quem não está logado (e opcionalmente por perfil). */
export default function RotaProtegida({ children, perfil }) {
  const { usuario, carregando, isAgente } = useAuth()

  if (carregando) return <p className="centro">Carregando...</p>
  if (!usuario) return <Navigate to="/login" replace />

  if (perfil === 'AGENTE' && !isAgente) return <Navigate to="/" replace />
  if (perfil === 'CLIENTE' && usuario.perfil !== 'CLIENTE') return <Navigate to="/" replace />

  return children
}
