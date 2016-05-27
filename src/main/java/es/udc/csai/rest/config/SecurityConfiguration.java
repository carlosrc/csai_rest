package es.udc.csai.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import es.udc.csai.rest.config.security.AuthoritiesConstants;
import es.udc.csai.rest.config.security.Http401UnauthorizedEntryPoint;
import es.udc.csai.rest.config.security.jwt.JWTConfigurer;
import es.udc.csai.rest.config.security.jwt.TokenProvider;
import es.udc.csai.rest.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private Http401UnauthorizedEntryPoint authenticationEntryPoint;

	@Autowired
	private UserService userService;

	@Autowired
	private TokenProvider tokenProvider;

	// Encoding de la contraseña desactivado
	// @Bean
	// public PasswordEncoder passwordEncoder() {
	// return new BCryptPasswordEncoder();
	// }

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService);
		// .passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**").antMatchers("/client/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		
		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
		
		http.csrf().disable().headers().frameOptions().disable();
				
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				
		http.authorizeRequests()
		
			.antMatchers("/api/login").permitAll()
			.antMatchers("/api/public/**").permitAll()
			
			.antMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
			.antMatchers("/api/user/**").hasAuthority(AuthoritiesConstants.USER)
			
			.antMatchers("/api/protected/**").authenticated();

		http.apply(securityConfigurerAdapter());
		
		// @formatter:on
	}

	private JWTConfigurer securityConfigurerAdapter() {
		return new JWTConfigurer(tokenProvider);
	}

}
