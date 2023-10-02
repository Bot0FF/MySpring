package com.bot0ff.decorator;

import org.springframework.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DecoratorBeans {

    @Bean
    public FindTaskByIdSpi findTaskByIdSpi(DataSource dataSource, Cache cache) {
        return new SpringCashingFindByIdDecorator(new FindTaskByIdSpiMappingSqlQuery(dataSource), cache);
    }
}
