CREATE TABLE house_members (

    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    house_id BIGINT NOT NULL,

    user_id BIGINT NOT NULL,

    joined_at DATETIME NOT NULL,

    UNIQUE(house_id, user_id),

    CONSTRAINT fk_member_house
        FOREIGN KEY(house_id)
        REFERENCES houses(id),

    CONSTRAINT fk_member_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
);