-- Check if jobs exist in the database
SELECT 
    id,
    title,
    recruiter_id,
    status,
    created_at,
    deadline
FROM jobs 
ORDER BY created_at DESC;

-- Check total count
SELECT COUNT(*) as total_jobs FROM jobs;

-- Check recruiters
SELECT 
    recruiter_id,
    name,
    (SELECT COUNT(*) FROM jobs WHERE jobs.recruiter_id = recruiters.recruiter_id) as job_count
FROM recruiters;

-- Check if there are any recruiter_auth records
SELECT recruiter_auth_id, email FROM recruiters_auth; 