package com.project.ToDoList_App.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class SecurityConfig  {
	private UserDetailsService userDetails;
	
	public SecurityConfig(UserDetailsService userDetails) {
		super();
		this.userDetails = userDetails;
	}
	@Bean
	
	public SecurityFilterChain sfc (HttpSecurity http) {
		http.csrf(csrf->csrf.disable());
		http.authorizeHttpRequests(auth->auth.requestMatchers("/register","/login","/error").permitAll().anyRequest().authenticated());
		http.formLogin(form->
		form.loginPage("/login")
		.defaultSuccessUrl("/",true)
		.permitAll())
		.logout(logout->
		logout.logoutSuccessUrl("/login?logout")
		.permitAll());
		return http.build();
	}
	@Bean
	public AuthenticationProvider authProvider() {
	DaoAuthenticationProvider provider=new DaoAuthenticationProvider(userDetails);
	provider.setPasswordEncoder(new BCryptPasswordEncoder(12));

	return provider;
	}

}
