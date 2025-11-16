-- ユーザーデータ (パスワード: password123)
INSERT INTO user (username, password_hash) 
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

-- 社員テストデータ（10件）
INSERT INTO employee (employee_code, full_name, full_name_kana, email, position, employment_type, hire_date)
VALUES 
  ('E001', 'John Smith', 'ｼﾞｮﾝ ｽﾐｽ', 'john.smith@example.com', '部長', '正社員', '2020-04-01'),
  ('E002', '田中 太郎', 'ﾀﾅｶ ﾀﾛｳ', 'tanaka.taro@example.com', '課長', '正社員', '2018-04-01'),
  ('E003', '佐藤 花子', 'ｻﾄｳ ﾊﾅｺ', 'sato.hanako@example.com', '主任', '正社員', '2019-04-01'),
  ('E004', 'Alice Johnson', 'ｱﾘｽ ｼﾞｮﾝｿﾝ', 'alice.johnson@example.com', NULL, '契約社員', '2021-07-01'),
  ('E005', '鈴木 一郎', 'ｽｽﾞｷ ｲﾁﾛｳ', 'suzuki.ichiro@example.com', '係長', '正社員', '2017-04-01'),
  ('E006', '山田 美咲', 'ﾔﾏﾀﾞ ﾐｻｷ', 'yamada.misaki@example.com', NULL, '派遣社員', '2022-01-15'),
  ('E007', 'Bob Wilson', 'ﾎﾞﾌﾞ ｳｨﾙｿﾝ', 'bob.wilson@example.com', 'マネージャー', '正社員', '2016-04-01'),
  ('E008', '高橋 健太', 'ﾀｶﾊｼ ｹﾝﾀ', 'takahashi.kenta@example.com', NULL, 'パート', '2023-04-01'),
  ('E009', '伊藤 由美', 'ｲﾄｳ ﾕﾐ', 'ito.yumi@example.com', 'リーダー', '正社員', '2019-10-01'),
  ('E010', 'Carol Davis', 'ｷｬﾛﾙ ﾃﾞｲﾋﾞｽ', 'carol.davis@example.com', NULL, 'アルバイト', '2023-08-01');
