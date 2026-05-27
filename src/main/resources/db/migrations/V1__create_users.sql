CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuidv7(),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(256) NOT NULL,
    email_confirmed BOOLEAN NOT NULL DEFAULT FALSE,
    password_hash TEXT,
    phone_number VARCHAR(20),
    access_failed_count INTEGER NOT NULL DEFAULT 0,
    lockout_count INTEGER NOT NULL DEFAULT 0,
    lockout_end TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by VARCHAR(150) NOT NULL,
    last_updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_updated_by VARCHAR(150) NOT NULL,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(150)
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email
    ON users(email) WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS idx_users_phone_number
    ON users(phone_number) WHERE deleted_at IS NULL AND phone_number IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_users_deleted_at
    ON users(deleted_at) WHERE deleted_at IS NOT NULL;