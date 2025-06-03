-- Xóa các sequence cũ nếu có để tránh lỗi khi chạy lại
BEGIN
EXECUTE IMMEDIATE 'DROP SEQUENCE RECRUITERS_AUTH_SEQ';
EXCEPTION
       WHEN OTHERS THEN
          IF SQLCODE != -2289 THEN -- ORA-02289: sequence does not exist
             RAISE;
END IF;
END;
/

    -- Tạo sequence mới
CREATE SEQUENCE RECRUITERS_AUTH_SEQ START WITH 1 INCREMENT BY 1;

-- Xóa dữ liệu cũ để script có thể chạy lại một cách sạch sẽ
DELETE FROM RECRUITERS WHERE recruiter_id IN (SELECT recruiter_auth_id FROM RECRUITERS_AUTH WHERE username = 'hacnguyet108@gmail.com');
DELETE FROM RECRUITERS_AUTH WHERE username = 'hacnguyet108@gmail.com';

DELETE FROM RECRUITERS WHERE recruiter_id IN (SELECT recruiter_auth_id FROM RECRUITERS_AUTH WHERE username = 'recruiter1');
DELETE FROM RECRUITERS_AUTH WHERE username = 'recruiter1';

DELETE FROM RECRUITERS WHERE recruiter_id IN (SELECT recruiter_auth_id FROM RECRUITERS_AUTH WHERE username = 'recruiter2');
DELETE FROM RECRUITERS_AUTH WHERE username = 'recruiter2';

-- Đảm bảo bảng roles được tạo và có dữ liệu trước khi các bảng khác tham chiếu
-- (Giả định roles.sql được chạy trước hoặc đã có sẵn dữ liệu roles)
-- Nếu không, bạn cần thêm INSERT INTO roles ở đây
-- Ví dụ:
-- INSERT INTO roles (role_id, name) VALUES (1, 'APPLICANT');
-- INSERT INTO roles (role_id, name) VALUES (2, 'RECRUITER');

-- Thêm tài khoản Recruiter: hacnguyet108@gmail.com / 123123
-- **Đảm bảo cả recruiters_auth và recruiters đều được chèn với cùng một ID**
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

-- Đặt lại sequence sau khi chèn thủ công để tránh xung đột ID
-- Next value của sequence sẽ là ID lớn nhất đã chèn + 1
ALTER SEQUENCE RECRUITERS_AUTH_SEQ RESTART WITH 4;

-- Dòng này không còn cần thiết nếu roles đã được quản lý riêng hoặc đã có dữ liệu
-- ALTER TABLE roles MODIFY (role_id NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 7));
    