# Spring AI 智能餐厅推荐系统 - 快速入门指南

> 本文档旨在帮助新成员快速了解项目背景、技术栈、业务逻辑及开发流程。

---

## 📋 目录

1. [项目概述](#项目概述)
2. [核心功能](#核心功能)
3. [技术架构](#技术架构)
4. [环境配置](#环境配置)
5. [项目结构](#项目结构)
6. [模块说明](#模块说明)
7. [开发规范](#开发规范)
8. [API 接口文档](#api-接口文档)
9. [数据库设计](#数据库设计)
10. [常见问题及解决方案](#常见问题及解决方案)

---

## 项目概述

### 项目简介

**Spring AI 智能餐厅推荐系统**是一个基于 Spring AI 框架构建的智能化餐饮推荐应用。该项目展示了如何利用不同的 AI 模型提供商（OpenAI、Mistral、Ollama 等）来构建功能丰富的 AI 应用。

### 项目目标

- 演示 Spring AI 框架的核心功能和最佳实践
- 提供多模型集成的参考实现
- 展示 RAG（检索增强生成）技术的实际应用
- 实现 Function Calling、流式响应等高级 AI 功能

### 业务场景

系统主要服务于以下场景：
- **餐厅推荐**：根据用户偏好（位置、菜系、价格等）智能推荐餐厅
- **菜品生成**：AI 自动生成菜品描述、营养信息和制作详情
- **智能对话**：多轮对话形式的餐厅咨询服务
- **知识问答**：基于餐厅知识库的 RAG 问答

---

## 核心功能

### 1. 基础功能

| 功能模块 | 描述 | 技术要点 |
|---------|------|---------|
| 🍽️ 餐厅推荐 | 基于用户偏好的智能餐厅推荐 | PromptTemplate + 结构化输出 |
| 🥘 菜品生成 | AI 生成菜品描述和营养信息 | Entity 类型转换 |
| 💬 智能对话 | 多语言餐厅推荐对话 | ChatMemory 对话记忆 |
| 📊 结构化输出 | JSON/XML 格式数据返回 | ParameterizedTypeReference |

### 2. RAG 功能

| 功能模块 | 描述 | 技术要点 |
|---------|------|---------|
| 🔍 向量搜索 | 基于语义相似度的文档检索 | PgVector + Embedding |
| 💡 RAG 聊天 | 检索增强生成的智能对话 | QuestionAnswerAdvisor |
| 🎯 个性化 RAG | 基于用户偏好的个性化推荐 | 自定义搜索策略 |

### 3. AI 扩展功能

| 功能模块 | 描述 | 技术要点 |
|---------|------|---------|
| 🔧 Function Calling | AI 自动调用外部工具和函数 | Tool Callbacks |
| 🌊 流式响应 | Server-Sent Events 实时流式输出 | SseEmitter + Flux |
| 🚀 高级 RAG | Re-ranking、混合搜索、多查询 RAG | 多种检索策略组合 |
| 😊 情感分析 | 分析用户评论的情感倾向 | 提示词工程 |

---

## 技术架构

### 架构概览

```
┌─────────────────────────────────────────────────────────────┐
│                        客户端层                              │
│         (Web Browser / Mobile App / API Client)             │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      REST API 层                             │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐        │
│  │   基础 API   │ │   RAG API    │ │  扩展 API    │        │
│  └──────────────┘ └──────────────┘ └──────────────┘        │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     业务服务层                               │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐        │
│  │  Restaurant  │ │   RAGChat    │ │  Advanced    │        │
│  │Recommendation│ │   Service    │ │    RAG       │        │
│  └──────────────┘ └──────────────┘ └──────────────┘        │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐        │
│  │   Function   │ │  Streaming   │ │  Sentiment   │        │
│  │   Calling    │ │   Service    │ │  Analysis    │        │
│  └──────────────┘ └──────────────┘ └──────────────┘        │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     AI 框架层                                │
│              Spring AI 1.1.2 + ChatClient                    │
│     ┌──────────────────────────────────────────────┐       │
│     │  Advisors: QuestionAnswer / ChatMemory / Log │       │
│     └──────────────────────────────────────────────┘       │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     数据存储层                               │
│  ┌──────────────────┐  ┌──────────────────┐                │
│  │  PostgreSQL      │  │   Vector Store   │                │
│  │  (关系数据)      │  │  (PgVector)      │                │
│  └──────────────────┘  └──────────────────┘                │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     AI 模型层                                │
│  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐   │
│  │ OpenAI │ │Mistral │ │ Ollama │ │DeepSeek│ │ Gemini │   │
│  └────────┘ └────────┘ └────────┘ └────────┘ └────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### 技术栈详情

| 层级 | 技术组件 | 版本 | 用途 |
|------|---------|------|------|
| 基础框架 | Spring Boot | 3.5.11 | Web 应用框架 |
| AI 框架 | Spring AI | 1.1.2 | AI 能力集成 |
| 向量数据库 | PostgreSQL + pgvector | pg16 | 向量存储与检索 |
| 构建工具 | Maven | 3.6+ | 项目构建管理 |
| 代码简化 | Lombok | - | 减少样板代码 |
| 容器化 | Docker Compose | - | 环境编排 |
| 监控 | Actuator + Prometheus | - | 应用监控 |

### 支持的 AI 模型

| 提供商 | 模型示例 | 配置方式 |
|--------|---------|---------|
| OpenAI | GPT-4, GPT-3.5 | `application.yml` |
| Mistral AI | mistral-large-latest | Profile: `mistral-ai` |
| Ollama | llama3.2, mxbai-embed-large | 本地部署 |
| DeepSeek | deepseek-chat | `application-deepseek.yml` |
| Gemini | gemini-pro | `application-gemini.yml` |
| Groq | mixtral-8x7b | `application-groq.yml` |
| OpenRouter | 多模型聚合 | `application-openrouter.yml` |
| Qwen | qwen-max | `application-qwen.yml` |

---

## 环境配置

### 前置要求

| 环境 | 版本要求 | 说明 |
|------|---------|------|
| JDK | 17+ | Java 开发工具包 |
| Maven | 3.6+ | 项目构建工具 |
| Docker | 20.10+ | 容器化平台 |
| Docker Compose | 2.0+ | 容器编排工具 |

### 快速启动步骤

#### 1. 克隆项目

```bash
git clone https://github.com/chensoul/spring-ai-restaurant-showcase.git
cd spring-ai-restaurant-showcase
```

#### 2. 启动数据库

```bash
docker-compose up -d
```

> 这将启动 PostgreSQL 15 + pgvector 扩展，端口为 5432

#### 3. 配置 API Key

选择以下任一方式配置：

**方式一：环境变量（推荐）**
```bash
# Windows PowerShell
$env:OPENAI_API_KEY="your-api-key"

# Linux/Mac
export OPENAI_API_KEY=your-api-key
```

**方式二：修改配置文件**
编辑 `src/main/resources/application.yml`：
```yaml
spring:
  ai:
    openai:
      api-key: your-api-key
```

#### 4. 运行应用

```bash
# 使用 Maven Wrapper
./mvnw spring-boot:run

# 或使用本地 Maven
mvn spring-boot:run
```

#### 5. 验证启动

```bash
curl http://localhost:8080/actuator/health
```

预期返回：`{"status":"UP"}`

### 多模型配置

#### OpenAI（默认）
```bash
./mvnw spring-boot:run -Popen-ai
```

#### Mistral AI
```bash
export MISTRAL_API_KEY=your-mistral-key
./mvnw spring-boot:run -Pmistral-ai
```

#### Ollama（本地模型）
```bash
# 1. 安装 Ollama
# 2. 拉取模型
ollama pull llama3.2
ollama pull mxbai-embed-large

# 3. 启动应用
./mvnw spring-boot:run -Pollama-ai
```

### IDE 配置

#### IntelliJ IDEA
1. 导入项目：`File → Open → pom.xml`
2. 启用 Lombok 插件：`Settings → Plugins → Lombok`
3. 启用注解处理：`Settings → Build → Annotation Processors → Enable`
4. 配置 JDK：`Project Structure → SDK → JDK 17`

#### VS Code
1. 安装插件：Extension Pack for Java、Spring Boot Extension Pack
2. 导入项目：打开项目文件夹
3. 配置 Java 版本：`Ctrl+Shift+P → Java: Configure Java Runtime`

---

## 项目结构

```
spring-ai-restaurant-showcase/
├── .github/
│   └── workflows/
│       └── ci.yml                    # GitHub Actions CI 配置
├── .mvn/
│   └── wrapper/                      # Maven Wrapper
├── examples/
│   ├── test-ai-extensions.sh         # AI 扩展功能测试脚本
│   ├── test-chat.md                  # 聊天测试用例
│   └── test-rag.sh                   # RAG 功能测试脚本
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── cc/
│   │   │       └── chensoul/
│   │   │           └── springai/
│   │   │               └── restaurant/
│   │   │                   ├── config/           # 配置类
│   │   │                   │   └── ChatConfig.java
│   │   │                   ├── controller/       # REST 控制器
│   │   │                   │   ├── AdvancedRagController.java
│   │   │                   │   ├── FunctionCallingController.java
│   │   │                   │   ├── RagController.java
│   │   │                   │   ├── RestaurantRecommendationController.java
│   │   │                   │   ├── SentimentAnalysisController.java
│   │   │                   │   ├── StreamingController.java
│   │   │                   │   └── StructuredOutputController.java
│   │   │                   ├── model/            # 数据模型
│   │   │                   │   ├── Dish.java
│   │   │                   │   ├── RecommendationRequest.java
│   │   │                   │   └── Restaurant.java
│   │   │                   ├── service/          # 业务服务
│   │   │                   │   ├── AdvancedRagService.java
│   │   │                   │   ├── DocumentService.java
│   │   │                   │   ├── FunctionCallingService.java
│   │   │                   │   ├── RagChatService.java
│   │   │                   │   └── SentimentAnalysisService.java
│   │   │                   └── RestaurantApplication.java
│   │   └── resources/
│   │       ├── application.yml                 # 主配置文件
│   │       ├── application-deepseek.yml        # DeepSeek 配置
│   │       ├── application-dmr.yml             # DMR 配置
│   │       ├── application-gemini.yml          # Gemini 配置
│   │       ├── application-groq.yml            # Groq 配置
│   │       ├── application-openrouter.yml      # OpenRouter 配置
│   │       ├── application-qwen.yml            # 通义千问配置
│   │       └── restaurant-knowledge.txt        # 餐厅知识库
│   └── test/
│       └── java/
│           └── cc/
│               └── chensoul/
│                   └── springai/
│                       └── restaurant/
│                           ├── model/            # 模型测试
│                           ├── util/             # 测试工具
│                           └── RestaurantApplicationTests.java
├── target/                           # 编译输出
├── .gitattributes
├── .gitignore
├── .sdkmanrc                         # SDKMAN 配置
├── AI_EXTENSIONS.md                  # AI 扩展功能文档
├── docker-compose.yml                # Docker 编排
├── LICENSE
├── mvnw                              # Maven Wrapper (Unix)
├── mvnw.cmd                          # Maven Wrapper (Windows)
├── pom.xml                           # Maven 配置
├── QUICKSTART.md                     # 本文件
├── README.md                         # 项目说明
└── renovate.json                     # 依赖更新配置
```

---

## 模块说明

### 1. Controller 层

| 控制器 | 职责 | 端点前缀 |
|--------|------|---------|
| `RestaurantRecommendationController` | 餐厅推荐、菜品生成、智能对话 | `/api/restaurants` |
| `RagController` | 文档加载、向量搜索、RAG 聊天 | `/api/rag` |
| `FunctionCallingController` | Function Calling 功能 | `/api/function-calling` |
| `StreamingController` | 流式响应（SSE） | `/api/streaming` |
| `AdvancedRagController` | 高级 RAG 功能 | `/api/advanced-rag` |
| `SentimentAnalysisController` | 情感分析 | `/api/sentiment` |
| `StructuredOutputController` | 结构化输出示例 | `/api/structured` |

### 2. Service 层

| 服务类 | 职责 | 核心方法 |
|--------|------|---------|
| `RagChatService` | RAG 聊天核心逻辑 | `chatWithRag()`, `searchSimilar()` |
| `DocumentService` | 文档加载与处理 | `loadDocuments()` |
| `AdvancedRagService` | 高级检索策略 | `rerankSearch()`, `hybridSearch()`, `multiQuerySearch()` |
| `FunctionCallingService` | 函数调用逻辑 | `chatWithFunctions()` |
| `SentimentAnalysisService` | 情感分析逻辑 | `analyzeSentiment()`, `batchAnalyze()` |

### 3. Model 层

| 模型类 | 字段 | 用途 |
|--------|------|------|
| `Restaurant` | id, name, cuisine, location, rating, description, priceRange, features | 餐厅信息 |
| `Dish` | id, name, description, cuisine, price, category, ingredients, dietaryInfo, calories, preparationTime, difficulty | 菜品信息 |
| `RecommendationRequest` | location, cuisine, priceRange, dietaryRestrictions, occasion, groupSize, timeOfDay, preferences | 推荐请求参数 |

### 4. Config 层

| 配置类 | 职责 |
|--------|------|
| `ChatConfig` | ChatClient 配置、Advisor 配置、ChatMemory 配置 |

---

## 开发规范

### 代码规范

1. **命名规范**
   - 类名：PascalCase（如 `RestaurantService`）
   - 方法名：camelCase（如 `findById`）
   - 常量：UPPER_SNAKE_CASE
   - 包名：全小写，使用反向域名（如 `cc.chensoul.springai.restaurant`）

2. **代码格式**
   - 使用 Spotless Maven 插件自动格式化
   - 缩进：4 个空格
   - 行尾：Unix 风格（LF）

3. **注释规范**
   - 类和方法必须添加 JavaDoc
   - 复杂逻辑添加行内注释
   - 使用中文注释（项目约定）

### Git 规范

1. **分支管理**
   - `main`：主分支，保持稳定
   - `feature/*`：功能分支
   - `bugfix/*`：修复分支

2. **提交信息**
   ```
   <type>: <subject>
   
   <body>
   
   <footer>
   ```
   
   Type 类型：
   - `feat`: 新功能
   - `fix`: 修复
   - `docs`: 文档
   - `style`: 格式
   - `refactor`: 重构
   - `test`: 测试
   - `chore`: 构建/工具

### 测试规范

1. **单元测试**
   - 使用 JUnit 5
   - 测试类命名：`XxxTest`
   - 测试方法命名：`shouldXxxWhenYyy`

2. **集成测试**
   - 使用 Testcontainers 进行数据库测试
   - 测试类：`RestaurantApplicationTests`

---

## API 接口文档

### 基础 API

#### 1. 餐厅推荐
```http
POST /api/restaurants/recommend
Content-Type: application/json

{
  "location": "北京市",
  "cuisine": "川菜",
  "priceRange": "100-200",
  "dietaryRestrictions": ["素食"],
  "occasion": "商务宴请",
  "groupSize": 4,
  "timeOfDay": "晚餐",
  "preferences": ["安静", "包间"]
}
```

**响应：**
```json
[
  {
    "id": 1,
    "name": "蜀九香火锅",
    "cuisine": "川菜",
    "location": "北京市朝阳区",
    "rating": 4.8,
    "description": "正宗川菜，环境优雅",
    "priceRange": "120-180",
    "features": ["包间", "停车方便"]
  }
]
```

#### 2. 生成菜品
```http
POST /api/restaurants/dishes/generate
Content-Type: application/json

{
  "cuisine": "川菜",
  "count": 5
}
```

#### 3. 智能对话
```http
POST /api/restaurants/chat
Content-Type: application/json

{
  "message": "推荐几家北京的好餐厅"
}
```

### RAG API

#### 1. 加载文档
```http
POST /api/rag/load
Content-Type: application/json

{
  "filePath": "classpath:restaurant-knowledge.txt"
}
```

#### 2. RAG 聊天
```http
POST /api/rag/chat
Content-Type: application/json

"北京有哪些好吃的川菜馆？"
```

#### 3. 向量搜索
```http
POST /api/rag/search
Content-Type: application/json

{
  "query": "川菜馆",
  "topK": 5
}
```

### AI 扩展 API

#### 1. Function Calling
```http
POST /api/function-calling/chat
Content-Type: application/json

{
  "message": "北京今天天气怎么样？"
}
```

#### 2. 流式聊天
```http
POST /api/streaming/chat
Content-Type: application/json
Accept: text/event-stream

{
  "message": "推荐餐厅"
}
```

#### 3. 情感分析
```http
POST /api/sentiment/analyze
Content-Type: application/json

{
  "text": "这家餐厅的服务非常好，菜品也很美味！"
}
```

#### 4. 高级 RAG - 混合搜索
```http
POST /api/advanced-rag/hybrid-search
Content-Type: application/json

{
  "query": "北京川菜馆",
  "topK": 5,
  "keywordWeight": 0.3
}
```

---

## 数据库设计

### PostgreSQL + PgVector

本项目使用 PostgreSQL 15 配合 pgvector 扩展作为向量数据库。

#### 连接配置

```yaml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: postgres
```

#### 向量存储配置

```yaml
spring:
  vectorstore:
    pgvector:
      index-type: ivfflat          # 索引类型
      distance-type: cosine_distance  # 距离计算方式
      dimensions: 1024             # 向量维度
      initialize-schema: true      # 自动创建表结构
      table-name: vector_store     # 表名
```

#### 向量表结构

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | uuid | 主键 |
| content | text | 文档内容 |
| metadata | json | 元数据 |
| embedding | vector(1024) | 向量嵌入 |

### 对话记忆存储

Spring AI 使用 JDBC 存储对话历史，自动创建以下表：

- `chat_memory`: 存储对话消息
- `chat_memory_conversation`: 存储会话信息

---

## 常见问题及解决方案

### 1. 应用启动失败

**问题：** `Connection refused: localhost/127.0.0.1:5432`

**原因：** PostgreSQL 数据库未启动

**解决：**
```bash
docker-compose up -d
```

### 2. API Key 错误

**问题：** `401 Unauthorized` 或 `Invalid API key`

**原因：** API Key 未配置或配置错误

**解决：**
1. 检查环境变量是否正确设置
2. 验证 API Key 是否有效
3. 查看 `application.yml` 配置

### 3. 向量维度不匹配

**问题：** `dimension mismatch`

**原因：** Embedding 模型维度与 PgVector 配置不一致

**解决：**
- OpenAI text-embedding-3-small: 1536 维
- Ollama mxbai-embed-large: 1024 维

修改 `application.yml`：
```yaml
spring:
  vectorstore:
    pgvector:
      dimensions: 1536  # 根据模型调整
```

### 4. 文档加载失败

**问题：** `FileNotFoundException: restaurant-knowledge.txt`

**解决：**
```java
// 使用 classpath 路径
documentService.loadDocuments("classpath:restaurant-knowledge.txt");
```

### 5. 流式响应无输出

**问题：** SSE 连接建立但没有数据返回

**原因：** 可能是网络超时或模型响应慢

**解决：**
- 检查 `SseEmitter` 超时设置（默认 60 秒）
- 验证 AI 模型服务是否正常
- 查看应用日志

### 6. Maven 构建失败

**问题：** `Could not find artifact org.springframework.ai:spring-ai-bom`

**解决：**
```bash
# 清理并重新构建
./mvnw clean install -U
```

### 7. Lombok 注解不生效

**问题：** `cannot find symbol` 或 `getter/setter` 方法不存在

**解决：**
1. IDE 中启用 Annotation Processing
2. 安装 Lombok 插件
3. 重新编译项目

### 8. Docker 端口冲突

**问题：** `Bind for 0.0.0.0:5432 failed: port is already allocated`

**解决：**
```bash
# 查找占用端口的进程
lsof -i :5432

# 停止冲突容器或修改 docker-compose.yml 端口映射
ports:
  - "5433:5432"  # 改为其他端口
```

---

## 参考资源

### 官方文档
- [Spring AI 官方文档](https://docs.spring.io/spring-ai/reference/)
- [Spring Boot 文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [PgVector 文档](https://github.com/pgvector/pgvector)

### 项目相关文章
- [基于 Spring AI 构建智能餐厅推荐系统：多模型集成的实践指南](https://blog.chensoul.cc/posts/2025/09/25/spring-ai-restaurant-showcase/)
- [基于 Spring AI 构建智能餐厅推荐系统：RAG 技术实战](https://blog.chensoul.cc/posts/2025/09/26/spring-ai-restaurant-showcase-rag/)

### 测试脚本

项目提供了测试脚本帮助快速验证功能：

```bash
# 测试 AI 扩展功能
./examples/test-ai-extensions.sh

# 测试 RAG 功能
./examples/test-rag.sh
```

---

## 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'feat: add amazing feature'`)
4. 推送分支 (`git push origin feature/amazing-feature`)
5. 创建 Pull Request

---

## 许可证

本项目采用 [MIT License](LICENSE) 许可证。

---

> 文档版本：v1.0 | 最后更新：2026-03-06
