/** Mostra erro da API, incluindo erros de validação campo a campo. */
export default function Mensagem({ erro, sucesso }) {
  if (sucesso) return <div className="alerta sucesso">{sucesso}</div>
  if (!erro) return null
  return (
    <div className="alerta erro">
      {erro.message}
      {erro.detalhes && (
        <ul>
          {Object.entries(erro.detalhes).map(([campo, msg]) => (
            <li key={campo}><b>{campo}</b>: {msg}</li>
          ))}
        </ul>
      )}
    </div>
  )
}
