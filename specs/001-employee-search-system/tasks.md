# 実装タスク: 社員検索システム（MVP）

**機能**: 001-employee-search-system  
**作成日**: 2025-11-17  
**ステータス**: 実装準備完了

## タスク構成

- **優先度**: P0（ブロッカー）、P1（高）、P2（中）
- **見積もり**: ストーリーポイント（1, 2, 3, 5, 8, 13）
- **依存関係**: タスクIDで記載
- **テストファースト**: すべてのタスクで実装前にテスト作成必須

---

## タスク一覧

### タスク 1: データベース基盤構築
**優先度**: P0  
**見積もり**: 5 SP  
**依存関係**: なし  
**担当**: バックエンド

**説明**: データベーススキーマ、初期データ、マイグレーションスクリプトを作成する。

**受け入れ基準**:
- [X] V1__initial_schema.sql を作成（5テーブル、インデックス、外部キー）
- [X] V2__initial_data.sql を作成（ユーザー1件、組織10件、社員100件、スキル20件）
- [X] MySQL 8.0 でマイグレーションが正常に実行される
- [X] 写真ディレクトリ `/photos/` とデフォルトアバター（default.png）を作成

**作成ファイル**:
- `backend/src/main/resources/db/migration/V1__initial_schema.sql`
- `backend/src/main/resources/db/migration/V2__initial_data.sql`
- `backend/src/main/resources/static/photos/default.png`

---

### タスク 2: ドメインモデル実装
**優先度**: P0  
**見積もり**: 8 SP  
**依存関係**: タスク 1  
**担当**: バックエンド

**説明**: すべてのドメインモデル（不変オブジェクト）をテストファーストで実装する。

**テストファースト手順**:
1. 各エンティティのテストを先に作成（Red）
2. 各エンティティを実装（Green）

**受け入れ基準**:
- [X] User, Organization, Skill, Employee, EmployeeSkill の5エンティティを作成
- [X] すべて不変（final フィールド、setterなし）
- [X] EmployeeSkill でレベル検証（1-5）を実装
- [X] SearchCriteria 値オブジェクトを作成
- [X] すべてのテストが通る

**作成ファイル**:
- `backend/src/test/java/com/example/talent/domain/model/*Test.java`（5ファイル）
- `backend/src/main/java/com/example/talent/domain/model/*.java`（6ファイル）

---

### タスク 3: Repository実装（Testcontainers）
**優先度**: P0  
**見積もり**: 13 SP  
**依存関係**: タスク 2  
**担当**: バックエンド

**説明**: すべてのRepositoryを実DBを使った統合テストで実装する。

**テストファースト手順**:
1. Testcontainersですべてのテストを先に作成（Red）
2. Spring Data JDBCで実装（Green）

**受け入れ基準**:
- [X] UserRepository, OrganizationRepository, SkillRepository, EmployeeRepository のインターフェースと実装を作成
- [X] EmployeeRepository で複雑な検索ロジック実装（氏名、組織、スキルAND、削除済み除外、五十音順ソート、ページネーション）
- [X] すべてのテストがTestcontainersで実DB検証済み
- [X] mockを使わない

**作成ファイル**:
- `backend/src/main/java/com/example/talent/domain/repository/*.java`（4インターフェース）
- `backend/src/test/java/com/example/talent/domain/repository/*Test.java`（4テスト）
- `backend/src/main/java/com/example/talent/infrastructure/repository/*Impl.java`（4実装）

---

### タスク 4: 認証機能実装（JWT）
**優先度**: P0  
**見積もり**: 8 SP  
**依存関係**: タスク 3  
**担当**: バックエンド

**説明**: JWT認証機能をテストファーストで実装する。

**テストファースト手順**:
1. AuthenticationUseCaseのテストを先に作成（Red）
2. AuthenticationUseCaseを実装（Green）
3. AuthControllerのテストを作成（Red）
4. AuthControllerを実装（Green）

**受け入れ基準**:
- [X] AuthenticationUseCase を実装（BCryptでパスワード検証、JWT生成、8時間有効期限）
- [X] AuthController を実装（POST /api/auth/login、入出力変換のみ）
- [X] Spring Security + JWTフィルター設定
- [X] /api/auth/login は公開、その他は認証必須
- [X] すべてのテストが通る

