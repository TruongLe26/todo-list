package dev.rlet.todoitem_service.repository;

import com.mongodb.client.result.UpdateResult;
import dev.rlet.todoitem_service.model.TodoItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomTodoItemRepositoryImpl implements CustomTodoItemRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public void updateTodoItemTitle(String id, String newTitle) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update();
        update.set("title", newTitle);

        UpdateResult result = mongoTemplate.updateFirst(query, update, TodoItem.class);
        System.out.println(result.getModifiedCount() + " document(s) updated..");
    }
}
