import { test, expect } from '@playwright/test';

test.describe('Employee Detail Flow', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[type="email"]', 'test@example.com');
    await page.fill('input[type="password"]', 'password');
    await page.click('button[type="submit"]');
    await page.waitForURL('/employees', { timeout: 10000 });
  });

  test('should navigate to employee detail page', async ({ page }) => {
    await page.goto('/employees/E001');
    await expect(page).toHaveURL('/employees/E001');
  });
});
