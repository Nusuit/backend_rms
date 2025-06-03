-- Thêm tài khoản Candidate: candidate1@example.com
INSERT INTO candidates_auth (auth_id, email, password, status, role_id)
VALUES (1, 'candidate1@example.com', -- email
        '$2a$10$z7U3lhyPozc7mKqqfqITSO4JhK8BVh.GkhrLBD60WLoQ3rRYsEqW.',
        'ACTIVE',
        1 -- Đảm bảo role_id 1 tương ứng với APPLICANT
       )
    /
INSERT INTO candidates (candidate_id, name) VALUES (1, 'Candidate One')
    / -- Thêm tên cho profile

-- Thêm tài khoản Candidate: candidate2@example.com
INSERT INTO candidates_auth (auth_id, email, password, status, role_id)
VALUES (2, 'candidate2@example.com',
    '$2a$10$z7U3lhyPozc7mKqqfqITSO4JhK8BVh.GkhrLBD60WLoQ3rRYsEqW.',
    'ACTIVE',
    1)
    /
INSERT INTO candidates (candidate_id, name) VALUES (2, 'Candidate Two')
    /

-- Thêm tài khoản Candidate: candidate3@example.com
INSERT INTO candidates_auth (auth_id, email, password, status, role_id)
VALUES (3, 'candidate3@example.com',
    '$2a$10$z7U3lhyPozc7mKqqfqITITSO4JhK8BVh.GkhrLBD60WLoQ3rRYsEqW.',
    'ACTIVE',
    1)
    /
INSERT INTO candidates (candidate_id, name) VALUES (3, 'Candidate Three')
    /

-- Cập nhật sequence để giá trị tiếp theo lớn hơn ID lớn nhất đã chèn thủ công
-- Đây là cách đúng trong Oracle để đồng bộ hóa sequence với ID lớn nhất trong bảng
DECLARE
v_current_max_id NUMBER;
    v_next_seq_val NUMBER;
    v_increment_by NUMBER;
BEGIN
    -- Lấy ID lớn nhất hiện có trong bảng
SELECT NVL(MAX(auth_id), 0) INTO v_current_max_id FROM candidates_auth;

-- Lấy giá trị tiếp theo của sequence
-- Nếu sequence chưa được sử dụng, NEXTVAL sẽ trả về START WITH value (thường là 1)
SELECT CANDIDATES_AUTH_SEQ.NEXTVAL INTO v_next_seq_val FROM DUAL;

-- Nếu giá trị lớn nhất trong bảng lớn hơn hoặc bằng giá trị tiếp theo của sequence
IF v_current_max_id >= v_next_seq_val THEN
        -- Tính toán giá trị cần tăng để sequence nhảy đến (max_id + 1)
        v_increment_by := v_current_max_id + 1 - v_next_seq_val;

        -- Tạm thời thay đổi INCREMENT BY của sequence
EXECUTE IMMEDIATE 'ALTER SEQUENCE CANDIDATES_AUTH_SEQ INCREMENT BY ' || v_increment_by;

-- Lấy giá trị tiếp theo để "nhảy" sequence đến vị trí mong muốn
SELECT CANDIDATES_AUTH_SEQ.NEXTVAL INTO v_next_seq_val FROM DUAL;

-- Đặt lại INCREMENT BY về giá trị ban đầu (thường là 1)
EXECUTE IMMEDIATE 'ALTER SEQUENCE CANDIDATES_AUTH_SEQ INCREMENT BY 1';
END IF;
END;
/
