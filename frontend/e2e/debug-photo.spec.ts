import { test } from '@playwright/test';

test('debug detail page photo loading', async ({ page }) => {
  // ログイン
  await page.goto('/login');
  await page.fill('input[type="email"]', 'test@example.com');
  await page.fill('input[type="password"]', 'password');
  await page.click('button[type="submit"]');
  await page.waitForURL('/employees', { timeout: 10000 });
  
  // コンソールログをキャプチャ
  page.on('console', msg => console.log('CONSOLE:', msg.text()));
  page.on('pageerror', err => console.log('PAGE ERROR:', err.message));
  
  // ネットワークリクエストをキャプチャ
  page.on('request', request => {
    if (request.url().includes('photo')) {
      console.log('REQUEST:', request.method(), request.url());
      console.log('HEADERS:', JSON.stringify(request.headers()));
    }
  });
  
  page.on('response', response => {
    if (response.url().includes('photo')) {
      console.log('RESPONSE:', response.status(), response.url());
    }
  });
  
  // 詳細ページに移動
  console.log('Navigating to detail page...');
  await page.goto('/employees/E001');
  await page.waitForTimeout(5000);
  
  // 画像要素を確認
  const images = page.locator('img[alt="Employee"]');
  const count = await images.count();
  console.log('Number of images:', count);
  
  for (let i = 0; i < count; i++) {
    const img = images.nth(i);
    const src = await img.getAttribute('src');
    const isVisible = await img.isVisible();
    console.log(`Image ${i}: src=${src}, visible=${isVisible}`);
  }
  
  await page.screenshot({ path: 'test-results/debug-detail-photo.png', fullPage: true });
});
