-- Database Diagnostic Queries for Job Issues
-- Run these queries to understand what's happening with job creation

-- 1. Check if jobs table exists and its structure
SELECT table_name, column_name, data_type, nullable 
FROM user_tab_columns 
WHERE table_name = 'JOBS' 
ORDER BY column_id;

-- 2. Check total count of jobs in database
SELECT COUNT(*) as total_jobs FROM jobs;

-- 3. Check all jobs with their recruiter information
SELECT 
    j.id,
    j.title,
    j.recruiter_id,
    j.created_at,
    j.status,
    r.email as recruiter_email
FROM jobs j
LEFT JOIN recruiters_auth r ON j.recruiter_id = r.recruiter_auth_id
ORDER BY j.created_at DESC;

-- 4. Check recruiter_auth table to see available recruiters
SELECT 
    recruiter_auth_id,
    email,
    first_name,
    last_name,
    is_verified
FROM recruiters_auth
ORDER BY recruiter_auth_id;

-- 5. Check if there are any jobs created today
SELECT 
    j.id,
    j.title,
    j.recruiter_id,
    j.created_at,
    j.responsibilities
FROM jobs j
WHERE DATE(j.created_at) = DATE(SYSDATE)
ORDER BY j.created_at DESC;

-- 6. Check for any orphaned jobs (jobs without valid recruiter)
SELECT 
    j.id,
    j.title,
    j.recruiter_id,
    j.created_at
FROM jobs j
LEFT JOIN recruiters_auth r ON j.recruiter_id = r.recruiter_auth_id
WHERE r.recruiter_auth_id IS NULL;

-- 7. Check jobs by specific recruiter (replace with actual recruiter_id)
-- SELECT * FROM jobs WHERE recruiter_id = 1;

-- 8. Check the structure of the jobs table
DESCRIBE jobs; 