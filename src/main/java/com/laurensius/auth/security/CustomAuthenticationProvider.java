package com.laurensius.auth.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.laurensius.auth.domain.User;
import com.laurensius.auth.repository.UserRepository;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserRepository userRepository;

	private final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

	public CustomAuthenticationProvider() {
		log.info("*** CustomAuthenticationProvider created");
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.debug("custom authentication provider {}",authentication);
		String username = (String) authentication.getName(); 
		String password = (String) authentication.getCredentials();
		User user = userRepository.findOneByEmail(username);
		if (user != null){
			String encrypted = encrypt(password);
			if (user.getPassword().equals(encrypted)){
				if (user.isDeleted()) {
		            throw new UserAuthenticationException("User " + user.getEmail() + " is deleted");
		        } else if (!user.getStatus().equalsIgnoreCase("CONFIRMED")){
		        	throw new UserAuthenticationException("User " + user.getEmail() + " is not confirmed");
		        } else {
		        	Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
		            grantedAuthorities.add(grantedAuthority);
					return new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
		        }
			} else {
				throw new BadCredentialsException("Bad credentials");
			}
		}else{
			//you can put another custom authentication process here
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}
	

	public String encrypt(String plain) {
		MessageDigest md;
		String result = "";
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(plain.getBytes());
			byte byteData[] = md.digest();
	        //convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        result = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			log.error("MD5 algorithm not found");
		}
        return result;	
	}

}