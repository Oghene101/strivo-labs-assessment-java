CREATE TABLE IF NOT EXISTS dead_letter_outbox_messages (
    id UUID PRIMARY KEY DEFAULT uuidv7(),
    type VARCHAR(200) NOT NULL,
    content JSONB NOT NULL,
    dead_lettered_on TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    retry_count INTEGER NOT NULL DEFAULT 0,
    error TEXT
);

CREATE INDEX idx_dead_letter_outbox_messages_dead_lettered_on
    ON dead_letter_outbox_messages (dead_lettered_on);