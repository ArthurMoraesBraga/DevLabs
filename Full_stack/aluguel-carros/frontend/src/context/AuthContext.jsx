import { createContext, useCallback, useContext, useEffect, useState } from 'react'
import { api, tokenStorage } from '../services/api.js'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [usuario, setUsuario] = useState(null)
  const [carregando, setCarregando] = useState(true)

  // Restaura a sessão a partir do token salvo
  useEffect(() => {
    if (!tokenStorage.get()) {
      setCarregando(false)
      return
    }
    api.me()
      .then(setUsuario)
      .catch(() => tokenStorage.clear())
      .finally(() => setCarregando(false))
  }, [])

  useEffect(() => {
    const expirou = () => setUsuario(null)
    window.addEventListener('sessao-expirada', expirou)
    return () => window.removeEventListener('sessao-expirada', expirou)
  }, [])

  const login = useCallback(async (login, senha) => {
    const { token, usuario } = await api.login(login, senha)
    tokenStorage.set(token)
    setUsuario(usuario)
    return usuario
  }, [])

  const logout = useCallback(async () => {
    try { await api.logout() } catch { /* token já inválido */ }
    tokenStorage.clear()
    setUsuario(null)
  }, [])

  const isAgente = usuario?.perfil === 'EMPRESA' || usuario?.perfil === 'BANCO'
  const isCliente = usuario?.perfil === 'CLIENTE'

  return (
    <AuthContext.Provider value={{ usuario, carregando, login, logout, isAgente, isCliente }}>
      {children}
    </AuthContext.Provider>
  )
}

// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => useContext(AuthContext)
