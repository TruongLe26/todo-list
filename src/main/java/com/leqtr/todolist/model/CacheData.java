package com.leqtr.todolist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@Accessors(chain = true)
@RedisHash("cacheData")
public class CacheData {

    @Id
    private String key;

    @Indexed
    private String value;

}
