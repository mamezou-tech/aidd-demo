# 社員検索システムMVP 実装プラン

**Version**: 1.0.0  
**Created**: 2025-11-16  
**Based on**: talent-management-mvp.md v1.0.0

## 1. 実装方針

### 1.1 開発原則
- テストファースト（Red → Green → Refactor）
- 実DB使用（Testcontainers）
- ライブラリ優先（domain/application層にロジック配置）
- Controller は入出力変換のみ
- 不要な抽象化を避ける

### 1.2 プロジェクト構成
```
backend/
  src/main/java/com/example/talent/
    domain/          # ドメインモデル
    application/     # ユースケース
    controller/      # REST API
    repository/      # データアクセス
  src/test/java/    # テストコード

frontend/
  src/
    components/      # React コンポーネント
    pages/           # 画面
    services/        # API 呼び出し
    types/           # TypeScript 型定義
```

## 2. 実装フェーズ

### Phase 1: 基盤構築
**目的**: プロジェクトセットアップとデータベース構築

#### 1.1 プロジェクト初期化
- Spring Boot プロジェクト作成
- React プロジェクト作成
- 依存関係設定

#### 1.2 データベース構築
- テーブル定義（employee, user）
- マイグレーションスクリプト作成
- 初期データ投入スクリプト作成

#### 1.3 テスト環境構築
- Testcontainers 設定
- テストデータ準備

**成果物**:
- `backend/pom.xml` または `build.gradle`
- `frontend/package.json`
- `backend/src/main/resources/db/migration/V1__create_tables.sql`
- `backend/src/main/resources/db/migration/V2__insert_initial_data.sql`

---

### Phase 2: 認証機能
**目的**: ログイン・ログアウト・セッション管理

#### 2.1 バックエンド実装
**テスト先行**:
- `UserRepositoryTest`: ユーザー検索テスト
- `AuthenticationServiceTest`: 認証ロジックテスト
- `AuthControllerTest`: ログインAPIテスト

**実装**:
- `User` エンティティ
- `UserRepository`
- `AuthenticationService`: ログイン・ログアウト処理
- `AuthController`: `/api/auth/login`, `/api/auth/logout`
- セッション管理設定

#### 2.2 フロントエンド実装
**実装**:
- `LoginPage`: ログイン画面
- `authService`: 認証API呼び出し
- セッション管理（axios interceptor）

**成果物**:
- SCR-001: ログイン画面
- UC-001: ログイン機能

---

### Phase 3: ホーム画面
**目的**: ログイン後の基本ナビゲーション

#### 3.1 フロントエンド実装
**実装**:
- `HomePage`: ホーム画面
- ログアウト機能
- ナビゲーション

**成果物**:
- SCR-002: ホーム画面

---

### Phase 4: 社員検索機能
**目的**: 社員情報の検索と一覧表示

#### 4.1 バックエンド実装
**テスト先行**:
- `EmployeeRepositoryTest`: 検索条件テスト（部分一致、AND検索、削除済み除外）
- `EmployeeSearchServiceTest`: 検索ロジックテスト
- `EmployeeControllerTest`: 検索APIテスト

**実装**:
- `Employee` エンティティ
- `EmployeeRepository`: 検索メソッド（JPA Specification使用）
- `EmployeeSearchService`: 検索ロジック
- `EmployeeController`: `GET /api/employees`
- ページネーション処理

#### 4.2 フロントエンド実装
**実装**:
- `EmployeeSearchPage`: 社員検索画面
- `EmployeeSearchForm`: 検索フォーム
- `EmployeeTable`: 検索結果一覧
- `Pagination`: ページネーション
- `employeeService`: 社員API呼び出し

**成果物**:
- SCR-003: 社員検索画面
- UC-002: 社員情報検索機能

---

### Phase 5: 社員詳細表示
**目的**: 社員の詳細情報表示

#### 5.1 バックエンド実装
**テスト先行**:
- `EmployeeRepositoryTest`: ID検索テスト
- `EmployeeDetailServiceTest`: 詳細取得テスト
- `EmployeeControllerTest`: 詳細APIテスト

**実装**:
- `EmployeeDetailService`: 詳細取得ロジック
- `EmployeeController`: `GET /api/employees/{id}`

