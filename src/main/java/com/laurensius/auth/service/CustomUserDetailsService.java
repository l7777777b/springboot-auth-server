package com.laurensius.auth.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laurensius.auth.domain.User;
import com.laurensius.auth.repository.UserRepository;
import com.laurensius.auth.security.UserAuthenticationException;


@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

	private final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String login) {

        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase();

        User userFromDatabase;
        try{
        userFromDatabase = userRepository.findOneByEmail(lowercaseLogin);
//        if(lowercaseLogin.contains("@")) {
//            userFromDatabase = userRepository.findByEmail(lowercaseLogin);
//        } else {
//            userFromDatabase = userRepository.findByUsernameCaseInsensitive(lowercaseLogin);
//        }
		if (userFromDatabase == null) {
            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
        } else if (userFromDatabase.isDeleted()) {
            throw new UserAuthenticationException("User " + lowercaseLogin + " is deleted");
        } else if (!userFromDatabase.getStatus().equalsIgnoreCase("CONFIRMED")){
        	throw new UserAuthenticationException("User " + lowercaseLogin + " is not confirmed");
        }
        } catch (Exception e) {
            throw new UsernameNotFoundException("database error ");
        }

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        grantedAuthorities.add(grantedAuthority);

        return new org.springframework.security.core.userdetails.User(userFromDatabase.getEmail(), userFromDatabase.getPassword(), grantedAuthorities);
//        return new UserRepositoryUserDetails(userFromDatabase);
    }
    
    
    private final static class UserRepositoryUserDetails extends User implements UserDetails {

		private static final long serialVersionUID = 1L;

		private UserRepositoryUserDetails(User user) {
			super(user);
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
	        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
	        grantedAuthorities.add(grantedAuthority);
			return grantedAuthorities;
		}

		@Override
		public String getUsername() {
			return getEmail();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

	}

    
}