**作成ファイル**:
- `backend/src/test/java/com/example/talent/application/AuthenticationUseCaseTest.java`
- `backend/src/main/java/com/example/talent/application/AuthenticationUseCase.java`
- `backend/src/test/java/com/example/talent/controller/AuthControllerTest.java`
- `backend/src/main/java/com/example/talent/controller/AuthController.java`
- `backend/src/main/java/com/example/talent/config/SecurityConfig.java`
- `backend/src/main/java/com/example/talent/config/JwtAuthenticationFilter.java`

**ステータス**: ✅ 完了

---

### タスク 5: 社員検索UseCase実装
**優先度**: P0  
**見積もり**: 8 SP  
**依存関係**: タスク 3  
**担当**: バックエンド

**説明**: 社員検索ユースケースをテストファーストで実装する。

**テストファースト手順**:
1. EmployeeSearchUseCaseのテストを先に作成（Red）
2. EmployeeSearchUseCaseを実装（Green）

**受け入れ基準**:
- [X] EmployeeSearchUseCase を実装
- [X] すべての検索条件をサポート（氏名、組織、役職、雇用区分、スキルAND）
- [X] ページネーション固定20件
- [X] 氏名五十音順ソート
- [X] 削除済み社員を除外
- [X] すべてのテストが通る

**作成ファイル**:
- `backend/src/test/java/com/example/talent/application/EmployeeSearchUseCaseTest.java`
- `backend/src/main/java/com/example/talent/application/EmployeeSearchUseCase.java`

**ステータス**: ✅ 完了

---

### タスク 6: 社員詳細UseCase実装
**優先度**: P0  
**見積もり**: 5 SP  
**依存関係**: タスク 3  
**担当**: バックエンド

**説明**: 社員詳細ユースケースをテストファーストで実装する。

**テストファースト手順**:
1. EmployeeDetailUseCaseのテストを先に作成（Red）
2. EmployeeDetailUseCaseを実装（Green）

**受け入れ基準**:
- [X] EmployeeDetailUseCase を実装
- [X] 社員情報とスキル一覧を返す
- [X] 削除済み/存在しない社員は例外をスロー
- [X] すべてのテストが通る

**作成ファイル**:
- `backend/src/test/java/com/example/talent/application/EmployeeDetailUseCaseTest.java`
- `backend/src/main/java/com/example/talent/application/EmployeeDetailUseCase.java`

**ステータス**: ✅ 完了

---

### タスク 7: REST API実装（Controller層）
**優先度**: P0  
**見積もり**: 8 SP  
**依存関係**: タスク 5, タスク 6  
**担当**: バックエンド

**説明**: すべてのREST APIエンドポイントをテストファーストで実装する。

**テストファースト手順**:
1. 各Controllerのテストを先に作成（Red）
2. 各Controllerを実装（Green）

**受け入れ基準**:
- [X] EmployeeController を実装（GET /api/employees、GET /api/employees/{id}）
- [X] PhotoController を実装（GET /api/employees/{id}/photo、デフォルトアバター対応）
- [X] MasterDataController を実装（GET /api/organizations、GET /api/skills）
- [X] すべてのControllerで入出力変換のみ（ビジネスロジックなし）
- [X] すべてのテストが通る

**作成ファイル**:
- `backend/src/test/java/com/example/talent/controller/EmployeeControllerTest.java`
- `backend/src/main/java/com/example/talent/controller/EmployeeController.java`
- `backend/src/test/java/com/example/talent/controller/PhotoControllerTest.java`
- `backend/src/main/java/com/example/talent/controller/PhotoController.java`
- `backend/src/test/java/com/example/talent/controller/MasterDataControllerTest.java`
- `backend/src/main/java/com/example/talent/controller/MasterDataController.java`

**ステータス**: ✅ 完了

---

### タスク 8: フロントエンド基盤構築
**優先度**: P0  
**見積もり**: 5 SP  
**依存関係**: タスク 4  
**担当**: フロントエンド

**説明**: フロントエンドの基盤（型定義、ユーティリティ、サービス層）を構築する。

