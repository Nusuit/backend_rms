-- Xóa các bảng và sequence hiện có để đảm bảo môi trường sạch
-- Thứ tự DROP TABLE quan trọng để tránh lỗi ràng buộc khóa ngoại
DROP TABLE IF EXISTS interviews CASCADE CONSTRAINTS;
DROP TABLE IF EXISTS applications CASCADE CONSTRAINTS;
DROP TABLE IF EXISTS job_skill CASCADE CONSTRAINTS;
DROP TABLE IF EXISTS job_stage CASCADE CONSTRAINTS;
DROP TABLE IF EXISTS schedules CASCADE CONSTRAINTS;
DROP TABLE IF EXISTS jobs CASCADE CONSTRAINTS;
DROP TABLE IF EXISTS recruiters CASCADE CONSTRAINTS;
DROP TABLE IF EXISTS oauth2 CASCADE CONSTRAINTS;
DROP TABLE IF EXISTS candidates CASCADE CONSTRAINTS;
DROP TABLE IF EXISTS recruiters_auth CASCADE CONSTRAINTS;
DROP TABLE IF EXISTS candidates_auth CASCADE CONSTRAINTS;
DROP TABLE IF EXISTS stages CASCADE CONSTRAINTS;
DROP TABLE IF EXISTS recruitment_processes CASCADE CONSTRAINTS;
DROP TABLE IF EXISTS skills CASCADE CONSTRAINTS;
DROP TABLE IF EXISTS roles CASCADE CONSTRAINTS;
DROP TABLE IF EXISTS industries CASCADE CONSTRAINTS; -- Nếu có bảng industries

-- Xóa các sequence được định nghĩa rõ ràng
DROP SEQUENCE IF EXISTS CANDIDATES_AUTH_SEQ;
DROP SEQUENCE IF EXISTS RECRUITERS_AUTH_SEQ;
-- Thêm các DROP SEQUENCE cho các sequence khác nếu có, ví dụ:
-- DROP SEQUENCE IF EXISTS APPLICATIONS_SEQ;
-- DROP SEQUENCE IF EXISTS INTERVIEWS_SEQ;
-- DROP SEQUENCE IF EXISTS JOB_SKILL_SEQ;
-- DROP SEQUENCE IF EXISTS JOB_STAGE_SEQ;
-- DROP SEQUENCE IF EXISTS JOBS_SEQ;
-- DROP SEQUENCE IF EXISTS OAUTH2_SEQ;
-- DROP SEQUENCE IF EXISTS RECRUITMENT_PROCESSES_SEQ;
-- DROP SEQUENCE IF EXISTS ROLES_SEQ;
-- DROP SEQUENCE IF EXISTS SCHEDULES_SEQ;
-- DROP SEQUENCE IF EXISTS SKILLS_SEQ;
-- DROP SEQUENCE IF EXISTS STAGES_SEQ;
-- DROP SEQUENCE IF EXISTS INDUSTRIES_SEQ;

-- Tạo các sequence cần thiết cho các cột sử dụng GenerationType.SEQUENCE
CREATE SEQUENCE CANDIDATES_AUTH_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE RECRUITERS_AUTH_SEQ START WITH 1 INCREMENT BY 1;

-- Tạo các bảng
CREATE TABLE roles (
                       role_id NUMBER(19,0) GENERATED AS IDENTITY,
                       name VARCHAR2(255 CHAR) CHECK (name IN ('APPLICANT','RECRUITER')),
                       PRIMARY KEY (role_id)
);

CREATE TABLE recruiters_auth (
                                 recruiter_auth_id NUMBER(19,0) NOT NULL,
                                 role_id NUMBER(19,0),
                                 password VARCHAR2(255 CHAR),
                                 refresh_token VARCHAR2(500),
                                 status VARCHAR2(255 CHAR) CHECK (status IN ('ACTIVE','BANNED','INACTIVE')),
                                 username VARCHAR2(255 CHAR),
                                 PRIMARY KEY (recruiter_auth_id)
);

CREATE TABLE recruiters (
                            recruiter_id NUMBER(19,0) NOT NULL,
                            description VARCHAR2(255 CHAR),
                            name VARCHAR2(255 CHAR),
                            profile_picture_url VARCHAR2(255 CHAR),
                            PRIMARY KEY (recruiter_id)
);

CREATE TABLE candidates_auth (
                                 otp_request_count INT DEFAULT 0,
                                 otp_type NUMBER(3,0) CHECK (otp_type BETWEEN 0 AND 1),
                                 auth_id NUMBER(19,0) NOT NULL,
                                 created_at TIMESTAMP(6),
                                 latest_otp_window TIMESTAMP(6),
                                 otp_created_at TIMESTAMP(6),
                                 role_id NUMBER(19,0),
                                 updated_at TIMESTAMP(6),
                                 email VARCHAR2(255 CHAR),
                                 otp VARCHAR2(255 CHAR),
                                 password VARCHAR2(255 CHAR),
                                 refresh_token VARCHAR2(500),
                                 status VARCHAR2(255 CHAR) CHECK (status IN ('ACTIVE','BANNED','INACTIVE')),
                                 PRIMARY KEY (auth_id)
);

