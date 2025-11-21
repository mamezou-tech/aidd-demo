# aidd-demo

**Amazon Q Developer CLI + spec-kit による仕様駆動開発のデモプロジェクト**

**注意**: このプロジェクトは講演用のデモです。本番環境での使用は想定していません。

このプロジェクトで体験できること：
- 自然言語から仕様書を自動生成
- 仕様から実装タスクを自動分解
- タレントマネジメントシステム（社員検索・スキル管理）の段階的な実装
- Spring Boot + React によるフルスタック開発

---

## 目次

- [前提条件](#前提条件)
- [デモ環境構築手順](#デモ環境構築手順)
- [アプリケーションの起動](#アプリケーションの起動)
- [動作確認](#動作確認)
- [トラブルシューティング](#トラブルシューティング)
- [環境のクリーンアップ](#環境のクリーンアップ)
- [詳細ドキュメント](#詳細ドキュメント)
- [技術スタック](#技術スタック)
- [使用しているOSS](#使用しているoss)
- [開発手法](#開発手法)
- [ライセンス](#ライセンス)

---

## 前提条件

### 想定環境
このプロジェクトは以下の環境で動作を確認しています：
- **OS**: Windows 10/11
- **WSL 2**: インストール済み
  - インストール手順: `wsl --install` （PowerShellを管理者権限で実行）

**注意**: 他のOS（macOS、Linux）での動作確認は行っていません。

### 必須環境
- **Git**: インストール済み
- **Visual Studio Code**: 最新版
- **VS Code拡張機能**: Dev Containers (ms-vscode-remote.remote-containers)
- **AWS Builder ID**: 発行済み
  - Amazon Q Developerを利用するために必要
  - 未取得の場合: https://docs.aws.amazon.com/ja_jp/signin/latest/userguide/sign-in-builder-id.html を参照

### Dev Container起動時に自動インストールされるもの
- **Docker Engine**: Dev Container起動時、「開発コンテナーでは、Dockerを実行する必要があります。WSLにDockerをインストールしますか？」という確認ダイアログが表示されます。インストールを選択すれば自動的にインストールされます。
- **Java 17 + Gradle**: Dev Container起動時に自動インストールされます。
- **Node.js 20 LTS**: Dev Container起動時に自動インストールされます。
- **Amazon Q Developer CLI v1.19.7**: Dev Container起動時に自動インストールされます。
- **spec-kit (specify-cli)**: Dev Container起動時に自動インストールされます。
- **AWS CLI**: Dev Container起動時に自動インストールされます。

---

## デモ環境構築手順

### 1. リポジトリの取得

以下のいずれかの方法でリポジトリを取得してください：

#### 方法A: Fork して使う（推奨）

自分のアカウントで改変・実験したい場合：

1. https://github.com/mamezou-tech/aidd-demo にアクセス
2. 右上の「Fork」ボタンをクリック
3. 自分のForkをクローン：

```powershell
git clone https://github.com/<your-username>/aidd-demo
cd aidd-demo
```

#### 方法B: 直接クローン（読み取り専用）

変更をプッシュしない場合：

```powershell
git clone https://github.com/mamezou-tech/aidd-demo
cd aidd-demo
```

### 2. Dev Containerで開く

Visual Studio Codeで開く：
```powershell
code .
```

コマンドパレット（`Ctrl+Shift+P`）から：
- **Dev Containers: Reopen in Container** を選択

**注意**: Docker Engineがインストールされていない場合、確認ダイアログが表示されます。インストールを選択してください。

初回起動時はコンテナのビルドとツールのインストールに時間がかかります（筆者環境では30分程度）。

---

## アプリケーションの起動

### 1. データベースとバックエンドの起動

Dev Container内のターミナルで：

```bash
docker compose up -d
```

このコマンドで以下が起動します：
- MySQLデータベース（ポート3306）
- Spring Bootアプリケーション（ポート8080）

**確認**: コンテナが起動していることを確認
```bash
docker compose ps
```

期待される出力: `mysqldb`と`app`コンテナのSTATUSが`Up (healthy)`

### 2. フロントエンドの起動

別のターミナルで：

```bash
cd frontend
npm install
npm run dev
```

フロントエンドは `http://localhost:3000` で起動します。

---

## 動作確認

### バックエンドのヘルスチェック

```bash
curl http://localhost:8080/actuator/health
```

または、ブラウザで `http://localhost:8080/actuator/health` にアクセス

**期待される結果**:
```json
{"status":"UP"}
```

### フロントエンドへのアクセス

ブラウザで `http://localhost:3000` にアクセス

**ログイン情報**:
- メールアドレス: `test@example.com`
- パスワード: `password`

### 主な機能

- **社員検索**: 名前、組織、役職、スキルで検索
- **社員詳細**: 社員情報とスキル一覧の表示
- **顔写真表示**: 社員の顔写真（サンプル画像）

---

## トラブルシューティング

### Dev Containerのビルドが失敗する

**症状**: コンテナビルド中にエラー

**確認手順**:
- Docker Engineが起動しているか確認
- WSL 2が正しくインストールされているか確認（`wsl --status`）
- VS Code Dev Containers拡張機能がインストールされているか確認

**対処法**:
- コマンドパレット > **Dev Containers: Rebuild Container** を実行
- WSL 2が未インストールの場合: PowerShellを管理者権限で開き `wsl --install` を実行

### Docker Engineが起動していない

**症状**: `Cannot connect to the Docker daemon`

**確認手順**:
```bash
docker version
```

**対処法**:
- Docker Engineを起動
- Windows環境でDocker Desktopを使用している場合は起動
- WSL 2統合が有効になっているか確認（Docker Desktop > Settings > Resources > WSL Integration）

### コンテナの起動が失敗する

**症状**: `docker compose up -d` でエラー

**確認手順**:
```bash
# ログの確認
docker compose logs

# 特定のサービスのログ
docker compose logs mysqldb
docker compose logs app
```

**対処法**:
```bash
# コンテナを停止してクリーンアップ
docker compose down -v

# 再ビルドして起動
docker compose up -d --build
```

### アプリケーションが起動しない

**症状**: `app`コンテナがヘルスチェックに失敗

**確認手順**:
```bash
# アプリケーションログの確認
docker compose logs app

# MySQLの接続確認
docker compose exec mysqldb mysql -uappuser -papppass appdb -e "SELECT 1"
```

**対処法**:
- MySQLが完全に起動するまで待つ（初回は時間がかかる場合があります）
- `docker compose restart app` でアプリケーションを再起動

### ポート衝突エラー

**症状**: `port is already allocated`

**確認手順**:
```bash
# ホストOS（Windows）で確認
netstat -ano | findstr :8080
netstat -ano | findstr :3306
netstat -ano | findstr :5173
```

**対処法**:
- `docker-compose.yml` のポート設定を変更
- または、ホストOSでポートを使用中のプロセスを終了

### フロントエンドが起動しない

**症状**: `npm run dev` でエラー

**確認手順**:
```bash
cd frontend
npm install
```

**対処法**:
- `node_modules` を削除して再インストール: `rm -rf node_modules && npm install`
- バックエンドが起動しているか確認

---

## 環境のクリーンアップ

```bash
# コンテナの停止と削除（データベースボリュームは保持）
docker compose down

# データベースボリュームも含めて完全削除
docker compose down -v

# フロントエンドのビルド成果物削除
cd frontend
rm -rf node_modules dist test-results
```

---

## 詳細ドキュメント

- **[AmazonQ.md](AmazonQ.md)**: Amazon Q Developer運用規範
- **[.specify/memory/constitution.md](.specify/memory/constitution.md)**: プロジェクトの原則とルール
- **[specs/001-employee-search-system/](specs/001-employee-search-system/)**: 社員検索システムの仕様書

---

## 技術スタック

### バックエンド
- Java 17
- Spring Boot 3.5.7
- Spring Security (JWT認証)
- Spring Data JPA
- MySQL 8.4
- Gradle 8.10.2
- Testcontainers 1.20.4

### フロントエンド
- React 18
- TypeScript 5
- Vite 6
- Playwright (E2Eテスト)

### インフラ
- Docker + Docker Compose V2
- MySQL 8.4

---

## 使用しているOSS

このプロジェクトは以下のオープンソースソフトウェアを使用しています：

### バックエンド

#### フレームワーク・ライブラリ
- **Spring Boot 3.5.7** - Apache License 2.0
  - spring-boot-starter-web
  - spring-boot-starter-security
  - spring-boot-starter-data-jpa
  - spring-boot-starter-actuator
  - spring-boot-starter-validation
- **MySQL Connector/J** - GPL v2 with FOSS Exception
- **Lombok** - MIT License
- **JJWT (Java JWT) 0.12.3** - Apache License 2.0

#### ドキュメント
- **SpringDoc OpenAPI 2.8.4** - Apache License 2.0

#### テスト
- **JUnit 5** - Eclipse Public License 2.0
- **Spring Security Test** - Apache License 2.0
- **Testcontainers 1.20.4** - MIT License

### フロントエンド
- **React 18** - MIT License
- **TypeScript 5** - Apache License 2.0
- **Vite 6** - MIT License
- **Playwright** - Apache License 2.0

### インフラストラクチャ
- **MySQL 8.4** - GPL v2 with FOSS Exception
- **Docker** - Apache License 2.0

### 開発ツール
- **Amazon Q Developer CLI v1.19.7** - MIT License / Apache License 2.0（デュアルライセンス）
- **spec-kit** - MIT License

詳細な依存関係は [build.gradle](build.gradle) と [frontend/package.json](frontend/package.json) を参照してください。

---

## 開発手法

このプロジェクトは**仕様駆動開発 (Specification-Driven Development)** を採用しています。

### 主な特徴

1. **テストファースト**: すべての機能実装前にテストコードを作成
2. **統合優先テスト**: 実データベース（MySQL）を使用した統合テスト
3. **ライブラリ優先の原則**: ビジネスロジックをUIから分離
4. **シンプルさのゲート**: 不要な抽象化を排除

詳細は [AmazonQ.md](AmazonQ.md) を参照してください。

---

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。
詳細は [LICENSE](LICENSE) ファイルを参照してください。

**注記**: このプロジェクトは講演用のデモです。本番環境での使用は想定していません。
