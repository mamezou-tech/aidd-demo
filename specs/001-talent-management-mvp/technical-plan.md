# 社員検索システムMVP 技術プラン

**Version**: 1.0.0  
**Created**: 2025-11-16  
**Based on**: talent-management-mvp.md v1.0.0

## 1. アーキテクチャ概要

### 1.1 システム構成図

```
┌─────────────────────────────────────────────────────────────┐
│                        ユーザー（人事）                        │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ HTTPS
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    フロントエンド (React)                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ ログイン画面  │  │ 検索画面      │  │ 詳細画面      │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │           API Client (Axios + Session)                │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ REST API (JSON)
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                 バックエンド (Spring Boot 3)                  │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │                   Controller 層                       │  │
│  │  AuthController  │  EmployeeController                │  │
│  │  (入出力変換のみ)                                      │  │
│  └──────────────────────────────────────────────────────┘  │
│                              │                               │
│  ┌──────────────────────────────────────────────────────┐  │
│  │                  Application 層                       │  │
│  │  AuthenticationService  │  EmployeeSearchService      │  │
│  │  (ユースケース実装)                                    │  │
│  └──────────────────────────────────────────────────────┘  │
│                              │                               │
│  ┌──────────────────────────────────────────────────────┐  │
│  │                    Domain 層                          │  │
│  │  User  │  Employee  (ドメインモデル)                   │  │
│  └──────────────────────────────────────────────────────┘  │
│                              │                               │
│  ┌──────────────────────────────────────────────────────┐  │
│  │                  Repository 層                        │  │
│  │  UserRepository  │  EmployeeRepository                │  │
│  │  (Spring Data JPA)                                    │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ JDBC
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      MySQL 8.x                               │
│  ┌──────────────┐  ┌──────────────┐                        │
│  │ user テーブル │  │employee テーブル│                       │
│  └──────────────┘  └──────────────┘                        │
└─────────────────────────────────────────────────────────────┘
```

### 1.2 レイヤー責務

#### Controller 層
- **責務**: HTTP リクエスト/レスポンスの変換のみ
- **禁止**: ビジネスロジックの実装
- **実装**: `@RestController`, `@RequestMapping`

#### Application 層
- **責務**: ユースケースの実装、トランザクション管理
- **実装**: `@Service`, `@Transactional`

#### Domain 層
- **責務**: ドメインモデルの定義
- **実装**: `@Entity`, ビジネスルール

#### Repository 層
- **責務**: データアクセス
- **実装**: `JpaRepository`, `JpaSpecificationExecutor`

## 2. データベーススキーマ

### 2.1 ER図

```
┌─────────────────────────────────────┐
│              user                    │
├─────────────────────────────────────┤
│ user_id (PK)         BIGINT         │
│ username             VARCHAR(50)    │
│ password_hash        VARCHAR(255)   │
│ employee_id (FK)     BIGINT         │
│ created_at           TIMESTAMP      │
│ updated_at           TIMESTAMP      │
└─────────────────────────────────────┘
                │
                │ 1:0..1
                ▼
┌─────────────────────────────────────┐
│            employee                  │
├─────────────────────────────────────┤
│ employee_id (PK)     BIGINT         │
│ employee_code        VARCHAR(20)    │
│ full_name            VARCHAR(100)   │
│ full_name_kana       VARCHAR(100)   │
│ email                VARCHAR(100)   │
│ position             VARCHAR(50)    │
│ employment_type      VARCHAR(20)    │
│ hire_date            DATE           │
│ deleted_at           TIMESTAMP      │
│ created_at           TIMESTAMP      │
│ updated_at           TIMESTAMP      │
└─────────────────────────────────────┘
```

### 2.2 テーブル定義

#### employee テーブル
```sql
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
```

