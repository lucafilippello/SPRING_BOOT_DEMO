package it.technologydata.tech4tech.FullRestApp.exception;

import lombok.AllArgsConstructor;

@SuppressWarnings("serial")
@AllArgsConstructor
public class AppUserNotFoundException extends MyNotFoundException {
	
	public String notFound;
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "App User not found: "+notFound;
	}
}
