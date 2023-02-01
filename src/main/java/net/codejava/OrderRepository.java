package net.codejava;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    @Query(value = "select  * from oder where id = ?1", nativeQuery = true)
    public Order updateStatus(Integer id);

}
