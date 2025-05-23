package com.leqtr.todolist.aspect;

import com.leqtr.todolist.annotation.CacheEvictAfter;
import com.leqtr.todolist.configuration.SecurityUtil;
import com.leqtr.todolist.service.impl.TodoItemCacheServiceImpl;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CacheEvictAspect {

    private final TodoItemCacheServiceImpl todoItemCacheServiceImpl;
    private final SecurityUtil securityUtil;
    private static final Logger logger = LoggerFactory.getLogger(CacheEvictAspect.class);

    @AfterReturning(pointcut = "@annotation(cacheEvictAfter)", returning = "result")
    public void evictCache(JoinPoint joinPoint, CacheEvictAfter cacheEvictAfter, Object result) {
        String username = securityUtil.getSessionUser();
        logger.info("Aspect triggered after method: {} with user: {}", joinPoint.getSignature(), username);

        String itemId = null;
        if (cacheEvictAfter.includeId()) {
            for (Object arg : joinPoint.getArgs()) {
                if (arg instanceof String) {
                    itemId = (String) arg;
                    break;
                }
            }
        }

        if (itemId != null) {
            logger.info("Invalidating cache for user: {}, itemId: {}", username, itemId);
            todoItemCacheServiceImpl.invalidateAllForUser(username, itemId);
        } else {
            logger.info("Invalidating cache for user: {}", username);
            todoItemCacheServiceImpl.invalidateAllForUser(username);
        }
    }
}