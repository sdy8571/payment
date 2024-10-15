#!/usr/bin/bash
BUILD_ID=DONTKILLME
APP_NAME=spring-boot-demo-api-service-1.0-SNAPSHOT.jar
PROFILE='--spring.profiles.active=dbg,security'
# 生产需更换此目录
APP_PATH=/data/joycode/cereshop
CONSOLE_PATH=${APP_PATH}/${APP_NAME}/logs
CONSOLE_LOG=${APP_NAME}-console.log
JAVA_OPTS='-Xms512M -Xmx512M'

if [ ! -d "${CONSOLE_PATH}" ]; then
    mkdir -p "${CONSOLE_PATH}"
fi
# 检查程序是否在运行
is_exist(){
        # 获取PID
        PID=$(ps -ef |grep ${APP_PATH}/${APP_NAME} | grep -v $0 |grep -v grep |awk '{print $2}')
        # -z "${pid}"判断pid是否存在，如果不存在返回1，存在返回0
        if [ -z "${PID}" ]; then
                # 如果进程不存在返回1
                return 1
        else
                # 进程存在返回0
                return 0
        fi
}

# 停止进程函数
stop(){
        is_exist
        if [ $? -eq "0" ]; then
                kill -15 ${PID}
                echo "${APP_NAME} process stop, PID=${PID}"
        else
                echo "There is not the process of ${APP_NAME}"
        fi
}

# 定义启动程序函数
start(){
        is_exist
        if [ $? -eq "0" ]; then
                echo "${APP_NAME} is already running, PID=${PID}"
        else
               nohup java -server ${JAVA_OPTS} -jar ${APP_PATH}/${APP_NAME}/${APP_NAME}.jar ${PROFILE} > ${CONSOLE_PATH}/${CONSOLE_LOG} 2>&1 &
                PID=$(echo $!)
                echo "${APP_NAME} start success, PID=$!"
        fi

}
callback(){
  echo "${APP_NAME} 部署失败,回滚"
  cd ${APP_PATH}/bak
  latest_file=$(ls -t "${APP_NAME}"* | head -n 1)
  cp $latest_file ${HOST_PATH}/${APP_NAME}.zip
  cd ${HOST_PATH}
  unzip -o ${APP_NAME}.zip -d ./
  cd ${APP_NAME}
  chmod +x srv.sh
  start 1
}
restart(){
  is_exist
  if [ $? -eq "0" ]; then
    stop
    while :
    do
      # 检查进程是否存在
      if ! ps -p ${PID} > /dev/null; then
          echo "进程已结束-重启"
          break
      fi
      # 等待一段时间后继续检查
      sleep 1
    done
  fi
  start
}
run()
{
  is_exist
  if [ $? -eq "0" ]; then
        echo "================================"
	echo "already started!"
	echo "================================"
	exit 1
    fi
    java ${JAVA_OPTS} -jar ${APP_PATH}/${APP_NAME}/${APP_NAME}.jar ${PROFILE}
}
status(){
  is_exist
  if [ $? -eq "0" ]; then
    echo "${APP_NAME} is running. Pid is ${PID}"
  else
    echo "${APP_NAME} is NOT running."
  fi
}

echo "======脚本启动======="

case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "run")
    run
    ;;
   "restart")
    restart
    ;;
  *)
    echo "Usage: $0 {run|start|stop|restart|status}"
    exit 1
esac
