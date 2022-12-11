package cdw.hk2.shop.laptop.repository;


import cdw.hk2.shop.laptop.model.Cart_Item;
import cdw.hk2.shop.laptop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICartItemRespository extends JpaRepository<Cart_Item,Long> {

    @Query(value = "SELECT * FROM cdwebshoplaptop.cart_items where cart_items.user_id= ?1",nativeQuery = true)
    List<Cart_Item> findProductByUserId(Long id);
    @Query(value = "SELECT * FROM cdwebshoplaptop.cart_items where cart_items.product_id= ?1",nativeQuery = true)
    Optional<Cart_Item> findProductByProductId(Long id);
    @Query(value = "SELECT * FROM cdwebshoplaptop.cart_items where cart_items.product_id= ?1",nativeQuery = true)
    Cart_Item findProductByProductId1(Long id);

    @Modifying
    @Query(value = " DELETE FROM cdwebshoplaptop.cart_items WHERE cart_items.product_id= ?1", nativeQuery = true)
    void deleteCartByProduct(long id);
}
