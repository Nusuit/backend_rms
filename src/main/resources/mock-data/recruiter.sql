-- Loại bỏ các dòng DROP/CREATE SEQUENCE và ALTER TABLE ... IDENTITY

INSERT INTO recruiters_auth (username, password, status, role_id)
VALUES ('recruiter1', '$2a$10$XbPrPeBkYgr4FBiHaNnfJOwG7c8crJULLbcthFDIiDTkm/w7YnQa2', 'ACTIVE', 2); -- role_id là 2

INSERT INTO recruiters_auth (username, password, status, role_id)
VALUES ('recruiter2', '$2a$10$XbPrPeBkYgr4FBiHaNnfJOwG7c8crJULLbcthFDIiDTkm/w7YnQa2', 'ACTIVE', 2); -- role_id là 2
