package com.leqtr.todolist.repository;

import com.leqtr.todolist.model.CacheData;
import org.springframework.data.repository.CrudRepository;

public interface CacheDataRepository extends CrudRepository<CacheData, String> {
}
