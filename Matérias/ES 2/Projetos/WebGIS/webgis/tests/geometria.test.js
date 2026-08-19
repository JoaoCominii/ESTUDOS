import {
  calcularDistancia,
  pontoEmPoligono,
  pontoDentroPoligono,
  distanciaMinimaAoPoligono,
  avaliarOperadorEspacial,
  calcularBoundsEmVolta
} from '../frontend/js/geometria.js';
import { teste, igual, verdadeiro, falso, proximo } from './harness.js';

// ---------------------------------------------------------------------------
// Geometria de referência usada nos testes (região de Belo Horizonte)
// ---------------------------------------------------------------------------
const ANEL = [
  [-19.9300, -43.9500], // sudoeste
  [-19.9300, -43.9300], // sudeste
  [-19.9100, -43.9300], // nordeste
  [-19.9100, -43.9500]  // noroeste
];
const CENTRO = { lat: -19.9205, lng: -43.9377 };

function ponto(lat, lng) {
  return { lat, lng };
}

// ---------------------------------------------------------------------------
console.log('\n[geometria] Distância (Haversine)');
// ---------------------------------------------------------------------------
teste('distância entre o mesmo ponto é zero', () => {
  igual(0, Math.round(calcularDistancia(CENTRO.lat, CENTRO.lng, CENTRO.lat, CENTRO.lng)));
});

teste('1 grau de latitude ≈ 111.320 m', () => {
  proximo(111320, calcularDistancia(0, -43.9377, 1, -43.9377), 1000);
});

teste('distância é simétrica', () => {
  const ida = calcularDistancia(-19.92, -43.94, -19.91, -43.93);
  const volta = calcularDistancia(-19.91, -43.93, -19.92, -43.94);
  proximo(ida, volta, 1e-6);
});

// ---------------------------------------------------------------------------
console.log('\n[geometria] Ponto em polígono (ray casting)');
// ---------------------------------------------------------------------------
teste('ponto no centro do polígono está dentro', () => {
  verdadeiro(pontoEmPoligono(-19.9200, -43.9400, ANEL));
});

teste('ponto fora do polígono não está dentro', () => {
  falso(pontoEmPoligono(-19.9200, -43.9800, ANEL));
});

teste('ponto sobre a borda: emPoligono é true, mas dentro estrito é false', () => {
  verdadeiro(pontoEmPoligono(-19.9100, -43.9400, ANEL));
  falso(pontoDentroPoligono(-19.9100, -43.9400, ANEL));
});

teste('distância de um ponto fora à borda norte ≈ 556 m', () => {
  // 0.005° acima da borda norte (-19.9100) ≈ 556.6 m
  proximo(556.6, distanciaMinimaAoPoligono(-19.9050, -43.9400, ANEL), 15);
});

// ---------------------------------------------------------------------------
console.log('\n[geometria] Operadores espaciais sobre polígono');
// ---------------------------------------------------------------------------
const POLIGONO = { tipo: 'poligono', vertices: ANEL };

teste('estacontido: ponto interno atende, ponto externo não', () => {
  verdadeiro(avaliarOperadorEspacial(ponto(-19.9200, -43.9400), POLIGONO, 'estacontido', 0));
  falso(avaliarOperadorEspacial(ponto(-19.9200, -43.9800), POLIGONO, 'estacontido', 0));
});

teste('estacontido: ponto na borda NÃO atende (estritamente dentro)', () => {
  falso(avaliarOperadorEspacial(ponto(-19.9100, -43.9400), POLIGONO, 'estacontido', 0));
});

teste('contem: ponto interno com buffer maior que a distância à borda não atende', () => {
  // ponto ~55 m da borda sul, buffer de 200 m → não cabe dentro do polígono
  falso(avaliarOperadorEspacial(ponto(-19.9295, -43.9400), POLIGONO, 'contem', 200));
});

teste('contem: ponto interno com buffer zero atende', () => {
  verdadeiro(avaliarOperadorEspacial(ponto(-19.9200, -43.9400), POLIGONO, 'contem', 0));
});

teste('contem: ponto na borda não atende', () => {
  falso(avaliarOperadorEspacial(ponto(-19.9100, -43.9400), POLIGONO, 'contem', 0));
});

