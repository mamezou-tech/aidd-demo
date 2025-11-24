# ---- build stage (Node.js + TypeScript + Vite) ----
FROM node:20-alpine AS builder
WORKDIR /app

# 依存関係のインストール (キャッシュ効率化)
COPY package*.json ./
RUN npm ci

# ソースコードをコピーしてビルド
COPY . .
RUN npm run build

# ---- runtime stage (Nginx) ----
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

# ビルド成果物をコピー
COPY --from=builder /app/dist .

# Nginx設定をコピー
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
