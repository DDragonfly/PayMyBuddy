-- View to mask sensitive user data (email)
CREATE OR REPLACE VIEW users_masked AS
SELECT
  users_id,
  username,
  CONCAT(LEFT(email,3),'***@',SUBSTRING_INDEX(email,'@',-1)) AS email_masked,
  created_at
FROM users;
