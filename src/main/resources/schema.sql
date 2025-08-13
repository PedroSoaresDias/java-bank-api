CREATE TABLE tb_user(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE account_wallet(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pix VARCHAR(255) UNIQUE NOT NULL,
    balance BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES tb_user(id)
);

CREATE TABLE investment_wallet (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pix VARCHAR(255) UNIQUE NOT NULL,
    balance BIGINT NOT NULL,
    tax BIGINT NOT NULL,
    initial_deposit BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_investment_user FOREIGN KEY (user_id) REFERENCES tb_user(id)
);