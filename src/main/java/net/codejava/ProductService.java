package net.codejava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired private ProductRepository repo;
    
    @Autowired private UserRepository repos;
    @Autowired private UserRepository2 repo2;

    public List<Product> listProduct() {
        return (List<Product>) repo.findAll();
    }
    public void saveProduct(Product p) {
        repo.save(p);
    }
     
    public Product getProduct(Integer id) {
        return repo.findById(id).get();
    }
     
    public void deleteProduct(Integer id) {
        repo.deleteById(id);
    }
    

    public List<Product> findByAo() {
        return (List<Product>) repo.findByAo();
    }

    public List<Product> findByAoThun() {
        return (List<Product>) repo.findByAoThun();
    }

    public List<Product> findByAoKhoac() {
        return (List<Product>) repo.findByAoKhoac();
    }

    public List<Product> findByQuan() {
        return (List<Product>) repo.findByQuan();
    }

    public List<Product> findByNon() {
        return (List<Product>) repo.findByNon();
    }
    public List<Product> getL(Integer id)
    {
    	return (List<Product>) repo.findAddById(id);
    }

    
    public Product get(Integer id) throws UserNotFoundException {
        Optional<Product> idP =  repo.findById(id);
        if(idP.isPresent()) {
            return idP.get();
        }
        throw new UserNotFoundException("Can't find the user with id: " + id);
    }

    public Optional<Product> findById(Integer id) {
        return repo.findById(id);
    }

    public List<User> listUsers() {
        return (List<User>) repos.findAllUsers();
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long count = repo2.countById(id);
        if(count == null || count == 0) {
            throw new UserNotFoundException("Can't find the user with id: " + id);
        }
        repo2.deleteById(id);
    }

    public User CheckAdmin(String email, String password) {
        return repo2.findAdmin(email,password);
    }

    public Product FindIdByName(String name) {
        return repo.findIdByName(name);
    }

    public Integer FindIdUs(String name) {
        return repo.findIdUs(name);
    }



}
