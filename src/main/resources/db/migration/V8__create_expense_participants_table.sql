CREATE TABLE expense_participants (

    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    expense_id BIGINT NOT NULL,

    user_id BIGINT NOT NULL,

    share_amount DECIMAL(10,2) NOT NULL,

    status VARCHAR(20) NOT NULL,

    CONSTRAINT fk_expense_participant_expense
        FOREIGN KEY (expense_id)
        REFERENCES expenses(id),

    CONSTRAINT fk_expense_participant_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);