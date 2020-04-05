package com.personal.addressservice.Repository;

import com.personal.addressservice.Entity.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDAO extends CrudRepository<Address,Integer> {
}
