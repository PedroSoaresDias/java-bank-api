CREATE TABLE investment_wallet (
    id SERIAL PRIMARY KEY,
    pix VARCHAR(255) UNIQUE NOT NULL,
    balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    tax DECIMAL(5, 2) NOT NULL,
    initial_deposit DECIMAL(19, 2) NOT NULL,
    user_id BIGINT NOT NULL,
);