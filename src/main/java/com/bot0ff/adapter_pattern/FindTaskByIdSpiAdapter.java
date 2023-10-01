package com.bot0ff.adapter_pattern;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FindTaskByIdSpiAdapter implements FindTaskByIdSpi, RowMapper<TaskData> {

    private final NamedParameterJdbcOperations jdbcOperations;

    public FindTaskByIdSpiAdapter(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Optional<TaskData> findTaskById(UUID id) {
        return this.jdbcOperations.query("select * from test where id = :id",
                Map.of("id", id), this).stream().findFirst();
    }

    @Override
    public TaskData mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TaskData(rs.getObject("id", UUID.class));
    }
}
