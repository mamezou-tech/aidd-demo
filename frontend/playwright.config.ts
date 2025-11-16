import { defineConfig } from '@playwright/test';

export default defineConfig({
  testDir: './e2e',
  use: {
    baseURL: 'http://talent-frontend',
  },
  webServer: {
    command: 'echo "Using existing docker-compose services"',
    url: 'http://talent-frontend',
    reuseExistingServer: true,
  },
});
