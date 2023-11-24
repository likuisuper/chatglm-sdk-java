curl -X POST \
        -H "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsInNpZ25fdHlwZSI6IlNJR04ifQ.eyJhcGlfa2V5IjoiNDI5NmI4YzdmZTQ3ZGRjYWViOWQwOGU2YmY4MDljMWUiLCJleHAiOjE3MDA2NDUwMDQxNzAsInRpbWVzdGFtcCI6MTcwMDY0MzIwNDE3NH0.jPGFML4rdU8wAoF6v7_aFW4jb-Z39SBqVNQ9FwYECD0" \
        -H "Content-Type: application/json" \
        -H "Accept: text/event-stream" \
        -d '{
        "top_p": 0.7,
        "sseFormat": "data",
        "temperature": 0.9,
        "incremental": true,
        "prompt": [
        {
        "role": "user",
        "content": "简单介绍下你自己"
        }
        ]
        }' \
  https://open.bigmodel.cn/api/paas/v3/model-api/chatglm_turbo/sse-invoke