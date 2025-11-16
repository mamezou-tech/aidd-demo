import { test, expect } from '@playwright/test';

test.describe('社員詳細画面', () => {
  test.beforeEach(async ({ page }) => {
    // ログイン
    await page.goto('/');
    await page.waitForSelector('input[type="text"]');
    await page.fill('input[type="text"]', 'admin');
    await page.fill('input[type="password"]', 'password123');
    await page.click('button[type="submit"]');
    
    // ログイン成功を待機（URLが変わるまで）
    await page.waitForTimeout(2000);
    
    // 社員検索画面へ移動
    await page.click('button:has-text("社員検索")');
    await page.waitForTimeout(1000);
  });

  test('詳細表示確認', async ({ page }) => {
    // 最初の社員の詳細ボタンをクリック
    await page.click('button:has-text("詳細")');
    
    // 詳細情報が表示されることを確認
    await expect(page.locator('h1')).toContainText('社員詳細');
    await expect(page.locator('text=社員コード')).toBeVisible();
    await expect(page.locator('text=氏名')).toBeVisible();
    await expect(page.locator('text=メールアドレス')).toBeVisible();
  });

  test('検索画面へ戻る動作確認', async ({ page }) => {
    // 詳細画面へ移動
    await page.click('button:has-text("詳細")');
    await page.waitForSelector('h1:has-text("社員詳細")');
    
    // 検索画面へ戻るボタンをクリック
    await page.click('button:has-text("検索画面へ戻る")');
    await page.waitForSelector('h1:has-text("社員検索")');
    
    // 検索画面に戻ったことを確認
    await expect(page.locator('h1')).toContainText('社員検索');
  });

  test('ホームへ戻る動作確認', async ({ page }) => {
    // 詳細画面へ移動
    await page.click('button:has-text("詳細")');
    await page.waitForSelector('h1:has-text("社員詳細")');
    
    // ホームへ戻るボタンをクリック
    await page.click('button:has-text("ホームへ戻る")');
    await page.waitForSelector('h1:has-text("タレントマネジメントシステム")');
    
    // ホーム画面に戻ったことを確認
    await expect(page.locator('h1')).toContainText('タレントマネジメントシステム');
  });
});
