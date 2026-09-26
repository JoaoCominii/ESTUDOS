import { Outlet } from "react-router-dom";
import Sidebar from "./Sidebar";

export default function Layout({ usuario, onLogout }: { usuario: { nome: string; perfil: string }; onLogout: () => void }) {
  return (
    <div className="shell">
      <Sidebar usuario={usuario} onLogout={onLogout} />
      <main className="main">
        <Outlet />
      </main>
    </div>
  );
}
