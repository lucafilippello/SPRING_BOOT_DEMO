package it.technologydata.tech4tech.FullRestApp.utility;

import java.text.SimpleDateFormat;

public class AppConstants {
	
	public static SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	static {
		OUTPUT_DATE_FORMAT.setLenient(false);
	}
	
	public static SimpleDateFormat REQUEST_PATH_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	static {
		REQUEST_PATH_DATE_FORMAT.setLenient(false);
	}

}
