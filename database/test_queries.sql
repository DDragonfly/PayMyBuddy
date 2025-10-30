-- virements dont Monica est sender

SELECT 
  t.trans_id,
  u_from.username AS sender,
  u_to.username   AS receiver,
  t.amount,
  t.fee,
  t.description,
  t.created_at
FROM transactions t
JOIN users u_from ON t.sender_id = u_from.users_id
JOIN users u_to   ON t.receiver_id = u_to.users_id
WHERE u_from.username = 'monica.geller';

-- virements avec Chandler comme receiver

SELECT 
  t.trans_id,
  u_from.username AS sender,
  t.amount,
  t.fee,
  t.description
FROM transactions t
JOIN users u_from ON t.sender_id = u_from.users_id
JOIN users u_to   ON t.receiver_id = u_to.users_id
WHERE u_to.username = 'chandler.bing';

-- les amis qui Ted suive

SELECT 
  u2.username AS connection
FROM user_connection uc
JOIN users u1 ON uc.user_id = u1.users_id
JOIN users u2 ON uc.connections_id = u2.users_id
WHERE u1.username = 'ted.mosby';

-- les users qui suivent Rachel

SELECT 
  u1.username AS follower
FROM user_connection uc
JOIN users u1 ON uc.user_id = u1.users_id
JOIN users u2 ON uc.connections_id = u2.users_id
WHERE u2.username = 'rachel.green';

-- virements totals et fees

SELECT 
  u.username,
  COUNT(t.trans_id) AS transactions_sent,
  SUM(t.amount)     AS total_amount_sent,
  SUM(t.fee)        AS total_fee_paid
FROM users u
LEFT JOIN transactions t ON u.users_id = t.sender_id
GROUP BY u.username
ORDER BY total_amount_sent DESC;

-- qui a fait au moins une virement vers un ami

SELECT 
  u_from.username AS sender,
  u_to.username   AS receiver,
  t.amount
FROM transactions t
JOIN users u_from ON t.sender_id = u_from.users_id
JOIN users u_to   ON t.receiver_id = u_to.users_id
WHERE EXISTS (
  SELECT 1
  FROM user_connection uc
  WHERE uc.user_id = t.sender_id
    AND uc.connections_id = t.receiver_id
);
