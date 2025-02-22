package org.jsp.reservationapp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.jsp.reservationapp.dao.AdminDao;
import org.jsp.reservationapp.dao.BusDao;
import org.jsp.reservationapp.dto.BusRequest;
import org.jsp.reservationapp.dto.ResponseStructure;
import org.jsp.reservationapp.exception.AdminNotFoundException;
import org.jsp.reservationapp.exception.BusNotFoundException;
import org.jsp.reservationapp.model.Admin;
import org.jsp.reservationapp.model.Bus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BusService {

    @Autowired
    private BusDao busDao;

    @Autowired
    private AdminDao adminDao;

    public ResponseEntity<ResponseStructure<Bus>> saveBus(BusRequest busRequest, int admin_id) {
        Optional<Admin> recAdmin = adminDao.findById(admin_id);
        ResponseStructure<Bus> structure = new ResponseStructure<>();
        if (recAdmin.isPresent()) {
            Bus bus = mapToBus(busRequest);
            bus.setAdmin(recAdmin.get());
            recAdmin.get().getBus().add(bus);
            adminDao.saveAdmin(recAdmin.get());
            structure.setData(busDao.saveBus(bus));
            structure.setMessage("Bus Added Successfully");
            structure.setStatus(HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(structure);
        }
        throw new AdminNotFoundException("Cannot Add Bus as Admin Id is invalid");
    }

    public ResponseEntity<ResponseStructure<Bus>> updateBus(BusRequest busRequest, int id) {
        ResponseStructure<Bus> structure = new ResponseStructure<>();
        Optional<Bus> recBus = busDao.findById(id);
        if (recBus.isEmpty()) {
            throw new BusNotFoundException("Cannot update bus, as ID is Invalid");
        }
        Bus dbBus = recBus.get();
        dbBus.setBusNumber(busRequest.getBusNumber());
        dbBus.setDateOfDeparture(busRequest.getDateOfDeparture());
        dbBus.setFrom(busRequest.getFrom());
        dbBus.setTo(busRequest.getTo());
        dbBus.setCostPerSeat(busRequest.getCostPerSeat());
        dbBus.setAvailableSeats(busRequest.getAvailableSeats());
        dbBus.setName(busRequest.getName());
        dbBus = busDao.saveBus(dbBus);
        structure.setData(dbBus);
        structure.setMessage("Bus Updated Successfully");
        structure.setStatus(HttpStatus.ACCEPTED.value());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(structure);
    }

    public ResponseEntity<ResponseStructure<List<Bus>>> findByAdminId(int admin_id) {
        ResponseStructure<List<Bus>> structure = new ResponseStructure<>();
        List<Bus> buses = busDao.findBusesByAdminId(admin_id);
        if (buses.isEmpty()) {
            throw new BusNotFoundException("No Buses for entered Admin Id");
        }
        structure.setData(buses);
        structure.setMessage("List of Buses for entered Admin ID");
        structure.setStatus(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(structure);
    }

    public ResponseEntity<ResponseStructure<List<Bus>>> findBuses(String from, String to, LocalDate dateOfDeparture) {
        ResponseStructure<List<Bus>> structure = new ResponseStructure<>();
        List<Bus> buses = busDao.findBuses(from, to, dateOfDeparture);
        if (buses.isEmpty()) {
            throw new BusNotFoundException("No Buses found for entered route on this Date");
        }
        structure.setData(buses);
        structure.setMessage("List of Buses for entered route on this Date");
        structure.setStatus(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(structure);
    }

    public ResponseEntity<ResponseStructure<String>> delete(int id) {
        Optional<Bus> recBus = busDao.findById(id);
        ResponseStructure<String> structure = new ResponseStructure<>();
        if (recBus.isPresent()) {
            busDao.delete(id);
            structure.setData("Bus deleted successfully");
            structure.setMessage("Bus Found and Deleted");
            structure.setStatus(HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(structure);
        }
        throw new BusNotFoundException("Cannot delete bus, ID not found");
    }

    public ResponseEntity<ResponseStructure<List<Bus>>> findAll() {
        List<Bus> buses = busDao.findAll();
        ResponseStructure<List<Bus>> response = new ResponseStructure<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Buses fetched successfully");
        response.setData(buses);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ResponseStructure<Bus>> findById(int id) {
        ResponseStructure<Bus> structure = new ResponseStructure<>();
        Optional<Bus> recBus = busDao.findById(id);
        if (recBus.isEmpty()) {
            throw new BusNotFoundException("Invalid Bus ID");
        }
        structure.setData(recBus.get());
        structure.setMessage("Bus Found");
        structure.setStatus(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(structure);
    }

    public ResponseEntity<ResponseStructure<List<Bus>>> findAllRegisteredBuses() {
        ResponseStructure<List<Bus>> structure = new ResponseStructure<>();
        List<Bus> buses = busDao.findAll();
        if (buses.isEmpty()) {
            throw new BusNotFoundException("No Registered Buses Available");
        }
        structure.setData(buses);
        structure.setMessage("List of All Admin Registered Buses");
        structure.setStatus(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(structure);
    }

    private Bus mapToBus(BusRequest busRequest) {
        return Bus.builder()
                .name(busRequest.getName())
                .busNumber(busRequest.getBusNumber())
                .dateOfDeparture(busRequest.getDateOfDeparture())
                .from(busRequest.getFrom())
                .to(busRequest.getTo())
                .costPerSeat(busRequest.getCostPerSeat())
                .availableSeats(busRequest.getAvailableSeats())
                .build();
    }
}