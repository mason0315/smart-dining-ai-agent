#!/bin/bash

# Spring AI 餐厅推荐系统 - AI 扩展功能测试脚本

BASE_URL="http://localhost:8080"

echo "=========================================="
echo "Spring AI 扩展功能测试"
echo "=========================================="
echo ""

# 颜色定义
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 1. Function Calling 测试
echo -e "${BLUE}1. Function Calling 测试${NC}"
echo "----------------------------------------"
curl -X POST "${BASE_URL}/api/function-calling/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "北京今天天气怎么样？我想去附近的川菜馆，帮我查一下营业时间"
  }' | jq '.'
echo ""
echo ""

# 2. 流式响应测试（仅显示开始部分）
echo -e "${BLUE}2. 流式响应测试${NC}"
echo "----------------------------------------"
echo "注意: 流式响应需要使用支持 SSE 的客户端"
echo "示例: curl -N -X POST \"${BASE_URL}/api/streaming/chat\" -H \"Content-Type: application/json\" -d '{\"message\":\"推荐几家北京的好餐厅\"}'"
echo ""

# 3. 高级 RAG - Re-ranking 测试
echo -e "${BLUE}3. 高级 RAG - Re-ranking 测试${NC}"
echo "----------------------------------------"
curl -X POST "${BASE_URL}/api/advanced-rag/rerank" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "北京最好的川菜馆",
    "topK": 10,
    "topN": 5
  }' | jq '. | length'
echo ""
echo ""

# 4. 高级 RAG - 混合搜索测试
echo -e "${BLUE}4. 高级 RAG - 混合搜索测试${NC}"
echo "----------------------------------------"
curl -X POST "${BASE_URL}/api/advanced-rag/hybrid-search" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "北京川菜馆",
    "topK": 5,
    "keywordWeight": 0.3
  }' | jq '. | length'
echo ""
echo ""

# 5. 高级 RAG - 多查询测试
echo -e "${BLUE}5. 高级 RAG - 多查询测试${NC}"
echo "----------------------------------------"
curl -X POST "${BASE_URL}/api/advanced-rag/multi-query" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "北京好吃的餐厅",
    "topK": 5
  }' | jq '. | length'
echo ""
echo ""

# 6. 高级 RAG 聊天测试
echo -e "${BLUE}6. 高级 RAG 聊天测试${NC}"
echo "----------------------------------------"
curl -X POST "${BASE_URL}/api/advanced-rag/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "北京有哪些值得推荐的餐厅？"
  }' | jq '.'
echo ""
echo ""

# 7. 情感分析 - 单条评论测试
echo -e "${BLUE}7. 情感分析 - 单条评论测试${NC}"
echo "----------------------------------------"
curl -X POST "${BASE_URL}/api/sentiment/analyze" \
  -H "Content-Type: application/json" \
  -d '{
    "review": "这家餐厅的菜品非常好吃，服务也很周到，强烈推荐！"
  }' | jq '.'
echo ""
echo ""

# 8. 情感分析 - 批量分析测试
echo -e "${BLUE}8. 情感分析 - 批量分析测试${NC}"
echo "----------------------------------------"
curl -X POST "${BASE_URL}/api/sentiment/batch-analyze" \
  -H "Content-Type: application/json" \
  -d '{
    "reviews": [
      "菜品很好吃，服务也不错",
      "价格有点贵，但味道还可以",
      "环境很好，推荐大家来"
    ]
  }' | jq '.'
echo ""
echo ""

echo -e "${GREEN}=========================================="
echo "测试完成！"
echo "==========================================${NC}"
