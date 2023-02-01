package net.codejava;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {


    @Query(value = "select * from products p where p.name like '%áo%' or p.description like '%áo%'" , nativeQuery = true)
    public List<Product> findByAo();
    @Query(value = "select * from products p where p.name like '%áo thun%' or p.description like '%áo thun%'" , nativeQuery = true)
    public List<Product> findByAoThun();

    @Query(value = "select * from products p where p.name like '%áo khoác%' or p.description like '%áo khoác%'" , nativeQuery = true)
    public List<Product> findByAoKhoac();

    @Query(value = "select * from products p where p.name like '%quần%' or p.description like '%quần%'" , nativeQuery = true)
    public List<Product> findByQuan();

    @Query(value = "select * from products p where p.name like '%nón%' or p.description like '%nón%'" , nativeQuery = true)
    public List<Product> findByNon();

    @Query(value = "select * from products p where id = ?1" , nativeQuery = true)
    public List<Product> findAddById(Integer id);

    @Query(value = "select * from users  where name = ?1" , nativeQuery = true)
    public Product findIdByName(String name);

    @Query(value = "select id from users  where name = ?1" , nativeQuery = true)
    public Integer findIdUs(String name);

}
