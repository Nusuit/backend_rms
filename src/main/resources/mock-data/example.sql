INSERT INTO users (user_id, role_id, created_at, updated_at, email, password, refresh_token, status)
VALUES ( 5,2, TIMESTAMP '2025-04-04 23:59:59', TIMESTAMP '2025-04-04 23:59:59',
         'candidate1@example.com',
         '$2a$10$NrwG73Qq.pITKoeYffbPn.pYQtVfPvIh.FLDi0qPdOsfxibV.XkuG',
         NULL, 'ACTIVE'
       );

INSERT INTO candidates (candidate_id, date_of_birth, address, cv_url, gender, name, phone, profile_picture_url, summary)
VALUES (
           5,
           TO_TIMESTAMP('1990-05-15 10:30:00', 'YYYY-MM-DD HH24:MI:SS'),
           '123 Fashion Street, Fashion City, 12345',
           null,
           'MALE',
           'John Doe',
           '1234567890',
           null,
           'A passionate fashion designer with over 5 years of experience in the industry'
       );


INSERT INTO users (user_id, role_id, created_at, updated_at, email, password, refresh_token, status)
VALUES (6, 2, TIMESTAMP '2025-04-04 23:59:59', TIMESTAMP '2025-04-04 23:59:59',
        'candidate2@example.com',
        '$2a$10$NrwG73Qq.pITKoeYffbPn.pYQtVfPvIh.FLDi0qPdOsfxibV.XkuG',
        NULL, 'ACTIVE'
       );

INSERT INTO candidates (candidate_id, date_of_birth, address, cv_url, gender, name, phone, profile_picture_url, summary)
VALUES (
           6,
           TO_TIMESTAMP('1993-02-25 10:30:00', 'YYYY-MM-DD HH24:MI:SS'),
           '456 Fashion Avenue, Fashion City, 54321',
           null,
           'FEMALE',
           'Alice Brown',
           '0987654321',
           null,
           'Creative fashion designer with a passion for sustainable fashion and eco-friendly design.'
       );


INSERT INTO users (user_id, role_id, created_at, updated_at, email, password, refresh_token, status)
VALUES (7, 2, TIMESTAMP '2025-04-04 23:59:59', TIMESTAMP '2025-04-04 23:59:59',
        'candidate3@example.com',
        '$2a$10$NrwG73Qq.pITKoeYffbPn.pYQtVfPvIh.FLDi0qPdOsfxibV.XkuG',
        NULL, 'ACTIVE'
       );


INSERT INTO candidates (candidate_id, date_of_birth, address, cv_url, gender, name, phone, profile_picture_url, summary)
VALUES (
           7,
           TO_TIMESTAMP('1995-11-20 16:45:00', 'YYYY-MM-DD HH24:MI:SS'),
           '789 Design Lane, Fashion City, 67890',
           null,
           'MALE',
           'Michael Clark',
           '1122334455',
           null,
           'Fashion enthusiast with a keen eye for color, design, and innovative patterns.'
       );


INSERT INTO users (user_id, role_id, created_at, updated_at, email, password, refresh_token, status)
VALUES (8, 2, TIMESTAMP '2025-04-04 23:59:59', TIMESTAMP '2025-04-04 23:59:59',
        'candidate4@example.com',
        '$2a$10$NrwG73Qq.pITKoeYffbPn.pYQtVfPvIh.FLDi0qPdOsfxibV.XkuG',
        NULL, 'ACTIVE'
       );

INSERT INTO candidates (candidate_id, date_of_birth, address, cv_url, gender, name, phone, profile_picture_url, summary)
VALUES (
           8,
           TO_TIMESTAMP('1994-03-15 09:30:00', 'YYYY-MM-DD HH24:MI:SS'),
           '123 Tech Street, Hanoi',
           null,
           'MALE',
           'James Wilson',
           '0987654321',
           null,
           'Full-stack developer with 4 years of experience in JavaScript and Python'
       );


