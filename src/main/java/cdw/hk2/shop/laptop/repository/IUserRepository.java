package cdw.hk2.shop.laptop.repository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import cdw.hk2.shop.laptop.dto.AccountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cdw.hk2.shop.laptop.model.Role;
import cdw.hk2.shop.laptop.model.User;


@Repository
@Transactional
public interface IUserRepository extends JpaRepository<User, Long>{
	@Query(value =  "Select * FROM account WHERE account.email = ?1",nativeQuery = true)
	Object findAccountByEmail(String email);
	@Query(value =  "SELECT * FROM cdwebshoplaptop.roles where roles.id== ?1",nativeQuery = true)
	Role findRoleById(long id);
	@Query(value ="SELECT * FROM cdwebshoplaptop.users where date(users.created_date)=date(?1)",nativeQuery = true)
	List<User> findAllByDate(Date date);


	@Query(value =  "Select * FROM account WHERE account.email = ?1",nativeQuery = true)
	AccountDto findAccountByEmail1(String email);

	@Query(value =  "SELECT * FROM cdwebshoplaptop.users where users.account_id== ?1",nativeQuery = true)
	User findUserByAccountId(long id);


}
