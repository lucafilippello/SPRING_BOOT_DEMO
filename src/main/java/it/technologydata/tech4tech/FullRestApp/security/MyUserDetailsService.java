package it.technologydata.tech4tech.FullRestApp.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import it.technologydata.tech4tech.FullRestApp.entity.AppUser;
import it.technologydata.tech4tech.FullRestApp.repository.AppUserRepository;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private AppUserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails userDetails = null;
		Optional<AppUser> optUser = repository.findByUsername(username);
		if(optUser.isPresent()) {
			AppUser appUser = optUser.get();//.orElseThrow();
			userDetails = User
					.withUsername(username)
					.password(appUser.getPassword())
					.roles(appUser.getRoles().split(","))
					.build();
		} else {
			throw new UsernameNotFoundException("User "+ username+" not found");
		}
		
		return userDetails;
	}

}
