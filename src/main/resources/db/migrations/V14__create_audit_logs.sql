CREATE TABLE IF NOT EXISTS audit_logs (
    id UUID PRIMARY KEY DEFAULT uuidv7(),
    action      VARCHAR(20) NOT NULL,
    user_id     UUID NOT NULL,
    entity_name VARCHAR(50) NOT NULL,
    entity_id   UUID NOT NULL,
    occurred_on  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    changes     TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_audit_logs_entity 
    ON audit_logs (entity_name, entity_id);

CREATE INDEX IF NOT EXISTS idx_audit_logs_user 
    ON audit_logs (user_id, action);