CREATE TABLE IF NOT EXISTS roles (
    id UUID PRIMARY KEY DEFAULT uuidv7(),
    name VARCHAR(256) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by VARCHAR(150) NOT NULL,
    last_updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_updated_by VARCHAR(150) NOT NULL,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(150)
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_roles_name
    ON roles(name) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_roles_deleted_at
    ON roles(deleted_at) WHERE deleted_at IS NOT NULL;
