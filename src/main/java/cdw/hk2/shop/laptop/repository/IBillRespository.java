package cdw.hk2.shop.laptop.repository;

import cdw.hk2.shop.laptop.model.Cart_Item;
import org.springframework.data.jpa.repository.JpaRepository;

import cdw.hk2.shop.laptop.model.Bill;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface IBillRespository extends JpaRepository<Bill, Long>{
    @Query(value = "SELECT  (MONTH(c.pay_date)) AS month, (YEAR(c.pay_date)) AS year,sum(c.total_price) as total FROM cdwebshoplaptop.bills c GROUP BY month,year  ORDER BY month DESC,year DESC;",nativeQuery = true)
    List<Object> thongke();
    @Query(value = "SELECT  (MONTH(c.pay_date)) AS month, (YEAR(c.pay_date)) AS year,sum(c.total_price) as total FROM cdwebshoplaptop.bills c where (c.pay_date between ?1 and ?2) GROUP BY month,year  ORDER BY month DESC,year DESC;",nativeQuery = true)
    List<Object> thongkeThang(Date fromDate,Date toDate);

    @Query(value = "Select (MONTH(c.created_date)) AS month, (YEAR(c.created_date)) AS year,sum(c.total) as total FROM cdwebshoplaptop.orders c where ( c.processs = 0 or c.processs =1 ) GROUP BY month,year  ORDER BY month DESC,year DESC;",nativeQuery = true)
    List<Object> thongkeDTCTH();


    @Query(value = "SELECT  (MONTH(a.created_date)) AS month, (YEAR(a.created_date)) AS year,sum(c.price) as total, c.id, c.name " +
            "FROM cdwebshoplaptop.orders a, cdwebshoplaptop.orders_products b,cdwebshoplaptop.products c " +
            "where (a.created_date between ?1 and ?2) and a.id=b.orders_id and c.id=b.products_id and (c.name LIKE %?3%)" +
            "GROUP BY month,year,c.id ORDER BY month DESC,year DESC;",nativeQuery = true)
    List<Object> thongkesanpham(Date fromDate, Date toDate, String kw);

    @Query(value = "SELECT  (MONTH(a.created_date)) AS month, (YEAR(a.created_date)) AS year,sum(c.price) as total, c.id, c.name  FROM cdwebshoplaptop.orders a, cdwebshoplaptop.orders_products b,cdwebshoplaptop.products c  where a.id=b.orders_id and c.id=b.products_id GROUP BY month,year,c.id ORDER BY month DESC,year DESC;",nativeQuery = true)
    List<Object> thongkeallsanpham();
}
