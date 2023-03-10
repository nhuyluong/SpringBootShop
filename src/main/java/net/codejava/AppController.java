package net.codejava;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AppController {
//
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CartService cartservice;
	
	@Autowired 
	private ProductService productService;

	@Autowired private ContactRepository contactRepo;

	@Autowired private OrderRepository orderRepo;
	
	@GetMapping("")
	public String viewHomePage() {
		return "login";
	}
	
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		
		return "signup_form";
	}
	
	@PostMapping("/process_register")
	public String processRegister(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		userRepo.save(user);
		
		return "login";
	}


	@GetMapping("/cart/null")
	public String backToLogin(Model model) {
		return "redirect:/login";
	}
	@GetMapping("/cart")
	public String openCart(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    String currentUserName = authentication.getName();
		    model.addAttribute("usName", currentUserName);

			// L???y id ????? qua cart ko b??? l???i khi thanh to??n
			Product product = productService.FindIdByName(currentUserName);
			if (product != null) {
				System.out.println(product.getId());
				model.addAttribute("id", product.getId());
			}

			// L???y list cart d???a v??o th??ng qua id c???a ng?????i ????ng nh???p
		    User user = userRepo.findByEmail(currentUserName);
		    List<Cart> listCart = cartservice.listCart(user.getId());
			model.addAttribute("listCart",listCart);
		    return "cart";
		}
		return "redirect:/login";
	}
	
	@GetMapping("/cart/{id}")
	public String showCartList(Model model, @PathVariable int id) {
		//Ki???m tra ???? ????ng nh???p ch??a
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
		    model.addAttribute("usName", currentUserName);

		    //L???y th??ng tin s???n ph???m khi b???m add
		    List<Product> listProduct = productService.getL(id);
		    Product product = listProduct.get(0);

		    //L???y t??n ng?????i d??ng ??ang ????ng nh???p
		    User user = userRepo.findByEmail(currentUserName);

		    //T???o c??c bi???n ????? add v??o Cart
		    Integer userIdAdd = user.getId();
		    Integer productIdAdd = product.getId();
		    String imageAdd = product.getImage();
		    String nameAdd = product.getName();
		    Integer quantityAdd = 1;
		    Double priceAdd = product.getPrice();

			if(cartservice.checkTrung(userIdAdd, productIdAdd ) != null) {
				Cart updateCart = cartservice.checkTrung(userIdAdd, productIdAdd );
				updateCart.setQuantity(updateCart.getQuantity() + 1);
				cartservice.save(updateCart);
			} else {
				Cart newCart = new Cart(userIdAdd, productIdAdd, imageAdd, nameAdd, quantityAdd, priceAdd);
				cartservice.save(newCart);
			}

		    List<Cart> oldListCart = cartservice.listCart(user.getId());
		    //????a newCart v??o database

		    List<Cart> listCart = cartservice.listCart(user.getId());

			model.addAttribute("id",userIdAdd);
			model.addAttribute("listCart",listCart);
			return "cart";
		}
		return "redirect:/login";
	}

	@GetMapping("/cart/cartDetail")
	public String cartDetail(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		User user = userRepo.findByEmail(currentUserName);
	    List<Cart> listCart = cartservice.listCart(user.getId());
		double result = 0.0;
		if(listCart.size() != 0) {
			for (int i = 0; i < listCart.size(); i++) {
				int num = listCart.get(i).getQuantity();
				Double price = listCart.get(i).getPrice();
				result += price * num;
			}
		}
		String kq = String.valueOf(result);
		switch(kq.length()) {
			case 6:
				kq = kq.substring(0, 1) + "." + kq.substring(1, kq.length());
				break;
			case 7:
				kq = kq.substring(0, 2) + "." + kq.substring(2, kq.length());
				break;
			case 8:
				kq = kq.substring(0, 3) + "." + kq.substring(3, kq.length());
				break;
			case 9:
				kq = kq.substring(0, 1) + "." + kq.substring(1, 4) + "." + kq.substring(4, kq.length());
				break;
			default:
				kq = kq;
		}
		model.addAttribute("total",kq);
		model.addAttribute("listCart",listCart);
		return "cart_detail";
	}
	@PostMapping("/sendCart")
	public String SendCart(String tongtien, String sdt, String location, Integer id) {
		Order order = new Order(id,sdt, location,tongtien,"Ch??? x??c nh???n" );
		orderRepo.save(order);
		return "shophome";
	}
	
	//Delete Selected User
	@RequestMapping("/cart/delete/{id}")
	public String deleteCartElement(@PathVariable(name = "id") int id) {
	    cartservice.delete(id);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
//			model.addAttribute("usName", currentUserName);
		}
	    return "redirect:/cart";
	}

	//Edit Selected User
