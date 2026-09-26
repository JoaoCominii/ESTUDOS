import { FormEvent, useState } from "react";
import { api } from "../api/client";

export default function Login({ onLogin }: { onLogin: (usuario: { nome: string; perfil: string }) => void }) {
  const [login, setLogin] = useState("administrador");
  const [senha, setSenha] = useState("admin123");
  const [erro, setErro] = useState("");
  const [carregando, setCarregando] = useState(false);

  async function entrar(event: FormEvent) {
    event.preventDefault();
    setErro("");
    setCarregando(true);
    try {
      const resposta = await api.login(login, senha);
      localStorage.setItem("rotaclara_token", resposta.token);
      localStorage.setItem("rotaclara_usuario", JSON.stringify({ nome: resposta.nome, perfil: resposta.perfil }));
      onLogin({ nome: resposta.nome, perfil: resposta.perfil });
    } catch (error) {
      setErro(error instanceof Error ? error.message : "Não foi possível entrar");
    } finally {
      setCarregando(false);
    }
  }

  return (
    <main className="main" style={{ maxWidth: 480, margin: "8vh auto" }}>
      <div className="form-card">
        <h1>Entrar no RotaClara</h1>
        <p className="hint">Acesse a operação com seu usuário do backend.</p>
        <form onSubmit={entrar}>
          <div className="field"><label>Usuário</label><input value={login} onChange={(e) => setLogin(e.target.value)} required /></div>
          <div className="field"><label>Senha</label><input type="password" value={senha} onChange={(e) => setSenha(e.target.value)} required /></div>
          {erro && <p style={{ color: "var(--rust)" }}>{erro}</p>}
          <button className="btn primary" type="submit" disabled={carregando}>{carregando ? "Entrando..." : "Entrar"}</button>
        </form>
      </div>
    </main>
  );
}
