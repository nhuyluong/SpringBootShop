package net.codejava;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Integer> {
	@Query("SELECT c FROM Cart c WHERE c.userid = ?1")
	public List<Cart> findByUserId(Integer userid);


	@Query(value = "select  * from cart where userid = ?1 and productid = ?2", nativeQuery = true)
	public Cart TrungLap( Integer usId, Integer proId);

	@Query(value = "select  * from cart where userid = ?1", nativeQuery = true)
	public List<Cart> detailOrder(Integer id);
}
