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

-- Dòng này đã gây lỗi và cần được loại bỏ hoàn toàn:
-- ALTER SEQUENCE CANDIDATES_AUTH_SEQ RESTART WITH 4;
