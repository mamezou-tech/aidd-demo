# Development container with full Java development tools
FROM mcr.microsoft.com/devcontainers/java:1-17-bookworm

# Install additional tools and Japanese fonts for Playwright
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends \
        curl \
        wget \
        git \
        vim \
        default-mysql-client \
        # Japanese fonts
        fonts-noto-cjk \
        fonts-ipafont-gothic \
        fonts-ipafont-mincho \
        # Playwright dependencies
        libcups2 \
        libcairo2 \
        libpango-1.0-0 \
        libatk1.0-0 \
        libatk-bridge2.0-0 \
        libgdk-pixbuf2.0-0 \
        libgtk-3-0 \
        libasound2 \
        libxcomposite1 \
        libxdamage1 \
        libxrandr2 \
    && apt-get clean -y && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /workspace

# Expose ports for Spring Boot and MySQL
EXPOSE 8080 3306
