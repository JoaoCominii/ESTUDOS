-- V1__init.sql
-- Schema inicial do MVP RotaClara, refletindo o modelo de dados da
-- especificação (seção 8) já normalizado em Ponto (catálogo) +
-- PontoRoteiro (ocorrência do ponto dentro de um roteiro).

CREATE TABLE motorista (
    id                    VARCHAR(36)     PRIMARY KEY,
    nome                  VARCHAR(150)    NOT NULL,
    telefone              VARCHAR(30)     NOT NULL,
    documento             VARCHAR(30)     NOT NULL,
    veiculo               VARCHAR(100)    NOT NULL,
    rendimento_km_litro   DOUBLE PRECISION NOT NULL CHECK (rendimento_km_litro > 0),
    CONSTRAINT uk_motorista_documento UNIQUE (documento)
);

CREATE TABLE gerente_coordenador (
    id          VARCHAR(36)   PRIMARY KEY,
    nome        VARCHAR(150)  NOT NULL,
    telefone    VARCHAR(30)   NOT NULL,
    email       VARCHAR(150)  NOT NULL,
    CONSTRAINT uk_gerente_email UNIQUE (email)
);

CREATE TABLE usuario (
    id            VARCHAR(36)   PRIMARY KEY,
    login         VARCHAR(100)  NOT NULL,
    senha_hash    VARCHAR(200)  NOT NULL,
    perfil        VARCHAR(20)   NOT NULL CHECK (perfil IN ('MOTORISTA', 'GERENTE', 'ADMINISTRADOR')),
    motorista_id  VARCHAR(36)   REFERENCES motorista (id),
    gerente_id    VARCHAR(36)   REFERENCES gerente_coordenador (id),
    CONSTRAINT uk_usuario_login UNIQUE (login)
);

CREATE TABLE ponto (
    id          VARCHAR(36)       PRIMARY KEY,
    endereco    VARCHAR(250)      NOT NULL,
    latitude    DOUBLE PRECISION  NOT NULL,
    longitude   DOUBLE PRECISION  NOT NULL
);

CREATE TABLE roteiro (
    id                            VARCHAR(36)       PRIMARY KEY,
    data                          DATE              NOT NULL,
    motorista_id                  VARCHAR(36)       NOT NULL REFERENCES motorista (id),
    distancia_total_km            DOUBLE PRECISION  NOT NULL CHECK (distancia_total_km > 0),
    tempo_total_parado_minutos    INTEGER           NOT NULL DEFAULT 0,
    custo_estimado                DOUBLE PRECISION  NOT NULL DEFAULT 0,
    status                        VARCHAR(20)       NOT NULL DEFAULT 'PLANEJADO'
                                  CHECK (status IN ('PLANEJADO', 'EM_ANDAMENTO', 'CONCLUIDO')),
    -- RN05: um roteiro pertence a um único motorista e a uma única data.
    CONSTRAINT uk_roteiro_motorista_data UNIQUE (motorista_id, data)
);

CREATE TABLE ponto_roteiro (
    id                        VARCHAR(36)   PRIMARY KEY,
    roteiro_id                VARCHAR(36)   NOT NULL REFERENCES roteiro (id) ON DELETE CASCADE,
    ponto_id                  VARCHAR(36)   NOT NULL REFERENCES ponto (id),
    ordem                     INTEGER       NOT NULL CHECK (ordem > 0),
    data_hora_chegada         TIMESTAMPTZ,
    data_hora_saida           TIMESTAMPTZ,
    tempo_parado_minutos      INTEGER,
    -- RN06: a ordem dos pontos é sequencial e única dentro do roteiro.
    CONSTRAINT uk_ponto_roteiro_ordem UNIQUE (roteiro_id, ordem)
);

CREATE TABLE parametro (
    id                              VARCHAR(20)       PRIMARY KEY,
    valor_combustivel_por_litro     DOUBLE PRECISION  NOT NULL CHECK (valor_combustivel_por_litro > 0),
    custo_por_km                    DOUBLE PRECISION  NOT NULL CHECK (custo_por_km > 0),
    -- RN04: jornada padrão de trabalho.
    jornada_padrao_horas            DOUBLE PRECISION  NOT NULL DEFAULT 8,
    limite_alerta_parada_minutos    INTEGER           NOT NULL DEFAULT 30
);

CREATE INDEX idx_roteiro_data ON roteiro (data);
CREATE INDEX idx_ponto_roteiro_roteiro ON ponto_roteiro (roteiro_id);

-- Parâmetros padrão (RF09/RF10) — podem ser alterados via PUT /parametros
-- sem qualquer mudança de código, conforme o critério de aceitação do MVP.
INSERT INTO parametro (id, valor_combustivel_por_litro, custo_por_km, jornada_padrao_horas, limite_alerta_parada_minutos)
VALUES ('GLOBAL', 6.19, 0.42, 8, 30);
