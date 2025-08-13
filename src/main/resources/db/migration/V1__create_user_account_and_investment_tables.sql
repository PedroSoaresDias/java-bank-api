CREATE TABLE tb_user (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE account_wallet (
    id SERIAL PRIMARY KEY,
    pix VARCHAR(255) UNIQUE NOT NULL,
    balance DECIMAL(19,2) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES tb_user(id)
);

CREATE TABLE investment_wallet (
    id SERIAL PRIMARY KEY,
    pix VARCHAR(255) UNIQUE NOT NULL,
    balance DECIMAL(19, 2) NOT NULL,
    tax DECIMAL(5, 2) NOT NULL,
    initial_deposit DECIMAL(19, 2) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_investment_user FOREIGN KEY (user_id) REFERENCES tb_user(id)
);