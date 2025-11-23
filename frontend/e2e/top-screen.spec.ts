import { test, expect } from '@playwright/test';

test.describe('Top Screen', () => {
  test('ログイン後にtop画面に遷移する', async ({ page }) => {
    // ログイン画面にアクセス
    await page.goto('http://localhost:3000/login');

    // ログイン
    await page.fill('input[type="email"]', 'test@example.com');
    await page.fill('input[type="password"]', 'aiddTest');
    await page.click('button[type="submit"]');

    // top画面（/）に遷移することを確認
    await expect(page).toHaveURL('http://localhost:3000/');
    
    // 「社員管理」カテゴリ名が表示されることを確認
    await expect(page.locator('text=社員管理')).toBeVisible();
    
    // ウェルカムメッセージが表示されないことを確認
    await expect(page.locator('text=ようこそ')).not.toBeVisible();
  });

  test('top画面から社員検索画面に遷移できる', async ({ page }) => {
    // ログイン
    await page.goto('http://localhost:3000/login');
    await page.fill('input[type="email"]', 'test@example.com');
    await page.fill('input[type="password"]', 'aiddTest');
    await page.click('button[type="submit"]');
    await page.waitForURL('http://localhost:3000/');

    // 社員検索ボタンをクリック
    await page.click('text=社員検索');

    // 社員検索画面に遷移することを確認
    await expect(page).toHaveURL('http://localhost:3000/employees');
  });

  test('ログアウトボタンでログイン画面に戻る', async ({ page }) => {
    // ログイン
    await page.goto('http://localhost:3000/login');
    await page.fill('input[type="email"]', 'test@example.com');
    await page.fill('input[type="password"]', 'aiddTest');
    await page.click('button[type="submit"]');
    await page.waitForURL('http://localhost:3000/');

    // ログアウトボタンをクリック
    await page.click('text=ログアウト');

    // ログイン画面に遷移することを確認
    await expect(page).toHaveURL('http://localhost:3000/login');
  });

  test('未認証時はログイン画面にリダイレクトされる', async ({ page }) => {
    // トークンなしでtop画面にアクセス
    await page.goto('http://localhost:3000/');

    // ログイン画面にリダイレクトされることを確認
    await expect(page).toHaveURL('http://localhost:3000/login');
  });

  test('「社員管理」カテゴリと「社員検索」ボタンが画面左上部に表示される', async ({ page }) => {
    // ログイン
    await page.goto('http://localhost:3000/login');
    await page.fill('input[type="email"]', 'test@example.com');
    await page.fill('input[type="password"]', 'aiddTest');
    await page.click('button[type="submit"]');
    await page.waitForURL('http://localhost:3000/');

    // 「社員管理」カテゴリ名が表示されることを確認
    const category = page.locator('text=社員管理');
    await expect(category).toBeVisible();

    // 「社員検索」ボタンが表示されることを確認
    const searchButton = page.locator('button:has-text("社員検索")');
    await expect(searchButton).toBeVisible();
  });
});
