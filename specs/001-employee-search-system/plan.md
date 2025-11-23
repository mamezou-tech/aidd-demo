# Implementation Plan: 社員検索システム（MVP）

**Feature**: 001-employee-search-system  
**Created**: 2025-11-17  
**Status**: Draft

## 1. Architecture Overview

### 1.1 System Architecture

```
┌─────────────────┐
│   Browser       │
│  (React + TS)   │
└────────┬────────┘
         │ HTTPS
         │ REST API
┌────────▼────────┐
│  Spring Boot 3  │
│   (Java 17)     │
├─────────────────┤
│   Controller    │ ← 入出力変換のみ
├─────────────────┤
│  Application    │ ← ユースケース
├─────────────────┤
│    Domain       │ ← ドメインモデル
└────────┬────────┘
         │ JDBC
┌────────▼────────┐
│     MySQL 8     │
└─────────────────┘
```

### 1.2 Layer Responsibilities

**Controller層**:
- HTTPリクエスト/レスポンスの変換のみ
- ビジネスロジックを含めない
- 入力バリデーション（形式チェックのみ）

**Application層**:
- ユースケースの実装
- トランザクション境界
- ドメインモデルの組み立て

**Domain層**:
- エンティティ（不変オブジェクト）
- ビジネスルール
- Repository インターフェース

## 2. Technology Stack

### 2.1 Backend

| Component | Technology | Version | Rationale |
|-----------|-----------|---------|-----------|
| Framework | Spring Boot | 3.x | プロジェクト標準 |
| Language | Java | 17 | プロジェクト標準 |
| Database | MySQL | 8.0 | プロジェクト標準 |
| Testing | JUnit 5 | 5.x | テストファースト原則 |
| Test DB | Testcontainers | latest | 実DB統合テスト |
| Security | Spring Security | 6.x | JWT認証 |
| Logging | SLF4J + Logback | latest | プロジェクト標準 |

### 2.2 Frontend

| Component | Technology | Version | Rationale |
|-----------|-----------|---------|-----------|
| Framework | React | 18.x | プロジェクト標準 |
| Language | TypeScript | 5.x | 型安全性 |
| State Management | React Hooks | - | シンプルさ優先 |
| HTTP Client | Fetch API | - | 標準API |
| Styling | CSS Modules | - | スコープ分離 |

### 2.3 Development Tools

- Build: Maven (backend), npm (frontend)
- Version Control: Git
- IDE: 任意（IntelliJ IDEA推奨）

## 3. Database Design

### 3.1 Schema

```sql
-- ユーザー
CREATE TABLE users (
    user_id VARCHAR(20) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 組織
CREATE TABLE organizations (
    organization_id VARCHAR(20) PRIMARY KEY,
    organization_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 社員
CREATE TABLE employees (
    employee_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    organization_id VARCHAR(20) NOT NULL,
    position VARCHAR(50) NOT NULL,
    employment_type VARCHAR(20) NOT NULL,
    hire_date DATE NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    photo_path VARCHAR(500),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (organization_id) REFERENCES organizations(organization_id),
    INDEX idx_name (name),
    INDEX idx_organization (organization_id),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- スキル
CREATE TABLE skills (
    skill_id VARCHAR(20) PRIMARY KEY,
    skill_name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 社員スキル
CREATE TABLE employee_skills (
    employee_id VARCHAR(20),
    skill_id VARCHAR(20),
    level INT NOT NULL CHECK (level BETWEEN 1 AND 5),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (employee_id, skill_id),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id),
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 3.2 Indexes

- `employees.name`: 氏名検索・ソート用
- `employees.organization_id`: 組織検索用
- `employees.deleted`: 削除済み除外用

## 4. API Design

### 4.1 Authentication

#### POST /api/auth/login
**Request**:
```json
{
  "email": "user@example.com",
  "password": "password"
}
```

**Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "userId": "U001",
  "userName": "山田太郎"
}
```

**Response** (401 Unauthorized):
```json
{
  "error": "認証に失敗しました"
}
```

**Note**: ログイン成功後、フロントエンドはtop画面（`/`または`/top`）に遷移する。

### 4.2 Employee Search & List

#### GET /api/employees
**Query Parameters**:
- `name` (optional): 氏名（部分一致）
- `organizationId` (optional): 所属組織ID
- `position` (optional): 役職
- `employmentType` (optional): 雇用区分
- `skillIds` (optional): スキルID（カンマ区切り、AND条件）
- `page` (optional, default=1): ページ番号
- `size` (optional, default=20): 1ページあたりの件数（固定20）

**Response** (200 OK):
```json
{
  "content": [
    {
      "employeeId": "E001",
      "name": "山田太郎",
      "organizationName": "開発部",
      "position": "エンジニア",
      "photoUrl": "/api/employees/E001/photo"
    }
  ],
  "totalElements": 100,
  "totalPages": 5,
  "currentPage": 1,
  "pageSize": 20
}
```

