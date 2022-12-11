package cdw.hk2.shop.laptop.services;


import cdw.hk2.shop.laptop.model.*;
import cdw.hk2.shop.laptop.repository.ICartItemRespository;
import cdw.hk2.shop.laptop.repository.ICartRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class CartItemServices {

    @Autowired
    private ICartItemRespository iCartItemRespository;

    @Autowired
    private ProductServices productServices;
    @Autowired
    private UserServiceImpl userService;

    @Autowired


    public void test()
    {
//        Product product = productServices.findByIdProduct(5);
//        User user = userService.findUserById(5);
//        Cart_Item cart_item = new Cart_Item();
//        cart_item.setUser(user);
//        cart_item.setProduct(product);
//        cart_item.setQuantity(1);
//        Cart_Item  c=iCartItemRespository.save(cart_item);

    }

    public Cart_Item findCartById(long id) {
        return iCartItemRespository.findById(id).orElse(null);

    }
    public List<Cart_Item> findCartByUserId(long id) {
        return iCartItemRespository.findProductByUserId(id);

    }


    public Optional<Cart_Item> findCartByProductId(long id) {
        return iCartItemRespository.findProductByProductId(id);

    }
    public Cart_Item findCartByProductId1(long id) {
        return iCartItemRespository.findProductByProductId1(id);

    }
    public Cart_Item saveCartItem(Cart_Item cart_item) {
        return iCartItemRespository.save(cart_item);

    }
    public void deleteCartItem(long id)
    {
        iCartItemRespository.deleteCartByProduct(id);
    }

    public void delete(long id) {
       iCartItemRespository.deleteById(id);
    }

}
