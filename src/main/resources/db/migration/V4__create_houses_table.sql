CREATE TABLE houses (

    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    name VARCHAR(100) NOT NULL,

    invite_code VARCHAR(20) NOT NULL UNIQUE,

    owner_id BIGINT NOT NULL,

    created_at DATETIME NOT NULL,

    CONSTRAINT fk_house_owner
        FOREIGN KEY(owner_id)
        REFERENCES users(id)
);