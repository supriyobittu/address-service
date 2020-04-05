package com.personal.addressservice.Service;

import com.personal.addressservice.Entity.Address;
import com.personal.addressservice.Repository.AddressDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
public class AddressService {
    private AddressDAO addressDAO;

    @Autowired
    public AddressService(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Address saveUserAddress(Address address) {
        return addressDAO.save(address);
    }
    
    public Iterable<Address> findAllUsers() {
        return addressDAO.findAll();
    }

    public Optional<Address> findById(Integer id){
        return addressDAO.findById(id);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteUser(Address addr){
        addressDAO.delete(addr);
    }
}
