package com.personal.addressservice.Model.Mapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.personal.addressservice.Entity.Address;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class AddressResponse extends ResponseEntity {

    @ApiModelProperty(readOnly = true, notes = "The database generated custom User ID")
    private int userId;

    @ApiModelProperty(notes = "The name of the user", required = true)
    @NotNull
    private String name;

    @ApiModelProperty(notes = "The address line 1 of the user")
    private String addressLine1;

    @ApiModelProperty(notes = "The address line 2 of the user", allowEmptyValue = true)
    private String addressLine2;

    @ApiModelProperty(notes = "The city of the user")
    private String city;

    @ApiModelProperty(notes = "The country of the user")
    private String country;

    @ApiModelProperty(notes = "Please input three words like 'filled.count.soap'.The request would fail on bad Input")
    @NotNull
    private String threeWordAddress;

    @ApiModelProperty(readOnly = true, notes = "Saves the Latitude retrieved from three words API")
    private double latitude;

    @ApiModelProperty(readOnly = true, notes = "Saves the Longitude retrieved from three words API")
    private double longitude;

    public AddressResponse(Address address) {
        this.userId = address.getUserId();
        this.name = address.getName();
        this.addressLine1 = address.getAddressLine1();
        this.addressLine2 = address.getAddressLine2();
        this.city = address.getCity();
        this.threeWordAddress = address.getThreeWordAddress();
        this.latitude = address.getLatitude();
        this.longitude = address.getLongitude();
    }
}
