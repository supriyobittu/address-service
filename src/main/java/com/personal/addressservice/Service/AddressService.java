package com.personal.addressservice.Service;

import com.personal.addressservice.Entity.Address;
import com.personal.addressservice.Model.LatLong;
import com.personal.addressservice.Model.Mapper.ErrorResponse;
import com.personal.addressservice.Model.Mapper.ResponseEntity;
import com.personal.addressservice.Repository.AddressDAO;
import com.personal.addressservice.Utilities.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
@Slf4j
@Service
public class AddressService {
    private AddressDAO addressDAO;

    @Autowired
    public AddressService(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity createUserAddress(Address addr) throws IOException {
        try {
            LatLong coordinates = (LatLong) Utilities.getInstance().checkThreeWords(addr.getThreeWordAddress());
            addr.setLatitude(coordinates.getLat());
            addr.setLongitude(coordinates.getLng());
            log.info("Address created Successfully");
            return addressDAO.save(addr);
        }catch (Exception e) {
            log.warn((String) Utilities.getInstance().checkThreeWords(addr.getThreeWordAddress()));
            return new ErrorResponse(404, (String) Utilities.getInstance().checkThreeWords(addr.getThreeWordAddress()));
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity updateUserAddress(Address addr, Integer id) throws IOException {
        Optional<Address> address = addressDAO.findById(id);
        if (address.isPresent()) {
            addr.setUserId(id);
            if (addr.getThreeWordAddress() != null && address.get().getThreeWordAddress().equalsIgnoreCase(addr.getThreeWordAddress()) == false) {
                try {
                    LatLong coordinates = (LatLong) Utilities.getInstance().checkThreeWords(addr.getThreeWordAddress());
                    addr.setLatitude(coordinates.getLat());
                    addr.setLongitude(coordinates.getLng());
                    log.info("Address Updated Successfully for user with Id:" + id);
                    return addressDAO.save(addr);
                } catch (Exception e) {
                    log.warn((String) Utilities.getInstance().checkThreeWords(addr.getThreeWordAddress()));
                    return new ErrorResponse(404, (String) Utilities.getInstance().checkThreeWords(addr.getThreeWordAddress()));
                }
            } else {
                addr.setLatitude(address.get().getLatitude());
                addr.setLongitude(address.get().getLongitude());
                log.info("Address Updated Successfully for user with Id:" + id);
                return addressDAO.save(addr);
            }
        } else{
            log.warn("No user found with id " + id);
            return new ErrorResponse(404, "No user found with id " + id);
        }
    }

    public Iterable<Address> findAllUsers() {
        return addressDAO.findAll();
    }

    public ResponseEntity findById(Integer id){
        if (addressDAO.findById(id).isPresent() == false) {
            log.warn("No Address Found for the user with " + id);
            return new ErrorResponse(404, "No Address Found for the user with " + id);
        }
        return addressDAO.findById(id).get();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity deleteUser(int id){
        Optional<Address> addr = addressDAO.findById(id);
        if (addr.isPresent()) {
            addressDAO.delete(addr.get());
            log.info(addr.get().toString() + "\n" + "Deleted Successfully");
            return addr.get();
        }else {
            log.warn("Address not found for user with id:" + id);
            return new ErrorResponse(404,"Address not found for user with id:" + id);
        }
    }
}
