import { useId } from 'react'

/** Paleta vibrante: cada carro recebe uma cor de forma determinística. */
const CORES = ['#D9F36A', '#4CC9F0', '#FF5C8A', '#FFB020', '#7B61FF', '#1FB5A6', '#FF6B6B', '#FFD23F']

function hash(texto) {
  let h = 2166136261
  for (const c of String(texto)) h = Math.imul(h ^ c.charCodeAt(0), 16777619)
  h ^= h >>> 15
  h = Math.imul(h, 2246822507)
  h ^= h >>> 13
  return h >>> 0
}

/** Silhuetas: sedã, hatch e SUV. */
const CARROCERIAS = [
  {
    corpo: 'M34 158 Q34 140 52 134 L66 116 Q74 110 92 108 L136 104 Q156 80 196 78 L252 78 Q288 80 306 108 L338 114 Q362 118 366 138 L370 160 Q370 168 360 168 L44 168 Q34 168 34 158 Z',
    janelas: ['M148 104 Q162 90 196 88 L226 88 L226 104 Z', 'M236 88 L252 88 Q274 90 290 104 L236 104 Z'],
  },
  {
    corpo: 'M34 158 Q34 142 50 136 L62 118 Q70 112 88 110 L122 106 Q142 80 182 78 L282 78 Q312 80 324 112 L348 118 Q364 124 366 142 L368 160 Q368 168 358 168 L44 168 Q34 168 34 158 Z',
    janelas: ['M136 106 Q150 90 184 88 L214 88 L214 106 Z', 'M224 88 L278 88 Q298 92 310 106 L224 106 Z'],
  },
  {
    corpo: 'M34 158 Q34 140 50 134 L60 112 Q66 106 82 104 L112 100 L128 74 Q134 68 148 68 L288 68 Q304 68 312 82 L326 108 L346 112 Q364 118 366 140 L368 160 Q368 168 358 168 L44 168 Q34 168 34 158 Z',
    janelas: ['M138 100 L152 80 L206 80 L206 100 Z', 'M216 80 L286 80 Q294 80 300 90 L314 100 L216 100 Z'],
  },
]

/**
 * Ilustração SVG de um automóvel (substitui fotos).
 * A cor e a carroceria mudam conforme a "semente" (placa/modelo).
 */
export default function CarArt({ seed = 'carro', className = '', titulo }) {
  const uid = useId().replace(/:/g, '')
  const h = hash(seed)
  const cor = CORES[(h >>> 4) % CORES.length]
  const car = CARROCERIAS[(h >>> 12) % CARROCERIAS.length]
  const fundo = ['#101820', '#1D2935']

  return (
    <svg className={`car-art ${className}`} viewBox="0 0 400 240" role="img" aria-label={titulo ?? 'Ilustração de automóvel'}
         preserveAspectRatio="xMidYMid slice">
      <defs>
        <linearGradient id={`bg${uid}`} x1="0" y1="0" x2="1" y2="1">
          <stop offset="0" stopColor={fundo[0]} />
          <stop offset="1" stopColor={fundo[1]} />
        </linearGradient>
        <radialGradient id={`glow${uid}`} cx="0.5" cy="0.55" r="0.5">
          <stop offset="0" stopColor={cor} stopOpacity="0.55" />
          <stop offset="1" stopColor={cor} stopOpacity="0" />
        </radialGradient>
        <linearGradient id={`body${uid}`} x1="0" y1="0" x2="0" y2="1">
          <stop offset="0" stopColor={cor} />
          <stop offset="1" stopColor={cor} stopOpacity="0.78" />
        </linearGradient>
      </defs>

      <rect width="400" height="240" fill={`url(#bg${uid})`} />
      <circle cx="200" cy="130" r="150" fill={`url(#glow${uid})`} />
      <circle cx="338" cy="44" r="18" fill={cor} opacity="0.9" />
      <rect x="0" y="194" width="400" height="46" fill="#0B1117" />
      <path d="M0 216 H400" stroke="#2A3745" strokeWidth="3" strokeDasharray="26 18" />

      <ellipse cx="200" cy="196" rx="170" ry="8" fill="#000" opacity="0.4" />

      {/* carroceria */}
      <path d={car.corpo} fill={`url(#body${uid})`} />
      <path d="M44 150 H362" stroke="#000" strokeOpacity="0.18" strokeWidth="2" />
      {car.janelas.map((d, i) => <path key={i} d={d} fill="#BFE6F5" opacity="0.92" />)}
      <rect x="296" y="132" width="12" height="4" rx="2" fill="#101820" opacity="0.4" />

      {/* faróis */}
      <rect x="352" y="132" width="14" height="9" rx="4" fill="#FFF7C2" />
      <rect x="35" y="136" width="10" height="9" rx="4" fill="#FF4D4D" />

      {/* rodas */}
      {[110, 292].map((x) => (
        <g key={x}>
          <circle cx={x} cy="168" r="30" fill="#0B1117" />
          <circle cx={x} cy="170" r="24" fill="#1D2935" stroke="#2F3C4B" strokeWidth="3" />
          <circle cx={x} cy="170" r="10" fill={cor} />
          <circle cx={x} cy="170" r="3.5" fill="#101820" />
        </g>
      ))}
    </svg>
  )
}
