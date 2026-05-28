package it.technologydata.tech4tech.FullRestApp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import it.technologydata.tech4tech.FullRestApp.dto.AppUserDTO;
import it.technologydata.tech4tech.FullRestApp.entity.AppUser;
import it.technologydata.tech4tech.FullRestApp.exception.AppUserNotFoundException;
import it.technologydata.tech4tech.FullRestApp.exception.DuplicateUsernameException;
import it.technologydata.tech4tech.FullRestApp.repository.AppUserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Validated
@Slf4j
public class AppUserService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AppUserRepository repository;
	
	
	public List<AppUser> getAll() {
		
		List<AppUser> users = repository.findAll();
		 
		return users;
	}
	
	public AppUser saveNew(@Valid AppUserDTO dto) {

		log.info("INSERTING new user");
		if(repository.findByUsername(dto.getUsername()).isPresent()) {
			throw new DuplicateUsernameException(String.format("Username %s already used", dto.getUsername()));
		}
		AppUser newUser = dto.convertDtoToEntity();
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		return repository.saveAndFlush(newUser);

	}
	
	public AppUser getByUsername(String username) {
		log.info("Looking for username "+username);
		return repository.findByUsername(username).orElseThrow(() -> new AppUserNotFoundException(username));
	}
}
