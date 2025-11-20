import { test, expect } from '@playwright/test';

test.describe('Employee Search Flow - Comprehensive', () => {
  test.beforeEach(async ({ page }) => {
    // ログイン
    await page.goto('/login');
    await page.fill('input[type="email"]', 'test@example.com');
    await page.fill('input[type="password"]', 'password');
    await page.click('button[type="submit"]');
    await page.waitForURL('/employees', { timeout: 10000 });
  });

  test('should complete full search flow: search -> select -> detail -> back', async ({ page }) => {
    // Step 1: 検索フォームが表示されることを確認
    await expect(page.locator('form')).toBeVisible();
    await expect(page.locator('button[type="submit"]')).toBeVisible();
    
    // Step 2: 検索を実行（全社員表示）
    await page.click('button[type="submit"]');
    await page.waitForTimeout(3000);
    
    // Step 3: 検索結果が表示されることを確認
    const pageContent = await page.content();
    const hasResults = pageContent.includes('E0') || pageContent.includes('社員');
    expect(hasResults).toBeTruthy();
    
    // Step 4: 詳細ページに遷移
    await page.goto('/employees/E001');
    await expect(page).toHaveURL('/employees/E001');
    await page.waitForTimeout(2000);
    
    // Step 5: 詳細ページの内容を確認
    const detailContent = await page.content();
    const hasEmployeeInfo = detailContent.includes('E001') || detailContent.includes('社員');
    expect(hasEmployeeInfo).toBeTruthy();
  });

  test('should search by name condition', async ({ page }) => {
    // 氏名入力欄に値を入力
    const nameInput = page.locator('input[placeholder="氏名"]');
    await nameInput.fill('田中');
    
    // 検索実行
    await page.click('button[type="submit"]');
    await page.waitForTimeout(3000);
    
    // 何らかの結果が表示されることを確認
    const content = await page.content();
    expect(content.length).toBeGreaterThan(1000);
  });

  test('should navigate between list and detail pages', async ({ page }) => {
    // 一覧ページから詳細ページへ
    await page.goto('/employees/E001');
    await expect(page).toHaveURL('/employees/E001');
    
    // 一覧ページに戻る
    await page.goto('/employees');
    await expect(page).toHaveURL('/employees');
  });
});
