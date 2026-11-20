package com.growmighty.lectures.firstday.tangledmonolith.cart;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(nullable = false)
    private Long userId;

    public static Cart create(Long userId) {
        Cart cart = new Cart();
        cart.userId = userId;
        return cart;
    }

    public void addItem(Long productId, int quantity) {
        validateQuantity(quantity);
        Optional<CartItem> existing = findItem(productId);
        if (existing.isPresent()) {
            existing.get().increaseQuantity(quantity);              // 있으면 합산
        } else {
            items.add(CartItem.create(this, productId, quantity));  // 없으면 새로 추가
        }
    }

    /** 수량 변경 */
    public void changeQuantity(Long productId, int quantity) {
        validateQuantity(quantity);
        getItemOrThrow(productId).changeQuantity(quantity);
    }

    /** 상품 빼기 */
    public void removeItem(Long productId) {
        items.remove(getItemOrThrow(productId));
    }

    public void clear() {
        this.items.clear();
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    private Optional<CartItem> findItem( Long productId ) {
        return items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();
    }

    private CartItem getItemOrThrow(Long productId) {
        return findItem(productId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니에 없는 상품입니다. productId=" + productId));
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다. quantity=" + quantity);
        }
    }

}
