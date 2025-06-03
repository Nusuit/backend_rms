-- Xóa dữ liệu cũ để script có thể chạy lại một cách sạch sẽ
DELETE FROM RECRUITERS WHERE recruiter_id IN (SELECT recruiter_auth_id FROM RECRUITERS_AUTH WHERE username = 'hacnguyet108@gmail.com');
DELETE FROM RECRUITERS_AUTH WHERE username = 'hacnguyet108@gmail.com';

DELETE FROM RECRUITERS WHERE recruiter_id IN (SELECT recruiter_auth_id FROM RECRUITERS_AUTH WHERE username = 'recruiter1');
DELETE FROM RECRUITERS_AUTH WHERE username = 'recruiter1';

DELETE FROM RECRUITERS WHERE recruiter_id IN (SELECT recruiter_auth_id FROM RECRUITERS_AUTH WHERE username = 'recruiter2');
DELETE FROM RECRUITERS_AUTH WHERE username = 'recruiter2';

-- Thêm tài khoản Recruiter: hacnguyet108@gmail.com / 123123
INSERT INTO recruiters_auth (recruiter_auth_id, username, password, status, role_id)
VALUES (1, 'hacnguyet108@gmail.com', '$2a$10$lnGTYMAvzKo.QToUyQ15u.I0o/1qq./Krl/eStVOV./ucZWhYhmu6', 'ACTIVE', 2);
INSERT INTO recruiters (recruiter_id, name, description)
VALUES (1, 'Hac Nguyet Recruiter', 'Tài khoản Super Recruiter');

-- Các tài khoản Recruiter hiện có khác
INSERT INTO recruiters_auth (recruiter_auth_id, username, password, status, role_id)
VALUES (2, 'recruiter1', '$2a$10$XbPrPeBkYgr4FBiHaNnfJOwG7c8crJULLbcthFDIiDTkm/w7YnQa2', 'ACTIVE', 2);
INSERT INTO recruiters (recruiter_id, name, description)
VALUES (2, 'Recruiter One', 'Mô tả cho Recruiter One');

INSERT INTO recruiters_auth (recruiter_auth_id, username, password, status, role_id)
VALUES (3, 'recruiter2', '$2a$10$XbPrPeBkYgr4FBiHaNnfJOwG7c8crJULLbcthFDIiDTkm/w7YnQa2', 'ACTIVE', 2);
INSERT INTO recruiters (recruiter_id, name, description)
VALUES (3, 'Recruiter Two', 'Mô tả cho Recruiter Two');
