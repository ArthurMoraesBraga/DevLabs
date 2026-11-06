import { useEffect, useState } from 'react'
import CarArt from './CarArt.jsx'

/**
 * Mostra a foto do carro quando há URL válida; se não houver link
 * (ou se a imagem falhar ao carregar), cai na ilustração SVG gerada.
 */
export default function FotoCarro({ url, seed = 'carro', titulo, className = '' }) {
  const [falhou, setFalhou] = useState(false)

  // Ao trocar a URL, tenta carregar de novo.
  useEffect(() => { setFalhou(false) }, [url])

  const limpa = typeof url === 'string' ? url.trim() : ''

  if (limpa && !falhou) {
    return (
      <img
        className={`car-foto ${className}`}
        src={limpa}
        alt={titulo || 'Foto do automóvel'}
        loading="lazy"
        referrerPolicy="no-referrer"
        onError={() => setFalhou(true)}
      />
    )
  }
  return <CarArt seed={seed} titulo={titulo} className={className} />
}
