package com.bot0ff;

import com.bot0ff.adapter.FindTaskByIdSpiAdapter;
import com.bot0ff.adapter.TaskData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
public class FindTaskByIdSpiMappingSqlQueryTest {

    @Mock
    NamedParameterJdbcOperations jdbcOperations;

    @InjectMocks
    FindTaskByIdSpiAdapter adapter;

    @Test
    void findTaskById_CallsQuery_ReturnsOptional() {
        //given
        var id = UUID.randomUUID();
        var task = new TaskData(id);

        doReturn(List.of(task)).when(this.jdbcOperations).query("select * from test where id = :id",
                Map.of("id", id), this.adapter);

        //when
        var optional = this.adapter.findTaskById(id);

        //then
        assertEquals(Optional.of(task), optional);
    }
}
