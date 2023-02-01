package net.codejava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired private CartRepository repo;

    public List<Cart> listCart(Integer userid) {
        return (List<Cart>) repo.findByUserId(userid);
    }
    public void save(Cart cart) {
        repo.save(cart);
    }
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    public Cart checkTrung(Integer usId, Integer proId) {
        return repo.TrungLap( usId, proId);
    }

    public List<Cart> detailOrder(Integer id) {
        return repo.detailOrder(id);
    }
}
