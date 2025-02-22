package org.jsp.reservationapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
	
	private int id;
	private String name;
	private long phone;
	private String email;
	private String gender;
	private int age;
	private String status;
	private String password;
	

}
