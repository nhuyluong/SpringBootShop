package net.codejava;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cart")
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private Integer userid;
	
	@Column
	private Integer productid;
	
	@Column(nullable = false, length = 1000, name = "image")
    private String image;
    @Column(length = 200, nullable = false)
    private String name;
    @Column(nullable = false, name = "quantity")
    private Integer quantity;
    @Column(nullable = false, name = "price")
    private Double price;

    public Cart()
    {
    	super();
    }
	public Cart(Integer userid, Integer productid, String image, String name, Integer quantity, Double price) {
		super();
		this.userid = userid;
		this.productid = productid;
		this.image = image;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getProductid() {
		return productid;
	}

	public void setProductid(Integer productid) {
		this.productid = productid;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	
}
