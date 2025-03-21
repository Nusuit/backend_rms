INSERT INTO roles (role_id, role_name) VALUES (1, 'CANDIDATE');
INSERT INTO roles (role_id, role_name) VALUES (2, 'RECRUITER');
-----------------------------------------------------------------------------------------------------------------
-- Password123!
INSERT INTO users (email, password, role_id) VALUES ('user1@example.com', '$2a$10$LB4U8.uVdk/MtRJtMyBfNet/ov411pQZDIyzR1cpwoHnWyaaJT44a', 1);
INSERT INTO users (email, password) VALUES ('user2@example.com', '$2a$10$LB4U8.uVdk/MtRJtMyBfNet/ov411pQZDIyzR1cpwoHnWyaaJT44a');
INSERT INTO users (email, password) VALUES ('user3@example.com', '$2a$10$LB4U8.uVdk/MtRJtMyBfNet/ov411pQZDIyzR1cpwoHnWyaaJT44a');
INSERT INTO users (email, password) VALUES ('user4@example.com', '$2a$10$LB4U8.uVdk/MtRJtMyBfNet/ov411pQZDIyzR1cpwoHnWyaaJT44a');
INSERT INTO users (email, password) VALUES ('user5@example.com', '$2a$10$LB4U8.uVdk/MtRJtMyBfNet/ov411pQZDIyzR1cpwoHnWyaaJT44a');

-----------------------------------------------------------------------------------------------------------------

