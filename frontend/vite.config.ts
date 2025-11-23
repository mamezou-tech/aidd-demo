import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    host: '0.0.0.0',
    port: 3000,
    proxy: {
      '/api': {
        // Dev Container環境ではホストのDockerゲートウェイ経由でアクセス
        // 通常の環境ではlocalhost、コンテナ環境では172.17.0.1（デフォルトゲートウェイ）
        target: process.env.VITE_API_TARGET || 'http://172.17.0.1:8080',
        changeOrigin: true
      }
    }
  }
});
