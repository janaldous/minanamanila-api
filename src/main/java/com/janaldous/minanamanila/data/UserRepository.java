package com.janaldous.minanamanila.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	List<User> findByAuth0Id(String auth0Id);

}
