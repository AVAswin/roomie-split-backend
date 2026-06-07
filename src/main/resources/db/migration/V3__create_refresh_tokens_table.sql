CREATE TABLE refresh_tokens(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(500) NOT NULL UNIQUE,
    expiry_date DATETIME NOT NULL,
    user_id BIGINT UNIQUE,

    CONSTRAINT fk_refresh_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
);