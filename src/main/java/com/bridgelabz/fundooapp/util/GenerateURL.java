package com.bridgelabz.fundooapp.util;

public class GenerateURL {

	public static String getUrl(String subDirectory, Long id) {
		return "http://localhost:4200/" + subDirectory + "/" + JWTUtil.generateToken(id);

	}

}