//	@RequestMapping("/cart/{id}")
//	public ModelAndView editUserPage(@PathVariable(name = "id") int id) {
//	    ModelAndView mav = new ModelAndView("cart");
//	    List<Cart> listCart = cartservice.listCart(7);
//	    Cart cart = cartservice.get(id);
//	    mav.addObject("user", user);
//	    return mav;
//	}
	
	

	@GetMapping("/vechungtoi")
	public String aboutUs() {
		return "vechungtoi";
	}

	@GetMapping("/contact")
	public String Contact() {
		return "contact";
	}


	//C?? th??? xem c??c d??? li???u ??? trang html b???ng thao t??c b??n d?????i
	//th:utext=${usName}: L???y mail ng?????i d??ng
	//[[${#request.userPrincipal.principal.fullId}]]: L???y id ng?????i d??ng(Xem trong class custom user detail)
	
	@GetMapping("/shophome")
	public String showProductList(Model model) {
		//L???y list s???n ph???m v?? g???i d??? li???u qua View b???ng bi???n listProduct
		List<Product> listProduct = productService.listProduct();
		model.addAttribute("listProduct",listProduct);
		
		//Ki???m tra ???? ????ng nh???p ch??a
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			//L???y th??ng tin user hi???n t???i ???? ????ng nh???p th??ng qua class CustomUserDetails
		    CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
		    String currentUserName = currentUser.getFullName();

			// L???y id ????? qua cart ko b??? l???i khi thanh to??n
			int idU = productService.FindIdUs(currentUserName);
			if (idU != 0) {
				model.addAttribute("id", idU);
			}

		    //????a t??n ng?????i ????ng nh???p qua View b???ng bi???n usName

		    model.addAttribute("usName", currentUserName);
		    model.addAttribute("loginOk", "Ok");

		}
		//N???u kh??ng c?? ????ng nh???p, m???c ?????nh l?? Kh??ch xem
		else model.addAttribute("loginNotOk", "Not Ok");
		return "shophome";
	}

	@GetMapping("/shophome/ao")
	public String findByAo(Model model) {
		List<Product> listProduct = productService.findByAo();
		model.addAttribute("listProduct",listProduct);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
		    String currentUserName = currentUser.getFullName();
		    model.addAttribute("usName", currentUserName);
		}
		else model.addAttribute("usName", "Kh??ch xem");
		return "shophome";
	}
	@GetMapping("/shophome/aothun")
	public String filterAoThun(Model model) {
		List<Product> listProduct = productService.findByAoThun();
		model.addAttribute("listProduct",listProduct);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
		    String currentUserName = currentUser.getFullName();
		    model.addAttribute("usName", currentUserName);
		}
		else model.addAttribute("usName", "Kh??ch xem");
		return "shophome";
	}

	@GetMapping("/shophome/aokhoac")
	public String findByAoKhoac(Model model) {
		List<Product> listProduct = productService.findByAoKhoac();
		model.addAttribute("listProduct",listProduct);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
		    String currentUserName = currentUser.getFullName();
		    model.addAttribute("usName", currentUserName);
		}
		else model.addAttribute("usName", "Kh??ch xem");
		return "shophome";
	}

	@GetMapping("/shophome/quan")
	public String findByQuanJean(Model model) {
		List<Product> listProduct = productService.findByQuan();
		model.addAttribute("listProduct",listProduct);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
		    String currentUserName = currentUser.getFullName();
		    model.addAttribute("usName", currentUserName);
		}
		else model.addAttribute("usName", "Kh??ch xem");
		return "shophome";
	}

	@GetMapping("/shophome/non")
	public String findByNon(Model model) {
		List<Product> listProduct = productService.findByNon();
		model.addAttribute("listProduct",listProduct);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
		    String currentUserName = currentUser.getFullName();
		    model.addAttribute("usName", currentUserName);
		}
		else model.addAttribute("usName", "Kh??ch xem");
		return "shophome";
	}
	
	@GetMapping("infPro/{id}")
	public String findByIdProduct(@PathVariable("id") Integer id, Model model) throws UserNotFoundException {
		Product product = productService.get(id);
		model.addAttribute("product", product);
		return "infPro";
	}

	@GetMapping("/shophome/{id}")
	public String showInfoProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes re) {
		try {
			Product product = productService.get(id);
			model.addAttribute("product", product);
			model.addAttribute("id",id );
			return "redirect:/infPro/{id}";
		} catch (UserNotFoundException e) {
//			re.addFlashAttribute(e.getMessage());
			return "redirect:/shophome";
		}
	}

	@GetMapping("/admin")
	public String adminPage() {
		return "admin_page_home";
	}

	@GetMapping("/admin/manageUser")
	public String adminPageManageUser(Model model) {
		List<User> listUsers = productService.listUsers();
		model.addAttribute("listUsers",listUsers);
		return "manageUser";
	}

	@GetMapping("/admin/delete/{id}")
	public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes re) {
		try {
			re.addFlashAttribute("message","The user " + id + " has been deleted.");
			productService.delete(id);
		} catch (UserNotFoundException e) {
			re.addFlashAttribute("message",e.getMessage());
		}
		return "redirect:/admin/manageUser";
	}
	
	//Quan Ly Product
	@GetMapping("/admin/manageProduct")
	public String adminPageManageProduct(Model model) {
		List<Product> listProduct = productService.listProduct();
		model.addAttribute("listProduct",listProduct);
		return "manageProduct";
	}
	
	//Add Product
	@RequestMapping("/admin/manageProduct/newProduct")
	public String addProductPage(Model model) {
	    Product product = new Product();
	    model.addAttribute("product", product);
	    return "new_product";
	}
	//Edit Selected Product
	@RequestMapping("/admin/manageProduct/editProduct/{id}")
	public ModelAndView editProductPage(@PathVariable(name = "id") int id) {
	    ModelAndView mav = new ModelAndView("edit_product");
	    Product product = productService.getProduct(id);
	    mav.addObject("product", product);
	    return mav;
	}
	
	//Delete Selected Product
	@RequestMapping("/admin/manageProduct/deleteProduct/{id}")
	public String deleteSelectedProduct(@PathVariable(name = "id") int id, RedirectAttributes re) {
		productService.deleteProduct(id);
		re.addFlashAttribute("message","The product " + id + " has been deleted.");
	    return "redirect:/admin/manageProduct";
	}
	
	//Save Change Product
	@RequestMapping(value = "/admin/manageProduct/save", method = RequestMethod.POST)
	public String saveProduct(@ModelAttribute("product") Product product) {
	    productService.saveProduct(product);     
	    return "redirect:/admin/manageProduct";
	}

	//Admin Login
	@GetMapping("/admin_login")
	public String loginPageAdmin() {
		return "admin_login";
	}

	@PostMapping("/admin_login/login")
	public String checkAdmin(String email, String password) {
		if (email.equals("admin@gmail.com") && !email.trim().isEmpty() && !password.trim().isEmpty()) {
			User user = productService.CheckAdmin(email, password);
			if (user != null) {
				return "admin_page_home";
			}
		}
		return "admin_login";
	}

