import { test, expect } from '@playwright/test';

test('ログインテスト', async ({ page }) => {
  // ネットワークリクエストを監視
  page.on('request', request => {
    if (request.url().includes('/api/')) {
      console.log('>>', request.method(), request.url());
    }
  });
  page.on('response', response => {
    if (response.url().includes('/api/')) {
      console.log('<<', response.status(), response.url());
    }
  });
  
  // ログインページへ移動
  await page.goto('/');
  await page.waitForLoadState('domcontentloaded');

  // 入力フィールドが表示されるまで待機（h2は日本語フォント問題でhidden）
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

  // 入力値を確認
  const usernameValue = await usernameInput.inputValue();
  const passwordValue = await passwordInput.inputValue();
  console.log('Username:', usernameValue);
  console.log('Password:', passwordValue);
  
  // ログインボタンをクリック
  await page.locator('button[type="submit"]').click();

  // ナビゲーションを待機
  await page.waitForTimeout(3000);

  console.log('Current URL:', page.url());
});
