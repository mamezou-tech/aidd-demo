# タレントマネジメントMVP タスク一覧

**Version**: 1.0.0  
**Created**: 2025-11-16  
**Based on**: implementation-plan.md v1.0.0

## タスク管理方針

- **テストファースト**: すべてのタスクでテストを先に作成
- **小さく分割**: 各タスクは2-4時間以内で完了
- **依存関係明記**: 前提タスクを明確化
- **成果物明記**: 各タスクの完了条件を定義

---

## Phase 1: 基盤構築（1日）

### TASK-001: プロジェクト初期化 ✅
**優先度**: 最高  
**見積**: 2時間  
**依存**: なし  
**ステータス**: DONE

**作業内容**:
- [x] Spring Boot プロジェクト作成（Spring Initializr）
  - Java 17
  - Spring Boot 3.x
  - 依存: Web, JPA, MySQL, Security, Validation, Testcontainers
- [x] React プロジェクト作成（Vite + TypeScript）
- [x] `.gitignore` 設定
- [x] README.md 作成（起動手順記載）

**成果物**:
- `backend/pom.xml` または `build.gradle`
- `frontend/package.json`
- `README.md`

---

### TASK-002: データベーススキーマ作成 ✅
**優先度**: 最高  
**見積**: 2時間  
**依存**: TASK-001  
**ステータス**: DONE

**作業内容**:
- [x] マイグレーションスクリプト作成
  - `V1__create_employee_table.sql`
  - `V2__create_user_table.sql`
- [x] インデックス定義
- [x] 外部キー制約設定

**成果物**:
- `backend/src/main/resources/db/migration/V1__create_employee_table.sql`
- `backend/src/main/resources/db/migration/V2__create_user_table.sql`

**テスト**:
- [x] マイグレーション実行確認
- [x] テーブル作成確認

---

### TASK-003: 初期データ投入スクリプト作成 ✅
**優先度**: 高  
**見積**: 1時間  
**依存**: TASK-002  
**ステータス**: DONE

**作業内容**:
- [x] ユーザーデータ投入（admin/password123）
- [x] 社員テストデータ10件投入

**成果物**:
- `backend/src/main/resources/db/migration/V3__insert_initial_data.sql`

**テスト**:
- [x] データ投入確認
- [ ] ログイン可能確認（後続タスクで）

---

### TASK-004: Testcontainers 設定 ✅
**優先度**: 最高  
**見積**: 2時間  
**依存**: TASK-001  
**ステータス**: DONE

**作業内容**:
- [x] テスト用ベースクラス作成
- [x] MySQL コンテナ設定
- [x] テストプロファイル設定

**成果物**:
- `backend/src/test/java/com/example/talent/TestBase.java`
- `backend/src/test/resources/application-test.yml`

**テスト**:
- [x] コンテナ起動確認
- [x] DB接続確認

---

### TASK-005: Docker Compose 設定 ✅
**優先度**: 中  
**見積**: 1時間  
**依存**: TASK-001  
**ステータス**: DONE

**作業内容**:
- [x] `docker-compose.yml` 作成
  - MySQL サービス
  - Backend サービス（後で追加）
  - Frontend サービス（後で追加）
- [x] 環境変数設定

**成果物**:
- `docker-compose.yml`
- `.env.example`

**テスト**:
- [x] `docker-compose up -d` で起動確認

---

## Phase 2: 認証機能（2日）

### TASK-101: User エンティティ作成（TDD） ✅
**優先度**: 最高  
**見積**: 1時間  
**依存**: TASK-002  
**ステータス**: DONE

**作業内容（テストファースト）**:
1. [x] テスト作成: `UserTest.java`
   - パスワードハッシュ化テスト
   - エンティティ生成テスト
2. [x] 実装: `User.java`
   - `@Entity` 定義
   - BCrypt パスワードハッシュ化メソッド

**成果物**:
- `backend/src/main/java/com/example/talent/domain/User.java`
- `backend/src/test/java/com/example/talent/domain/UserTest.java`

**テスト**:
- [x] すべてのユニットテストがパス

---

### TASK-102: UserRepository 作成（TDD） ✅
**優先度**: 最高  
**見積**: 1時間  
**依存**: TASK-101  
**ステータス**: DONE

**作業内容（テストファースト）**:
1. [x] テスト作成: `UserRepositoryTest.java`
   - username で検索テスト
   - 存在しないユーザー検索テスト
