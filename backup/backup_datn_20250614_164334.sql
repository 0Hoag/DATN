-- Mock Backup File
-- Created: 2025-06-14T16:43:34.955057700
-- Database: Mock Database

CREATE TABLE IF NOT EXISTS mock_table (
  id INT PRIMARY KEY,
  name VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO mock_table (id, name) VALUES (1, 'Mock Data');
