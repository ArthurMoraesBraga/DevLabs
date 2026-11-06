import { useTheme } from '../context/ThemeContext.jsx'

/** Botão que alterna entre modo claro e escuro. */
export default function ThemeToggle({ className = '' }) {
  const { escuro, alternarTema } = useTheme()

  return (
    <button
      type="button"
      className={`tema-toggle ${className}`}
      onClick={alternarTema}
      title={escuro ? 'Mudar para o modo claro' : 'Mudar para o modo escuro'}
      aria-label={escuro ? 'Ativar modo claro' : 'Ativar modo escuro'}
      aria-pressed={escuro}
    >
      {escuro ? (
        // Sol
        <svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round">
          <circle cx="12" cy="12" r="4" />
          <path d="M12 2v2M12 20v2M2 12h2M20 12h2M5 5l1.5 1.5M17.5 17.5L19 19M19 5l-1.5 1.5M6.5 17.5L5 19" />
        </svg>
      ) : (
        // Lua
        <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
          <path d="M21 12.8A9 9 0 1 1 11.2 3a7 7 0 0 0 9.8 9.8z" />
        </svg>
      )}
    </button>
  )
}