#### user テーブル
```sql
CREATE TABLE user (
  user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE COMMENT 'ユーザー名',
  password_hash VARCHAR(255) NOT NULL COMMENT 'パスワードハッシュ（BCrypt）',
  employee_id BIGINT NULL COMMENT '社員ID',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (employee_id) REFERENCES employee(employee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 2.3 初期データ

#### ユーザーデータ
```sql
-- パスワード: password123 (BCrypt)
INSERT INTO user (username, password_hash) 
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
```

#### 社員テストデータ（10件）
```sql
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
```

## 3. 主要API一覧

### 3.1 認証API

#### POST /api/auth/login
**責務**: ユーザー認証、セッション作成

**リクエスト**:
```json
{
  "username": "admin",
  "password": "password123"
}
```

**レスポンス（成功）**:
```json
{
  "username": "admin",
  "sessionId": "..."
}
```
**ステータスコード**: 200 OK

**レスポンス（失敗）**:
```json
{
  "error": "ユーザーIDまたはパスワードが正しくありません。"
}
```
**ステータスコード**: 401 Unauthorized

---

#### POST /api/auth/logout
**責務**: セッション破棄

**レスポンス**:
```json
{
  "message": "ログアウトしました。"
}
```
**ステータスコード**: 200 OK

---

### 3.2 社員API

#### GET /api/employees
**責務**: 社員検索、一覧取得

**クエリパラメータ**:
- `page`: ページ番号（0始まり）
- `size`: 固定20件
- `name`: 氏名（部分一致）
- `nameKana`: 氏名カナ（部分一致）
- `employeeCode`: 社員コード（部分一致）
- `email`: メールアドレス（部分一致）
- `position`: 役職（部分一致）

**レスポンス**:
```json
{
  "content": [
    {
      "employeeId": 1,
      "employeeCode": "E001",
      "fullName": "John Smith",
      "fullNameKana": "ｼﾞｮﾝ ｽﾐｽ",
      "email": "john.smith@example.com",
      "position": "部長"
    }
  ],
  "totalElements": 100,
  "totalPages": 5,
  "currentPage": 0,
  "size": 20
}
```
**ステータスコード**: 200 OK

---

#### GET /api/employees/{id}
**責務**: 社員詳細取得

**レスポンス**:
```json
{
  "employeeId": 1,
  "employeeCode": "E001",
  "fullName": "John Smith",
  "fullNameKana": "ｼﾞｮﾝ ｽﾐｽ",
  "email": "john.smith@example.com",
  "position": "部長",
  "employmentType": "正社員",
  "hireDate": "2020-04-01"
}
```
**ステータスコード**: 200 OK

**レスポンス（存在しない）**:
```json
{
  "error": "社員が見つかりません。"
}
```
**ステータスコード**: 404 Not Found

---

## 4. テスト戦略

### 4.1 テストピラミッド

```
        ┌─────────────┐
        │   E2E (5%)  │  ← フロント含む全体動作
        ├─────────────┤
        │  API (15%)  │  ← Controller + DB
        ├─────────────┤
        │ Unit (80%)  │  ← Service, Repository
        └─────────────┘
```

### 4.2 ユニットテスト

#### 対象
- Application 層（Service）
- Domain 層（エンティティのビジネスロジック）

#### ツール
- JUnit 5
- Mockito（必要最小限）

#### テストケース例
```java
@Test
void 氏名で部分一致検索できる() {
    // Given
    Employee employee = new Employee("E001", "John Smith", ...);
    
    // When
    List<Employee> result = employeeSearchService.search("John", null, null, null, null);
    
    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getFullName()).contains("John");
}
```

---

### 4.3 統合テスト（API + DB）

#### 対象
- Repository 層（実DB使用）
- Controller 層（MockMvc使用）

#### ツール
- Spring Boot Test
- Testcontainers（MySQL）
- MockMvc

#### テストケース例
```java
@SpringBootTest
@Testcontainers
class EmployeeRepositoryTest {
    
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");
    
    @Test
    void 削除済み社員は検索結果に含まれない() {
        // Given
        Employee active = employeeRepository.save(new Employee(...));
        Employee deleted = employeeRepository.save(new Employee(...));
        deleted.setDeletedAt(LocalDateTime.now());
        employeeRepository.save(deleted);
        
        // When
        List<Employee> result = employeeRepository.findActiveEmployees();
        
        // Then
        assertThat(result).containsOnly(active);
    }
}
```

---

### 4.4 E2Eテスト

#### 対象
- 主要ユーザーフロー

#### ツール
- Playwright または Cypress

#### テストシナリオ
1. **正常系フロー**:
   - ログイン → ホーム画面 → 検索 → 詳細表示 → ログアウト

2. **検索機能**:
   - 複数条件でのAND検索
   - ページネーション動作

3. **エラー系**:
   - ログイン失敗
   - セッションタイムアウト

---

## 5. ローカル開発環境（Docker Compose）

### 5.1 コンテナ構成

docker-compose.ymlで3つのモードを提供：

#### デフォルトモード（開発用）
```bash
docker-compose up -d
```
- **起動**: `mysql` のみ
- **用途**: devcontainer内で開発
- **接続**: `localhost:3306`

#### 本番モード
```bash
docker-compose --profile prod up -d
```
- **起動**: `mysql` + `backend` + `frontend`
- **用途**: 本番環境の動作確認
- **接続**: Docker Network経由

#### 開発コンテナモード
```bash
docker-compose --profile dev up -d
```
- **起動**: `devcontainer` + `mysql`
- **用途**: VS Code devcontainer使用時
- **接続**: `localhost:3306`

### 5.2 サービス定義

| サービス | プロファイル | ポート | 説明 |
|---------|-------------|--------|------|
| mysql | (常時) | 3306 | データベース |
| backend | prod | 8080 | Spring Boot API |
| frontend | prod | 80 | React アプリ |
| devcontainer | dev | - | 開発環境 |

### 5.3 ネットワーク設計

#### 開発時
```
devcontainer内プロセス
  └─ jdbc:mysql://localhost:3306 (ポートマッピング)
