import { test } from '@playwright/test';

test('DOM構造確認', async ({ page }) => {
  await page.goto('/');
  await page.waitForLoadState('networkidle');
  
  // h2要素のCSS情報を取得
  const h2 = page.locator('h2:has-text("ログイン")');
  const styles = await h2.evaluate((el) => {
    const computed = window.getComputedStyle(el);
    return {
      display: computed.display,
      visibility: computed.visibility,
      opacity: computed.opacity,
      position: computed.position,
      zIndex: computed.zIndex,
      color: computed.color,
      fontSize: computed.fontSize,
    };
  });
  
  console.log('=== H2 STYLES ===');
  console.log(JSON.stringify(styles, null, 2));
  
  // 要素の位置情報を取得
  const box = await h2.boundingBox();
  console.log('=== H2 BOUNDING BOX ===');
  console.log(JSON.stringify(box, null, 2));
});
