CREATE TABLE IF NOT EXISTS subscriptions (
    id UUID PRIMARY KEY DEFAULT uuidv7(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    service_id UUID NOT NULL REFERENCES services(id) ON DELETE CASCADE,

    status VARCHAR(20) NOT NULL DEFAULT 'INITIATED',
    subscribed_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    unsubscribed_at TIMESTAMPTZ,
    expires_at TIMESTAMPTZ NOT NULL,

    price_amount DECIMAL(10,2) NOT NULL,
    currency_code VARCHAR(3) NOT NULL DEFAULT 'NGN',
    billing_period VARCHAR(20) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by VARCHAR(150) NOT NULL,
    last_updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_updated_by VARCHAR(150) NOT NULL,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(150)
);

-- only one active subscription per user per service
CREATE UNIQUE INDEX IF NOT EXISTS idx_subscriptions_active_user_service
    ON subscriptions(user_id, service_id) WHERE status = 'ACTIVE' AND deleted_at IS NULL;

-- query all subscriptions for a user filtered by status
CREATE INDEX IF NOT EXISTS idx_subscriptions_user_id_status
    ON subscriptions(user_id, status);

-- query all subscriptions for a service
CREATE INDEX IF NOT EXISTS idx_subscriptions_service_id
    ON subscriptions(service_id);

-- find expiring subscriptions for renewal jobs
CREATE INDEX IF NOT EXISTS idx_subscriptions_expires_at
    ON subscriptions(expires_at) WHERE status = 'ACTIVE';
