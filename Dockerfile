FROM openjdk:17
# 類似cd的指令，指定工作目錄
WORKDIR /app
# 複製 jar 到工作目錄
COPY target/app.jar app.jar
# 設定容器的port
EXPOSE 8080
# 指定容器啟動時執行的指令
ENTRYPOINT ["java", "-jar", "app.jar"]
