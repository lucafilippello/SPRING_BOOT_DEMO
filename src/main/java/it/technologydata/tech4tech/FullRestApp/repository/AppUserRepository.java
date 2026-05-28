package it.technologydata.tech4tech.FullRestApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.technologydata.tech4tech.FullRestApp.entity.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Integer> {
	
	Optional<AppUser> findByUsername(String username);
	
	
	
	

}
