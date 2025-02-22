package org.jsp.reservationapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
	private String name;
	private long phone;
	private String email;
	private String gender;
	private int age;
	private String status;
	private String password;

}
