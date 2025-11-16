# AmazonQ.md（社員検索システムMVP / Amazon Q Developer 運用規範）

## 0. 目的

本書は、Amazon Q Developer が本プロジェクトで遵守すべき具体的行動ルールである。
憲法（Project Constitution）の全条文を、AIが実務で守れるレベルに翻訳し、
「コード生成・設計・タスク実行時の禁止事項・必須事項」を明文化する。

**Amazon Q Developer は、この文書の内容を常に優先して行動しなければならない。**

## 1. プロジェクト概要（AI向け要約）

このプロジェクトは **社員検索システム（MVP）** を構築する。

### MVP範囲の主要機能：

- ログイン
- 社員情報 CRUD（顔写真は表示のみ）
- スキル情報の登録／閲覧
- 組織の階層管理（親子構造のみ）

### MVP除外：

- 権限管理
- 詳細監査ログ
- スキル分析
- 履歴管理

### 技術構成：

- バックエンド：Spring Boot 3 + Java 17
- フロントエンド：React + TypeScript
- DB：MySQL
- 構成管理：Spec-Kit / Amazon Q Developer

## 2. 憲法の原則 → Amazon Q への具体指示

以下は、憲法の各条文を AI が破らず実行できるよう変換した「行動指針」である。

### 2.1 第一条（テストファースト命令） → Qへの指示

Amazon Q Developer は：

1. **あらゆるコード提案より前にテストコードを作成せよ。**
   - 新規機能 → ユニットテストを必ず先に生成
   - 既存改修 → 該当テストの修正を先に生成

2. すべてのテストは "Red → Green → Refactor" の順で成立するよう生成せよ。
3. テストコードの無い機能提案を禁止する。
4. テストの受け入れ基準が曖昧な場合、必ずユーザーに質問せよ。

### 2.2 第二条（統合優先テスト） → Qへの指示

Amazon Q Developer は：

1. **重要機能はすべて MySQL（実DB）で検証可能なテストを生成せよ。**
   - mock に逃げることを禁止する。

2. Repository テストは必ず実 DB（Testcontainers可）を使用せよ。
3. 外部APIやサービスは、可能な限り実インスタンスまたはサンドボックスで検証する方針とせよ。
4. クラス / モジュール間の境界には Contract Test を生成し、実装前に定義せよ。

### 2.3 第三条（ライブラリ優先の原則） → Qへの指示

Amazon Q Developer は：

1. **ビジネスロジックを Controller に書いてはならない。**
   - 必ず下記へ配置せよ：
     ```
     backend/src/main/java/.../domain        ← ドメインモデル（不変）
     backend/src/main/java/.../application   ← ユースケース（アプリケーション層）
     ```

2. すべての機能は "ライブラリ化可能な構造" で実装せよ。
3. UI／REST に依存する形のロジック埋め込みを禁止する。

### 2.4 第四条（インターフェース境界の原則） → Qへの指示

Amazon Q Developer は：

1. ライブラリは UI / フレームワークに依存してはならない。
2. ライブラリ部分の入出力は JSON または構造化データに限定せよ。
3. 公開手段は REST または CLI のいずれかでよい。
4. **Controller の責務は "入出力変換のみ" とする。**
   - ビジネスロジックを Controller に書くことを禁止する。

### 2.5 第五条（抽象化の抑制） → Qへの指示

Amazon Q Developer は：

1. フレームワーク（Spring Boot）を安易にラップするクラスを生成してはならない。
2. "Service層" のような薄い抽象層を自動生成することを禁止する。
3. モデルは可能な限り単一の形で維持し、複数表現が必要な場合は理由を明記せよ。
4. 不必要な Factory / Adapter / Wrapper を禁止する。

### 2.6 第六条（シンプルさのゲート） → Qへの指示

Amazon Q Developer は：

1. 初期実装のプロジェクト数は 3 を超える構成を提案してはならない。
2. 新しいサブプロジェクトやレイヤーを追加する場合、必ず理由をコメントとして明記せよ。
3. YAGNI を遵守し、将来使う予定のない抽象化や拡張ポイントを生成してはならない。
4. 「might need」的な曖昧な機能追加を禁止する。

## 3. フォルダ構成（Qが従うべき固定ルール）

```
backend/
  src/main/java/.../domain
  src/main/java/.../application
  src/main/java/.../controller
  src/test/java/...   ← 必ずJunit5で記述

frontend/
specs/
.specify/
docs/
```

**Amazon Q Developer は、この構造を変更する提案をしてはならない。**

## 4. Spec-Kit × Amazon Q の運用ルール

Amazon Q Developer は、以下のコマンドに基づいて行動しなければならない。

### 4.1 仕様（Spec）の作成・変更
`@speckit.specify`

### 4.2 プラン作成
`@speckit.plan`

### 4.3 タスク分解
`@speckit.tasks`

### 4.4 実装生成
`@speckit.implement`

### 4.5 整合性チェック
`@speckit.analyze`

### 4.6 Spec-Kit コマンドの実行ルール（Q Dev 引数仕様）

Amazon Q Developer は、すべての `@speckit.` コマンドで引数を渡す場合、必ず下記いずれかの形式を使用しなければならない。

#### 許可される形式（いずれか）

**A. 引数を引用符で囲む形式**

```
@speckit.command "text to pass"
```

**B. $ARGUMENTS に明示して渡す形式**

```
$ARGUMENTS="text to pass" @speckit.command
```

#### 禁止事項

- `@speckit.command text` のように引用符なしで引数を続けて書く形式は禁止する。
  （Q Developer の仕様で引数が渡らないため）

## 5. コード生成ルール

Amazon Q Developer は、以下を必ず守る。

### 5.1 Java（バックエンド）

- JUnit5 でテストを書く
- ログは SLF4J
- DTO と Domain Model は分離
- Controller は入出力変換のみ
- 薄いService層は生成禁止

### 5.2 TypeScript（フロントエンド）

- Functional Component
- 冗長な useEffect を禁止
- UIロジックとデータロジックを分離

### 5.3 ドキュメント

- Markdown 形式
- H2=機能 / H3=ユースケース

## 6. Amazon Q Developer の禁止事項

Amazon Q Developer は次の行為をしてはならない。

- 本番接続情報を出力する
- 実在する社名・個人名の使用
- 不要な抽象層（Service/Factory/Adapter）の作成
- フレームワークの過度なラップ
- 仕様にない機能追加
- 複数プロジェクト構成の無断追加
- テストコード無しの実装生成

## 7. 現在の開発フォーカス（随時更新）

- **Feature 002: 社員情報 CRUD + 検索** を最優先とする
- 実装よりもテスト整合性を優先せよ
- パフォーマンスよりまず正しさと境界の明確化を優先する
