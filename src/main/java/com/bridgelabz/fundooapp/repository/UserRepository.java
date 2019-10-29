package com.bridgelabz.fundooapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bridgelabz.fundooapp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmailId(String emailId);

	Optional<User> findByMobileNumber(Long mobileNumber);

	@Query(value = "select * from users where id in :userIds", nativeQuery = true)
	Optional<List<User>> findUsersIn(List<Long> userIds);

}
