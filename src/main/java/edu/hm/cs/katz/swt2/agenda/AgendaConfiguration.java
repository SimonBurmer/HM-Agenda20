package edu.hm.cs.katz.swt2.agenda;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring Boot Configuration. 
 * 
 * @author katz.bastian
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class AgendaConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
        .antMatchers("/assets/**", "/", "/login**", "/logout**", "/h2-console/**").permitAll()
		.anyRequest().fullyAuthenticated()
		.and().formLogin().and()
        .logout().logoutSuccessUrl("/").permitAll();
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}
	
}
