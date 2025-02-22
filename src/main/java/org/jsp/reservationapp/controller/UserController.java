package org.jsp.reservationapp.controller;

import org.jsp.reservationapp.dto.ResponseStructure;
import org.jsp.reservationapp.dto.UserRequest;
import org.jsp.reservationapp.dto.UserResponse;
import org.jsp.reservationapp.model.User;
import org.jsp.reservationapp.service.UserService;
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

@CrossOrigin
@RestController
@RequestMapping("api/users")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(@RequestBody UserRequest userRequest) {
		return userService.saveUser(userRequest);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseStructure<UserResponse>> updateUser(@RequestBody UserRequest userRequest,
			@PathVariable int id) {
		return userService.update(userRequest, id);
	}

	@GetMapping("{id}")
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(@PathVariable int id) {
		return userService.findById(id);
	}


	@PostMapping("/verify-by-email")
	public ResponseEntity<ResponseStructure<User>> verify(@RequestBody UserRequest request) {
	    return userService.verify(request.getEmail(), request.getPassword());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> delete(@PathVariable int id) {
		return userService.delete(id);
	}
	

}
