package ru.example.JWT_Auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ru.example.JWT_Auth.filter.JwtAuthenticationFilter;
import ru.example.JWT_Auth.model.enums.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
			UserDetailsService userDetailsService) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.userDetailsService = userDetailsService;
	}

	@Bean
	protected SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
    	return http
    		.csrf(csrf -> csrf.disable())
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
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptVersion.$2Y);
    }
  
}
