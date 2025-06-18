package com.example.jdbc;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Map;

@SpringBootApplication
public class JdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(JdbcApplication.class, args);
    }

    @Bean
    ApplicationRunner dataSourceRunner(DataSource dataSource) {
        return _ -> System.out.println(dataSource.toString());
    }

    @Bean
    ApplicationRunner dogDataRunner(Map<String, DogService> dogServiceMap) {
        return _ -> dogServiceMap
                .forEach((beanName, dogService) ->
                        System.out.println(beanName + " : " + dogService.all()));
    }


}


@Service
class JdbcClientDogService implements DogService {

    private final JdbcClient db;

    private final RowMapper<Dog> dogRowMapper = (rs, _) ->
            new Dog(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("owner"),
                    rs.getString("description"));

    JdbcClientDogService(DataSource db) {
        this.db = JdbcClient.create(db);
    }

    @Override
    public Collection<Dog> all() {
        return db
                .sql("select * from dog")
                .query(this.dogRowMapper)
                .list();
    }

    @Override
    public Dog findById(Integer id) {
        return db.sql("select * from dog where id = ? ")
                .query(this.dogRowMapper)
                .single();
    }
}

@Service
class DataJdbcRepositoryDogService implements DogService {

    private final DogRepository repository;

    DataJdbcRepositoryDogService(DogRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<Dog> all() {
        return this.repository //
                .findAll();
    }

    @Override
    public Dog findById(Integer id) {
        return this.repository
                .findById(id) //
                .orElse(null);
    }
}

interface DogService {
    Collection<Dog> all();

    Dog findById(Integer id);
}

record Dog(int id, String name, String owner, String description) {
}

interface DogRepository extends ListCrudRepository<Dog, Integer> {
}