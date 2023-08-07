package project.messageApp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyMessageRepository extends CrudRepository<MyMessage, Long > {
}
