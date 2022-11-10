package com.rentacar.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rentacar.exception.message.ErrorMessages;



//@AllArgsConstructor
//@NoArgsConstructor

public class AuthTokenFilter extends OncePerRequestFilter{
// 	@RequiredArgsConstructor final olmayan objeleri enjecte etmiyor
	
	
	@Autowired
	private  JwtUtils jwtUtils;
	@Autowired
	private  UserDetailsService userDetailsService;
	
//	@Autowired
//	public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
//		
//		this.jwtUtils = jwtUtils;
//		this.userDetailsService = userDetailsService;
//	}

	Logger logger=LoggerFactory.getLogger(AuthTokenFilter.class);
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
		String jtwToken=parseJwt(request); 
		
		try {
			if (jtwToken!=null && jwtUtils.validateToken(jtwToken)) {
				String email=jwtUtils.getEmailFromToken(jtwToken);
				UserDetails userDetails=userDetailsService.loadUserByUsername(email);
				UsernamePasswordAuthenticationToken authenticationToken=
						new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		} catch (UsernameNotFoundException e) {
			logger.error(ErrorMessages.USER_NOT_FOUND_LOGGER,e.getMessage());
			e.printStackTrace();
		}
		
		filterChain.doFilter(request, response);
		
		
		
		
		
	}


	private String parseJwt(HttpServletRequest request) {
		String header=request.getHeader("Authorization");
		if(StringUtils.hasText(header) && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		
		return null;
		
	}
	
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		AntPathMatcher antPathMatcher=new AntPathMatcher();
		
		return antPathMatcher.match("/register", request.getServletPath()) ||
				antPathMatcher.match("/login", request.getServletPath()) ||	
		antPathMatcher.match("/contactmessage", request.getServletPath()) ;	
		
	}
	
	

}
