package com.bridgelabz.fundooapp.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.Verification;

public class JWTUtil {

	private JWTUtil() {
	}

	private static final String SECRET_PASS = "password1234@";

	public static String generateToken(Long id) {
		Algorithm algorithm = Algorithm.HMAC256(SECRET_PASS);
		return JWT.create().withClaim("Id", id).sign(algorithm);
	}

	public static Long verifyToken(String token) {
		Verification verification = JWT.require(Algorithm.HMAC256(SECRET_PASS));
		JWTVerifier jwtVerifier = verification.build();
		DecodedJWT decodedJWT = jwtVerifier.verify(token);
		Claim claim = decodedJWT.getClaim("Id");
		return claim.asLong();
	}
}
