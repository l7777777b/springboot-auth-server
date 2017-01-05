package com.laurensius.auth.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import com.laurensius.auth.service.CustomUserDetailsService;

@Configuration
public class OAuth2Configuration {

	private static final String RESOURCE_ID = "test";
	
	@Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends
            ResourceServerConfigurerAdapter {

		@Autowired
		private TokenStore tokenStore;
		
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources
                    .resourceId(RESOURCE_ID);
            		//.tokenStore(tokenStore);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
//                    .antMatchers("/test").hasRole("ADMIN")
            		.authorizeRequests().antMatchers("/test/**").authenticated()
                    .and()
                    .csrf().disable()
                    .headers()
                    .frameOptions().disable()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

    }
	
	@Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter { // implements EnvironmentAware
		
//		private static final String ENV_OAUTH = "authentication.oauth.";
		
//		private RelaxedPropertyResolver propertyResolver;
		
		private Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
		
		@Autowired
	    @Qualifier("authenticationManagerBean")
	    private AuthenticationManager authenticationManager;
		
//	    @Autowired
//		private UserDetailsService userDetailsService;
	    
		@Autowired
        private DataSource dataSource;

        @Bean
        public TokenStore tokenStore() {
            return new JdbcTokenStore(dataSource);
        }
        
        @Bean
		protected AuthorizationCodeServices authorizationCodeServices() {
			return new JdbcAuthorizationCodeServices(dataSource);
		}
        
        @Override
		public void configure(AuthorizationServerSecurityConfigurer security)
				throws Exception {
        	security
        	.allowFormAuthenticationForClients()
        	.checkTokenAccess("isAuthenticated()");
//        	.checkTokenAccess("hasAuthority('Admin')");
			
		}
        
        @Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints)
				throws Exception {
			endpoints
//					.userDetailsService(userDetailsService)
					.authorizationCodeServices(authorizationCodeServices())
					.authenticationManager(authenticationManager)
					.tokenStore(tokenStore());
//					.approvalStoreDisabled();
		}
        
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        	clients
        	.jdbc(dataSource);
//        	.inMemory()
//            .withClient("clientid")
//            .scopes("read", "write")
//            .authorities("ROLE_ADMIN","ROLE_USER")
//            .authorizedGrantTypes("password", "refresh_token", "authorization_code", "implicit")
//            .secret("clientsecret")
//            .accessTokenValiditySeconds(600);
        }
        
//        @Bean
//        @Primary
//        public DefaultTokenServices tokenServices() {
//            DefaultTokenServices tokenServices = new DefaultTokenServices();
//            tokenServices.setSupportRefreshToken(true);
//            tokenServices.setTokenStore(tokenStore());
//            return tokenServices;
//        }

//        @Override
//        public void setEnvironment(Environment environment) {
//            this.propertyResolver = new RelaxedPropertyResolver(environment, ENV_OAUTH);
//        }

	}
	
}
