// ============================================================================
// compartilhamento.js — Componente "Compartilhamento em tempo real"
// ----------------------------------------------------------------------------
// Conecta ao servidor Node (WebSocket) e compartilha, em tempo real, o estado
// da aplicação entre usuários: centro de referência, visão do mapa, filtros,
// operador espacial e resultados.
// ============================================================================

const IDs = {
  status: 'status-conexao',
  usuarios: 'contador-usuarios'
};

function selecionar(id) {
  return document.getElementById(id);
}

let socket = null;
let tentativas = 0;
let reconectando = false;

let aoReceberEstado = () => {};
let aoMudarUsuarios = () => {};

function definirStatus(texto, classe) {
  const el = selecionar(IDs.status);
  if (!el) return;
  el.textContent = texto;
  el.className = `status ${classe}`;
}

export function iniciarCompartilhamento({ onEstadoRecebido, onUsuarios, obterEstado }) {
  aoReceberEstado = onEstadoRecebido;
  aoMudarUsuarios = onUsuarios;

  const protocolo = location.protocol === 'https:' ? 'wss' : 'ws';
  const host = location.hostname || 'localhost';
  const porta = location.port || '3000';
  const url = `${protocolo}://${host}:${porta}`;

  conectar(url, obterEstado);
}

function conectar(url, obterEstado) {
  try {
    socket = new WebSocket(url);
  } catch {
    definirStatus('Sem servidor de tempo real', 'offline');
    return;
  }

  socket.onopen = () => {
    tentativas = 0;
    definirStatus('● Tempo real conectado', 'online');
    if (obterEstado) socket.send(JSON.stringify({ tipo: 'estado', estado: obterEstado() }));
  };

  socket.onmessage = (evento) => {
    let mensagem;
    try {
      mensagem = JSON.parse(evento.data);
    } catch {
      return;
    }

    if (mensagem.tipo === 'bem-vindo' || mensagem.tipo === 'usuarios') {
      if (selecionar(IDs.usuarios)) selecionar(IDs.usuarios).textContent = mensagem.usuarios;
      aoMudarUsuarios(mensagem.usuarios);
    }

    if (mensagem.tipo === 'estado' || mensagem.tipo === 'estado-inicial') {
      aoReceberEstado(mensagem.estado);
    }
  };

  socket.onclose = () => {
    definirStatus('Tempo real desconectado (reconectando…)', 'offline');
    if (!reconectando) {
      reconectando = true;
      setTimeout(() => {
        reconectando = false;
        conectar(url, obterEstado);
      }, Math.min(2000 * (tentativas + 1), 10000));
    }
    tentativas += 1;
  };

  socket.onerror = () => socket.close();
}

export function enviarEstado(estado) {
  if (socket && socket.readyState === WebSocket.OPEN) {
    socket.send(JSON.stringify({ tipo: 'estado', estado }));
  }
}
