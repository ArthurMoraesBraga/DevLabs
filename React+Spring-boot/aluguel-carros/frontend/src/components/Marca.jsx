/** Logotipo: ícone de carro com chave sobre fundo escuro + nome. */
export function LogoMark({ className = 'logo-mark' }) {
  return (
    <svg className={className} viewBox="0 0 48 48" aria-hidden="true">
      <rect width="48" height="48" rx="13" fill="#D9F36A" />
      <path d="M9 30l3-8q1-3 5-3h14q4 0 5 3l3 8v5H9z" fill="#101820" />
      <circle cx="16" cy="35" r="4.5" fill="#101820" stroke="#D9F36A" strokeWidth="2" />
      <circle cx="32" cy="35" r="4.5" fill="#101820" stroke="#D9F36A" strokeWidth="2" />
      <path d="M15 27l2-5h14l2 5z" fill="#D9F36A" />
      <circle cx="35" cy="12" r="3.5" fill="none" stroke="#101820" strokeWidth="2.4" />
      <path d="M32 14l-6 5" stroke="#101820" strokeWidth="2.4" strokeLinecap="round" />
    </svg>
  )
}

export default function Marca() {
  return (
    <span className="logo">
      <LogoMark />
      <span style={{ color: 'inherit' }}>Aluguel<span>Car</span></span>
    </span>
  )
}
