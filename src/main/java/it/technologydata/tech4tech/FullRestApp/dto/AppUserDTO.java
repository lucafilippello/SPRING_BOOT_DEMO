package it.technologydata.tech4tech.FullRestApp.dto;

import it.technologydata.tech4tech.FullRestApp.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppUserDTO {
	
	private Integer id;
	
	private String username;
	
	private String password;
	
	private String roles;
	
	public AppUser convertDtoToEntity() {
		
		return AppUser.builder()
		.id(this.id)
		.username(this.username)
		.password(this.password)
		.roles(this.roles)
		.build();
	}

}
