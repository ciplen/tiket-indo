package com.panemu.tiketIndo.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtUtil {

	// Expried 5 minutes later
	private static final long EXPIRE_TIME = 5 * 60 * 1000;
	private static final String JWT_SECRET = "Lkjasdfoiu&*olakjsdlfkj*&9lkajsdf2098094)(*";
	private static final String TICKET_JWT_SECRET = "f191* a3f()%)be][594 b14794932";

	/**
	 * Checke TOKEN is OK or NOT
	 *
	 * @param token
	 * @param secret User password
	 * @return
	 */
	public static boolean verify(String token, String username) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
			JWTVerifier verifier = JWT.require(algorithm)
					  .withClaim("username", username)
					  .build();
			DecodedJWT jwt = verifier.verify(token);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	public static boolean verifyTicket(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(TICKET_JWT_SECRET);
			JWTVerifier verifier = JWT.require(algorithm)
					  .build();
			DecodedJWT jwt = verifier.verify(token);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	public static String getUsername(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim("username").asString();
		} catch (JWTDecodeException e) {
			return null;
		}
	}

	/**
	 * Generate TOKEN return it,the token will be expired 5 minutes later
	 *
	 * @param username
	 * @param secret
	 * @return TOKEN
	 */
	public static String sign(String username) {
//		Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
		Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
		return JWT.create()
				  .withClaim("username", username)
				  .sign(algorithm);
	}

	public static String signTicket(int id, String venueName, String ticketName, String ticketCode, int qty) {
		Algorithm algorithm = Algorithm.HMAC256(TICKET_JWT_SECRET);
		return JWT.create()
				  .withClaim("id", id + "")
				  .withClaim("venue", venueName)
				  .withClaim("ticket", ticketName)
				  .withClaim("ticketCode", ticketCode)
				  .withClaim("qty", qty + "")
				  .sign(algorithm);

	}

}