**受け入れ基準**:
- [X] TypeScript型定義を作成（Employee, SearchCriteria, Auth, Organization, Skill）
- [X] APIクライアントユーティリティを作成（api.ts、JWT対応）
- [X] localStorageユーティリティを作成（storage.ts）
- [X] サービス層を作成（authService, employeeService, masterDataService）

**作成ファイル**:
- `frontend/src/types/*.ts`（5ファイル）
- `frontend/src/utils/api.ts`
- `frontend/src/utils/storage.ts`
- `frontend/src/services/*.ts`（3ファイル）

**ステータス**: ✅ 完了

---

### タスク 9: 共通コンポーネント実装
**優先度**: P1  
**見積もり**: 5 SP  
**依存関係**: タスク 8  
**担当**: フロントエンド

**説明**: 再利用可能な共通コンポーネントを実装する。

**受け入れ基準**:
- [X] Spinner コンポーネント（ローディング表示）
- [X] Avatar コンポーネント（写真表示、デフォルトアバター対応）
- [X] Pagination コンポーネント（ページネーション、最大5ページ表示）
- [X] すべてFunctional Component
- [X] CSS Modulesでスタイリング

**作成ファイル**:
- `frontend/src/components/common/Spinner.tsx`
- `frontend/src/components/common/Spinner.module.css`
- `frontend/src/components/common/Avatar.tsx`
- `frontend/src/components/common/Avatar.module.css`
- `frontend/src/components/common/Pagination.tsx`
- `frontend/src/components/common/Pagination.module.css`

**ステータス**: ✅ 完了

---

### タスク 10: ログイン画面実装
**優先度**: P0  
**見積もり**: 5 SP  
**依存関係**: タスク 8  
**担当**: フロントエンド

**説明**: ログイン画面を実装する。

**受け入れ基準**:
- [X] LoginForm コンポーネント（メールアドレス、パスワード入力）
- [X] ログイン成功時にlocalStorageにトークン保存
- [X] ログイン成功時に社員一覧画面へ遷移
- [X] ログイン失敗時に「認証に失敗しました」を表示
- [X] ローディング中はスピナー表示
- [X] Functional Component

**作成ファイル**:
- `frontend/src/components/auth/LoginForm.tsx`
- `frontend/src/components/auth/LoginForm.module.css`

**ステータス**: ✅ 完了

---

### タスク 10.1: Top画面実装
**優先度**: P1  
**見積もり**: 3 SP  
**依存関係**: タスク 10  
**担当**: フロントエンド

**説明**: ログイン後のホーム画面（top画面）を実装。社員検索へのナビゲーションを提供。top画面のレイアウトは社員検索画面を踏襲し、画面左上部に「社員管理」カテゴリ名と「社員検索」ボタンを配置する。ウェルカムメッセージは表示しない。

**テストファースト手順**:
1. E2Eテストを先に作成（Red）
   - ログイン後に `/` に遷移することを確認
   - Top画面に「社員管理」カテゴリ名が表示されることを確認
   - 「社員検索」ボタンが画面左上部に表示されることを確認
   - ログアウトボタンが機能することを確認
2. Top.tsx を実装（Green）
3. リファクタリング（Refactor）

**受け入れ基準**:
- [X] E2Eテスト作成（`frontend/e2e/top-screen.spec.ts`）
- [X] Top.tsx 実装（社員検索画面のレイアウトを踏襲）
- [X] 「社員管理」カテゴリ名を画面左上部に表示
- [X] 「社員検索」ボタンを画面左上部に配置
- [X] ウェルカムメッセージを表示しない
- [X] ログアウト機能実装（localStorageトークンクリア + `/login` へ遷移）
- [X] App.tsx のルート修正（`/` → Top画面、認証必須）
- [X] Login.tsx の遷移先を `/employees` から `/` に変更
- [X] 未認証時は `/login` にリダイレクト
- [X] Functional Component
- [X] E2Eテストを更新（レイアウト検証を追加）
- [X] すべてのE2Eテストが通る

**作成ファイル**:
- `frontend/e2e/top-screen.spec.ts` (作成済み・更新済み)
- `frontend/src/pages/Top.tsx` (作成済み・修正済み)
- `frontend/src/pages/Top.module.css` (作成済み・修正済み)
- `frontend/src/App.tsx` (修正済み)
- `frontend/src/pages/Login.tsx` (修正済み)

