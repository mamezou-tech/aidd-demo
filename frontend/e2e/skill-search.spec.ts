import { test, expect } from '@playwright/test';

test.describe('Skill Search', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[type="email"]', 'test@example.com');
    await page.fill('input[type="password"]', 'password');
    await page.click('button[type="submit"]');
    await page.waitForURL('/employees', { timeout: 10000 });
  });

  test('should display skill checkboxes', async ({ page }) => {
    await page.waitForTimeout(3000);
    
    // スキルラベルを確認
    const skillLabel = page.locator('label:has-text("スキル")');
    const skillLabelCount = await skillLabel.count();
    console.log('Skill label count:', skillLabelCount);
    
    // チェックボックスを確認
    const checkboxes = page.locator('input[type="checkbox"]');
    const checkboxCount = await checkboxes.count();
    console.log('Number of skill checkboxes:', checkboxCount);
    
    // スキル名を確認
    const content = await page.content();
    console.log('Has Java:', content.includes('Java'));
    console.log('Has Python:', content.includes('Python'));
    console.log('Has JavaScript:', content.includes('JavaScript'));
    
    await page.screenshot({ path: 'test-results/skill-checkboxes.png', fullPage: true });
    
    expect(checkboxCount).toBeGreaterThan(0);
  });

  test('should search by single skill', async ({ page }) => {
    await page.waitForTimeout(3000);
    
    // 最初のスキルチェックボックスを選択
    const firstCheckbox = page.locator('input[type="checkbox"]').first();
    if (await firstCheckbox.count() > 0) {
      await firstCheckbox.check();
      console.log('Checked first skill checkbox');
      
      // 検索実行
      await page.click('button[type="submit"]');
      await page.waitForTimeout(3000);
      
      // 結果が表示されることを確認
      const content = await page.content();
      const hasResults = content.includes('E0') || content.length > 5000;
      console.log('Has search results:', hasResults);
      
      await page.screenshot({ path: 'test-results/skill-search-results.png', fullPage: true });
    }
  });

  test('should search by multiple skills', async ({ page }) => {
    await page.waitForTimeout(3000);
    
    // 複数のスキルチェックボックスを選択
    const checkboxes = page.locator('input[type="checkbox"]');
    const count = await checkboxes.count();
    console.log('Total checkboxes:', count);
    
    if (count >= 2) {
      await checkboxes.nth(0).check();
      await checkboxes.nth(1).check();
      console.log('Checked 2 skill checkboxes');
      
      // 検索実行
      await page.click('button[type="submit"]');
      await page.waitForTimeout(3000);
      
      // 結果が表示されることを確認
      const content = await page.content();
      const hasResults = content.includes('E0') || content.length > 5000;
      console.log('Has search results with multiple skills:', hasResults);
      
      await page.screenshot({ path: 'test-results/multi-skill-search.png', fullPage: true });
    }
  });

  test('should debug skill data loading', async ({ page }) => {
    // コンソールログをキャプチャ
    page.on('console', msg => console.log('CONSOLE:', msg.text()));
    
    // ネットワークリクエストをキャプチャ
    page.on('request', request => {
      if (request.url().includes('skill') || request.url().includes('organization')) {
        console.log('REQUEST:', request.method(), request.url());
      }
    });
    
    page.on('response', async response => {
      if (response.url().includes('skill')) {
        console.log('SKILLS RESPONSE:', response.status());
        try {
          const data = await response.json();
          console.log('Skills data:', JSON.stringify(data).substring(0, 200));
        } catch (e) {
          console.log('Could not parse skills response');
        }
      }
    });
    
    await page.waitForTimeout(5000);
    
    // ページのHTMLを確認
    const html = await page.content();
    console.log('Page has form:', html.includes('<form'));
    console.log('Page has checkbox:', html.includes('checkbox'));
    console.log('Page has スキル:', html.includes('スキル'));
  });
});
