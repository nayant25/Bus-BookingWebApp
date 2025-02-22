package org.jsp.reservationapp.controller;

import org.jsp.reservationapp.dto.AdminRequest;
import org.jsp.reservationapp.dto.AdminResponse;
import org.jsp.reservationapp.dto.ResponseStructure;
import org.jsp.reservationapp.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/admins")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	

	@PostMapping
	public ResponseEntity<ResponseStructure<AdminResponse>> saveadmin( @Valid @RequestBody AdminRequest adminRequest,HttpServletRequest request){

		return adminService.saveAdmin(adminRequest,request);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseStructure<AdminResponse>> updateadmin(@RequestBody AdminRequest adminRequest,@PathVariable int id){
		return adminService.update(adminRequest, id);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<ResponseStructure<AdminResponse>> findByid(@PathVariable int id){
		return adminService.findById(id);
	}
	
	@PostMapping("/verify-by-phone")
	public ResponseEntity<ResponseStructure<AdminResponse>> verify(@RequestParam long phone,@RequestParam String password){
		return adminService.verify(phone, password);
	}
	
	@PostMapping("/verify-by-email")
	public ResponseEntity<ResponseStructure<AdminResponse>> verifyByEmail(@RequestBody AdminRequest loginRequest) {
		return adminService.verify(loginRequest.getEmail(), loginRequest.getPassword());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> delete(@PathVariable int id){
		return adminService.delete(id);
	}
	
	@GetMapping("/activate")
	public String activate(@RequestParam String token) {
		return adminService.activate(token);
	}
	
	@GetMapping("/forgot-password")
	public String forgotPassword(@RequestParam String email,HttpServletRequest request) {
		return adminService.forgotpassword(email, request);
	}

}
