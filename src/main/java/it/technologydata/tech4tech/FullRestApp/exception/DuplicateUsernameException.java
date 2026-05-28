package it.technologydata.tech4tech.FullRestApp.exception;

import lombok.AllArgsConstructor;

@SuppressWarnings("serial")
@AllArgsConstructor
public class DuplicateUsernameException extends RuntimeException {
	
	private String username;
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Duplicated username found: "+username;
	}

}
