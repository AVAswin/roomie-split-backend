ALTER TABLE house_members
ADD CONSTRAINT uk_house_member_user
UNIQUE(user_id);