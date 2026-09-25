-- V2__seed_exemplo.sql
-- Dados de exemplo para desenvolvimento/demonstração — os mesmos usados
-- no protótipo e no front-end mock (Carlos Mendes, Roteiro A etc.).
-- Em um ambiente real de produção, este arquivo não deveria ser
-- aplicado (mover para um perfil "dev" do Flyway, se necessário).

INSERT INTO gerente_coordenador (id, nome, telefone, email) VALUES
    ('seed-ger-1', 'Ana Souza', '(31) 98888-0000', 'ana.souza@rotaclara.app');

INSERT INTO motorista (id, nome, telefone, documento, veiculo, rendimento_km_litro) VALUES
    ('seed-mot-1', 'Carlos Mendes', '(31) 99999-0001', '111.111.111-11', 'Moto Honda CG 160', 35),
    ('seed-mot-2', 'Renata Alves',  '(31) 99999-0002', '222.222.222-22', 'Fiorino',           11),
    ('seed-mot-3', 'João Pires',    '(31) 99999-0003', '333.333.333-33', 'Moto Honda CG 160', 33);

-- Senhas de exemplo (hash BCrypt): administrador -> admin123,
-- ana.souza -> gerente123, carlos.mendes -> motorista123.
INSERT INTO usuario (id, login, senha_hash, perfil, motorista_id, gerente_id) VALUES
    ('seed-usr-admin',   'administrador',  '$2b$10$5MxzZvYVEMq7uia7kvP3MeQvEUuHcd3a5GJaeoc5IkA8GEzDKvF8C', 'ADMINISTRADOR', NULL, NULL),
    ('seed-usr-gerente', 'ana.souza',      '$2b$10$JO/IOuc6HCxtE/Wu6fWqje7Ys0dFL5gzq8uQ/2JpLTtJFiB64gqVK', 'GERENTE', NULL, 'seed-ger-1'),
    ('seed-usr-mot-1',   'carlos.mendes',  '$2b$10$JHq8dRM92Z/QUB3NReiOcOJM3cmiqGkyouemposX0FHAgJ2ratIWG', 'MOTORISTA', 'seed-mot-1', NULL);

-- Catálogo de pontos — mesmo "Roteiro A" usado como exemplo na
-- especificação (seção 5).
INSERT INTO ponto (id, endereco, latitude, longitude) VALUES
    ('seed-pt-1', 'Seg. Família (partida)', -19.9245, -43.9352),
    ('seed-pt-2', 'Rua Peru, 55',           -19.9420, -43.9378),
    ('seed-pt-3', 'Rua X, 5',               -19.9187, -43.9401),
    ('seed-pt-4', 'Av. João César, 340',    -19.9033, -43.9455);

INSERT INTO roteiro (id, data, motorista_id, distancia_total_km, tempo_total_parado_minutos, custo_estimado, status) VALUES
    ('seed-rot-a', CURRENT_DATE, 'seed-mot-1', 18.4, 75, 7.73, 'CONCLUIDO');

INSERT INTO ponto_roteiro (id, roteiro_id, ponto_id, ordem, data_hora_chegada, data_hora_saida, tempo_parado_minutos) VALUES
    ('seed-pr-1', 'seed-rot-a', 'seed-pt-1', 1, CURRENT_DATE + TIME '08:00', CURRENT_DATE + TIME '08:00', 0),
    ('seed-pr-2', 'seed-rot-a', 'seed-pt-2', 2, CURRENT_DATE + TIME '08:22', CURRENT_DATE + TIME '08:37', 15),
    ('seed-pr-3', 'seed-rot-a', 'seed-pt-3', 3, CURRENT_DATE + TIME '08:51', CURRENT_DATE + TIME '09:01', 10),
    ('seed-pr-4', 'seed-rot-a', 'seed-pt-4', 4, CURRENT_DATE + TIME '09:20', CURRENT_DATE + TIME '10:10', 50);
