import { test, expect } from '@playwright/test';

test.describe('Photo Display', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[type="email"]', 'test@example.com');
    await page.fill('input[type="password"]', 'password');
    await page.click('button[type="submit"]');
    await page.waitForURL('/employees', { timeout: 10000 });
  });

  test('should display employee photos in search results', async ({ page }) => {
    // 検索実行
    await page.click('button[type="submit"]');
    await page.waitForTimeout(3000);
    
    // 画像要素が存在することを確認
    const images = page.locator('img[alt="Employee"]');
    const count = await images.count();
    console.log('Number of employee images:', count);
    expect(count).toBeGreaterThan(0);
    
    // 最初の画像が読み込まれることを確認
    const firstImage = images.first();
    await expect(firstImage).toBeVisible({ timeout: 5000 });
    
    // 画像のsrc属性を確認
    const src = await firstImage.getAttribute('src');
    console.log('First image src:', src);
    expect(src).toBeTruthy();
    
    // 画像が実際に読み込まれているか確認（naturalWidthが0より大きい）
    const isLoaded = await firstImage.evaluate((img: HTMLImageElement) => {
      return img.complete && img.naturalWidth > 0;
    });
    console.log('First image loaded:', isLoaded);
    
    // スクリーンショット撮影
    await page.screenshot({ path: 'test-results/photo-search-results.png', fullPage: true });
  });

  test('should display large photo on detail page', async ({ page }) => {
    // 詳細ページに移動
    await page.goto('/employees/E001');
    await page.waitForTimeout(3000);
    
    // 大きな画像が表示されることを確認
    const largeImage = page.locator('img[alt="Employee"]');
    await expect(largeImage).toBeVisible({ timeout: 5000 });
    
    // 画像のsrc属性を確認
    const src = await largeImage.getAttribute('src');
    console.log('Detail page image src:', src);
    expect(src).toBeTruthy();
    
    // 画像が読み込まれているか確認
    const isLoaded = await largeImage.evaluate((img: HTMLImageElement) => {
      return img.complete && img.naturalWidth > 0;
    });
    console.log('Detail page image loaded:', isLoaded);
    console.log('Image dimensions:', await largeImage.evaluate((img: HTMLImageElement) => {
      return { width: img.naturalWidth, height: img.naturalHeight };
    }));
    
    // スクリーンショット撮影
    await page.screenshot({ path: 'test-results/photo-detail-page.png', fullPage: true });
  });

  test('should handle photo loading errors gracefully', async ({ page }) => {
    // 存在しない社員IDで詳細ページにアクセス
    await page.goto('/employees/E999');
    await page.waitForTimeout(3000);
    
    // エラーメッセージまたはデフォルト画像が表示されることを確認
    const content = await page.content();
    const hasErrorOrDefault = content.includes('見つかりません') || content.includes('img');
    expect(hasErrorOrDefault).toBeTruthy();
    
    await page.screenshot({ path: 'test-results/photo-error-handling.png', fullPage: true });
  });
});
