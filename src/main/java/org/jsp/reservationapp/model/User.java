package org.jsp.reservationapp.model;

import java.util.List;

import org.springframework.web.jsf.FacesContextUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false,unique = true)
	private long phone;
	@Column(nullable = false,unique = true)
	private String email;
	@Column(nullable = false)
	private String gender;
	@Column(nullable = false)
	private int age;
	@Column(nullable = false)
	private String password;
	@Column(unique = true)
	private String token;
	@Column(nullable = false)
	private String status;
	
	@OneToMany(mappedBy = "user")
	private List<Ticket> tickets;
	

}
