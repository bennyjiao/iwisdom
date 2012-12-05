/**
 *  OpenKM, Open Document Management System (http://www.openkm.com)
 *  Copyright (c) 2006-2012  Paco Avila & Josep Llort
 *
 *  No bytes were intentionally harmed during the development of this application.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.openkm.spring;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.openkm.module.db.stuff.DbSessionManager;

/**
 * http://www.mkyong.com/spring-security/get-current-logged-in-username-in-spring-security/
 * 
 * @author pavila
 */
public class PrincipalUtils {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(PrincipalUtils.class);
	
	/**
	 * Obtain the logged user.
	 */
	public static String getUser() {
		String user = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth != null) {
			user = auth.getName();
		}
		
		return user;
	}
	
	/**
	 * Obtain the list of user roles.
	 */
	public static Set<String> getRoles() {
		Set<String> roles = new HashSet<String>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth != null) {
			for (GrantedAuthority ga : auth.getAuthorities()) {
				roles.add(ga.getAuthority());
			}
		}
		
		return roles;
	}
	
	/**
	 * Check for role
	 */
	public static boolean hasRole(String role) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth != null) {
			User user = (User) auth.getPrincipal();
			
			for (GrantedAuthority ga : user.getAuthorities()) {
				if (ga.getAuthority().equals(role)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Get user from SessionManager and set the credentials.
	 */
	public static String getUserByToken(String token) {
		Authentication auth = DbSessionManager.getInstance().get(token);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String user = auth.getName();
		return user;
	}
	
	/**
	 * 
	 */
	public static Authentication getAuthenticationByToken(String token) {
		Authentication auth = DbSessionManager.getInstance().get(token);
		SecurityContextHolder.getContext().setAuthentication(auth);
		return auth;
	}
	
	/**
	 * Obtain authentication token
	 */
	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	/**
	 * Set authentication token
	 */
	public static void setAuthentication(Authentication auth) {
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
