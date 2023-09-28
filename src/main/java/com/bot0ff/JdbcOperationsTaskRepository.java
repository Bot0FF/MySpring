package com.bot0ff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcOperationsTaskRepository implements TaskRepository, RowMapper<Task> {

    @Autowired
    private JdbcOperations jdbcOperations;

    @Override
    public List<Task> findAll() {
        return this.jdbcOperations.query("select * from test", this);
    }

    @Override
    public void save(Task task) {
        this.jdbcOperations.update("""
                insert into test(id, c_details, c_completed) values (?,?,?)
                """, new Object[]{task.id(), task.details(), task.completed()});
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return this.jdbcOperations.query("select * from test where id = ?",
                new Object[]{id}, this).stream().findFirst();
    }

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Task(rs.getObject("id", UUID.class),
                rs.getString("c_details"),
                rs.getBoolean("c_completed"));
    }
}
