# タレントマネジメントシステム MVP

**Version**: 1.0.0  
**Branch**: 001-talent-management-mvp

## 概要

社員の基本情報を検索・閲覧するシステムのMVP実装。

### MVP機能
- ログイン（人事のみ）
- 社員検索（氏名、氏名カナ、社員コード、メールアドレス、役職）
- 社員詳細表示

## 技術スタック

- **Backend**: Java 17, Spring Boot 3, MySQL 8.0
- **Frontend**: React 18, TypeScript
- **Test**: JUnit 5, Testcontainers

## セットアップ

### データベース起動
```bash
docker-compose up -d mysql
```

### バックエンド起動
```bash
cd backend
./gradlew bootRun
```

### フロントエンド起動
```bash
cd frontend
npm install
npm run dev
```

## 初期ログイン
- **ユーザー名**: admin
- **パスワード**: password123

## ドキュメント
- [仕様書](specs/001-talent-management-mvp/spec.md)
- [実装プラン](specs/001-talent-management-mvp/plan.md)
- [技術プラン](specs/001-talent-management-mvp/technical-plan.md)
- [タスク一覧](specs/001-talent-management-mvp/tasks.md)
