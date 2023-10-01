package com.bot0ff.adapter_pattern;

import java.util.Optional;
import java.util.UUID;

@FunctionalInterface
public interface FindTaskByIdSpi {
    Optional<TaskData> findTaskById(UUID id);
}
