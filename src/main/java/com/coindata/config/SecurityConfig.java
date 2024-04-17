package com.coindata.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.coindata.repository.UserRepository;
import com.coindata.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JWTAuthEntryPoint unauthorizedHandler;

	@Bean
	JWTAuthTokenFilter authFilter() {
		return new JWTAuthTokenFilter();
	}

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	AuthenticationManager authManagerBean(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	DaoAuthenticationProvider authProviderBean(AuthenticationManagerBuilder authManagerBuilder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		if (null == userDetailsService) {
			provider.setUserDetailsService(new UserDetailsServiceImpl(userRepository));
		} else {
			provider.setUserDetailsService(userDetailsService);
		}
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandler))
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(requests -> requests.requestMatchers("/api/auth/**").permitAll()
						.requestMatchers("/api/**").permitAll().anyRequest().authenticated())
				.addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class).build();
	}
}