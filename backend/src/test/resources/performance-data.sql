-- Insert 100 employees for performance testing (simplified)
INSERT INTO employees (employee_id, name, name_kana, email, organization_id, position, employment_type, hire_date, deleted, created_at, updated_at) VALUES
('PERF0001', 'Performance User 1', 'パフォーマンス ユーザー 1', 'perf1@example.com', 'ORG001', 'エンジニア', '正社員', '2020-01-01', false, NOW(), NOW()),
('PERF0002', 'Performance User 2', 'パフォーマンス ユーザー 2', 'perf2@example.com', 'ORG001', 'エンジニア', '正社員', '2020-01-02', false, NOW(), NOW()),
('PERF0003', 'Performance User 3', 'パフォーマンス ユーザー 3', 'perf3@example.com', 'ORG001', 'エンジニア', '正社員', '2020-01-03', false, NOW(), NOW()),
('PERF0004', 'Performance User 4', 'パフォーマンス ユーザー 4', 'perf4@example.com', 'ORG001', 'エンジニア', '正社員', '2020-01-04', false, NOW(), NOW()),
('PERF0005', 'Performance User 5', 'パフォーマンス ユーザー 5', 'perf5@example.com', 'ORG001', 'エンジニア', '正社員', '2020-01-05', false, NOW(), NOW());
