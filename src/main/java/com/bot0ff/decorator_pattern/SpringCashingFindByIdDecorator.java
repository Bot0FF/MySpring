package com.bot0ff.decorator_pattern;

import org.springframework.cache.Cache;

import java.util.Optional;
import java.util.UUID;

public class SpringCashingFindByIdDecorator extends CashingFindByIdDecorator{

    private final Cache cache;

    public SpringCashingFindByIdDecorator(FindTaskByIdSpi delegate, Cache cache) {
        super(delegate);
        this.cache = cache;
    }

    @Override
    Optional<TaskData> retrieveFromCache(UUID id) {
        return Optional.ofNullable(this.cache.get(id, TaskData.class));
    }

    @Override
    void storeInCache(TaskData task) {
        this.cache.put(task.id(), task);
    }
}