### 4.3 Employee Detail

#### GET /api/employees/{employeeId}
**Response** (200 OK):
```json
{
  "employeeId": "E001",
  "name": "山田太郎",
  "organizationId": "ORG001",
  "organizationName": "開発部",
  "position": "エンジニア",
  "employmentType": "正社員",
  "hireDate": "2020-04-01",
  "email": "yamada@example.com",
  "photoUrl": "/api/employees/E001/photo",
  "skills": [
    {
      "skillId": "SK001",
      "skillName": "Java",
      "level": 4
    }
  ]
}
```

**Response** (404 Not Found):
```json
{
  "error": "社員が見つかりません"
}
```

### 4.4 Photo

#### GET /api/employees/{employeeId}/photo
**Response** (200 OK): 画像バイナリ (image/jpeg)

**Response** (404 Not Found): 404エラー（フロントエンドで「no image」テキストを表示）

**Note**: 顔写真が存在しない場合や読み込み失敗時、フロントエンドは「no image」という文字列を表示する。

### 4.5 Master Data

#### GET /api/organizations
**Response** (200 OK):
```json
{
  "organizations": [
    {
      "organizationId": "ORG001",
      "organizationName": "開発部"
    }
  ]
}
```

#### GET /api/skills
**Response** (200 OK):
```json
{
  "skills": [
    {
      "skillId": "SK001",
      "skillName": "Java",
      "category": "プログラミング言語"
    }
  ]
}
```

## 5. Module Structure

### 5.1 Backend Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/talent/
│   │   │       ├── domain/
│   │   │       │   ├── model/
│   │   │       │   │   ├── Employee.java
│   │   │       │   │   ├── Organization.java
│   │   │       │   │   ├── Skill.java
│   │   │       │   │   ├── EmployeeSkill.java
│   │   │       │   │   └── User.java
│   │   │       │   └── repository/
│   │   │       │       ├── EmployeeRepository.java
│   │   │       │       ├── OrganizationRepository.java
│   │   │       │       ├── SkillRepository.java
│   │   │       │       └── UserRepository.java
│   │   │       ├── application/
│   │   │       │   ├── AuthenticationUseCase.java
│   │   │       │   ├── EmployeeSearchUseCase.java
│   │   │       │   └── EmployeeDetailUseCase.java
│   │   │       └── controller/
│   │   │           ├── AuthController.java
│   │   │           ├── EmployeeController.java
│   │   │           └── MasterDataController.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/migration/
│   │           └── V1__initial_schema.sql
│   └── test/
│       └── java/
│           └── com/example/talent/
│               ├── domain/
│               │   └── repository/
│               │       └── EmployeeRepositoryTest.java
│               ├── application/
│               │   ├── AuthenticationUseCaseTest.java
│               │   └── EmployeeSearchUseCaseTest.java
│               └── controller/
│                   └── EmployeeControllerTest.java
└── pom.xml
```

### 5.2 Frontend Structure

```
frontend/
├── src/
│   ├── components/
│   │   ├── auth/
│   │   │   └── LoginForm.tsx
│   │   ├── employee/
│   │   │   ├── EmployeeList.tsx
│   │   │   ├── EmployeeCard.tsx
│   │   │   ├── EmployeeDetail.tsx
│   │   │   ├── EmployeePhoto.tsx
│   │   │   └── SearchForm.tsx
│   │   └── common/
│   │       ├── Spinner.tsx
│   │       └── Pagination.tsx
│   ├── pages/
│   │   ├── Login.tsx
│   │   ├── Top.tsx
│   │   └── EmployeeSearch.tsx
│   ├── services/
│   │   ├── authService.ts
│   │   ├── employeeService.ts
│   │   └── masterDataService.ts
│   ├── types/
│   │   ├── Employee.ts
│   │   ├── SearchCriteria.ts
│   │   └── Auth.ts
│   ├── utils/
│   │   ├── storage.ts
│   │   └── api.ts
│   ├── App.tsx
│   └── index.tsx
└── package.json
```

## 6. Implementation Phases

### Phase 1: Foundation (P1)
**Goal**: 認証基盤とデータアクセス層の構築

**Tasks**:
1. データベーススキーマ作成
2. Domain Model実装（Entity）
3. Repository実装（実DB統合テスト付き）
4. 認証機能実装（JWT）
5. Top画面実装（ログイン後のホーム画面）
6. 初期データ投入スクリプト

**Acceptance**:
- すべてのRepositoryテストがTestcontainersで実DB検証済み
- JWT認証が動作し、localStorageに保存される
- ログイン成功後にtop画面が表示される
- top画面から社員検索画面に遷移できる
- 初期データ（社員100件、スキル20件、組織10件）が投入可能

### Phase 2: Search & List (P1)
**Goal**: 社員検索・一覧表示機能

**Tasks**:
1. EmployeeSearchUseCase実装
2. EmployeeController実装（検索・一覧）
3. SearchForm実装（React）
4. EmployeeList実装（React）
5. Pagination実装（React）
6. Spinner実装（React）

**Acceptance**:
- スキルAND検索が動作する
- 氏名五十音順ソートが動作する
- ページネーション（20件固定）が動作する
- 検索中にスピナーが表示される
- 0件時に「該当する社員が見つかりません」が表示される

### Phase 3: Detail View (P1)
**Goal**: 社員詳細表示機能

**Tasks**:
1. EmployeeDetailUseCase実装
2. EmployeeController実装（詳細取得）
3. PhotoController実装（画像配信）
4. EmployeeDetail実装（React）
5. 画像表示コンポーネント実装（React、写真なし時は「no image」テキスト表示）

**Acceptance**:
- 社員詳細画面で基本情報・保有スキルが表示される
- 顔写真が表示される（存在しない場合は「no image」テキスト）
- 画像読み込み失敗時に「no image」テキストが表示される

### Phase 4: Integration & Testing (P1)
**Goal**: E2Eテストと統合検証

**Tasks**:
1. E2Eテストシナリオ実装
2. パフォーマンステスト（1000件データ）
3. セキュリティテスト（JWT検証）
4. ブラウザ互換性テスト（Chrome, Edge, Firefox）

**Acceptance**:
- すべてのUser Storyが検証済み
- SC-002, SC-003, SC-004のパフォーマンス基準を満たす
- 削除済み社員が表示されないことを確認

## 7. Testing Strategy

### 7.1 Test Pyramid

```
        ┌─────────────┐
        │   E2E (5%)  │  ← ブラウザ自動化
        ├─────────────┤
        │ Integration │  ← 実DB統合テスト
        │   (30%)     │     (Testcontainers)
        ├─────────────┤
        │    Unit     │  ← ドメインロジック
        │   (65%)     │     テスト
        └─────────────┘
