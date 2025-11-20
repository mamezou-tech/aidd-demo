import { test } from '@playwright/test';
import * as fs from 'fs';

test('debug employee detail page', async ({ page }) => {
  // ログイン
  await page.goto('/login');
  await page.fill('input[type="email"]', 'test@example.com');
  await page.fill('input[type="password"]', 'password');
  await page.click('button[type="submit"]');
  await page.waitForURL('/employees', { timeout: 10000 });
  
  // 詳細ページ
  await page.goto('/employees/E001');
  await page.waitForTimeout(3000);
  
  // HTMLを保存
  const html = await page.content();
  fs.writeFileSync('test-results/detail-page.html', html);
  
  // コンソールエラーをキャプチャ
  page.on('console', msg => console.log('CONSOLE:', msg.text()));
  page.on('pageerror', err => console.log('ERROR:', err.message));
  
  await page.waitForTimeout(2000);
  
  console.log('HTML length:', html.length);
  console.log('Has E001:', html.includes('E001'));
  console.log('Has error:', html.includes('error') || html.includes('Error'));
});
