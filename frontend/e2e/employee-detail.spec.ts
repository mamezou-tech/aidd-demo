import { test, expect } from '@playwright/test';

test.describe('社員詳細画面', () => {
  test.beforeEach(async ({ page }) => {
    // ログイン（login.spec.tsと同じ実装）
    await page.goto('/');
    await page.waitForLoadState('domcontentloaded');

    // 入力フィールドが表示されるまで待機
    const usernameInput = page.locator('input[type="text"]');
    const passwordInput = page.locator('input[type="password"]');

    await usernameInput.waitFor({ state: 'visible' });
    await passwordInput.waitFor({ state: 'visible' });

    // React制御コンポーネントのためにネイティブセッターを使用
    await page.evaluate(() => {
      const usernameEl = document.querySelector('input[type="text"]') as HTMLInputElement;
      const passwordEl = document.querySelector('input[type="password"]') as HTMLInputElement;

      // ネイティブのvalue setterを取得
      const nativeSetter = Object.getOwnPropertyDescriptor(
        window.HTMLInputElement.prototype,
        'value'
      )!.set!;

      // ユーザー名を設定
      nativeSetter.call(usernameEl, 'admin');
      usernameEl.dispatchEvent(new Event('input', { bubbles: true }));
      usernameEl.dispatchEvent(new Event('change', { bubbles: true }));

      // パスワードを設定
      nativeSetter.call(passwordEl, 'password123');
      passwordEl.dispatchEvent(new Event('input', { bubbles: true }));
      passwordEl.dispatchEvent(new Event('change', { bubbles: true }));
    });

    // ログインボタンをクリック
    await page.locator('button[type="submit"]').click();

    // ナビゲーションを待機
    await page.waitForTimeout(3000);

    // 社員検索画面へ移動（JavaScriptで直接クリック）
    await page.evaluate(() => {
      const button = Array.from(document.querySelectorAll('button'))
        .find(b => b.textContent?.includes('社員検索'));
      button?.click();
    });
    await page.waitForURL('**/employees', { timeout: 5000 });
    
    // 検索結果が表示されるまで待機
    await page.waitForSelector('button:has-text("詳細")', { timeout: 5000 });
  });

  test('詳細表示確認', async ({ page }) => {
    // 最初の社員の詳細ボタンをクリック
    await page.evaluate(() => {
      const button = Array.from(document.querySelectorAll('button'))
        .find(b => b.textContent?.includes('詳細'));
      button?.click();
    });
    
    // 詳細情報が表示されることを確認
    await page.waitForURL('**/employees/*');
    await expect(page.locator('text=社員コード').first()).toBeAttached();
    await expect(page.locator('th:has-text("氏名")').first()).toBeAttached();
    await expect(page.locator('text=メールアドレス')).toBeAttached();
  });

  test('検索画面へ戻る動作確認', async ({ page }) => {
    // 詳細画面へ移動
    await page.evaluate(() => {
      const button = Array.from(document.querySelectorAll('button'))
        .find(b => b.textContent?.includes('詳細'));
      button?.click();
    });
    await page.waitForURL('**/employees/*');

    // 検索画面へ戻るボタンをクリック（Playwright標準click）
    await page.locator('button:has-text("検索画面へ戻る")').click();
    await page.waitForURL('**/employees');

    // 検索画面に戻ったことを確認
    await expect(page.locator('text=社員検索').first()).toBeAttached();
  });

  test('ホームへ戻る動作確認', async ({ page }) => {
    // 詳細画面へ移動
    await page.evaluate(() => {
      const button = Array.from(document.querySelectorAll('button'))
        .find(b => b.textContent?.includes('詳細'));
      button?.click();
    });
    await page.waitForURL('**/employees/*');

    // ホームへ戻るボタンをクリック（Playwright標準click）
    await page.locator('button:has-text("ホームへ戻る")').click();
    await page.waitForURL('**/home');

    // ホーム画面に戻ったことを確認
    await expect(page.locator('text=タレントマネジメントシステム').first()).toBeAttached();
  });
});