```

### 7.2 Test Coverage Requirements

- Domain層: 100%（ビジネスルール）
- Application層: 90%以上（ユースケース）
- Controller層: 80%以上（入出力変換）
- Repository層: 100%（実DB統合テスト）

### 7.3 Test-First Approach

**すべての実装は以下の順序で行う**:

1. **Red**: テストを先に書く（失敗することを確認）
2. **Green**: 最小限の実装でテストを通す
3. **Refactor**: コードを整理（テストは通ったまま）

**禁止事項**:
- テストなしの実装
- mockに逃げる（Repository層は実DB必須）
- テスト後回し

## 8. Security Considerations

### 8.1 Authentication

- JWT（HS256）でトークン生成
- トークン有効期限: 8時間
- localStorage保存（XSS対策はCSP設定）

### 8.2 Authorization

- MVP時点では認証のみ（全ユーザー同権限）
- 削除済み社員の非表示は必須

### 8.3 Input Validation

- Controller層で形式チェック
- SQLインジェクション対策（PreparedStatement使用）
- XSS対策（React自動エスケープ）

## 9. Performance Targets

| Metric | Target | Measurement |
|--------|--------|-------------|
| 社員一覧表示 | 1秒以内 | 100件データ |
| 社員検索実行 | 2秒以内 | 複数条件 |
| 社員詳細表示 | 1秒以内 | 単一レコード |
| 顔写真表示 | 500ms以内 | 画像ファイル |

## 10. Deployment

### 10.1 Environment

- 単一サーバー構成（MVP）
- Java 17 + MySQL 8.0
- 静的ファイル配信（React build）

### 10.2 Initial Data

- SQL投入スクリプトで初期データ登録
- 顔写真は `/photos/{employeeId}.jpg` に配置

### 10.3 Backup

- 日次バックアップ（MySQL dump）
- 顔写真ディレクトリのバックアップ

## 11. Risks & Mitigations

| Risk | Impact | Mitigation |
|------|--------|-----------|
| 実DB統合テストの実行時間 | 中 | Testcontainersの並列実行、テストデータ最小化 |
| 顔写真ファイルの管理 | 低 | 「no image」テキスト表示、エラーハンドリング |
| 五十音順ソートの実装 | 中 | MySQL照合順序（utf8mb4_ja_0900_as_cs）使用 |
| 1000件データのパフォーマンス | 中 | インデックス最適化、ページネーション |

## 12. Definition of Done

各フェーズ完了の条件:

- [ ] すべてのテストがグリーン（実DB統合テスト含む）
- [ ] テストカバレッジが基準を満たす
- [ ] User Storyの受け入れ基準をすべて満たす
- [ ] コードレビュー完了
- [ ] ドキュメント更新完了
- [ ] パフォーマンス基準を満たす

## 13. Next Steps

1. Phase 1の実装開始
2. データベーススキーマのマイグレーションスクリプト作成
3. Domain Modelの実装（テストファースト）
4. Repository実装（Testcontainers統合テスト）

**Ready for `/speckit.tasks`**