INSERT INTO users (user_id, role_id, created_at, updated_at, email, password, refresh_token, status)
VALUES (9, 2, TIMESTAMP '2025-04-04 23:59:59', TIMESTAMP '2025-04-04 23:59:59',
        'candidate5@example.com',
        '$2a$10$NrwG73Qq.pITKoeYffbPn.pYQtVfPvIh.FLDi0qPdOsfxibV.XkuG',
        NULL, 'ACTIVE'
       );

INSERT INTO candidates (candidate_id, date_of_birth, address, cv_url, gender, name, phone, profile_picture_url, summary)
VALUES (
           9,
           TO_TIMESTAMP('1992-07-22 14:20:00', 'YYYY-MM-DD HH24:MI:SS'),
           '456 Marketing Ave, Ho Chi Minh City',
           null,
           'FEMALE',
           'Emma Johnson',
           '0912345678',
           null,
           'Digital marketing specialist with expertise in SEO and social media'
       );


INSERT INTO users (user_id, role_id, created_at, updated_at, email, password, refresh_token, status)
VALUES (10, 2, TIMESTAMP '2025-04-04 23:59:59', TIMESTAMP '2025-04-04 23:59:59',
        'candidate6@example.com',
        '$2a$10$NrwG73Qq.pITKoeYffbPn.pYQtVfPvIh.FLDi0qPdOsfxibV.XkuG',
        NULL, 'ACTIVE'
       );

INSERT INTO candidates (candidate_id, date_of_birth, address, cv_url, gender, name, phone, profile_picture_url, summary)
VALUES (
           10,
           TO_TIMESTAMP('1991-05-10 11:15:00', 'YYYY-MM-DD HH24:MI:SS'),
           '789 Finance Blvd, Da Nang',
           null,
           'MALE',
           'David Brown',
           '0909123456',
           null,
           'Financial analyst with 5 years of experience in corporate finance'
       );


INSERT INTO users (user_id, role_id, created_at, updated_at, email, password, refresh_token, status)
VALUES (11, 2, TIMESTAMP '2025-04-04 23:59:59', TIMESTAMP '2025-04-04 23:59:59',
        'candidate7@example.com',
        '$2a$10$0EpF.0nKjs9mLPiTkDJVJOKGh9FUBRV5QvFz1bMW/HCHdWnXAQRhS',
        NULL, 'ACTIVE'
       );

INSERT INTO candidates (candidate_id, date_of_birth, address, cv_url, gender, name, phone, profile_picture_url, summary)
VALUES (
           11,
           TO_TIMESTAMP('1993-09-18 10:45:00', 'YYYY-MM-DD HH24:MI:SS'),
           '101 Design Road, Hanoi',
           null,
           'FEMALE',
           'Sophia Martinez',
           '0978123456',
           null,
           'UI/UX designer with a passion for creating intuitive user experiences'
       );


INSERT INTO users (user_id, role_id, created_at, updated_at, email, password, refresh_token, status)
VALUES (12, 2, TIMESTAMP '2025-04-04 23:59:59', TIMESTAMP '2025-04-04 23:59:59',
        'candidate8@example.com',
        '$2a$10$0EpF.0nKjs9mLPiTkDJVJOKGh9FUBRV5QvFz1bMW/HCHdWnXAQRhS',
        NULL, 'ACTIVE'
       );

INSERT INTO candidates (candidate_id, date_of_birth, address, cv_url, gender, name, phone, profile_picture_url, summary)
VALUES (
           12,
           TO_TIMESTAMP('1990-12-05 13:30:00', 'YYYY-MM-DD HH24:MI:SS'),
           '202 HR Street, Ho Chi Minh City',
           null,
           'MALE',
           'William Taylor',
           '0987654321',
           null,
           'HR specialist with expertise in talent acquisition and employee relations'
       );


INSERT INTO users (user_id, role_id, created_at, updated_at, email, password, refresh_token, status)
VALUES (13, 2, TIMESTAMP '2025-04-04 23:59:59', TIMESTAMP '2025-04-04 23:59:59',
        'candidate9@example.com',
        '$2a$10$0EpF.0nKjs9mLPiTkDJVJOKGh9FUBRV5QvFz1bMW/HCHdWnXAQRhS',
        NULL, 'ACTIVE'
       );

