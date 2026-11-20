package com.growmighty.lectures.firstday.tangledmonolith.cart;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    static CartItem create(Cart cart, Long productId, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.cart = cart;
        cartItem.productId = productId;
        cartItem.quantity = quantity;
        return cartItem;
    }

    void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