#### 5.2 フロントエンド実装
**実装**:
- `EmployeeDetailPage`: 社員詳細画面
- 画面遷移（検索画面へ戻る）

**成果物**:
- SCR-004: 社員詳細画面
- UC-003: 社員情報詳細閲覧機能

---

### Phase 6: エラー処理・統合テスト
**目的**: エラーハンドリングと全体動作確認

#### 6.1 エラー処理実装
**実装**:
- バリデーションエラー処理
- システムエラー処理
- セッションタイムアウト処理
- エラー画面

#### 6.2 統合テスト
**テスト**:
- E2Eテスト（ログイン → 検索 → 詳細表示）
- セッションタイムアウトテスト
- エラーケーステスト

**成果物**:
- 完全動作するMVPシステム

---

## 3. 技術詳細

### 3.1 バックエンド技術スタック
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring Security（セッション管理）
- MySQL 8.0
- Testcontainers（テスト用）
- JUnit 5

### 3.2 フロントエンド技術スタック
- React 18
- TypeScript
- React Router
- Axios
- CSS Modules または Tailwind CSS

### 3.3 データベーススキーマ

#### employee テーブル
```sql
CREATE TABLE employee (
  employee_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  employee_code VARCHAR(20) NOT NULL UNIQUE,
  full_name VARCHAR(100) NOT NULL,
  full_name_kana VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  position VARCHAR(50),
  employment_type VARCHAR(20) NOT NULL,
  hire_date DATE NOT NULL,
  deleted_at TIMESTAMP NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### user テーブル
```sql
CREATE TABLE user (
  user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  employee_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (employee_id) REFERENCES employee(employee_id)
);
```

### 3.4 初期データ
```sql
-- ユーザーデータ
INSERT INTO user (username, password_hash) 
VALUES ('admin', '$2a$10$...');  -- BCrypt hash of "password123"

-- 社員データ（10件）
INSERT INTO employee (employee_code, full_name, full_name_kana, email, position, employment_type, hire_date)
VALUES 
  ('E001', 'John Smith', 'ｼﾞｮﾝ ｽﾐｽ', 'john.smith@example.com', '部長', '正社員', '2020-04-01'),
  ('E002', '田中 太郎', 'ﾀﾅｶ ﾀﾛｳ', 'tanaka.taro@example.com', '課長', '正社員', '2018-04-01'),
  -- ... 残り8件
;
```

## 4. テスト戦略

### 4.1 テストレベル
1. **ユニットテスト**: 各クラス・メソッドの単体テスト
2. **統合テスト**: Repository（実DB使用）、Controller（MockMvc使用）
3. **E2Eテスト**: フロントエンド含む全体動作確認

### 4.2 テストカバレッジ目標
- ドメイン層: 100%
- アプリケーション層: 100%
- Controller層: 90%以上

### 4.3 重要テストケース
- 検索条件の組み合わせ（AND検索）
- 部分一致検索（氏名、カナ）
- ページネーション
- 削除済み社員の除外
- セッションタイムアウト
- 認証失敗

## 5. 実装順序（推奨）

1. **Phase 1**: 基盤構築（1日）
2. **Phase 2**: 認証機能（2日）
3. **Phase 3**: ホーム画面（0.5日）
4. **Phase 4**: 社員検索機能（3日）
5. **Phase 5**: 社員詳細表示（1日）
6. **Phase 6**: エラー処理・統合テスト（1.5日）

**合計**: 約9日間

## 6. リスクと対策

### 6.1 技術リスク
- **リスク**: Testcontainers の起動が遅い
- **対策**: テストクラスごとにコンテナを共有

### 6.2 仕様リスク
- **リスク**: 検索条件の複雑さ（部分一致、AND検索）
- **対策**: JPA Specification を使用、テストで十分に検証

### 6.3 スケジュールリスク
- **リスク**: フロントエンド実装の遅延
- **対策**: バックエンドAPIを先行完成、Postmanで動作確認

## 7. 完了条件

- [ ] すべてのユースケースが動作する
- [ ] すべてのテストがパスする
- [ ] 仕様書の要件をすべて満たす
- [ ] エラー処理が適切に動作する
- [ ] セッション管理が正しく動作する
- [ ] 初期データが投入されている
- [ ] README に起動手順が記載されている
