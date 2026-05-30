CREATE TABLE IF NOT EXISTS outbox_message_consumers (
    outbox_message_id UUID NOT NULL REFERENCES outbox_messages(id) ON DELETE CASCADE,
    name VARCHAR(200) NOT NULL,
    consumed_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    PRIMARY KEY (outbox_message_id, name)
);