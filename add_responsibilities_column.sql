-- Add responsibilities column to jobs table for Oracle database
-- Run this script in your Oracle database

ALTER TABLE jobs 
ADD responsibilities VARCHAR2(1000);

-- Optional: Add a comment to document the column
COMMENT ON COLUMN jobs.responsibilities IS 'Job responsibilities and duties description';

-- Verify the column was added successfully
-- You can run this query to check:
-- SELECT column_name, data_type, data_length FROM user_tab_columns WHERE table_name = 'JOBS' AND column_name = 'RESPONSIBILITIES'; 