2. [x] 実装: `UserRepository.java`
   - `JpaRepository` 継承
   - `findByUsername` メソッド

**成果物**:
- `backend/src/main/java/com/example/talent/repository/UserRepository.java`
- `backend/src/test/java/com/example/talent/repository/UserRepositoryTest.java`

**テスト**:
- [x] 実DBでテストがパス（Testcontainers使用）

---

### TASK-103: AuthenticationService 作成（TDD） ✅
**優先度**: 最高  
**見積**: 2時間  
**依存**: TASK-102  
**ステータス**: DONE

**作業内容（テストファースト）**:
1. [x] テスト作成: `AuthenticationServiceTest.java`
   - ログイン成功テスト
   - ログイン失敗テスト（パスワード不一致）
   - ログイン失敗テスト（ユーザー存在しない）
2. [x] 実装: `AuthenticationService.java`
   - `login` メソッド
   - パスワード検証ロジック

**成果物**:
- `backend/src/main/java/com/example/talent/application/AuthenticationService.java`
- `backend/src/test/java/com/example/talent/application/AuthenticationServiceTest.java`

**テスト**:
- [x] すべてのユニットテストがパス

---

### TASK-104: AuthController 作成（TDD） ✅
**優先度**: 最高  
**見積**: 2時間  
**依存**: TASK-103  
**ステータス**: DONE

**作業内容（テストファースト）**:
1. [x] テスト作成: `AuthControllerTest.java`
   - POST /api/auth/login 成功テスト
   - POST /api/auth/login 失敗テスト
   - POST /api/auth/logout テスト
2. [x] 実装: `AuthController.java`
   - ログインエンドポイント
   - ログアウトエンドポイント
   - セッション管理

**成果物**:
- `backend/src/main/java/com/example/talent/controller/AuthController.java`
- `backend/src/test/java/com/example/talent/controller/AuthControllerTest.java`

**テスト**:
- [x] MockMvc でテストがパス

---

### TASK-105: Spring Security 設定 ✅
**優先度**: 最高  
**見積**: 2時間  
**依存**: TASK-104  
**ステータス**: DONE

**作業内容**:
- [x] SecurityConfig 作成
  - セッションベース認証設定
  - CSRF 設定
  - 認証不要パス設定（/api/auth/login）
- [x] セッションタイムアウト設定（30分）

**成果物**:
- `backend/src/main/java/com/example/talent/config/SecurityConfig.java`

**テスト**:
- [x] 認証なしでアクセス拒否確認
- [x] ログイン後アクセス可能確認

---

### TASK-106: ログイン画面作成（フロント） ✅
**優先度**: 高  
**見積**: 3時間  
**依存**: TASK-104  
**ステータス**: DONE

**作業内容**:
- [x] LoginPage コンポーネント作成
- [x] authService 作成（API呼び出し）
- [x] フォームバリデーション
- [x] エラーメッセージ表示
- [x] セッション管理（axios interceptor）

**成果物**:
- `frontend/src/pages/LoginPage.tsx`
- `frontend/src/services/authService.ts`
- `frontend/src/types/auth.ts`

**テスト**:
- [x] ログイン成功確認
- [x] ログイン失敗時エラー表示確認

---

## Phase 3: ホーム画面（0.5日）
**見積**: 3時間  
**依存**: TASK-104

**作業内容**:
- [ ] LoginPage コンポーネント作成
- [ ] authService 作成（API呼び出し）
- [ ] フォームバリデーション
- [ ] エラーメッセージ表示
- [ ] セッション管理（axios interceptor）

**成果物**:
- `frontend/src/pages/LoginPage.tsx`
- `frontend/src/services/authService.ts`
- `frontend/src/types/auth.ts`

**テスト**:
- [ ] ログイン成功確認
- [ ] ログイン失敗時エラー表示確認

---

## Phase 3: ホーム画面（0.5日）

### TASK-201: ホーム画面作成（フロント） ✅
**優先度**: 高  
**見積**: 2時間  
**依存**: TASK-106  
**ステータス**: DONE

**作業内容**:
- [x] HomePage コンポーネント作成
- [x] システム名表示
- [x] ユーザー名表示
- [x] 社員検索リンク
- [x] ログアウトボタン

**成果物**:
- `frontend/src/pages/HomePage.tsx`

**テスト**:
- [x] ログイン後ホーム画面表示確認
- [x] ログアウト動作確認

**備考**: TASK-106で同時に実装済み

---

