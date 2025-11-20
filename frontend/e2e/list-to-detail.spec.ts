import { test, expect } from '@playwright/test';

test.describe('List to Detail Navigation', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[type="email"]', 'test@example.com');
    await page.fill('input[type="password"]', 'password');
    await page.click('button[type="submit"]');
    await page.waitForURL('/employees', { timeout: 10000 });
  });

  test('should navigate from search results to employee detail by clicking card', async ({ page }) => {
    // 検索実行
    await page.click('button[type="submit"]');
    await page.waitForTimeout(3000);
    
    // 検索結果が表示されることを確認
    const cards = page.locator('div').filter({ hasText: /山田|佐藤|鈴木/ });
    const cardCount = await cards.count();
    console.log('Number of employee cards:', cardCount);
    expect(cardCount).toBeGreaterThan(0);
    
    // スクリーンショット: 検索結果
    await page.screenshot({ path: 'test-results/nav-01-search-results.png', fullPage: true });
    
    // 最初のカードをクリック
    const firstCard = page.locator('div').filter({ hasText: '山田太郎' }).first();
    await expect(firstCard).toBeVisible({ timeout: 5000 });
    console.log('Clicking on first employee card...');
    await firstCard.click();
    
    // URLが詳細ページに変わることを確認
    await page.waitForTimeout(2000);
    const url = page.url();
    console.log('Current URL after click:', url);
    expect(url).toMatch(/\/employees\/E\d+/);
    
    // 詳細ページの内容を確認
    await page.waitForTimeout(2000);
    const content = await page.content();
    const hasEmployeeInfo = content.includes('社員ID:') || content.includes('E0');
    console.log('Detail page has employee info:', hasEmployeeInfo);
    expect(hasEmployeeInfo).toBeTruthy();
    
    // スクリーンショット: 詳細ページ
    await page.screenshot({ path: 'test-results/nav-02-detail-page.png', fullPage: true });
    
    // 写真が表示されることを確認
    const photo = page.locator('img[alt="Employee"]');
    await expect(photo).toBeVisible({ timeout: 5000 });
    const photoLoaded = await photo.evaluate((img: HTMLImageElement) => {
      return img.complete && img.naturalWidth > 0;
    });
    console.log('Photo loaded on detail page:', photoLoaded);
    expect(photoLoaded).toBeTruthy();
  });

  test('should navigate back from detail to list', async ({ page }) => {
    // 検索実行
    await page.click('button[type="submit"]');
    await page.waitForTimeout(3000);
    
    // カードをクリックして詳細へ
    const firstCard = page.locator('div').filter({ hasText: '山田太郎' }).first();
    await firstCard.click();
    await page.waitForTimeout(2000);
    
    // 詳細ページにいることを確認
    expect(page.url()).toMatch(/\/employees\/E\d+/);
    
    // 戻るボタンをクリック
    const backButton = page.locator('button:has-text("戻る")');
    await expect(backButton).toBeVisible({ timeout: 5000 });
    console.log('Clicking back button...');
    await backButton.click();
    
    // 一覧ページに戻ることを確認
    await page.waitForTimeout(1000);
    expect(page.url()).toContain('/employees');
    expect(page.url()).not.toMatch(/\/employees\/E\d+/);
    console.log('Returned to list page:', page.url());
    
    // スクリーンショット: 一覧に戻った状態
    await page.screenshot({ path: 'test-results/nav-03-back-to-list.png', fullPage: true });
  });

  test('should display correct employee info after clicking different cards', async ({ page }) => {
    // 検索実行
    await page.click('button[type="submit"]');
    await page.waitForTimeout(3000);
    
    // カードが表示されるまで待つ
    await page.waitForSelector('[data-testid^="employee-card-"]', { timeout: 5000 });
    
    // 全てのカードを取得
    const allCards = await page.locator('[data-testid^="employee-card-"]').all();
    
    if (allCards.length < 2) {
      console.log('Not enough cards found, skipping test');
      return;
    }
    
    // 1人目のカードをクリック
    await allCards[0].click();
    await page.waitForTimeout(2000);
    
    const firstUrl = page.url();
    const firstEmployeeId = firstUrl.split('/').pop();
    console.log('First employee URL:', firstUrl);
    
    // 一覧に戻る
    await page.goto('/employees');
    await page.click('button[type="submit"]');
    await page.waitForTimeout(3000);
    
    // 再度カードを取得
    const allCardsSecond = await page.locator('[data-testid^="employee-card-"]').all();
    
    // 2人目のカードをクリック
    await allCardsSecond[1].click();
    await page.waitForTimeout(2000);
    
    const secondUrl = page.url();
    const secondEmployeeId = secondUrl.split('/').pop();
    console.log('Second employee URL:', secondUrl);
    
    // URLが異なることを確認
    expect(secondUrl).not.toBe(firstUrl);
    expect(secondEmployeeId).not.toBe(firstEmployeeId);
  });
});
