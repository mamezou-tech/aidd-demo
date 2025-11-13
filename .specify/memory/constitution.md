<!--
Sync Impact Report:
- Version: N/A → 1.0.0 (initial creation)
- Modified principles: N/A (initial creation)
- Added sections: All 6 principles
- Removed sections: N/A
- Templates requiring updates:
  ✅ .specify/templates/plan-template.md (to be validated)
  ✅ .specify/templates/spec-template.md (to be validated)
  ✅ .specify/templates/tasks-template.md (to be validated)
- Follow-up TODOs: Validate template alignment with new principles
-->

# Project Constitution

**Version**: 1.0.0
**Ratified**: 2025-11-13
**Last Amended**: 2025-11-13

## Principles

### Principle 1: Library-First Principle（ライブラリ優先の原則）

すべての機能は独立したライブラリとして実装する。

- 機能をアプリケーションコードに直接実装しない
- 再利用可能なライブラリコンポーネントとして抽象化
- 明確な境界と最小限の依存関係を持つモジュール設計

### Principle 2: CLI Interface Mandate（CLIインターフェース義務）

すべてのライブラリはCLIを通じて機能を公開する。

- テキストを入力として受け入れる（stdin、引数、ファイル）
- テキストを出力として生成する（stdout）
- 構造化データ交換のためのJSON形式をサポート

### Principle 3: Test-First Imperative（テストファースト命令）

コードの前に必ずテストを書く。

- ユニットテストを先に作成
- ユーザーによるテストの検証と承認
- テストが失敗すること（Red phase）を確認してから実装
- 非交渉事項として厳格に適用

### Principle 4: Simplicity Gate（シンプルさのゲート）

過度なエンジニアリングを防ぐ。

- 初期実装は最大3プロジェクトまで
- 追加プロジェクトには文書化された正当化が必要
- 将来の拡張性のための設計を避ける
- 「might need」機能を排除

### Principle 5: Anti-Abstraction（抽象化の抑制）

不要な抽象化レイヤーを作らない。

- フレームワーク機能をラップせず直接使用
- 複雑性の各レイヤーを正当化する必要がある
- 単一のモデル表現を維持

### Principle 6: Integration-First Testing（統合優先テスト）

実環境でのテストを優先する。

- モックではなく実際のデータベースを使用
- スタブではなく実際のサービスインスタンスを使用
- 実装前にコントラクトテストが必須
- 現実的な環境でのテストを優先
