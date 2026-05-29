CREATE TABLE IF NOT EXISTS sessions (
    id UUID PRIMARY KEY DEFAULT uuidv7 (),
    user_id UUID NOT NULL,
    jwt_id VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    expires_at TIMESTAMPTZ NOT NULL,
    last_updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_updated_by VARCHAR(150) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_sessions_active_user_id 
    ON sessions (user_id) WHERE status = 'ACTIVE';

CREATE UNIQUE INDEX IF NOT EXISTS idx_sessions_jwt_id 
    ON sessions (jwt_id) WHERE status = 'ACTIVE';

CREATE INDEX IF NOT EXISTS idx_sessions_status_expires 
    ON sessions (status, expires_at);