package com.bot0ff.decorator;

import java.util.Optional;
import java.util.UUID;

@FunctionalInterface
public interface FindTaskByIdSpi {
    Optional<TaskData> findTaskById(UUID id);
}
