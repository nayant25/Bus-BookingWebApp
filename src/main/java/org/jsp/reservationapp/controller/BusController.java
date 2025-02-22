package org.jsp.reservationapp.controller;

import java.time.LocalDate;
import java.util.List;

import org.jsp.reservationapp.dto.BusRequest;
import org.jsp.reservationapp.dto.ResponseStructure;
import org.jsp.reservationapp.model.Bus;
import org.jsp.reservationapp.service.BusService;
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
@RequestMapping("/api/bus")
public class BusController {
	
	@Autowired
	private BusService busService;
	
	@PostMapping("/{admin_id}")
	public ResponseEntity<ResponseStructure<Bus>> saveBus(@RequestBody BusRequest busRequest,@PathVariable int admin_id) {
		return busService.saveBus(busRequest, admin_id);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseStructure<Bus>> updateBus(@RequestBody BusRequest busRequest,@PathVariable int id) {
		return busService.updateBus(busRequest, id);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Bus>> findById(@PathVariable int id) {
		return busService.findById(id);
	}
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Bus>>> findAll() {
		return busService.findAll();
	}
	
	@GetMapping("/find")
	public ResponseEntity<ResponseStructure<List<Bus>>> findBuses(@RequestParam String from,@RequestParam String to,@RequestParam LocalDate dateofDeparture) {
		return busService.findBuses(from, to, dateofDeparture);
		
	}
	
	public ResponseEntity<ResponseStructure<List<Bus>>> findbyAdminId(@PathVariable int admin_id) {
		return busService.findByAdminId(admin_id);
	}
	
	@DeleteMapping("{id}")
        public ResponseEntity<ResponseStructure<String>> deleteBus(@PathVariable int id) {
        	return busService.delete(id);
        			
        }
}
