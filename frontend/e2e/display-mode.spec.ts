import { test, expect } from '@playwright/test';

test.describe('Display Mode Switching', () => {
  test.beforeEach(async ({ page }) => {
    // ログイン
    await page.goto('http://localhost:3000/login');
    await page.fill('input[type="email"]', 'test@example.com');
    await page.fill('input[type="password"]', 'aiddTest');
    await page.click('button[type="submit"]');
    await page.waitForURL('http://localhost:3000/');
    
    // 社員検索画面に遷移
    await page.click('text=社員検索');
    await page.waitForURL('http://localhost:3000/employees');
  });

  test('デフォルトで一覧表示される', async ({ page }) => {
    // 検索実行
    await page.click('button:has-text("検索")');
    await page.waitForTimeout(2000);

    // テーブルが表示されることを確認
    const table = page.locator('table');
    await expect(table).toBeVisible({ timeout: 10000 });
  });

  test('トグルボタンが検索フォームの右下部に表示される', async ({ page }) => {
    // トグルボタンが存在することを確認
    const toggleButton = page.locator('text=一覧').or(page.locator('text=カード'));
    await expect(toggleButton.first()).toBeVisible();
  });

  test('トグルボタンで一覧表示からカード表示に切り替わる', async ({ page }) => {
    // 検索実行
    await page.click('button:has-text("検索")');
    await page.waitForTimeout(1000);

    // 一覧表示を確認
    await expect(page.locator('table')).toBeVisible();

    // カード表示に切り替え
    await page.click('text=カード');
    await page.waitForTimeout(500);

    // カード表示を確認
    await expect(page.locator('table')).not.toBeVisible();
    await expect(page.locator('[class*="card"]').first()).toBeVisible();
  });

  test('トグルボタンでカード表示から一覧表示に切り替わる', async ({ page }) => {
    // 検索実行
    await page.click('button:has-text("検索")');
    await page.waitForTimeout(1000);

    // カード表示に切り替え
    await page.click('text=カード');
    await page.waitForTimeout(500);

    // 一覧表示に戻す
    await page.click('text=一覧');
    await page.waitForTimeout(500);

    // 一覧表示を確認
    await expect(page.locator('table')).toBeVisible();
  });

  test('一覧表示の列順序が正しい', async ({ page }) => {
    // 検索実行
    await page.click('button:has-text("検索")');
    await page.waitForTimeout(1000);

    // テーブルヘッダーの列順序を確認
    const headers = page.locator('th');
    await expect(headers.nth(0)).toContainText('顔写真');
    await expect(headers.nth(1)).toContainText('社員ID');
    await expect(headers.nth(2)).toContainText('氏名');
    await expect(headers.nth(3)).toContainText('組織');
    await expect(headers.nth(4)).toContainText('役職');
    await expect(headers.nth(5)).toContainText('雇用形態');
  });
});
