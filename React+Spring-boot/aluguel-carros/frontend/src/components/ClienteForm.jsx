import { useState } from 'react'

const VAZIO = {
  login: '', senha: '', nome: '', endereco: '', rg: '', cpf: '', profissao: '', rendimentos: [],
}
const MAX_RENDIMENTOS = 3

/**
 * Formulário de cliente usado no cadastro e na edição.
 * Na edição a senha é opcional (em branco mantém a atual).
 */
export default function ClienteForm({ inicial, edicao = false, onSubmit, textoBotao = 'Salvar' }) {
  const [form, setForm] = useState({ ...VAZIO, ...inicial, senha: '' })
  const [enviando, setEnviando] = useState(false)

  const alterar = (campo) => (e) => setForm({ ...form, [campo]: e.target.value })

  const alterarRendimento = (i, campo, valor) => {
    const rendimentos = form.rendimentos.map((r, idx) => (idx === i ? { ...r, [campo]: valor } : r))
    setForm({ ...form, rendimentos })
  }
  const adicionarRendimento = () =>
    setForm({ ...form, rendimentos: [...form.rendimentos, { entidadeEmpregadora: '', valor: '' }] })
  const removerRendimento = (i) =>
    setForm({ ...form, rendimentos: form.rendimentos.filter((_, idx) => idx !== i) })

  async function enviar(e) {
    e.preventDefault()
    setEnviando(true)
    try {
      await onSubmit({
        login: form.login,
        senha: form.senha,
        nome: form.nome,
        endereco: form.endereco,
        rg: form.rg,
        cpf: form.cpf.replace(/\D/g, ''),
        profissao: form.profissao,
        rendimentos: form.rendimentos.map((r) => ({
          entidadeEmpregadora: r.entidadeEmpregadora,
          valor: Number(r.valor),
        })),
      })
    } finally {
      setEnviando(false)
    }
  }

  return (
    <form onSubmit={enviar} className="formulario">
      <fieldset>
        <legend>Acesso</legend>
        <label>Usuário<input value={form.login} onChange={alterar('login')} required minLength={3} /></label>
        <label>
          Senha {edicao && <small>(deixe em branco para manter)</small>}
          <input type="password" value={form.senha} onChange={alterar('senha')}
                 required={!edicao} minLength={6} />
        </label>
      </fieldset>

      <fieldset>
        <legend>Identificação</legend>
        <label>Nome<input value={form.nome} onChange={alterar('nome')} required /></label>
        <label>Endereço<input value={form.endereco} onChange={alterar('endereco')} required /></label>
        <div className="linha">
          <label>RG<input value={form.rg} onChange={alterar('rg')} required /></label>
          <label>CPF<input value={form.cpf} onChange={alterar('cpf')} required placeholder="Somente números" /></label>
        </div>
        <label>Profissão<input value={form.profissao} onChange={alterar('profissao')} required /></label>
      </fieldset>

      <fieldset>
        <legend>Rendimentos ({form.rendimentos.length}/{MAX_RENDIMENTOS})</legend>
        {form.rendimentos.map((r, i) => (
          <div className="linha" key={i}>
            <label>Entidade empregadora
              <input value={r.entidadeEmpregadora}
                     onChange={(e) => alterarRendimento(i, 'entidadeEmpregadora', e.target.value)} required />
            </label>
            <label>Valor (R$)
              <input type="number" min="0.01" step="0.01" value={r.valor}
                     onChange={(e) => alterarRendimento(i, 'valor', e.target.value)} required />
            </label>
            <button type="button" className="btn btn-danger btn-sm" onClick={() => removerRendimento(i)}>Remover</button>
          </div>
        ))}
        {form.rendimentos.length < MAX_RENDIMENTOS && (
          <button type="button" className="btn btn-outline" onClick={adicionarRendimento}>+ Adicionar rendimento</button>
        )}
      </fieldset>

      <button className="btn btn-primary" disabled={enviando}>{enviando ? 'Salvando...' : textoBotao}</button>
    </form>
  )
}