### TASK-202: ルーティング設定（フロント） ✅
**優先度**: 高  
**見積**: 1時間  
**依存**: TASK-201  
**ステータス**: DONE

**作業内容**:
- [x] React Router 設定
- [x] 認証ガード実装
- [x] セッションタイムアウト処理

**成果物**:
- `frontend/src/App.tsx`
- `frontend/src/components/AuthGuard.tsx`

**テスト**:
- [x] 未認証時ログイン画面リダイレクト確認
- [x] 認証後ホーム画面表示確認

**備考**: TASK-106で同時に実装済み

---

## Phase 4: 社員検索機能（3日）

### TASK-301: Employee エンティティ作成（TDD） ✅
**優先度**: 最高  
**見積**: 1時間  
**依存**: TASK-002
**ステータス**: DONE


**作業内容（テストファースト）**:
1. [x] テスト作成: `EmployeeTest.java`
   - エンティティ生成テスト
   - 論理削除テスト
2. [x] 実装: `Employee.java`
   - `@Entity` 定義
   - 全フィールド定義

**成果物**:
- `backend/src/main/java/com/example/talent/domain/Employee.java`
- `backend/src/test/java/com/example/talent/domain/EmployeeTest.java`

**テスト**:
- [ ] すべてのユニットテストがパス

### TASK-302: EmployeeRepository 作成（TDD） ✅

### TASK-302: EmployeeRepository 作成（TDD）
**優先度**: 最高  
**見積**: 3時間  
**依存**: TASK-301

**作業内容（テストファースト）**:
1. [x] テスト作成: `EmployeeRepositoryTest.java`
   - 氏名部分一致検索テスト
   - カナ部分一致検索テスト
   - 複数条件AND検索テスト
   - 削除済み社員除外テスト
   - ページネーションテスト
2. [x] 実装: `EmployeeRepository.java`
   - `JpaRepository` + `JpaSpecificationExecutor` 継承
   - 検索メソッド

**成果物**:
- `backend/src/main/java/com/example/talent/repository/EmployeeRepository.java`
- `backend/src/test/java/com/example/talent/repository/EmployeeRepositoryTest.java`

**テスト**:
- [ ] 実DBでテストがパス（Testcontainers使用）

---

### TASK-303: EmployeeSpecification 作成（TDD）
### TASK-303: EmployeeSpecification 作成（TDD） ✅
**見積**: 2時間  
**依存**: TASK-302

**作業内容（テストファースト）**:
1. [x] テスト作成: `EmployeeSpecificationTest.java`
   - 各検索条件のSpecificationテスト
   - 条件組み合わせテスト
2. [x] 実装: `EmployeeSpecification.java`
   - 氏名検索Specification
   - カナ検索Specification
   - その他検索条件Specification
   - 削除済み除外Specification

**成果物**:
- `backend/src/main/java/com/example/talent/repository/EmployeeSpecification.java`
- `backend/src/test/java/com/example/talent/repository/EmployeeSpecificationTest.java`

**テスト**:
- [ ] すべてのユニットテストがパス

---

### TASK-304: EmployeeSearchService 作成（TDD）
**優先度**: 最高  
**見積**: 2時間  
**依存**: TASK-303

**作業内容（テストファースト）**:
1. [x] テスト作成: `EmployeeSearchServiceTest.java`
   - 検索ロジックテスト
   - ページネーションテスト
   - ソートテスト
2. [x] 実装: `EmployeeSearchService.java`
   - `search` メソッド
   - Specification組み立てロジック

**成果物**:
- `backend/src/main/java/com/example/talent/application/EmployeeSearchService.java`
- `backend/src/test/java/com/example/talent/application/EmployeeSearchServiceTest.java`

**テスト**:
- [ ] すべてのユニットテストがパス

---

### TASK-305: EmployeeController 作成（TDD） ✅
**優先度**: 最高  
**見積**: 2時間  
**依存**: TASK-304

**作業内容（テストファースト）**:
1. [x] テスト作成: `EmployeeControllerTest.java`
   - GET /api/employees テスト（各検索条件）
   - ページネーションテスト
2. [x] 実装: `EmployeeController.java`
   - 検索エンドポイント
   - リクエスト/レスポンス変換

**成果物**:
- `backend/src/main/java/com/example/talent/controller/EmployeeController.java`
- `backend/src/test/java/com/example/talent/controller/EmployeeControllerTest.java`

**テスト**:
- [ ] MockMvc でテストがパス

---

