CREATE TABLE IF NOT EXISTS outbox_messages (
    id UUID PRIMARY KEY DEFAULT uuidv7(),
    type VARCHAR(200) NOT NULL,
    content JSONB NOT NULL,
    occurred_on TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    processed_on TIMESTAMPTZ,
    next_retry_on TIMESTAMPTZ,
    retry_count INTEGER NOT NULL DEFAULT 0,
    error TEXT
);

CREATE INDEX idx_outbox_unprocessed_retry
ON outbox_messages (occurred_on)
INCLUDE (next_retry_on)
WHERE processed_on IS NULL;