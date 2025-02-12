# SpringAI-RAG
# 版本資訊
- Spring Boot 版本：3.4.1
- Java 版本：17
- AI model 版本：gpt-4o-mini
## 其他
- lombok
- Spring WebFlux
---
# 第一步：配置 application.properties
## 必填
  - AI 配置
```
  spring.ai.openai.api-key=your_api_key
  spring.ai.openai.chat.options.model=your_llm_model_name
```
---

## 選填
- AI配置
> 回答創意性，數字愈小愈準確(預設值:0.8)
```
  spring.ai.openai.chat.options.temperature=your_temperature_value
```
---
- 其他配置
> 配置台灣時區
```
spring.jackson.time-zone=GMT+8
```

> 配置日期格式
```
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
```
---
