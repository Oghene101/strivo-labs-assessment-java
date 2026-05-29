CREATE TABLE IF NOT EXISTS services (
    id UUID PRIMARY KEY DEFAULT uuidv7(),

    name VARCHAR(150) NOT NULL,
    description VARCHAR(500) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by VARCHAR(150) NOT NULL,
    last_updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_updated_by VARCHAR(150) NOT NULL,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(150)
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_services_name
    ON services(name) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_services_is_active
    ON services(is_active) WHERE is_active = TRUE AND deleted_at IS NULL;