**ステータス**: ✅ 完了

---

### タスク 10.2: Top画面レイアウト修正
**優先度**: P1  
**見積もり**: 2 SP  
**依存関係**: タスク 10.1  
**担当**: フロントエンド

**説明**: Top画面のレイアウトを仕様に合わせて修正。社員検索画面のレイアウトを踏襲し、ウェルカムメッセージを削除、「社員管理」カテゴリ名と「社員検索」ボタンを画面左上部に配置する。

**テストファースト手順**:
1. E2Eテストを更新（Red）
   - ウェルカムメッセージの検証を削除
   - 「社員管理」カテゴリ名の検証を追加
   - ボタンが左上部に配置されていることを確認
2. Top.tsx とスタイルを修正（Green）
3. リファクタリング（Refactor）

**受け入れ基準**:
- [X] E2Eテスト更新（レイアウト検証）
- [X] ウェルカムメッセージを削除
- [X] 「社員管理」カテゴリ名を画面左上部に表示
- [X] 「社員検索」ボタンを画面左上部に配置
- [X] 社員検索画面のレイアウトスタイルを踏襲
- [X] ヘッダー（タイトル + ログアウトボタン）は維持
- [X] すべてのE2Eテストが通る

**修正ファイル**:
- `frontend/e2e/top-screen.spec.ts` (テスト更新)
- `frontend/src/pages/Top.tsx` (レイアウト修正)
- `frontend/src/pages/Top.module.css` (スタイル修正)

**ステータス**: ✅ 完了

---

### タスク 11: 社員検索・一覧画面実装
**優先度**: P0  
**見積もり**: 13 SP  
**依存関係**: タスク 9  
**担当**: フロントエンド

**説明**: 社員検索フォームと一覧表示を実装する。

**受け入れ基準**:
- [X] SearchForm コンポーネント（氏名、組織、役職、雇用区分、スキル複数選択）
- [X] EmployeeCard コンポーネント（写真サムネイル、氏名、組織、役職）
- [X] EmployeeList コンポーネント（検索フォーム、一覧、ページネーション統合）
- [X] 初期状態は空（検索ボタン押下で全社員表示）
- [X] 検索中はスピナー表示
- [X] 0件時は「該当する社員が見つかりません」を表示
- [X] ページネーション（20件固定）
- [X] すべてFunctional Component

**作成ファイル**:
- `frontend/src/components/employee/SearchForm.tsx`
- `frontend/src/components/employee/SearchForm.module.css`
- `frontend/src/components/employee/EmployeeCard.tsx`
- `frontend/src/components/employee/EmployeeCard.module.css`
- `frontend/src/components/employee/EmployeeList.tsx`
- `frontend/src/components/employee/EmployeeList.module.css`

**ステータス**: ✅ 完了

---

### タスク 11.1: 検索結果表示切り替え機能実装
**優先度**: P1  
**見積もり**: 5 SP  
**依存関係**: タスク 11  
**担当**: フロントエンド

**説明**: 検索結果の表示形式をカード表示と一覧表示で切り替えられる機能を実装。デフォルトは一覧表示。一覧表示の列順序は固定で、左から顔写真、社員ID、氏名、組織、役職、雇用形態の順。表示切り替えはトグルボタンで行い、検索ボタンが配置されているボックス（検索フォーム）の右下部に配置する。

**テストファースト手順**:
1. E2Eテストを先に作成（Red）
   - デフォルトで一覧表示されることを確認
   - トグルボタンが検索フォームの右下部に表示されることを確認
   - トグルボタンで表示が切り替わることを確認
   - 一覧表示の列順序を確認
2. 表示切り替え機能を実装（Green）
3. リファクタリング（Refactor）

**受け入れ基準**:
- [ ] E2Eテスト作成（表示切り替え検証）
- [ ] トグルボタン実装（検索フォームの右下部に配置）
- [ ] トグルボタンデザイン（[一覧|カード]形式）
- [ ] デフォルトで一覧表示
- [ ] 一覧表示コンポーネント実装（テーブル形式）
- [ ] 列順序: 顔写真、社員ID、氏名、組織、役職、雇用形態
- [ ] カード表示は既存のEmployeeCardを使用
- [ ] 表示モードの状態管理（useState）
- [ ] すべてのE2Eテストが通る

