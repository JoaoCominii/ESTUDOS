import { NavLink } from "react-router-dom";
import {
  IconPainel,
  IconHistorico,
  IconRoteiros,
  IconPontos,
  IconRegistrar,
  IconMotoristas,
  IconParametros,
} from "./icons";

function navClass({ isActive }: { isActive: boolean }) {
  return isActive ? "navitem active" : "navitem";
}

export default function Sidebar({ usuario, onLogout }: { usuario: { nome: string; perfil: string }; onLogout: () => void }) {
  const perfil = usuario.perfil.toLowerCase();
  return (
    <aside className="sidebar">
      <div className="brand">
        RotaClara<span className="dot">•</span>
      </div>
      <div className="brand-sub">Monitoramento de tempo parado</div>

      <nav className="mainnav">
        <div className="navgroup-label">Visão geral</div>
        <NavLink to="/" end className={navClass}>
          <IconPainel /> Painel
        </NavLink>
        <NavLink to="/historico" className={navClass}>
          <IconHistorico /> Histórico
        </NavLink>

        <div className="navgroup-label">Operação</div>
        <NavLink to="/roteiros" className={navClass}>
          <IconRoteiros /> Roteiros
        </NavLink>
        <NavLink to="/pontos" className={navClass}>
          <IconPontos /> Pontos
        </NavLink>
        <NavLink to="/registrar" className={navClass}>
          <IconRegistrar /> Registrar ponto
        </NavLink>

        <div className="navgroup-label">Cadastros</div>
        <NavLink to="/motoristas" className={navClass}>
          <IconMotoristas /> Motoristas
        </NavLink>
        <NavLink to="/parametros" className={navClass}>
          <IconParametros /> Parâmetros
        </NavLink>
      </nav>

      <div className="sidebar-foot">
        <div className="avatar">{usuario.nome.slice(0, 2).toUpperCase()}</div>
        <div>
          <div className="who-name">{usuario.nome}</div>
          <div className="who-role">{perfil}</div>
        </div>
        <button className="btn ghost" onClick={onLogout} title="Sair da conta" style={{ marginLeft: "auto", padding: "4px 8px" }}>
          Sair
        </button>
      </div>
    </aside>
  );
}
