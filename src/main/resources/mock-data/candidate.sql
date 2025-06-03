-- Xóa các sequence cũ nếu có để tránh lỗi khi chạy lại
-- Sử dụng cách DROP SEQUENCE an toàn hơn để tránh lỗi PLS-00103
DECLARE
seq_exists NUMBER;
BEGIN
SELECT COUNT(*) INTO seq_exists FROM ALL_SEQUENCES WHERE SEQUENCE_NAME = 'CANDIDATES_AUTH_SEQ';
IF seq_exists > 0 THEN
            EXECUTE IMMEDIATE 'DROP SEQUENCE CANDIDATES_AUTH_SEQ';
END IF;
END;
/ -- Ký tự '/' trên một dòng riêng biệt là QUAN TRỌNG cho PL/SQL block

    -- Tạo sequence mới
CREATE SEQUENCE CANDIDATES_AUTH_SEQ START WITH 1 INCREMENT BY 1;

-- Xóa các tài khoản Candidate cũ nếu có để script có thể chạy lại
DELETE FROM CANDIDATES WHERE candidate_id IN (SELECT auth_id FROM CANDIDATES_AUTH WHERE email = 'candidate1@example.com');
DELETE FROM CANDIDATES_AUTH WHERE email = 'candidate1@example.com';

DELETE FROM CANDIDATES WHERE candidate_id IN (SELECT auth_id FROM CANDIDATES_AUTH WHERE email = 'candidate2@example.com');
DELETE FROM CANDIDATES_AUTH WHERE email = 'candidate2@example.com';

DELETE FROM CANDIDATES WHERE candidate_id IN (SELECT auth_id FROM CANDIDATES_AUTH WHERE email = 'candidate3@example.com');
DELETE FROM CANDIDATES_AUTH WHERE email = 'candidate3@example.com';


-- Thêm tài khoản Candidate: candidate1@example.com
INSERT INTO candidates_auth (auth_id, email, password, status, role_id)
VALUES (1, 'candidate1@example.com', -- email
        '$2a$10$z7U3lhyPozc7mKqqfqITSO4JhK8BVh.GkhrLBD60WLoQ3rRYsEqW.',
        'ACTIVE',
        1 -- Đảm bảo role_id 1 tương ứng với APPLICANT
       );
INSERT INTO candidates (candidate_id, name) VALUES (1, 'Candidate One'); -- Thêm tên cho profile

-- Thêm tài khoản Candidate: candidate2@example.com
INSERT INTO candidates_auth (auth_id, email, password, status, role_id)
VALUES (2, 'candidate2@example.com',
        '$2a$10$z7U3lhyPozc7mKqqfqITSO4JhK8BVh.GkhrLBD60WLoQ3rRYsEqW.',
        'ACTIVE',
        1);
INSERT INTO candidates (candidate_id, name) VALUES (2, 'Candidate Two');

-- Thêm tài khoản Candidate: candidate3@example.com
INSERT INTO candidates_auth (auth_id, email, password, status, role_id)
VALUES (3, 'candidate3@example.com',
        '$2a$10$z7U3lhyPozc7mKqqfqITITSO4JhK8BVh.GkhrLBD60WLoQ3rRYsEqW.',
        'ACTIVE',
        1);
INSERT INTO candidates (candidate_id, name) VALUES (3, 'Candidate Three');

-- Đặt lại sequence sau khi chèn thủ công để tránh xung đột ID
ALTER SEQUENCE CANDIDATES_AUTH_SEQ RESTART WITH 4;

-- ALTER TABLE candidates_auth MODIFY (auth_id NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 7));
-- Dòng này không cần thiết nữa.
