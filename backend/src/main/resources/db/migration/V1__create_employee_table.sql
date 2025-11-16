CREATE TABLE employee (
  employee_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  employee_code VARCHAR(20) NOT NULL UNIQUE COMMENT '社員コード',
  full_name VARCHAR(100) NOT NULL COMMENT '氏名（半角スペース区切り）',
  full_name_kana VARCHAR(100) NOT NULL COMMENT '氏名カナ（半角カタカナ）',
  email VARCHAR(100) NOT NULL UNIQUE COMMENT 'メールアドレス',
  position VARCHAR(50) NULL COMMENT '役職',
  employment_type VARCHAR(20) NOT NULL COMMENT '雇用区分',
  hire_date DATE NOT NULL COMMENT '入社日',
  deleted_at TIMESTAMP NULL COMMENT '削除日時（論理削除）',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_full_name (full_name),
  INDEX idx_full_name_kana (full_name_kana),
  INDEX idx_employee_code (employee_code),
  INDEX idx_email (email),
  INDEX idx_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