INSERT INTO candidates (candidate_id, date_of_birth, address, cv_url, gender, name, phone, profile_picture_url, summary)
VALUES (
           13,
           TO_TIMESTAMP('1994-08-25 15:20:00', 'YYYY-MM-DD HH24:MI:SS'),
           '303 Sales Avenue, Da Nang',
           null,
           'FEMALE',
           'Olivia Anderson',
           '0911222333',
           null,
           'Sales manager with 6 years of experience in B2B sales'
       );


INSERT INTO users (user_id, role_id, created_at, updated_at, email, password, refresh_token, status)
VALUES (14, 2, TIMESTAMP '2025-04-04 23:59:59', TIMESTAMP '2025-04-04 23:59:59',
        'candidate10@example.com',
        '$2a$10$0EpF.0nKjs9mLPiTkDJVJOKGh9FUBRV5QvFz1bMW/HCHdWnXAQRhS',
        NULL, 'ACTIVE'
       );

INSERT INTO candidates (candidate_id, date_of_birth, address, cv_url, gender, name, phone, profile_picture_url, summary)
VALUES (
           14,
           TO_TIMESTAMP('1992-04-12 08:45:00', 'YYYY-MM-DD HH24:MI:SS'),
           '404 Logistics Lane, Hanoi',
           null,
           'MALE',
           'Daniel Thomas',
           '0909988776',
           null,
           'Supply chain specialist with expertise in logistics optimization'
       );


INSERT INTO users (user_id, role_id, created_at, updated_at, email, password, refresh_token, status)
VALUES (15, 2, TIMESTAMP '2025-04-04 23:59:59', TIMESTAMP '2025-04-04 23:59:59',
        'candidate11@example.com',
        '$2a$10$0EpF.0nKjs9mLPiTkDJVJOKGh9FUBRV5QvFz1bMW/HCHdWnXAQRhS',
        NULL, 'ACTIVE'
       );

INSERT INTO candidates (candidate_id, date_of_birth, address, cv_url, gender, name, phone, profile_picture_url, summary)
VALUES (
           15,
           TO_TIMESTAMP('1995-01-30 16:10:00', 'YYYY-MM-DD HH24:MI:SS'),
           '505 Education Road, Ho Chi Minh City',
           null,
           'FEMALE',
           'Ava White',
           '0977888999',
           null,
           'Education specialist with experience in curriculum development'
       );


INSERT INTO users (user_id, role_id, created_at, updated_at, email, password, refresh_token, status)
VALUES (16, 2, TIMESTAMP '2025-04-04 23:59:59', TIMESTAMP '2025-04-04 23:59:59',
        'candidate12@example.com',
        '$2a$10$0EpF.0nKjs9mLPiTkDJVJOKGh9FUBRV5QvFz1bMW/HCHdWnXAQRhS',
        NULL, 'ACTIVE'
       );

INSERT INTO candidates (candidate_id, date_of_birth, address, cv_url, gender, name, phone, profile_picture_url, summary)
VALUES (
           16,
           TO_TIMESTAMP('1993-06-08 12:25:00', 'YYYY-MM-DD HH24:MI:SS'),
           '606 Health Street, Da Nang',
           null,
           'MALE',
           'Ethan Lee',
           '0966555444',
           null,
           'Medical representative with 4 years of pharmaceutical sales experience'
       );


INSERT INTO users (user_id, role_id, created_at, updated_at, email, password, refresh_token, status)
VALUES (17, 2, TIMESTAMP '2025-04-04 23:59:59', TIMESTAMP '2025-04-04 23:59:59',
        'candidate13@example.com',
        '$2a$10$0EpF.0nKjs9mLPiTkDJVJOKGh9FUBRV5QvFz1bMW/HCHdWnXAQRhS',
        NULL, 'ACTIVE'
       );

