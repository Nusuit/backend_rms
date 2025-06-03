-- Loại bỏ các dòng DROP/CREATE SEQUENCE và ALTER TABLE ... IDENTITY

INSERT INTO recruiters_auth (email, password, status, role_id) -- Đã đổi username thành email
VALUES ('recruiter1@example.com', '$2a$10$XbPrPeBkYgr4FBiHaNnfJOwG7c8crJULLbcthFDIiDTkm/w7YnQa2', 'ACTIVE', 2)/ -- role_id là 2

INSERT INTO recruiters_auth (email, password, status, role_id) -- Đã đổi username thành email
VALUES ('recruiter2@example.com', '$2a$10$XbPrPeBkYgr4FBiHaNnfJOwG7c8crJULLbcthFDIiDTkm/w7YnQa2', 'ACTIVE', 2)/ -- role_id là 2
