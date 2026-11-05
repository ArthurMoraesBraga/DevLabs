const TONS = ['#D9F36A', '#4CC9F0', '#FF8FAB', '#FFC857', '#B8A9FF', '#7EE0D3']

/** Círculo colorido com as iniciais do nome. */
export default function Avatar({ nome = '' }) {
  const iniciais = nome.trim().split(/\s+/).slice(0, 2).map((p) => p[0]?.toUpperCase()).join('') || '?'
  const tom = TONS[[...nome].reduce((s, c) => s + c.charCodeAt(0), 0) % TONS.length]
  return <span className="avatar" style={{ '--avatar-bg': tom }} aria-hidden="true">{iniciais}</span>
}
