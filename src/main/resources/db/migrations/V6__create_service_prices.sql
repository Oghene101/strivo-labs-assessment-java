CREATE TABLE IF NOT EXISTS service_prices (
    id UUID PRIMARY KEY DEFAULT uuidv7(),
    service_id UUID NOT NULL REFERENCES services(id) ON DELETE CASCADE,

    amount DECIMAL(10,2) NOT NULL,
    currency_code VARCHAR(3) NOT NULL DEFAULT 'NGN',
    billing_period VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by VARCHAR(150) NOT NULL,
    last_updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_updated_by VARCHAR(150) NOT NULL,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(150)
);

CREATE INDEX IF NOT EXISTS idx_service_prices_service_id
    ON service_prices(service_id);

CREATE INDEX IF NOT EXISTS idx_service_prices_active
    ON service_prices(service_id, is_active) WHERE is_active = TRUE AND deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS idx_service_prices_unique_active
    ON service_prices(service_id, billing_period) WHERE is_active = TRUE AND deleted_at IS NULL;