INSERT INTO candidates (candidate_id, date_of_birth, address, cv_url, gender, name, phone, profile_picture_url, summary)
VALUES (
           17,
           TO_TIMESTAMP('1991-11-20 17:50:00', 'YYYY-MM-DD HH24:MI:SS'),
           '707 IT Park, Hanoi',
           null,
           'FEMALE',
           'Mia Harris',
           '0933444555',
           null,
           'Data scientist with expertise in machine learning and predictive modeling'
       );







--------------------------------------------------------


-- Ứng viên John Doe (ID:5) ứng tuyển thêm vào vị trí thứ 2
INSERT INTO applications (candidate_id, job_id, cover_letter, recruiter_note, status)
VALUES (5, 2,
        'I have extensive experience in pattern making and sewing for high-end fashion brands.',
        'Excellent technical skills in garment construction.', 'APPLIED'
       );

-- Ứng viên Alice Brown (ID:6) ứng tuyển vào vị trí thứ 3
INSERT INTO applications (candidate_id, job_id, cover_letter, recruiter_note, status)
VALUES (6, 3,
        'My passion for sustainable fashion aligns perfectly with this role. I have published research on eco-friendly materials.',
        'Strong background in sustainable fashion research.', 'INTERVIEW_QUALIFIED'
       );

-- Ứng viên Michael Clark (ID:7) ứng tuyển vào vị trí thứ 1
INSERT INTO applications (candidate_id, job_id, cover_letter, recruiter_note, status)
VALUES (7, 1,
        'I specialize in color theory and innovative textile applications for modern fashion.',
        'Creative with unique approach to textile design.', 'APPLIED'
       );

-- Ứng viên James Wilson (ID:8) ứng tuyển vào vị trí IT
INSERT INTO applications (candidate_id, job_id, cover_letter, recruiter_note, status)
VALUES (8, 5,
        'As a full-stack developer, I have built numerous web applications using JavaScript and Python frameworks.',
        'Strong portfolio of web projects.', 'APPLIED'
       );

-- Ứng viên Emma Johnson (ID:9) ứng tuyển vào vị trí thứ 3 (marketing liên quan)
INSERT INTO applications (candidate_id, job_id, cover_letter, recruiter_note, status)
VALUES (9, 3,
        'My marketing background gives me unique insight into consumer trends in sustainable fashion.',
        'Interesting crossover skills.', 'APPLIED'
       );

-- Ứng viên Sophia Martinez (ID:11) ứng tuyển vào vị trí IT
INSERT INTO applications (candidate_id, job_id, cover_letter, recruiter_note, status)
VALUES (11, 5,
        'UI/UX design experience combined with front-end development skills makes me an ideal candidate.',
        'Excellent design sensibility.', 'INTERVIEW_QUALIFIED'
       );

-- Ứng viên Mia Harris (ID:17) ứng tuyển vào vị trí AI
INSERT INTO applications (candidate_id, job_id, cover_letter, recruiter_note, status)
VALUES (17, 4,
        'My data science background and machine learning expertise align perfectly with your requirements.',
        'Top candidate with published ML research.', 'ACCEPTED'
       );

-- Ứng viên John Doe (ID:5) ứng tuyển thêm vào vị trí thứ 3
INSERT INTO applications (candidate_id, job_id, cover_letter, recruiter_note, status)
VALUES (5, 3,
        'While my focus has been design, I closely follow global fashion trends and would love to transition into trend analysis.',
        'Potential for growth in this direction.', 'APPLIED'
       );

-- Ứng viên Alice Brown (ID:6) ứng tuyển vào vị trí thứ 2
INSERT INTO applications (candidate_id, job_id, cover_letter, recruiter_note, status)
VALUES (6, 2,
        'I have developed innovative pattern-making techniques that reduce fabric waste by 30%.',
        'Sustainable approach to pattern making.', 'REJECTED'
       );

-- Ứng viên David Brown (ID:10) ứng tuyển vào vị trí database
INSERT INTO applications (candidate_id, job_id, cover_letter, recruiter_note, status)
VALUES (10, 6,
        'Financial analysis experience has given me strong data management skills applicable to database administration.',
        'Unconventional background but strong skills.', 'APPLIED'
       );