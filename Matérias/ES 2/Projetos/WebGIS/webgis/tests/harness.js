// harness.js — utilitário mínimo de teste sem dependências externas.
let total = 0;
let falhas = 0;
const erros = [];

export function teste(nome, fn) {
  total += 1;
  try {
    fn();
    console.log(`  ✔ ${nome}`);
  } catch (erro) {
    falhas += 1;
    erros.push({ nome, erro });
    console.error(`  ✘ ${nome}\n      → ${erro.message}`);
  }
}

export function igual(esperado, atual, mensagem = '') {
  if (esperado !== atual) {
    throw new Error(`${mensagem} | esperado: ${JSON.stringify(esperado)} | atual: ${JSON.stringify(atual)}`);
  }
}

export function verdadeiro(valor, mensagem = 'esperado verdadeiro') {
  if (!valor) throw new Error(mensagem);
}

export function falso(valor, mensagem = 'esperado falso') {
  if (valor) throw new Error(mensagem);
}

export function proximo(esperado, atual, tolerancia = 1, mensagem = '') {
  if (Math.abs(esperado - atual) > tolerancia) {
    throw new Error(`${mensagem} | esperado: ${esperado} ± ${tolerancia} | atual: ${atual}`);
  }
}

export function relatorio() {
  console.log('\n==================================================');
  if (falhas === 0) {
    console.log(`  ✅ Todos os ${total} testes passaram.`);
  } else {
    console.error(`  ❌ ${falhas} de ${total} testes falharam.`);
  }
  console.log('==================================================');
  process.exitCode = falhas === 0 ? 0 : 1;
  return falhas;
}