### TASK-306: 社員検索画面作成（フロント） ✅
**優先度**: 高  
**見積**: 4時間  
**依存**: TASK-305

**作業内容**:
- [x] EmployeeSearchPage コンポーネント作成
- [x] EmployeeSearchForm コンポーネント作成
- [x] EmployeeTable コンポーネント作成
- [x] Pagination コンポーネント作成
- [x] employeeService 作成（API呼び出し）

**成果物**:
- `frontend/src/pages/EmployeeSearchPage.tsx`
- `frontend/src/components/EmployeeSearchForm.tsx`
- `frontend/src/components/EmployeeTable.tsx`
- `frontend/src/components/Pagination.tsx`
- `frontend/src/services/employeeService.ts`
- `frontend/src/types/employee.ts`

**テスト**:
- [ ] 検索動作確認
- [ ] ページネーション動作確認
- [ ] 検索結果0件時メッセージ表示確認

---

## Phase 5: 社員詳細表示（1日）

### TASK-401: EmployeeDetailService 作成（TDD） ✅
**優先度**: 高  
**見積**: 1時間  
**依存**: TASK-302

**作業内容（テストファースト）**:
1. [x] テスト作成: `EmployeeDetailServiceTest.java`
   - ID検索テスト
   - 存在しない社員テスト
   - 削除済み社員テスト
2. [x] 実装: `EmployeeDetailService.java`
   - `findById` メソッド

**成果物**:
- `backend/src/main/java/com/example/talent/application/EmployeeDetailService.java`
- `backend/src/test/java/com/example/talent/application/EmployeeDetailServiceTest.java`

**テスト**:
- [x] すべてのユニットテストがパス

---

### TASK-402: EmployeeController 詳細API追加（TDD） ✅
**優先度**: 高  
**見積**: 1時間  
**依存**: TASK-401

**作業内容（テストファースト）**:
1. [x] テスト作成: `EmployeeControllerTest.java` に追加
   - GET /api/employees/{id} テスト
   - 404エラーテスト
2. [x] 実装: `EmployeeController.java` に追加
   - 詳細取得エンドポイント

**成果物**:
- `EmployeeController.java` 更新

**テスト**:
- [x] MockMvc でテストがパス

---

### TASK-403: 社員詳細画面作成（フロント） ✅
**優先度**: 高  
**見積**: 3時間  
**依存**: TASK-402

**作業内容**:
- [x] EmployeeDetailPage コンポーネント作成
- [x] 詳細情報表示
- [x] 検索画面へ戻るボタン
- [x] ホームへ戻るリンク

**成果物**:
- `frontend/src/pages/EmployeeDetailPage.tsx`

**テスト**:
- [ ] 詳細表示確認
- [ ] 検索画面へ戻る動作確認（検索条件クリア）
- [ ] ホームへ戻る動作確認

---

## Phase 6: エラー処理・統合テスト（1.5日）

### TASK-501: エラーハンドリング実装（バックエンド）
**優先度**: 高  
**見積**: 2時間  
**依存**: TASK-402

**作業内容**:
- [ ] GlobalExceptionHandler 作成
- [ ] バリデーションエラー処理
- [ ] システムエラー処理
- [ ] 404エラー処理

**成果物**:
- `backend/src/main/java/com/example/talent/exception/GlobalExceptionHandler.java`

**テスト**:
- [ ] 各エラーケースのテスト

---

### TASK-502: エラー画面作成（フロント）
**優先度**: 中  
**見積**: 2時間  
**依存**: TASK-501

**作業内容**:
- [ ] ErrorPage コンポーネント作成
- [ ] エラーメッセージ表示
- [ ] ホームへ戻るボタン

**成果物**:
- `frontend/src/pages/ErrorPage.tsx`

**テスト**:
- [ ] システムエラー時の表示確認

---

### TASK-503: セッションタイムアウト処理（フロント）
**優先度**: 高  
**見積**: 2時間  
**依存**: TASK-202

**作業内容**:
- [ ] axios interceptor でタイムアウト検知
- [ ] ログイン画面へリダイレクト
- [ ] メッセージ表示

**成果物**:
- `frontend/src/services/axiosConfig.ts` 更新

**テスト**:
- [ ] タイムアウト時の動作確認

---

### TASK-504: E2Eテスト作成
**優先度**: 中  
**見積**: 4時間  
**依存**: TASK-403

**作業内容**:
- [ ] Playwright または Cypress セットアップ
- [ ] ログイン → 検索 → 詳細表示フロー
- [ ] ログイン失敗テスト
- [ ] セッションタイムアウトテスト

