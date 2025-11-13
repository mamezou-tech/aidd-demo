# Development container with full Java development tools
FROM mcr.microsoft.com/devcontainers/java:1-17-bookworm

# Install additional tools
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends \
        curl \
        wget \
        git \
        vim \
        default-mysql-client \
    && apt-get clean -y && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /workspace

# Expose ports for Spring Boot and MySQL
EXPOSE 8080 3306