// star here
	@PostMapping("/contact/send_contact")
	public String SendSuccess(String name, String email, String subject, String message) {
		Contact contact = new Contact(name, email, subject, message );
		contactRepo.save(contact);
		return "shophome";
	}


	@GetMapping("/admin/userResponse")
	public String UserResponse(Model model) {
		List<Contact> listContact = (List<Contact>) contactRepo.findAll();
		model.addAttribute("listContact",listContact);
		return "manageResponse";
	}

	@GetMapping("/admin/manageOrder")
	public String ManageOrders(Model model) {
		List<Order> listOrder = (List<Order>) orderRepo.findAll();
		model.addAttribute("listOrder",listOrder);
		return "manageO";
	}

	@GetMapping("/admin/manageOrder/delete/{id}")
	public String deleteOrder(@PathVariable("id") Integer id, RedirectAttributes re) {
		if(id != 0) {
			re.addFlashAttribute("message","The order " + id + " has been deleted.");
			orderRepo.deleteById(id);
		}
		return "redirect:/admin/manageOrder";
	}

	@GetMapping("/admin/manageOrder/accept/{id}")
	public String acceptOrder(@PathVariable("id") Integer id, RedirectAttributes re) {
		if(id != 0) {
			Order order = orderRepo.updateStatus(id);
			order.setStatus("???? x??c nh???n");
			orderRepo.save(order);
			re.addFlashAttribute("message","The order " + id + " has been accepted.");
		}
		return "redirect:/admin/manageOrder";
	}

	@GetMapping("/admin/manageOrder/detail/{id}/{price}")
	public String detailOrder(@PathVariable("id") Integer id,@PathVariable("price") String price, Model model) {
		System.out.println(id);
		if(id != 0) {
			List<Cart> listDetail = cartservice.detailOrder(id);
			System.out.println(listDetail.size());
			model.addAttribute("listDetail", listDetail);
			model.addAttribute("total", price);
		}
		return "detailOrder";
	}

}