CREATE TABLE candidates (
                            date_of_birth DATE,
                            candidate_id NUMBER(19,0) NOT NULL,
                            address VARCHAR2(255 CHAR),
                            avatar_url VARCHAR2(255 CHAR),
                            cv_url VARCHAR2(255 CHAR),
                            gender VARCHAR2(255 CHAR) CHECK (gender IN ('MALE','FEMALE')),
                            name VARCHAR2(255 CHAR),
                            phone VARCHAR2(255 CHAR),
                            PRIMARY KEY (candidate_id)
);

CREATE TABLE recruitment_processes (
                                       recruitment_process_id NUMBER(19,0) GENERATED AS IDENTITY,
                                       name VARCHAR2(255 CHAR),
                                       PRIMARY KEY (recruitment_process_id)
);

CREATE TABLE stages (
                        stage_id NUMBER(19,0) GENERATED AS IDENTITY,
                        name VARCHAR2(255 CHAR),
                        stage_order NUMBER(10,0),
                        recruitment_process_id NUMBER(19,0),
                        PRIMARY KEY (stage_id)
);

CREATE TABLE skills (
                        skill_id NUMBER(19,0) GENERATED AS IDENTITY,
                        name VARCHAR2(255 CHAR),
                        PRIMARY KEY (skill_id)
);

CREATE TABLE jobs (
                      deadline DATE,
                      created_at TIMESTAMP(6),
                      id NUMBER(19,0) GENERATED AS IDENTITY,
                      max_salary NUMBER(19,0),
                      min_salary NUMBER(19,0),
                      recruiter_id NUMBER(19,0),
                      recruitment_process_id NUMBER(19,0),
                      updated_at TIMESTAMP(6),
                      benefit VARCHAR2(255 CHAR),
                      description VARCHAR2(255 CHAR),
                      location VARCHAR2(255 CHAR),
                      note VARCHAR2(255 CHAR),
                      requirement VARCHAR2(255 CHAR),
                      salary_currency VARCHAR2(255 CHAR),
                      status VARCHAR2(255 CHAR) CHECK (status IN ('OPEN','CLOSED','CANCELED')),
                      title VARCHAR2(255 CHAR),
                      PRIMARY KEY (id)
);

CREATE TABLE job_skill (
                           required NUMBER(1,0) NOT NULL CHECK (required IN (0,1)),
                           job_id NUMBER(19,0),
                           job_skill_id NUMBER(19,0) GENERATED AS IDENTITY,
                           skill_id NUMBER(19,0),
                           PRIMARY KEY (job_skill_id)
);

CREATE TABLE job_stage (
                           job_stage_order NUMBER(10,0),
                           job_id NUMBER(19,0),
                           job_stage_id NUMBER(19,0) GENERATED AS IDENTITY,
                           stage_id NUMBER(19,0),
                           PRIMARY KEY (job_stage_id)
);

CREATE TABLE schedules (
                           created_at TIMESTAMP(6),
                           job_id NUMBER(19,0),
                           job_stage_id NUMBER(19,0),
                           schedule_id NUMBER(19,0) GENERATED AS IDENTITY,
                           stage_id NUMBER(19,0),
                           start_time TIMESTAMP(6),
                           updated_at TIMESTAMP(6),
                           interviewer_name VARCHAR2(255 CHAR),
                           location VARCHAR2(255 CHAR),
                           name VARCHAR2(255 CHAR),
                           PRIMARY KEY (schedule_id)
);

CREATE TABLE applications (
                              application_id NUMBER(19,0) GENERATED AS IDENTITY,
                              candidate_id NUMBER(19,0),
                              created_at TIMESTAMP(6),
                              job_id NUMBER(19,0),
                              updated_at TIMESTAMP(6),
                              cover_letter VARCHAR2(255 CHAR),
                              cv_url VARCHAR2(255 CHAR),
                              note VARCHAR2(255 CHAR),
                              status VARCHAR2(255 CHAR) CHECK (status IN ('PROGRESS','WITHDRAWN','REJECTED','OFFERED','ACCEPTED','NO_SHOW','JOB_CANCELLED')),
                              PRIMARY KEY (application_id)
);

CREATE TABLE interviews (
                            application_id NUMBER(19,0) NOT NULL,
                            created_date TIMESTAMP(6),
                            interview_id NUMBER(19,0) GENERATED AS IDENTITY,
                            job_stage_id NUMBER(19,0),
                            schedule_id NUMBER(19,0),
                            stage_id NUMBER(19,0),
                            updated_date TIMESTAMP(6),
                            status VARCHAR2(255 CHAR) CHECK (status IN ('PROGRESS','PASSED','FAILED')),
                            PRIMARY KEY (interview_id)
);

