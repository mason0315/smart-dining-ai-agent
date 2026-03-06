# API 测试示例

## 1. 餐厅推荐

### 基本推荐
```bash
curl -X POST http://localhost:8080/api/restaurants/recommend \
  -H "Content-Type: application/json" \
  -d '{
    "location": "北京市",
    "cuisine": "中餐",
    "priceRange": "中等价位",
    "dietaryRestrictions": ["素食"],
    "occasion": "商务聚餐",
    "groupSize": 4,
    "timeOfDay": "晚餐"
  }'
```

### 简单推荐
```bash
curl -X POST http://localhost:8080/api/restaurants/recommend \
  -H "Content-Type: application/json" \
  -d '{
    "location": "上海市",
    "cuisine": "日料"
  }'
```

## 2. 菜品生成

```bash
# 直接使用中文字符，无需编码
curl -X POST "http://localhost:8080/api/restaurants/dishes/generate" \
  -H "Content-Type: application/json" \
  -d '{"cuisine": "中餐", "count": 3}'

curl -X POST "http://localhost:8080/api/restaurants/dishes/generate" \
  -H "Content-Type: application/json" \
  -d '{"cuisine": "川菜", "count": 5}'

curl -X POST "http://localhost:8080/api/restaurants/dishes/generate" \
  -H "Content-Type: application/json" \
  -d '{"cuisine": "日料", "count": 4}'

curl -X POST "http://localhost:8080/api/restaurants/dishes/generate" \
  -H "Content-Type: application/json" \
  -d '{"cuisine": "西餐", "count": 3}'

curl -X POST "http://localhost:8080/api/restaurants/dishes/generate" \
  -H "Content-Type: application/json" \
  -d '{"cuisine": "韩料", "count": 5}'
```

## 3. 智能建议

```bash
# 直接使用中文字符，无需编码
curl -X POST "http://localhost:8080/api/restaurants/advice" \
  -H "Content-Type: application/json" \
  -d '{"query": "适合情侣约会的餐厅"}'

curl -X POST "http://localhost:8080/api/restaurants/advice" \
  -H "Content-Type: application/json" \
  -d '{"query": "家庭聚餐推荐"}'

curl -X POST "http://localhost:8080/api/restaurants/advice" \
  -H "Content-Type: application/json" \
  -d '{"query": "低卡路里餐厅推荐"}'

curl -X POST "http://localhost:8080/api/restaurants/advice" \
  -H "Content-Type: application/json" \
  -d '{"query": "best restaurants for business dinner"}'
```

## 4. 多语言聊天

```bash
# 直接使用中文字符，无需编码
curl -X POST "http://localhost:8080/api/restaurants/chat" \
  -H "Content-Type: application/json" \
  -d '{"message": "推荐一家好吃的火锅店", "language": "zh"}'

curl -X POST "http://localhost:8080/api/restaurants/chat" \
  -H "Content-Type: application/json" \
  -d '{"message": "推荐一家好吃的川菜馆", "language": "zh"}'

curl -X POST "http://localhost:8080/api/restaurants/chat" \
  -H "Content-Type: application/json" \
  -d '{"message": "有哪些好吃的日本菜", "language": "zh"}'

curl -X POST "http://localhost:8080/api/restaurants/chat" \
  -H "Content-Type: application/json" \
  -d '{"message": "recommend a good Italian restaurant", "language": "en"}'

curl -X POST "http://localhost:8080/api/restaurants/chat" \
  -H "Content-Type: application/json" \
  -d '{"message": "What are the best seafood restaurants?", "language": "en"}'
```

## 5. 餐厅详情

```bash
curl "http://localhost:8080/api/restaurants/1/details"
```

## 6. 使用 Postman 测试

### 导入集合
1. 打开 Postman
2. 点击 Import
3. 选择 "Raw text" 并粘贴以下 JSON：

```json
{
  "info": {
    "name": "Spring AI Restaurant API",
    "description": "智能餐厅推荐系统 API 测试集合"
  },
  "item": [
    {
      "name": "推荐餐厅",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"location\": \"北京市\",\n  \"cuisine\": \"中餐\",\n  \"priceRange\": \"中等价位\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/restaurants/recommend",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "restaurants", "recommend"]
        }
      }
    },
    {
      "name": "生成菜品",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"cuisine\": \"中餐\",\n  \"count\": 3\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/restaurants/dishes/generate",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "restaurants", "dishes", "generate"]
        }
      }
    },
    {
      "name": "获取建议",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"query\": \"适合情侣约会的餐厅\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/restaurants/advice",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "restaurants", "advice"]
        }
      }
    },
    {
      "name": "多语言聊天",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"message\": \"推荐一家好吃的川菜馆\",\n  \"language\": \"zh\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/restaurants/chat",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "restaurants", "chat"]
        }
      }
    },
    {
      "name": "获取餐厅详情",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/restaurants/1/details",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "restaurants", "1", "details"]
        }
      }
    }
  ]
}
```