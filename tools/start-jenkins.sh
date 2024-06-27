#!/bin/bash

# Jenkins启动脚本
# 作者: luoxiaodong
# 创建日期：2023-06-01

JAVA_OPTS="-server -Xms1024m -Xmx2048m -XX:PermSize=256m -XX:MaxPermSize=512m"

# 建议：通过环境变量或配置文件动态设置以下路径和端口
JENKINS_HOME="/root/.jenkinsHome"
HTTP_PORT="8000"
LOGFILE="jenkins.log"

# 确保Jenkins的主目录存在且权限正确
mkdir -p "$JENKINS_HOME" && chown -R jenkins:jenkins "$JENKINS_HOME" || { echo "Failed to create or set permissions for Jenkins home directory"; exit 1; }

# 启动Jenkins，拆分为多行以增强可读性
nohup java \
    $JAVA_OPTS \
    -Dhudson.util.ProcessTree.disable=true \
    -Dhudson.security.csrf.GlobalCrumbIssuerConfiguration.DISABLE_CSRF_PROTECTION=true \
    -DJENKINS_HOME="$JENKINS_HOME" \
    -jar jenkins.war \
    --httpPort="$HTTP_PORT" \
    > "$LOGFILE" 2>&1 &

# 确认Jenkins进程是否成功启动（此步骤为可选，取决于是否需要即时反馈）
echo "Jenkins has been started. Check '$LOGFILE' for details."