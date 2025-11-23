import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    host: '0.0.0.0',
    port: 3000,
    proxy: {
      '/api': {
        // docker-composeネットワーク内のコンテナ名で直接アクセス
        // 開発環境・本番環境で同じ設定
        target: process.env.VITE_API_TARGET || 'http://q-developer-demo-app:8080',
        changeOrigin: true
      }
    }
  }
});
