package org.jsp.reservationapp.service;

import static org.jsp.reservationapp.util.ApplicationConstants.ADMIN_RESET_PASSWORD_LINK;
import static org.jsp.reservationapp.util.ApplicationConstants.ADMIN_VERIFY_LINK;
import static org.jsp.reservationapp.util.ApplicationConstants.USER_RESET_PASSWORD_LINK;
import static org.jsp.reservationapp.util.ApplicationConstants.USER_VERIFY_LINK;

import org.jsp.reservationapp.dao.AdminDao;
import org.jsp.reservationapp.dao.UserDao;
import org.jsp.reservationapp.model.Admin;
import org.jsp.reservationapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;

@Service
public class LinkGeneratorService {
	
	@Autowired
	private AdminDao adminDao;
	
	@Autowired
	private UserDao userDao;
	
	public String getActivationLink(Admin admin,HttpServletRequest request) {
		admin.setToken(RandomString.make(45));
//		admin.setStatus(AccountStatus.IN_ACTIVE.toString());
		adminDao.saveAdmin(admin);
		String siteurl=request.getRequestURL().toString();
		return siteurl.replace(request.getServletPath(), ADMIN_VERIFY_LINK+admin.getToken());
	}
	
	public String getUserActivationLink(User user,HttpServletRequest request) {
		String siteUrl = request.getRequestURL().toString();
		return siteUrl.replace(request.getServletPath(), USER_VERIFY_LINK + user.getToken());
	}
	public String getResetPasswordLink(Admin admin,HttpServletRequest request) {
		admin.setToken(RandomString.make(45));
		adminDao.saveAdmin(admin);
		String siteUrl=request.getRequestURL().toString();
		return siteUrl.replace(request.getServletPath(), ADMIN_RESET_PASSWORD_LINK+admin);
	}
	
	public String getResetPasswordLink(User user,HttpServletRequest request) {
		user.setToken(RandomString.make(45));
		userDao.saveUser(user);
		String siteUrl=request.getRequestURL().toString();
		return siteUrl.replace(request.getServletPath(), USER_RESET_PASSWORD_LINK+user);
	}

}