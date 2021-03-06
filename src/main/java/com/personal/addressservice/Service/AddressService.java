package com.personal.addressservice.Service;

import com.personal.addressservice.Client.ThreeWordsService;
import com.personal.addressservice.Entity.Address;
import com.personal.addressservice.Model.LatLong;
import com.personal.addressservice.Model.Mapper.AddressResponse;
import com.personal.addressservice.Model.Mapper.ErrorResponse;
import com.personal.addressservice.Model.Mapper.ResponseEntity;
import com.personal.addressservice.Repository.AddressDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class AddressService {
    private AddressDAO addressDAO;
    private ThreeWordsService threeWordsService;

    @Autowired
    public AddressService(AddressDAO addressDAO, ThreeWordsService threeWordsService) {
        this.addressDAO = addressDAO;
        this.threeWordsService = threeWordsService;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity createUserAddress(Address addr) throws IOException {
        try {
            LatLong coordinates = (LatLong) threeWordsService.getThreeWordsCoordinates(addr.getThreeWordAddress());
            addr.setLatitude(coordinates.getLat());
            addr.setLongitude(coordinates.getLng());
            log.info("Address created Successfully");
            Address addressCreated = addressDAO.save(addr);
            AddressResponse addressResponse = new AddressResponse("200",addressCreated);
            return addressResponse;
        }catch (Exception e) {
            log.warn((String) threeWordsService.getThreeWordsCoordinates(addr.getThreeWordAddress()));
            return new ErrorResponse(404, (String) threeWordsService.getThreeWordsCoordinates(addr.getThreeWordAddress()));
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity updateUserAddress(Address addr, Integer id) throws IOException {
        Optional<Address> address = addressDAO.findById(id);
        if (address.isPresent()) {
            addr.setUserId(id);
            if (addr.getThreeWordAddress() != null && address.get().getThreeWordAddress().equalsIgnoreCase(addr.getThreeWordAddress()) == false) {
                try {
                    LatLong coordinates = (LatLong) threeWordsService.getThreeWordsCoordinates(addr.getThreeWordAddress());
                    addr.setLatitude(coordinates.getLat());
                    addr.setLongitude(coordinates.getLng());
                    log.info("Address Updated Successfully for user with Id:" + id);
                    Address addressCreated = addressDAO.save(addr);
                    AddressResponse addressResponse = new AddressResponse("200",addressCreated);
                    return addressResponse;
                } catch (Exception e) {
                    log.warn((String) threeWordsService.getThreeWordsCoordinates(addr.getThreeWordAddress()));
                    return new ErrorResponse(404, (String) threeWordsService.getThreeWordsCoordinates(addr.getThreeWordAddress()));
                }
            } else {
                addr.setLatitude(address.get().getLatitude());
                addr.setLongitude(address.get().getLongitude());
                log.info("Address Updated Successfully for user with Id:" + id);
                Address addressCreated = addressDAO.save(addr);
                AddressResponse addressResponse = new AddressResponse("200",addressCreated);
                return addressResponse;
            }
        } else{
            log.warn("No user found with id " + id);
            return new ErrorResponse(404, "No user found with id " + id);
        }
    }

    public Stream<AddressResponse> findAllUsers() {

        List<Address> addressList =  StreamSupport
                .stream(addressDAO.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return addressList.stream().map(address -> new AddressResponse(address));
    }

    public ResponseEntity findById(Integer id){
        if (addressDAO.findById(id).isPresent() == false) {
            log.warn("No Address Found for the user with " + id);
            return new ErrorResponse(404, "No Address Found for the user with " + id);
        }
        AddressResponse addressResponse = new AddressResponse("200",addressDAO.findById(id).get());
        return addressResponse;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity deleteUser(int id){
        Optional<Address> addr = addressDAO.findById(id);
        if (addr.isPresent()) {
            addressDAO.delete(addr.get());
            log.info(addr.get().toString() + "\n" + "Deleted Successfully");
            AddressResponse addressResponse = new AddressResponse("200",addr.get());
            return addressResponse;
        }else {
            log.warn("Address not found for user with id:" + id);
            return new ErrorResponse(404,"Address not found for user with id:" + id);
        }
    }
}
