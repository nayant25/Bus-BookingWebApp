package org.jsp.reservationapp.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.jsp.reservationapp.model.Bus;
import org.jsp.reservationapp.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BusDao {
	
	@Autowired
	private BusRepository busRepository;
	
	public Bus saveBus(Bus bus) {
		return busRepository.save(bus);
	}
	
	public Optional<Bus> findById(int id){
		return busRepository.findById(id);	
	}
	
	public List<Bus> findAll(){
		return busRepository.findAll();
	}
	public List<Bus> findBuses(String from,String to,LocalDate dateofDeparture){
		return busRepository.findBuses(from, to, dateofDeparture);
	}
	
	public List<Bus> findBusesByAdminId(int admin_id){
		return busRepository.findByAdminId(admin_id);
	}
	
	public void delete(int id) {
	 busRepository.deleteById(id);
	}

}
