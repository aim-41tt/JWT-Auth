package ru.example.JWT_Auth.config;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import io.jsonwebtoken.security.SignatureException;
import ru.example.JWT_Auth.model.User;

@Service
public class JwtService {

	private static final String SECRET_KEY = Base64.getEncoder().encodeToString("51e8ea280b44e16934d4d611901f3d3afc41789840acdff81942c2f65009cd52".getBytes());
	private static final long TOKEN_EXPIRATION_MS = 1000 * 60 * 60 * 24;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
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
		extraClaims.put("id", userDetails.getId());
		extraClaims.put("email", userDetails.getEmail());
		extraClaims.put("verified", userDetails.getVerified());
		extraClaims.put("role", userDetails.getRole());
		return Jwts.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_MS))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
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
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