**成果物**:
- `e2e/login.spec.ts`
- `e2e/employee-search.spec.ts`

**テスト**:
- [ ] すべてのE2Eテストがパス

---

### TASK-505: Docker Compose 完成
**優先度**: 中  
**見積**: 2時間  
**依存**: TASK-403

**作業内容**:
- [ ] Backend Dockerfile 作成
- [ ] Frontend Dockerfile 作成
- [ ] docker-compose.yml 完成
- [ ] 起動手順ドキュメント更新

**成果物**:
- `backend/Dockerfile`
- `frontend/Dockerfile`
- `docker-compose.yml` 更新
- `README.md` 更新

**テスト**:
- [ ] `docker-compose up -d` で全サービス起動確認
- [ ] ブラウザでアクセス確認

---

### TASK-506: 最終統合テスト
**優先度**: 最高  
**見積**: 2時間  
**依存**: TASK-505

**作業内容**:
- [ ] すべてのユースケース動作確認
- [ ] エラーケース動作確認
- [ ] パフォーマンステスト（簡易）
- [ ] テストカバレッジ確認

**成果物**:
- テスト結果レポート

**完了条件**:
- [ ] すべてのユースケースが動作
- [ ] すべてのテストがパス
- [ ] テストカバレッジ80%以上
- [ ] 仕様書の要件をすべて満たす

---

## タスク依存関係図

```
Phase 1: 基盤構築
TASK-001 (プロジェクト初期化)
  ├─ TASK-002 (DBスキーマ)
  │   └─ TASK-003 (初期データ)
  ├─ TASK-004 (Testcontainers)
  └─ TASK-005 (Docker Compose)

Phase 2: 認証機能
TASK-002 → TASK-101 (User エンティティ)
         → TASK-102 (UserRepository)
         → TASK-103 (AuthenticationService)
         → TASK-104 (AuthController)
         → TASK-105 (Spring Security)
         → TASK-106 (ログイン画面)

Phase 3: ホーム画面
TASK-106 → TASK-201 (ホーム画面)
         → TASK-202 (ルーティング)

Phase 4: 社員検索
TASK-002 → TASK-301 (Employee エンティティ)
         → TASK-302 (EmployeeRepository)
         → TASK-303 (EmployeeSpecification)
         → TASK-304 (EmployeeSearchService)
         → TASK-305 (EmployeeController)
         → TASK-306 (検索画面)

Phase 5: 社員詳細
TASK-302 → TASK-401 (EmployeeDetailService)
         → TASK-402 (詳細API)
         → TASK-403 (詳細画面)

Phase 6: エラー処理・統合
TASK-402 → TASK-501 (エラーハンドリング)
         → TASK-502 (エラー画面)
TASK-202 → TASK-503 (タイムアウト処理)
TASK-403 → TASK-504 (E2Eテスト)
         → TASK-505 (Docker Compose完成)
         → TASK-506 (最終統合テスト)
```

---

## 進捗管理

### タスクステータス
- [ ] TODO: 未着手
- [ ] IN_PROGRESS: 作業中
- [ ] REVIEW: レビュー待ち
- [ ] DONE: 完了

### 見積合計
- Phase 1: 8時間（1日）
- Phase 2: 16時間（2日）
- Phase 3: 3時間（0.5日）
- Phase 4: 24時間（3日）
- Phase 5: 5時間（1日）
- Phase 6: 12時間（1.5日）

**合計**: 68時間（約9日間）

---

## 優先順位

### 最高優先度（クリティカルパス）
1. TASK-001: プロジェクト初期化
2. TASK-002: DBスキーマ
3. TASK-004: Testcontainers
4. TASK-101-105: 認証機能（バックエンド）
5. TASK-301-305: 社員検索（バックエンド）
6. TASK-506: 最終統合テスト

### 高優先度
- TASK-003: 初期データ
- TASK-106: ログイン画面
- TASK-201-202: ホーム画面
- TASK-306: 検索画面
- TASK-401-403: 詳細表示
- TASK-501: エラーハンドリング
- TASK-503: タイムアウト処理

### 中優先度
- TASK-005: Docker Compose
- TASK-502: エラー画面
- TASK-504: E2Eテスト
- TASK-505: Docker Compose完成

---

## 次のアクション

1. **TASK-001** から開始
2. テストファーストを厳守
3. 各タスク完了後、コミット
4. Phase完了後、レビュー実施
