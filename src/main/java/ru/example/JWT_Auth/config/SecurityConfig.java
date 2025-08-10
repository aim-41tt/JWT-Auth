package ru.example.JWT_Auth.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import ru.example.JWT_Auth.filter.JwtAuthenticationFilter;
import ru.example.JWT_Auth.model.enums.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
			@Qualifier("databaseUserDetailsService") UserDetailsService userDetailsService) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.userDetailsService = userDetailsService;
	}

	@Bean
	protected SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
    	return http
    		.csrf(csrf -> csrf.disable())
    		.cors(Customizer.withDefaults())
    		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    		.httpBasic(httpBasic -> httpBasic.disable())
            .authorizeHttpRequests(authorize -> authorize
            		.requestMatchers("/api/admin/**").hasAuthority(Role.ADMIN.name())
            		.requestMatchers( "/api-docs/**", "/swagger-ui/**", "/api/v1/confirming/**", "/api/v1/auth/**")
            		.permitAll()
            		.anyRequest()
            		.authenticated()
                )
                .authenticationManager(authenticationManager(userDetailsService,passwordEncoder()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

	@Bean
	protected AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
	    return authentication -> {
	        String username = authentication.getName();
	        String password = authentication.getCredentials().toString();

	        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	        
	        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
	            throw new BadCredentialsException("Bad credentials");
	        }

	        return new UsernamePasswordAuthenticationToken(
	            userDetails,
	            password,
	            userDetails.getAuthorities()
	        );
	    };
	}
	
	@Bean
	protected CorsConfigurationSource corsConfigurationSource(@Value("${cors.allowed-origins}") String corsOrigins) {

	    CorsConfiguration config = new CorsConfiguration();

	    List<String> origins = Arrays.stream(corsOrigins.split(","))
	            .map(String::trim)
	            .toList();
	    
	    config.setAllowedOrigins(origins);
	    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	    config.setAllowedHeaders(List.of("*"));
	    config.setAllowCredentials(true);

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", config);
	    return source;
	}

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(
        		16,     // длина соли
        	    32,     // длина хэша
        	    2,      // параллелизм
        	    65536,  // память (в KB)
        	    4       // итерации
        	    );
    }
}
