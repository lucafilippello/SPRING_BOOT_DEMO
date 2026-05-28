package it.technologydata.tech4tech.FullRestApp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import it.technologydata.tech4tech.FullRestApp.entity.AppUser;
import it.technologydata.tech4tech.FullRestApp.repository.AppUserRepository;
import it.technologydata.tech4tech.FullRestApp.security.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile("!test")
public class SecurityConfiguration {
	
	@Bean
	static RoleHierarchy roleHierarchy() {
	    return RoleHierarchyImpl.withDefaultRolePrefix()
	        .role("ADMIN").implies("HR")
	        .build();
	}
	
	@Bean
	static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setRoleHierarchy(roleHierarchy);
		return expressionHandler;
	}

	@Autowired 
	private BasicAuthenticationEntryPoint myAuthenticationEntryPoint;

	
	@Autowired
	public AccessDeniedHandler myAccessDeniedHandler;
	
	@Autowired
	private AppUserRepository repository;



    
    
    
    @Bean
    @Order(1)
    SecurityFilterChain employeeeSecurityfilterChain(HttpSecurity http) throws Exception {
        return http
        	.securityMatcher("/employee/**")
        	.sessionManagement(cust -> cust.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        	.csrf(cust -> cust.disable())
        	.authorizeHttpRequests(authorize -> //authorize.anyRequest().permitAll()
        			authorize.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
        			//.requestMatchers(HttpMethod.POST).hasRole("ADMIN")
        			.requestMatchers(HttpMethod.PUT).hasRole("ADMIN")
        			.requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")
        			.anyRequest().authenticated()
        			)
        	.exceptionHandling(conf -> conf.accessDeniedHandler(myAccessDeniedHandler))
            .httpBasic(conf -> conf.authenticationEntryPoint(myAuthenticationEntryPoint))
            .authenticationProvider(authenticationProvider())
            .build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain adminSecurityfilterChain(HttpSecurity http) throws Exception {
        return http
        	.securityMatcher("/user/**")
        	.sessionManagement(cust -> cust.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        	.csrf(cust -> cust.disable()) //usato solo per funzionamento con POSTMAN
        	//.csrf(cust -> cust.csrfTokenRepository(csrfTokenRepository()))
        	.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll()
        			//.requestMatchers("/swagger-ui/**", "/v3/api-docs/**","/h2-console/**").permitAll()
        			//.requestMatchers(HttpMethod.POST).hasRole("ADMIN")
        			//.requestMatchers("/**").hasRole("ADMIN")
        			//.anyRequest().denyAll()
        			)
        	.exceptionHandling(conf -> conf.accessDeniedHandler(myAccessDeniedHandler))
            .httpBasic(conf -> conf.authenticationEntryPoint(myAuthenticationEntryPoint))
            .authenticationProvider(authenticationProvider())
            .build();
    }
	
	//Custom user details service
	@Bean
    UserDetailsService userDetailsService() {
		AppUser user = AppUser.builder().username("hr1").password(passwordEncoder().encode("@hr1@")).roles("HR").build();
		repository.save(user);
		user = AppUser.builder().username("hr2").password(passwordEncoder().encode("@hr2@")).roles("HR").build();
		repository.save(user);
		user = AppUser.builder().username("admin1").password(passwordEncoder().encode("#admin1#")).roles("ADMIN").build();
		repository.save(user);
		user = AppUser.builder().username("admin2").password(passwordEncoder().encode("#admin2#")).roles("ADMIN").build();
		repository.save(user);

		return new MyUserDetailsService();
	}
    

    @Bean
    PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder();
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    //Creating custom AuthenticationProvider based on custom UserDetailsService and password encoder
    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;

    }
}
