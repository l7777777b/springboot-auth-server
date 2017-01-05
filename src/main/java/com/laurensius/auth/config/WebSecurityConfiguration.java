package com.laurensius.auth.config;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

import com.laurensius.auth.security.CustomAuthenticationProvider;


@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	
	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService userDetailsService;
    
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("endy").password("123").roles("ADMIN").and()
//                .withUser("anton").password("123").roles("STAFF");
//    }
	
	@Bean
    public Md5PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();
    }

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth
        	.authenticationProvider(customAuthenticationProvider);
//        	.userDetailsService(userDetailsService)
//        	.passwordEncoder(passwordEncoder());

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//            .antMatchers(HttpMethod.OPTIONS, "/**");
//    }
            
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
//    	.csrf().disable()
//    	.headers().disable()
    	.httpBasic().realmName("stem")
    	.and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .requestMatchers().antMatchers("/oauth/authorize")
        .and()
        .authorizeRequests()
        .antMatchers("/oauth/authorize").authenticated();
    }
    
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
    
//    @EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
//    private static class GlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration {
//        @Override
//        protected MethodSecurityExpressionHandler createExpressionHandler() {
//            return new OAuth2MethodSecurityExpressionHandler();
//        }
//
//    }
    
//    @Autowired private Oauth2LogoutHandler logoutHandler;
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true);
//        
//        http.authorizeRequests()
//                .antMatchers("/login.jsp").permitAll()
//                .and()
//                .formLogin()
//                    .loginPage("/login.jsp")
//                    .loginProcessingUrl("/j_spring_security_check")
//                    .usernameParameter("j_username")
//                    .passwordParameter("j_password")
//                .and()
//                    .logout()
//                    .logoutUrl("/j_spring_security_logout")
//                    .logoutSuccessHandler(logoutHandler);
//    }
    
    
}