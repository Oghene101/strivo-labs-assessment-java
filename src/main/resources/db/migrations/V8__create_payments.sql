CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY DEFAULT uuidv7(),
    reference VARCHAR(50) NOT NULL UNIQUE,
    
    payable_type VARCHAR(20) NOT NULL,
    payable_id UUID NOT NULL,               
    
    amount DECIMAL(10,2) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    
    user_id UUID NOT NULL REFERENCES users(id),
    gateway_response JSONB,                   
    paid_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by VARCHAR(150) NOT NULL,
    last_updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_updated_by VARCHAR(150) NOT NULL,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(150)
);

CREATE INDEX IF NOT EXISTS idx_payments_user_id
    ON payments(user_id);