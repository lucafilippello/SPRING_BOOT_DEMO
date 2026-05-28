package it.technologydata.tech4tech.FullRestApp.entity;

import it.technologydata.tech4tech.FullRestApp.dto.AppUserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="APP_USER")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="USERNAME", length=50, nullable=false, unique=true)
	private String username;
	
	@Column(name="PASSWORD", nullable=false, unique=false)
	private String password;
	
	@Column(name="ROLES", length=50, nullable=false, unique=false)
	private String roles;
	
	public AppUserDTO convertEntityToDto() {
		
		return AppUserDTO.builder()
		//.id(this.id)
		.username(this.username)
		.password(this.password)
		.roles(this.roles)
		.build();
		
	}
	
	
	
}
