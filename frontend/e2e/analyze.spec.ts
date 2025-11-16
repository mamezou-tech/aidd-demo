import { test } from '@playwright/test';

test('React レンダリング確認', async ({ page }) => {
  await page.goto('/');
  await page.waitForLoadState('networkidle');
  
  // root要素を確認
  const root = await page.locator('#root').innerHTML();
  console.log('=== ROOT ELEMENT ===');
  console.log(root.substring(0, 500));
  
  // すべてのinput要素を確認
  const inputs = await page.locator('input').all();
  console.log('=== INPUT ELEMENTS ===');
  console.log('Count:', inputs.length);
  
  for (let i = 0; i < inputs.length; i++) {
    const input = inputs[i];
    const type = await input.getAttribute('type');
    const value = await input.getAttribute('value');
    console.log(`Input ${i}: type=${type}, value=${value}`);
  }
  
  // fill()を試す
  if (inputs.length > 0) {
    console.log('=== TESTING FILL ===');
    await inputs[0].fill('test');
    const afterFill = await inputs[0].inputValue();
    console.log('After fill:', afterFill);
    
    // clear して type() を試す
    await inputs[0].clear();
    await inputs[0].pressSequentially('admin', { delay: 100 });
    const afterType = await inputs[0].inputValue();
    console.log('After pressSequentially:', afterType);
  }
});
