-- 1. Create the database
CREATE DATABASE decision;

-- 2. Grant CRUD privileges to user1 on the 'decision' database
GRANT SELECT, INSERT, UPDATE, DELETE ON decision.* TO 'user1'@'%';

-- 3. Apply the privileges
FLUSH PRIVILEGES;


CREATE TABLE decision.decision_seq (
    next_val BIGINT NOT NULL
);