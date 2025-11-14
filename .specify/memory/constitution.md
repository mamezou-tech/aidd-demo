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

# 憲法（Project Constitution）

**Version**: 1.0.0
**Ratified**: 2025-11-13
**Last Amended**: 2025-11-13

## 第一章　開発プロセス原則

### 第一条（テストファースト命令）

コードの前に、必ずテストを書かなければならない。

- ユニットテストを先に作成すること
- ユーザーによるテストの検証と承認を得ること
- テストが失敗すること（Red phase）を確認してから実装すること
- 本条は非交渉事項として厳格に適用される

### 第二条（統合優先テスト）

実環境でのテストを優先しなければならない。

- モックではなく実際のデータベースを使用すること
- スタブではなく実際のサービスインスタンスを使用すること
- 実装前にコントラクトテストを必須とする
- 現実的な環境でのテストを優先すること

## 第二章　アーキテクチャ原則

### 第三条（ライブラリ優先の原則）

すべての機能は、独立したライブラリとして実装しなければならない。

- 機能をアプリケーションコードに直接実装してはならない
- 再利用可能なライブラリコンポーネントとして抽象化すること
- 明確な境界と最小限の依存関係を持つモジュール設計とすること

### 第四条（インターフェース境界の原則）

ライブラリは、特定のUIやフレームワークに依存しない独立したインターフェースを持たなければならない。

- ライブラリはREST、CLIなど任意の手段で利用可能であるべき
- UI層からライブラリへ直接ビジネスロジックを書いてはならない
- 境界を越える入出力はJSONまたは同等の構造化データ形式とする

### 第五条（抽象化の抑制）

不要な抽象化レイヤーを作ってはならない。

- フレームワーク機能をラップせず直接使用すること
- 複雑性の各レイヤーを正当化しなければならない
- 単一のモデル表現を維持すること

## 第三章　設計・実装原則

### 第六条（シンプルさのゲート）

過度なエンジニアリングを防がなければならない。

- 初期実装は最大3プロジェクトまでとする
- 追加プロジェクトには文書化された正当化を要する
- 将来の拡張性のための設計を避けること（YAGNIの原則）
- 「might need（必要になるかもしれない）」機能を排除すること
