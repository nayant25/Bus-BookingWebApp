package org.jsp.reservationapp.service;

import java.util.Optional;

import org.jsp.reservationapp.dao.UserDao;
import org.jsp.reservationapp.dto.ResponseStructure;
import org.jsp.reservationapp.dto.UserRequest;
import org.jsp.reservationapp.dto.UserResponse;
import org.jsp.reservationapp.exception.UserNotFoundException;
import org.jsp.reservationapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;

	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequest userRequest) {
		ResponseStructure<UserResponse> structure = new ResponseStructure<>();
		User user = mapToUser(userRequest);

		if (user.getStatus() == null || user.getStatus().isEmpty()) {
			user.setStatus("ACTIVE"); // Ensure default value
		}
		structure.setMessage("User saved");
		structure.setData(maptoUserResponse(userDao.saveUser(mapToUser(userRequest))));
		structure.setStatus(HttpStatus.CREATED.value());
		return ResponseEntity.status(HttpStatus.CREATED).body(structure);
	}

	public ResponseEntity<ResponseStructure<UserResponse>> update(UserRequest userRequest, int id) {
		Optional<User> recUser = userDao.findById(id);
		ResponseStructure<UserResponse> structure = new ResponseStructure<>();
		if (recUser.isPresent()) {
			User dbUser = mapToUser(userRequest);
			dbUser.setAge(userRequest.getAge());
			dbUser.setEmail(userRequest.getEmail());
			dbUser.setGender(userRequest.getGender());
			dbUser.setName(userRequest.getName());
			dbUser.setPhone(userRequest.getPhone());
			dbUser.setPassword(userRequest.getPassword());
			if (userRequest.getStatus() != null) {
				dbUser.setStatus(userRequest.getStatus());
			}
			structure.setData(maptoUserResponse(userDao.saveUser(dbUser)));
			structure.setMessage("User Updated");
			structure.setStatus(HttpStatus.ACCEPTED.value());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(structure);
		}
		throw new UserNotFoundException("Cannot Update User Id Is Invalid");
	}

	public ResponseEntity<ResponseStructure<UserResponse>> findById(int id) {
		ResponseStructure<UserResponse> structure = new ResponseStructure<>();
		Optional<User> dbUser = userDao.findById(id);
		if (dbUser.isPresent()) {
			structure.setData(maptoUserResponse(dbUser.get()));
			structure.setMessage("User Found");
			structure.setStatus(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
		}
		throw new UserNotFoundException("Invalid User Id");
	}

	public ResponseEntity<ResponseStructure<User>> verify(long phone, String password) {
		ResponseStructure<User> structure = new ResponseStructure<>();
		Optional<User> dbUser = userDao.verify(phone, password);
		if (dbUser.isPresent()) {
			structure.setData(dbUser.get());
			structure.setMessage("Verification Succesfull");
			structure.setStatus(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
		}
		throw new UserNotFoundException("Invalid Phone And Password");
	}

	public ResponseEntity<ResponseStructure<User>> verify(String email, String password) {
		ResponseStructure<User> structure = new ResponseStructure<>();
		Optional<User> dbUser = userDao.verify(email, password);
		if (dbUser.isPresent()) {
			structure.setData(dbUser.get());
			structure.setMessage("Verification Succesfull");
			structure.setStatus(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
		}
		throw new UserNotFoundException("Invalid Email And Password");
	}

	public ResponseEntity<ResponseStructure<String>> delete(int id) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		Optional<User> dbUser = userDao.findById(id);
		if (dbUser.isPresent()) {
			userDao.delete(id);
			structure.setData("User Found");
			structure.setMessage("User deleted");
			structure.setStatus(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(structure);
		}
		throw new UserNotFoundException("Cannot Delete User as Id is Inavlid");
	}

	private User mapToUser(UserRequest userRequest) {
		return User.builder().email(userRequest.getEmail()).name(userRequest.getName()).phone(userRequest.getPhone())
				.gender(userRequest.getGender()).age(userRequest.getAge()).password(userRequest.getPassword())
				.status(userRequest.getStatus() != null ? userRequest.getStatus() : "ACTIVE") // Set default if null
				.build();
	}

	private UserResponse maptoUserResponse(User user) {
		return UserResponse.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
				.phone(user.getPhone()).gender(user.getGender()).age(user.getAge()).password(user.getPassword())
				.status(user.getStatus()) // Add status
				.build();
	}
}
