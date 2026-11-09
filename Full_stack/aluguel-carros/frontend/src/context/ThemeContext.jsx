import { createContext, useCallback, useContext, useEffect, useState } from 'react'

const ThemeContext = createContext(null)
const STORAGE_KEY = 'aluguel.tema'

/** Lê o tema salvo; se não houver, respeita a preferência do sistema. */
function temaInicial() {
  const salvo = localStorage.getItem(STORAGE_KEY)
  if (salvo === 'dark' || salvo === 'light') return salvo
  const prefereEscuro = window.matchMedia?.('(prefers-color-scheme: dark)').matches
  return prefereEscuro ? 'dark' : 'light'
}

export function ThemeProvider({ children }) {
  const [tema, setTema] = useState(temaInicial)

  // Aplica o tema no <html> e persiste a escolha.
  useEffect(() => {
    document.documentElement.setAttribute('data-theme', tema)
    localStorage.setItem(STORAGE_KEY, tema)
  }, [tema])

  const alternarTema = useCallback(() => {
    setTema((t) => (t === 'dark' ? 'light' : 'dark'))
  }, [])

  const escuro = tema === 'dark'

  return (
    <ThemeContext.Provider value={{ tema, escuro, alternarTema }}>
      {children}
    </ThemeContext.Provider>
  )
}

// eslint-disable-next-line react-refresh/only-export-components
export const useTheme = () => useContext(ThemeContext)
