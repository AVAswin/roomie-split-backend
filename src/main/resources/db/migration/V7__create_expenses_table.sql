CREATE TABLE expenses (

    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    title VARCHAR(255) NOT NULL,

    description VARCHAR(500),

    amount DECIMAL(10,2) NOT NULL,

    paid_by_user_id BIGINT NOT NULL,

    house_id BIGINT NOT NULL,

    created_at DATETIME NOT NULL,

    CONSTRAINT fk_expense_user
        FOREIGN KEY (paid_by_user_id)
        REFERENCES users(id),

    CONSTRAINT fk_expense_house
        FOREIGN KEY (house_id)
        REFERENCES houses(id)
);