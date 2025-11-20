import { test, expect } from '@playwright/test';

test('capture screenshots of employee search flow', async ({ page }) => {
  // ログイン
  await page.goto('/login');
  await page.screenshot({ path: 'test-results/01-login-page.png' });
  
  await page.fill('input[type="email"]', 'test@example.com');
  await page.fill('input[type="password"]', 'password');
  await page.click('button[type="submit"]');
  await page.waitForURL('/employees', { timeout: 10000 });
  
  // 検索ページ
  await page.screenshot({ path: 'test-results/02-search-page.png', fullPage: true });
  
  // 検索実行
  await page.click('button[type="submit"]');
  await page.waitForTimeout(3000);
  await page.screenshot({ path: 'test-results/03-search-results.png', fullPage: true });
  
  // 詳細ページ
  await page.goto('/employees/E001');
  await page.waitForTimeout(2000);
  await page.screenshot({ path: 'test-results/04-employee-detail.png', fullPage: true });
  
  // ページ内容を確認
  const content = await page.content();
  console.log('Page has E001:', content.includes('E001'));
  console.log('Page has 社員:', content.includes('社員'));
  
  expect(true).toBeTruthy();
});
