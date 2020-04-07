package com.personal.addressservice.Controller;

import com.google.gson.Gson;
import com.personal.addressservice.Entity.Address;
import com.personal.addressservice.Model.LatLong;
import com.personal.addressservice.Model.Mapper.ErrorResponse;
import com.personal.addressservice.Model.Mapper.ResponseEntity;
import com.personal.addressservice.Model.ThreeWordsResponse;
import com.personal.addressservice.Service.AddressService;
import com.personal.addressservice.Utilities.Utilities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Slf4j
@RestController
@RequestMapping("/address")
@Api(value="Address", description="Adding,Updating and Retrieving user Address based on what3Words")
public class AddressController {
    private AddressService addressService;

    @Value("${application.properties.threeWordsUrl}")
    private String baseUrl;

    @Value("${application.properties.APIkey}")
    private String authKey;

    @Autowired
    public AddressController(AddressService addressService){
        this.addressService = addressService;
    }

    @ApiOperation(value = "To Check If Controller is Working fine")
    @GetMapping("/response")
    public String checkThreeWords() {
        log.debug("Address Controller Working fine");
        return "Address Controller Working fine";
    }


    @ApiOperation(value = "Add a User Address.Returns ErrorResponse If input is Invalid.",response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user address",response = Address.class),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource",response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden",response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found",response = ErrorResponse.class)
    })
    //@PostMapping("/create")
    @RequestMapping(value = "/create", method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity createUserAddress(@RequestBody Address addr) throws IOException {
        try {
            LatLong coordinates = (LatLong) Utilities.getInstance().checkThreeWords(addr.getThreeWordAddress());
            addr.setLatitude(coordinates.getLat());
            addr.setLongitude(coordinates.getLng());
            log.info("Address created Successfully");
            return addressService.saveUserAddress(addr);
        }catch (Exception e) {
            log.warn((String) Utilities.getInstance().checkThreeWords(addr.getThreeWordAddress()));
            return new ErrorResponse(404,(String) Utilities.getInstance().checkThreeWords(addr.getThreeWordAddress()));
        }

    }

    @ApiOperation(value = "View all user address",response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user address",response = Iterable.class),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource",response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden",response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found",response = ErrorResponse.class)
    }
    )
    //@GetMapping("/getAllUsersAddress")
    @RequestMapping(value = "/getAllUsersAddress",method = RequestMethod.GET,produces = "application/json")
    public Iterable<Address> getAllUserAddress() {
        log.info(addressService.findAllUsers().toString());
        //ResponseEntity<Iterable<Address>> address = new ResponseEntity<Iterable<Address>>(addressService.findAllUsers());
        return addressService.findAllUsers();
    }

    @ApiOperation(value = "Search a user address with ID.Returns ErrorResponse if input is invalid",response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user address",response = Address.class),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource",response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden",response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found",response = ErrorResponse.class)
    })
    // @GetMapping("/getAddress/{id}")
    @RequestMapping(value = "/getAddress/{id}",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity getUserById(@PathVariable Integer id){
        if (addressService.findById(id).isPresent() == false) {
            log.warn("No Address Found for the user with "+id);
            return new ErrorResponse(404,"No Address Found for the user with "+id);
        }
        return addressService.findById(id).get();
    }

    @ApiOperation(value = "Update a user address specifying the ID.Return Error Response if input is invalid",response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user address",response = Address.class),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource",response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden",response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found",response = ErrorResponse.class)
    })
    //@PutMapping("/update/{id}")
    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity updateAddress(@RequestBody @Valid Address addr, @PathVariable Integer id) throws IOException {
        Optional<Address> address = addressService.findById(id);
        if (address.isPresent()) {
            addr.setUserId(id);
            if ( addr.getThreeWordAddress() != null && address.get().getThreeWordAddress().equalsIgnoreCase(addr.getThreeWordAddress()) == false) {
                try {
                    LatLong coordinates = (LatLong) Utilities.getInstance().checkThreeWords(addr.getThreeWordAddress());
                    addr.setLatitude(coordinates.getLat());
                    addr.setLongitude(coordinates.getLng());
                    log.info("Address Updated Successfully for user with Id:"+id);
                    return addressService.saveUserAddress(addr);
                }catch (Exception e) {
                    log.warn((String) Utilities.getInstance().checkThreeWords(addr.getThreeWordAddress()));
                    return new ErrorResponse(404,(String) Utilities.getInstance().checkThreeWords(addr.getThreeWordAddress()));
                }
            }else {
                addr.setLatitude(address.get().getLatitude());
                addr.setLongitude(address.get().getLongitude());
                log.info("Address Updated Successfully for user with Id:"+id);
                return addressService.saveUserAddress(addr);
            }
        }else {
            log.warn("No user found with id " + id);
            return new ErrorResponse(404,"No user found with id " + id);
        }
    }

    @ApiOperation(value = "Delete a user address specifying the ID.Returns ErrorResponse if input is invalid",response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user address",response = Address.class),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource",response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden",response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found",response = ErrorResponse.class)
    })
    //@DeleteMapping("/delete/{id}")
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE,produces = "application/json")
    public ResponseEntity deleteAddress(@PathVariable Integer id){
        Optional<Address> addr = addressService.findById(id);
        if (addr.isPresent()) {
            addressService.deleteUser(addr.get());
            log.info(addr.get().toString() + "\n" + "Deleted Successfully");
            return addr.get();
        }else {
            log.warn("Address not found for user with id:" + id);
            return new ErrorResponse(404,"Address not found for user with id:" + id);
        }
    }
}