**作成ファイル**:
- `frontend/e2e/display-mode.spec.ts` (新規作成)
- `frontend/src/components/employee/EmployeeTable.tsx` (新規作成)
- `frontend/src/components/employee/EmployeeTable.module.css` (新規作成)
- `frontend/src/components/employee/SearchForm.tsx` (修正 - トグルボタン追加)
- `frontend/src/components/employee/EmployeeList.tsx` (修正 - 表示切り替え対応)

**ステータス**: 🔲 未実装

---

### タスク 12: 社員詳細画面実装
**優先度**: P0  
**見積もり**: 8 SP  
**依存関係**: タスク 9  
**担当**: フロントエンド

**説明**: 社員詳細画面を実装する。

**受け入れ基準**:
- [X] EmployeeDetail コンポーネント（大きな写真、基本情報、スキル一覧）
- [X] URLパラメータから社員IDを取得
- [X] ローディング中はスピナー表示
- [X] 社員が見つからない場合はエラーメッセージ
- [X] 写真エラー時はデフォルトアバター表示
- [X] 一覧へ戻るボタン
- [X] Functional Component

**作成ファイル**:
- `frontend/src/components/employee/EmployeeDetail.tsx`
- `frontend/src/components/employee/EmployeeDetail.module.css`

**ステータス**: ✅ 完了

---

### タスク 13: ルーティング設定
**優先度**: P0  
**見積もり**: 3 SP  
**依存関係**: タスク 10, タスク 11, タスク 12  
**担当**: フロントエンド

**説明**: React Routerでルーティングと認証ガードを設定する。

**受け入れ基準**:
- [X] ルート設定（/login、/employees、/employees/:id）
- [X] 認証ガード（未認証時は/loginへリダイレクト）
- [X] ログイン成功時は/employeesへリダイレクト
- [X] App.tsx でルーティング統合

**更新ファイル**:
- `frontend/src/App.tsx`

**ステータス**: ✅ 完了

---

### タスク 14: E2Eテスト実装
**優先度**: P1  
**見積もり**: 8 SP  
**依存関係**: タスク 13  
**担当**: QA/フロントエンド

**説明**: 主要なユーザーフローのE2Eテストを実装する。

**受け入れ基準**:
- [X] ログインフローのE2Eテスト
- [X] 社員検索フローのE2Eテスト（氏名検索、スキルAND検索、ページネーション）
- [X] 社員詳細フローのE2Eテスト（写真表示、デフォルトアバター）
- [X] すべてのテストが通る
- [X] 検索→選択→詳細→戻るの完全フロー検証

**作成ファイル**:
- `frontend/e2e/login.spec.ts` (3テスト)
- `frontend/e2e/employee-search.spec.ts` (3テスト - 包括的検索フロー)
- `frontend/e2e/employee-detail.spec.ts` (1テスト)

**ステータス**: ✅ 完了（全10テスト成功）

---

### タスク 15: パフォーマンス・セキュリティテスト
**優先度**: P1  
**見積もり**: 5 SP  
**依存関係**: タスク 7  
**担当**: バックエンド

**説明**: パフォーマンステストとセキュリティテストを実装する。

**受け入れ基準**:
- [X] 1000件データでのパフォーマンステスト（検索<2秒、一覧<1秒、詳細<1秒）
- [X] JWTセキュリティテスト（有効期限、無効トークン、未認証）
- [X] 削除済み社員除外の統合テスト
- [X] すべてのパフォーマンス基準を満たす
- [X] すべてのセキュリティテストが通る

**作成ファイル**:
- `backend/src/test/resources/performance-data.sql`
- `backend/src/test/java/com/example/talent/performance/EmployeeSearchPerformanceTest.java`
- `backend/src/test/java/com/example/talent/security/JwtSecurityTest.java`
- `backend/src/test/java/com/example/talent/integration/DeletedEmployeeTest.java`

**ステータス**: ✅ 完了

