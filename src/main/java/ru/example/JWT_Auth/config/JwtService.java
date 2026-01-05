package ru.example.JWT_Auth.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import io.jsonwebtoken.security.SignatureException;
import ru.example.JWT_Auth.model.User;

/**
 * краткое описание.
 *
 * <p>
 * Подробное описание
 * </p>
 *
 *
 * @author aim_41tt
 * @version 1.0
 * @since 02.05.2025
 */
@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private long jwtExpirationMs;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public UUID extractUserID(String token) {
		return UUID.fromString(extractClaim(token, Claims::getId));
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		if (claims == null) {
			return null;
		}
		return claimsResolver.apply(claims);
	}

	public String generateToken(User userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	/**
	 * @param extraClaims
	 * @param userDetails
	 * @return JWT
	 */
	public String generateToken(Map<String, Object> extraClaims, User userDetails) {
		extraClaims.put("email", userDetails.getEmail());
		extraClaims.put("verified", userDetails.getVerified());
		extraClaims.put("locked", userDetails.getLocked());
		return Jwts.builder()
				.setClaims(extraClaims)
				.setId(String.valueOf(userDetails.getId()))
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		if (username == null) {
			return false;
		}
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(getSignInKey())
					.build()
					.parseClaimsJws(token)
					.getBody();
		} catch (SignatureException e) {
			System.err.println("Invalid JWT signature: " + e.getMessage());
			return null;
		} catch (Exception e) {
			System.err.println("Token parsing error: " + e.getMessage());
			return null;
		}
	}

	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
