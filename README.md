# 🍽️ Spring AI 智能餐厅推荐系统 (Smart Restaurant AI Agent)

[![Java](https://img.shields.io/badge/Java-17%2B-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-Latest-blue)](https://spring.io/projects/spring-ai)
[![PgVector](https://img.shields.io/badge/Database-PgVector-blueviolet)](https://github.com/pgvector/pgvector)

## 📖 项目简介
本项目是一个基于 **Spring AI** 框架构建的智能化餐饮推荐应用。系统不仅展示了如何统一接入各类底层大模型（如 OpenAI、Mistral 以及通过 Ollama 本地部署的模型），还深度结合了具体的餐饮业务场景，实现了智能推荐、菜品生成与多轮点餐对话。



## ✨ 核心功能与业务场景
* **📍 智能餐厅推荐 (RAG 技术)**：利用检索增强生成技术，根据用户的个性化偏好（地理位置、菜系偏好、消费预算等），从私有数据库中精准检索并推荐餐厅。
* **🍳 AI 菜品生成与解析**：模型可自动生成极具吸引力的菜品描述、详细的营养成分信息以及制作工艺详情。
* **🤖 智能对话与意图识别 (Function Calling)**：支持多轮自然语言交互。AI 能够识别用户意图，自动调用后端工具类完成真实业务逻辑（如：查询营业状态、预订排号等）。
* **⚡ 流式响应体验**：支持 Streaming 流式数据输出，提供类似 ChatGPT 的极速响应体验。

## 🛠️ 技术架构体系
| 核心模块 | 技术选型 |
| :--- | :--- |
| **基础框架** | Java 17+, Spring Boot 3.x |
| **AI 编排框架** | Spring AI |
| **大模型支持** | OpenAI, Mistral, Ollama (本地模型) |
| **关系型/向量数据库** | PostgreSQL + PgVector 插件 |
| **构建工具** | Maven |
| **容器化部署** | Docker, Docker Compose |