```

#### 本番時
```
Docker Network: talent-management-network
  backend → mysql:3306
  frontend → backend:8080
```

### 5.4 ボリューム

- `mysql_data`: データベースデータ永続化
- `backend_logs`: アプリケーションログ（本番モード）

---

## 6. 技術選定理由

### 6.1 バックエンド

#### Spring Boot 3
- **理由**: エンタープライズ標準、豊富なエコシステム
- **メリット**: セキュリティ、トランザクション管理が容易

#### Spring Data JPA
- **理由**: ボイラープレートコード削減
- **メリット**: Specification による動的クエリ構築

#### BCrypt
- **理由**: パスワードハッシュ化の標準
- **メリット**: ソルト自動生成、計算コスト調整可能

---

### 6.2 フロントエンド

#### React 18
- **理由**: コンポーネントベース、豊富なエコシステム
- **メリット**: 再利用性、保守性

#### TypeScript
- **理由**: 型安全性
- **メリット**: バグ早期発見、IDE補完

#### Axios
- **理由**: HTTP クライアントの標準
- **メリット**: インターセプター、エラーハンドリング

---

### 6.3 データベース

#### MySQL 8.0
- **理由**: 実績豊富、AWS RDS対応
- **メリット**: トランザクション、インデックス最適化

---

### 6.4 テスト

#### Testcontainers
- **理由**: 実DB使用による高信頼性テスト
- **メリット**: 本番環境に近い状態でテスト可能

---

## 7. セキュリティ考慮事項

### 7.1 認証・認可
- セッションベース認証（Spring Security）
- CSRF トークン
- セッションタイムアウト（30分）

### 7.2 パスワード管理
- BCrypt によるハッシュ化
- 最小8文字のポリシー

### 7.3 通信
- HTTPS 必須（本番環境）
- CORS 設定

### 7.4 SQL インジェクション対策
- JPA による Prepared Statement 自動生成

---

## 8. パフォーマンス考慮事項

### 8.1 データベース
- 検索頻度の高いカラムにインデックス作成
- ページネーションによる大量データ対策

### 8.2 キャッシュ
- MVP では実装しない（将来拡張）

### 8.3 目標値
- 検索レスポンス: 3秒以内（1000件以下）
- 画面遷移: 2秒以内

---

## 9. 監視・ログ

### 9.1 ログ出力
- SLF4J + Logback
- ログレベル: INFO（本番）、DEBUG（開発）

### 9.2 ログ内容
- 認証成功/失敗
- API アクセスログ
- エラーログ（スタックトレース含む）

### 9.3 監視（将来拡張）
- Spring Boot Actuator
- Prometheus + Grafana

---

## 10. デプロイ戦略（将来）

### 10.1 AWS 構成案
```
Internet
   │
   ▼
ALB (HTTPS)
   │
   ├─ ECS (Backend)
   │   └─ Spring Boot Container
   │
   ├─ S3 + CloudFront (Frontend)
   │   └─ React Static Files
   │
   └─ RDS (MySQL)
       └─ Multi-AZ
```

### 10.2 CI/CD
- GitHub Actions
- 自動テスト実行
- Docker イメージビルド
- ECS デプロイ

---

## 11. 開発環境要件

### 11.1 必須ツール
- Java 17
- Node.js 18+
- Docker Desktop
- Git

### 11.2 推奨IDE
- IntelliJ IDEA（バックエンド）
- VS Code（フロントエンド）

### 11.3 ブラウザ
- Chrome（最新版）
- Firefox（最新版）