CREATE TABLE oauth2 (
                        candidate_auth_id NUMBER(19,0) UNIQUE,
                        code_created_at TIMESTAMP(6),
                        created_at TIMESTAMP(6),
                        id NUMBER(19,0) GENERATED AS IDENTITY,
                        code VARCHAR2(255 CHAR),
                        provider_name VARCHAR2(255 CHAR),
                        provider_user_id VARCHAR2(255 CHAR),
                        PRIMARY KEY (id)
);

CREATE TABLE industries (
                            industry_id NUMBER(19,0) GENERATED AS IDENTITY,
                            name VARCHAR2(255 CHAR),
                            PRIMARY KEY (industry_id)
);


-- Thêm các ràng buộc khóa ngoại
ALTER TABLE recruiters ADD CONSTRAINT FKf68ui2gxl8anngkityh9uaq87 FOREIGN KEY (recruiter_id) REFERENCES recruiters_auth;
ALTER TABLE candidates ADD CONSTRAINT FKtm6cpkq0do4s8jv7j4qk5ueyu FOREIGN KEY (candidate_id) REFERENCES candidates_auth;
ALTER TABLE candidates_auth ADD CONSTRAINT FK2cdd753kdb99ykh23y1tstymn FOREIGN KEY (role_id) REFERENCES roles;
ALTER TABLE recruiters_auth ADD CONSTRAINT FK8vo26d7es48f427uymfqe2j3w FOREIGN KEY (role_id) REFERENCES roles;

ALTER TABLE stages ADD CONSTRAINT FKjyi0n3kxavog717y50mf0vtbn FOREIGN KEY (recruitment_process_id) REFERENCES recruitment_processes;
ALTER TABLE jobs ADD CONSTRAINT FKkcn8apwdb41nuipqsiokk831d FOREIGN KEY (recruitment_process_id) REFERENCES recruitment_processes;
ALTER TABLE jobs ADD CONSTRAINT FKd8fvy5t8ref40fode1t1bku14 FOREIGN KEY (recruiter_id) REFERENCES recruiters;

ALTER TABLE job_skill ADD CONSTRAINT FKje4q8ajxb3v5bel11dhbxrb8d FOREIGN KEY (job_id) REFERENCES jobs;
ALTER TABLE job_skill ADD CONSTRAINT FKdh76859joo68p6dbj9erh4pbs FOREIGN KEY (skill_id) REFERENCES skills;

ALTER TABLE job_stage ADD CONSTRAINT FKnwdvxeq0b8vkwbfcd8rldg8fd FOREIGN KEY (job_id) REFERENCES jobs;
ALTER TABLE job_stage ADD CONSTRAINT FK5niysfcw3mrl19rpwl2rxwe0i FOREIGN KEY (stage_id) REFERENCES stages;

ALTER TABLE schedules ADD CONSTRAINT FKjxtlln4xu9h7o52h4uqm3o9gk FOREIGN KEY (job_id) REFERENCES jobs;
ALTER TABLE schedules ADD CONSTRAINT FKdqr2u0qdu4tea68q36g1iwrr4 FOREIGN KEY (job_stage_id) REFERENCES job_stage;
ALTER TABLE schedules ADD CONSTRAINT FKneih8caoeto12rhvg3sc4j0bm FOREIGN KEY (stage_id) REFERENCES stages;

ALTER TABLE applications ADD CONSTRAINT FKg4e16cwk1qrad923bpx4hamdh FOREIGN KEY (candidate_id) REFERENCES candidates;
ALTER TABLE applications ADD CONSTRAINT FK65weib1lru9dkrbto5pv389vi FOREIGN KEY (job_id) REFERENCES jobs;

ALTER TABLE interviews ADD CONSTRAINT FKok2bail5ls3jjbjgl5c6nt620 FOREIGN KEY (application_id) REFERENCES applications;
ALTER TABLE interviews ADD CONSTRAINT FKc7ppr1arxltyukkl9iij17xq8 FOREIGN KEY (job_stage_id) REFERENCES job_stage;
ALTER TABLE interviews ADD CONSTRAINT FKlsh0neumtam3vmk6pumgvv3rc FOREIGN KEY (schedule_id) REFERENCES schedules;
ALTER TABLE interviews ADD CONSTRAINT FK5dt1w675jgkjv7cnqrv43u5hj FOREIGN KEY (stage_id) REFERENCES stages;

ALTER TABLE oauth2 ADD CONSTRAINT FKewxyn2hujcjwybfjhw3jmfp91 FOREIGN KEY (candidate_auth_id) REFERENCES candidates_auth;
