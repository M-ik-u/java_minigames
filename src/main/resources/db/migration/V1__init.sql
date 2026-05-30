-- Схема онлайн-платформы мини-игр
CREATE TABLE IF NOT EXISTS users (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(64)  NOT NULL UNIQUE,
    password_hash VARCHAR(120) NOT NULL,
    role          VARCHAR(16)  NOT NULL DEFAULT 'USER',
    balance       NUMERIC(18, 2) NOT NULL DEFAULT 0,
    is_active     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS transactions (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type        VARCHAR(16) NOT NULL, -- DEPOSIT / WITHDRAWAL / BET / WIN
    amount      NUMERIC(18, 2) NOT NULL,
    balance_after NUMERIC(18, 2) NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_tx_user ON transactions(user_id, created_at DESC);

CREATE TABLE IF NOT EXISTS game_sessions (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    game        VARCHAR(16) NOT NULL, -- SLOT / ROULETTE / BLACKJACK
    bet         NUMERIC(18, 2) NOT NULL,
    payout      NUMERIC(18, 2) NOT NULL,
    result      TEXT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_gs_user ON game_sessions(user_id, created_at DESC);

-- Сид администратора создаётся приложением при старте (AdminSeedListener).
