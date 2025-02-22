package org.jsp.reservationapp.service;

import java.util.Optional;

import org.jsp.reservationapp.dao.AdminDao;
import org.jsp.reservationapp.dto.AdminRequest;
import org.jsp.reservationapp.dto.AdminResponse;
import org.jsp.reservationapp.dto.EmailConfiguration;
import org.jsp.reservationapp.dto.ResponseStructure;
import org.jsp.reservationapp.exception.AdminNotFoundException;
import org.jsp.reservationapp.model.Admin;
import org.jsp.reservationapp.util.AccountStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AdminService{
	
	
	@Autowired
	private AdminDao adminDao;
	
	@Autowired
	private ReservationApiMailService mailService;
	
	@Autowired
	private LinkGeneratorService linkGeneratorService;
	
	@Autowired
	private EmailConfiguration emailConfiguration;
	
	private Admin mapToAdmin(AdminRequest adminRequest) {
		return Admin.builder().email(adminRequest.getEmail()).name(adminRequest.getName())
				.phone(adminRequest.getPhone()).gst_number(adminRequest.getGst_number())
				.travels_name(adminRequest.getTravels_name()).password(adminRequest.getPassword())
				.build();
	}
	
	private AdminResponse mapToAdminResponse(Admin admin) {
		return AdminResponse.builder().name(admin.getName()).email(admin.getEmail()).gst_number(admin.getGst_number()).phone(admin.getPhone()).password(admin.getPassword()).id(admin.getId()).build();
	}
	
	public ResponseEntity<ResponseStructure<AdminResponse>> saveAdmin(AdminRequest adminRequest,HttpServletRequest request){
		ResponseStructure<AdminResponse> structure = new ResponseStructure<>();
		Admin admin = mapToAdmin(adminRequest);
		admin.setStatus(AccountStatus.IN_ACTIVE.toString());
		admin = adminDao.saveAdmin(admin);
		String activation_link = linkGeneratorService.getActivationLink(admin, request);
		emailConfiguration.setSubject("Activate Your Account");
		emailConfiguration.setText(
				"Dear Admin Please Activate Your Account by clicking on the following link:" + activation_link);
		emailConfiguration.setToAddress(admin.getEmail());
		structure.setMessage(mailService.sendMail(emailConfiguration));
		structure.setData(mapToAdminResponse(admin));
		structure.setStatus(HttpStatus.CREATED.value());
		return ResponseEntity.status(HttpStatus.CREATED).body(structure);
	}
	
	public ResponseEntity<ResponseStructure<AdminResponse>> update(AdminRequest adminRequest,int id){
		Optional<Admin> recAdmin=adminDao.findById(id);
		ResponseStructure<AdminResponse> structure=new ResponseStructure<>();
		if(recAdmin.isPresent()) {
			Admin dbAdmin=recAdmin.get();
			dbAdmin.setEmail(adminRequest.getEmail());
			dbAdmin.setGst_number(adminRequest.getGst_number());
			dbAdmin.setName(adminRequest.getName());
			dbAdmin.setPhone(adminRequest.getPhone());
			dbAdmin.setPassword(adminRequest.getPassword());
			dbAdmin.setTravels_name(adminRequest.getTravels_name());
			structure.setData(mapToAdminResponse(adminDao.saveAdmin(dbAdmin)));
			structure.setMessage("Admin Updated");
			structure.setStatus(HttpStatus.ACCEPTED.value());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(structure);
			
		}
		throw new AdminNotFoundException("Cannot Updated Admin as ID is invalid");
	}
	
	public ResponseEntity<ResponseStructure<AdminResponse>> findById(int id) {
		ResponseStructure<AdminResponse> structure = new ResponseStructure<>();
		Optional<Admin> dbAdmin = adminDao.findById(id);
		if (dbAdmin.isPresent()) {
			structure.setData(mapToAdminResponse(dbAdmin.get()));
			structure.setMessage("Admin Found");
			structure.setStatus(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
		}
		throw new AdminNotFoundException("Invalid Admin Id");
	}
	
	public ResponseEntity<ResponseStructure<AdminResponse>> verify(long phone, String password) {
		ResponseStructure<AdminResponse> structure = new ResponseStructure<>();
		Optional<Admin> dbAdmin = adminDao.verify(phone, password);
		if (dbAdmin.isPresent()) {
			structure.setData(mapToAdminResponse(dbAdmin.get()));
			structure.setMessage("Verification Succesfull");
			structure.setStatus(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
		}
		throw new AdminNotFoundException("Invalid Phone Number And Password");
	}
	
	public ResponseEntity<ResponseStructure<AdminResponse>> verify(String email, String password) {
	    ResponseStructure<AdminResponse> structure = new ResponseStructure<>();
	    Optional<Admin> dbAdmin = adminDao.verify(email, password);
	    if (dbAdmin.isPresent()) {
	        structure.setData(mapToAdminResponse(dbAdmin.get()));
	        structure.setMessage("Verification Successful");
	        structure.setStatus(HttpStatus.OK.value());
	        return ResponseEntity.status(HttpStatus.OK).body(structure);
	    }
	    throw new AdminNotFoundException("Invalid Email Id or Password");
	}
	
	public ResponseEntity<ResponseStructure<String>> delete(int id) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		Optional<Admin> dbAdmin = adminDao.findById(id);
		if (dbAdmin.isPresent()) {
			adminDao.delete(id);
			structure.setData("Admin Found");
			structure.setMessage("Admin deleted");
			structure.setStatus(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
		}
		throw new AdminNotFoundException("Cannot delete Admin as Id is Invalid");
	}
	
	public String activate(String token) {
		Optional<Admin> recAdmin=adminDao.findByToken(token);
		if(recAdmin.isEmpty()) {
			throw new AdminNotFoundException("Invalid Token");
		}
		Admin dbAdmin=recAdmin.get();
		dbAdmin.setStatus("ACTIVE");
		dbAdmin.setToken(null);
		adminDao.saveAdmin(dbAdmin);
		
		return "Your Account has been activated";
		
	}
	
	public String forgotpassword(String email,HttpServletRequest request) {
		Optional<Admin> recAdmin=adminDao.findByEmail(email);
		if(recAdmin.isEmpty())
			throw new AdminNotFoundException("Invalid Email Id");
		Admin admin=recAdmin.get();
		String resetPassword=linkGeneratorService.getResetPasswordLink(admin, request);
		emailConfiguration.setToAddress(email);
		emailConfiguration.setText("RESET Password Link Has Been Send Please Follow The Given Link"+resetPassword);
		emailConfiguration.setSubject("RESET PASSWORD LINK SEND");
		mailService.sendMail(emailConfiguration);
		
		return "Reset Password link Has Been Send To Entered Mail Id";
	}
	
	public AdminResponse verifyLink(String token) {
		Optional<Admin> recAdmin=adminDao.findByToken(token);
		if(recAdmin.isEmpty())
			throw new AdminNotFoundException("Link Is Invalid");
		Admin dbAdmin=recAdmin.get();
		dbAdmin.setToken(null);
		adminDao.saveAdmin(dbAdmin);
		return mapToAdminResponse(dbAdmin);
	}
	
		
}
