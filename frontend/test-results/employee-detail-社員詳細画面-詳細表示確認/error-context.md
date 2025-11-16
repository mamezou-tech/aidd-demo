# Page snapshot

```yaml
- generic [ref=e3]:
  - heading "タレントマネジメントシステム" [level=1]
  - heading "ログイン" [level=2]
  - generic [ref=e4]: ユーザーIDとパスワードを入力してください。
  - generic [ref=e5]:
    - generic [ref=e6]:
      - generic:
        - text: "ユーザーID:"
        - textbox "ユーザーID:" [ref=e7]
    - generic [ref=e8]:
      - generic:
        - text: "パスワード:"
        - textbox "パスワード:" [ref=e9]
    - button "ログイン" [active] [ref=e10] [cursor=pointer]
```