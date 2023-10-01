package com.bot0ff;

import com.bot0ff.decorator_pattern.FindTaskByIdSpi;
import com.bot0ff.decorator_pattern.SpringCashingFindByIdDecorator;
import com.bot0ff.decorator_pattern.TaskData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class SpringCashingFindByIdDecoratorTest {

    @Mock
    FindTaskByIdSpi delegate;

    @Mock
    Cache cache;

    @InjectMocks
    SpringCashingFindByIdDecorator decorator;

    @Test
    void findTaskById_TaskIsCached_ReturnsNotEmptyOptional() {
        //given
        var taskId = UUID.randomUUID();
        var task = new TaskData(taskId);

        doReturn(task).when(this.cache).get(taskId, TaskData.class);

        //when
        var optional = this.decorator.findTaskById(taskId);

        //then
        assertEquals(Optional.of(task), optional);
        verify(this.cache, never()).put(any(), any());
    }

    @Test
    void findByTaskId_TaskIsNotCachedByExist_ReturnsNotEmptyOptional() {
        //given
        var taskId = UUID.randomUUID();
        var task = new TaskData(taskId);

        doReturn(Optional.of(task)).when(this.delegate).findTaskById(taskId);

        //when
        var optional = this.decorator.findTaskById(taskId);

        //then
        assertEquals(Optional.of(task), optional);
        verify(this.cache).put(taskId, task);
    }

    @Test
    void findTaskById_TaskDoesNotExist_ReturnsEmptyOptional() {
        //given
        var taskId = UUID.randomUUID();

        //when
        var optional = this.decorator.findTaskById(taskId);

        //then
        assertEquals(Optional.empty(), optional);
        verify(this.cache, never()).put(any(), any());
    }
}
