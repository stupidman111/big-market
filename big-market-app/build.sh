
# 普通镜像构建，随系统版本构建 amd/arm
# docker build -t system/big-market-app:1.0-SNAPSHOT -f ./Dockerfile .

# 兼容 amd、arm 构建镜像
# docker buildx build --load --platform liunx/amd64,linux/arm64 -t zy/big-market-app:1.0-SNAPSHOT -f ./Dockerfile . --push
# docker buildx build --platform liunx/amd64,linux/arm64 -t zy/big-market-app:1.0-SNAPSHOT -f ./Dockerfile . --push

#!/bin/bash
# 创建并使用新的 builder
docker buildx create --name mybuilder --use --driver docker-container
# 启动 builder
docker buildx inspect mybuilder --bootstrap
# 运行多平台构建并推送镜像
docker buildx build --platform linux/amd64,linux/arm64 -t zysdaoke/big-market-app-1.0 -f ./Dockerfile . --push
# 删除 builder（可选）
docker buildx rm mybuilder
