package com.personal.addressservice.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "Address")
public class Address {
    @Id
    @TableGenerator(name = "Address_Gen", table = "ID_GEN",pkColumnName = "GEN_NAME",valueColumnName = "GEN_VAL",pkColumnValue = "Addr_Gen",initialValue = 10000,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "Address_Gen")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(readOnly = true,notes = "The database generated custom User ID")
    private int userId;

    @ApiModelProperty(notes = "The name of the user", required = true)
    @NotNull
    private String name;

    @ApiModelProperty(notes = "The address line 1 of the user")
    private String addressLine1;

    @ApiModelProperty(notes = "The address line 2 of the user",allowEmptyValue = true)
    private String addressLine2;

    @ApiModelProperty(notes = "The city of the user")
    private String city;

    @ApiModelProperty(notes = "The country of the user")
    private String country;

    @ApiModelProperty(notes = "Please input three words like 'filled.count.soap'.The request would fail on bad Input")
    @NotNull
    private String threeWordAddress;

    @ApiModelProperty(readOnly = true,notes = "Saves the Latitude retrieved from three words API")
    private double latitude;

    @ApiModelProperty(readOnly = true,notes = "Saves the Longitude retrieved from three words API")
    private double longitude;
}

