//package net.codejava;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.List;
//
//@Controller
//public class productController {
//    @Autowired private ProductService service;
//
//    @GetMapping("/shophome")
//    public String showProductList(Model model) {
//        List<Product> listProduct = service.listProduct();
//        model.addAttribute("listProduct",listProduct);
//        return "shophome";
//    }
//
//}
