import { Navigate, Route, Routes } from 'react-router-dom'
import Layout from './components/Layout.jsx'
import RotaProtegida from './components/RotaProtegida.jsx'
import Login from './pages/Login.jsx'
import Cadastro from './pages/Cadastro.jsx'
import Inicio from './pages/Inicio.jsx'
import Pedidos from './pages/Pedidos.jsx'
import PedidoForm from './pages/PedidoForm.jsx'
import PedidoDetalhe from './pages/PedidoDetalhe.jsx'
import Clientes from './pages/Clientes.jsx'
import ClienteEditar from './pages/ClienteEditar.jsx'
import Automoveis from './pages/Automoveis.jsx'

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/cadastro" element={<Cadastro />} />

      <Route element={<RotaProtegida><Layout /></RotaProtegida>}>
        <Route path="/" element={<Inicio />} />
        <Route path="/pedidos" element={<Pedidos />} />
        <Route path="/pedidos/:id" element={<PedidoDetalhe />} />
        <Route path="/pedidos/novo" element={<RotaProtegida perfil="CLIENTE"><PedidoForm /></RotaProtegida>} />
        <Route path="/pedidos/:id/editar" element={<PedidoForm />} />
        <Route path="/automoveis" element={<Automoveis />} />
        <Route path="/clientes" element={<RotaProtegida perfil="AGENTE"><Clientes /></RotaProtegida>} />
        <Route path="/clientes/:id/editar" element={<ClienteEditar />} />
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
