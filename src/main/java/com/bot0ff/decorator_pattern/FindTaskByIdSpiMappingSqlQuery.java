package com.bot0ff.decorator_pattern;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FindTaskByIdSpiMappingSqlQuery extends MappingSqlQuery<TaskData> implements FindTaskByIdSpi {

    public FindTaskByIdSpiMappingSqlQuery(DataSource ds) {
        super(ds, """
                select * from test where id = :id
                """);
        this.declareParameter(new SqlParameter("id", Types.OTHER));
        this.compile();
    }

    @Override
    public Optional<TaskData> findTaskById(UUID id) {
        return Optional.ofNullable(this.findObjectByNamedParam(Map.of("id", id)));
    }

    @Override
    protected TaskData mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TaskData(rs.getObject("id", UUID.class));
    }
}
