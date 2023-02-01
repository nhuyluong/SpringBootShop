package net.codejava;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository2 extends CrudRepository<User, Integer> {
    public Long countById(Integer id);

    @Query(value = "select * from users where email = ?1 and password = ?2", nativeQuery = true)
    public User findAdmin(String email, String password);
}
