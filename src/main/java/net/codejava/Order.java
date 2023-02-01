package net.codejava;

import javax.persistence.*;

@Entity
@Table(name = "oder")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Integer ido;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column( nullable = false, length = 200)
    private String location;

    @Column (name = "price", nullable = false, length = 20)
    private String price;

    @Column(nullable = false, length = 20)
    private String status;

    public Integer getIdo() {
        return ido;
    }

    public void setIdo(Integer ido) {
        this.ido = ido;
    }

    public Order() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdOrder() {
        return ido;
    }

    public void setIdOrder(Integer idOrder) {
        this.ido = idOrder;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Order( Integer idOrder, String phone, String location, String price, String status) {
        this.ido = idOrder;
        this.phone = phone;
        this.location = location;
        this.price = price;
        this.status = status;
    }
}
