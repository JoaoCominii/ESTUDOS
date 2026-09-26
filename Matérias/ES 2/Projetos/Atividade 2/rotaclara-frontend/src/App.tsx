import { BrowserRouter, Routes, Route } from "react-router-dom";
import Layout from "./components/Layout";
import Painel from "./pages/Painel";
import Historico from "./pages/Historico";
import Roteiros from "./pages/Roteiros";
import Pontos from "./pages/Pontos";
import Registrar from "./pages/Registrar";
import Motoristas from "./pages/Motoristas";
import Parametros from "./pages/Parametros";
import { useState } from "react";
import Login from "./pages/Login";

export default function App() {
  const [usuario, setUsuario] = useState<{ nome: string; perfil: string } | null>(() => {
    const salvo = localStorage.getItem("rotaclara_usuario");
    return salvo ? JSON.parse(salvo) : null;
  });

  if (!usuario) return <Login onLogin={setUsuario} />;

  function sair() {
    localStorage.removeItem("rotaclara_token");
    localStorage.removeItem("rotaclara_usuario");
    setUsuario(null);
  }

  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout usuario={usuario} onLogout={sair} />}>
          <Route index element={<Painel />} />
          <Route path="historico" element={<Historico />} />
          <Route path="roteiros" element={<Roteiros />} />
          <Route path="pontos" element={<Pontos />} />
          <Route path="registrar" element={<Registrar />} />
          <Route path="motoristas" element={<Motoristas />} />
          <Route path="parametros" element={<Parametros />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
