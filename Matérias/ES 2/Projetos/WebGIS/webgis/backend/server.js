import http from 'node:http';
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { WebSocketServer } from 'ws';

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const FRONTEND_DIR = path.resolve(__dirname, '..', 'frontend');
const PORT = Number(process.env.PORT) || 3000;
const WFS_URL = 'http://bhmap.pbh.gov.br/v2/api/idebhgeo/wfs';
const TIMEOUT_MS = 30000;

const MIME = {
  '.html': 'text/html; charset=utf-8',
  '.js': 'text/javascript; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon',
  '.map': 'application/json'
};

const CAMADAS = {
  hospitais: {
    typeName: 'ide_bhgeo:HOSPITAIS',
    propertyName: 'ID_EQ_SAUDE,NOME,CATEGORIA,TIPO_LOGRADOURO,LOGRADOURO,NUMERO_IMOVEL,TELEFONE,NOME_BAIRRO_POPULAR,GEOMETRIA'
  },
  farmacias: {
    typeName: 'ide_bhgeo:ATIVIDADE_ECONOMICA',
    cql: "CNAE_PRINCIPAL LIKE '4771%'",
    propertyName: 'NOME,CNAE_PRINCIPAL,DESCRICAO_CNAE_PRINCIPAL,NOME_BAIRRO,NOME_LOGRADOURO,NUMERO_IMOVEL,GEOMETRIA'
  },
  laboratorios: {
    typeName: 'ide_bhgeo:ATIVIDADE_ECONOMICA',
    cql: "CNAE_PRINCIPAL LIKE '864%'",
    propertyName: 'NOME,CNAE_PRINCIPAL,DESCRICAO_CNAE_PRINCIPAL,NOME_BAIRRO,NOME_LOGRADOURO,NUMERO_IMOVEL,GEOMETRIA'
  }
};

function servirArquivo(arquivo, res) {
  const ext = path.extname(arquivo).toLowerCase();
  fs.readFile(arquivo, (erro, dados) => {
    if (erro) {
      res.writeHead(404, { 'Content-Type': 'text/plain' });
      res.end('Not Found');
      return;
    }
    res.writeHead(200, {
      'Content-Type': MIME[ext] || 'application/octet-stream',
      'Cache-Control': 'no-cache'
    });
    res.end(dados);
  });
}

function servirEstatico(req, res) {
  let caminho;
  try {
    caminho = decodeURIComponent(new URL(req.url, 'http://localhost').pathname);
  } catch {
    res.writeHead(400);
    res.end('Bad Request');
    return;
  }

  if (caminho === '/') caminho = '/index.html';

  const arquivo = path.normalize(path.join(FRONTEND_DIR, caminho));
  if (!arquivo.startsWith(FRONTEND_DIR)) {
    res.writeHead(403);
    res.end('Forbidden');
    return;
  }

  fs.stat(arquivo, (erro) => {
    if (erro || !fs.statSync(arquivo).isFile()) {
      res.writeHead(404, { 'Content-Type': 'text/plain' });
      res.end('Not Found');
      return;
    }
    servirArquivo(arquivo, res);
  });
}

function montarUrlWfs(tipo) {
  const conf = CAMADAS[tipo];
  if (!conf) return null;
  const params = new URLSearchParams({
    service: 'WFS',
    version: '1.0.0',
    request: 'GetFeature',
    typeName: conf.typeName,
    srsName: 'EPSG:4326',
    outputFormat: 'application/json'
  });
  if (conf.cql) params.set('CQL_FILTER', conf.cql);
  if (conf.propertyName) params.set('propertyName', conf.propertyName);
  return `${WFS_URL}?${params.toString().replace(/\+/g, '%20')}`;
}

function proxificarPbh(req, res, tipo) {
  const url = montarUrlWfs(tipo);
  if (!url) {
    res.writeHead(400, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ erro: `Tipo desconhecido: ${tipo}` }));
    return;
  }

  const controle = new AbortController();
  const temporizador = setTimeout(() => controle.abort(), TIMEOUT_MS);

  fetch(url, { signal: controle.signal })
    .then(async (resp) => {
      const texto = await resp.text();
      res.writeHead(resp.status, {
        'Content-Type': 'application/json; charset=utf-8',
        'Access-Control-Allow-Origin': '*',
        'Cache-Control': 'no-cache'
      });
      res.end(texto);
    })
    .catch((erro) => {
      console.error(`[proxy] Falha ao buscar ${tipo}:`, erro.message);
      res.writeHead(502, { 'Content-Type': 'application/json' });
      res.end(JSON.stringify({ erro: 'Falha ao acessar a API da PBH', detalhe: erro.message }));
    })
    .finally(() => clearTimeout(temporizador));
}

const servidor = http.createServer((req, res) => {
  const url = new URL(req.url, 'http://localhost');

  if (url.pathname === '/api/pbh') {
    const tipo = url.searchParams.get('tipo') || '';
    proxificarPbh(req, res, tipo);
    return;
  }

  if (req.url === '/api/status') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ ok: true, servidor: 'FarmáciaFinder', usuarios: [...wss.clients].length }));
    return;
  }

  if (req.method === 'GET') {
    servirEstatico(req, res);
    return;
  }

  res.writeHead(405);
  res.end('Method Not Allowed');
});

const wss = new WebSocketServer({ server: servidor });

let ultimoEstadoCompartilhado = null;

wss.on('connection', (socket) => {
  socket.id = `${Date.now()}-${Math.random().toString(16).slice(2)}`;
  console.log(`[ws] Cliente conectado (${wss.clients.size} online)`);

  socket.send(JSON.stringify({
    tipo: 'bem-vindo',
    id: socket.id,
    usuarios: wss.clients.size
  }));

  if (ultimoEstadoCompartilhado) {
    socket.send(JSON.stringify({ tipo: 'estado-inicial', estado: ultimoEstadoCompartilhado }));
  }

  socket.on('message', (dados) => {
    let mensagem;
    try {
      mensagem = JSON.parse(dados.toString());
    } catch {
      return;
    }

    if (mensagem.tipo === 'estado') {
      ultimoEstadoCompartilhado = mensagem.estado;
      const serializado = JSON.stringify(mensagem);
      for (const cliente of wss.clients) {
        if (cliente !== socket && cliente.readyState === 1) {
          cliente.send(serializado);
        }
      }
    }

    if (mensagem.tipo === 'usuarios') {
      const aviso = JSON.stringify({ tipo: 'usuarios', usuarios: wss.clients.size });
      for (const cliente of wss.clients) {
        if (cliente.readyState === 1) cliente.send(aviso);
      }
    }
  });

  socket.on('close', () => {
    console.log(`[ws] Cliente desconectado (${wss.clients.size} online)`);
    const aviso = JSON.stringify({ tipo: 'usuarios', usuarios: wss.clients.size });
    for (const cliente of wss.clients) {
      if (cliente.readyState === 1) cliente.send(aviso);
    }
  });

  socket.on('error', (erro) => console.error('[ws] Erro:', erro.message));
});

servidor.listen(PORT, () => {
  console.log('==================================================');
  console.log('  FarmáciaFinder - Servidor WebGIS');
  console.log(`  Acesse:  http://localhost:${PORT}`);
  console.log(`  Proxy PBH: http://localhost:${PORT}/api/pbh?tipo=hospitais`);
  console.log('==================================================');
});
