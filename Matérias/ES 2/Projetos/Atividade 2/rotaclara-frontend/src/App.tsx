import { BrowserRouter, Routes, Route } from "react-router-dom";
import Layout from "./components/Layout";
import Painel from "./pages/Painel";
import Historico from "./pages/Historico";
import Roteiros from "./pages/Roteiros";
import Pontos from "./pages/Pontos";
import Registrar from "./pages/Registrar";
import Motoristas from "./pages/Motoristas";
import Parametros from "./pages/Parametros";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
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