teste('intercepta: ponto próximo da borda (dentro do buffer) atende', () => {
  // ponto a ~261 m a oeste da borda (-43.9500); buffer 270 → intercepta
  verdadeiro(avaliarOperadorEspacial(ponto(-19.9250, -43.9525), POLIGONO, 'intercepta', 270));
});

teste('intercepta: ponto distante da borda (fora do buffer) não atende', () => {
  falso(avaliarOperadorEspacial(ponto(-19.9250, -43.9525), POLIGONO, 'intercepta', 240));
});

teste('hierarquia: intercepta ⊇ estacontido ⊇ contem', () => {
  const pt = ponto(-19.9200, -43.9400);
  verdadeiro(avaliarOperadorEspacial(pt, POLIGONO, 'intercepta', 0));
  verdadeiro(avaliarOperadorEspacial(pt, POLIGONO, 'estacontido', 0));
  verdadeiro(avaliarOperadorEspacial(pt, POLIGONO, 'contem', 0));
});

// ---------------------------------------------------------------------------
console.log('\n[geometria] Operadores espaciais sobre círculo');
// ---------------------------------------------------------------------------
const CIRCULO = { tipo: 'circulo', centro: CENTRO, raio: 1000 };
const P500 = ponto(CENTRO.lat + 500 / 111320, CENTRO.lng);
const P900 = ponto(CENTRO.lat + 900 / 111320, CENTRO.lng);
const P1100 = ponto(CENTRO.lat + 1100 / 111320, CENTRO.lng);
const P2000 = ponto(CENTRO.lat + 2000 / 111320, CENTRO.lng);

teste('círculo: ponto a 500 m → estacontido, contem (buffer 200) e intercepta', () => {
  verdadeiro(avaliarOperadorEspacial(P500, CIRCULO, 'estacontido', 0));
  verdadeiro(avaliarOperadorEspacial(P500, CIRCULO, 'contem', 200));
  verdadeiro(avaliarOperadorEspacial(P500, CIRCULO, 'intercepta', 0));
});

teste('círculo: ponto a 900 m → estacontido, mas contem com buffer 200 NÃO', () => {
  verdadeiro(avaliarOperadorEspacial(P900, CIRCULO, 'estacontido', 0));
  falso(avaliarOperadorEspacial(P900, CIRCULO, 'contem', 200));
  verdadeiro(avaliarOperadorEspacial(P900, CIRCULO, 'intercepta', 0));
});

teste('círculo: ponto a 1.100 m → apenas intercepta (buffer 200 alcança)', () => {
  falso(avaliarOperadorEspacial(P1100, CIRCULO, 'estacontido', 0));
  falso(avaliarOperadorEspacial(P1100, CIRCULO, 'contem', 200));
  verdadeiro(avaliarOperadorEspacial(P1100, CIRCULO, 'intercepta', 200));
});

teste('círculo: ponto a 2.000 m → nenhum operador atende', () => {
  falso(avaliarOperadorEspacial(P2000, CIRCULO, 'estacontido', 0));
  falso(avaliarOperadorEspacial(P2000, CIRCULO, 'contem', 200));
  falso(avaliarOperadorEspacial(P2000, CIRCULO, 'intercepta', 200));
});

// ---------------------------------------------------------------------------
console.log('\n[geometria] Bounds para enquadramento do mapa');
// ---------------------------------------------------------------------------
teste('bounds de raio de 1.000 m: norte maior que sul, leste maior que oeste', () => {
  const b = calcularBoundsEmVolta(CENTRO, 1000);
  verdadeiro(b.norte > b.sul);
  verdadeiro(b.leste > b.oeste);
  verdadeiro(b.norte > CENTRO.lat && b.sul < CENTRO.lat);
  verdadeiro(b.leste > CENTRO.lng && b.oeste < CENTRO.lng);
});

teste('span norte-sul do bounds (raio 1.000 m) ≈ 2.000 m', () => {
  const b = calcularBoundsEmVolta(CENTRO, 1000);
  proximo(2000, calcularDistancia(b.sul, CENTRO.lng, b.norte, CENTRO.lng), 50);
});
