package com.personal.addressservice.Model.Mapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse extends ResponseEntity {
    @ApiModelProperty(notes = "Error code handling specified by the user")
    public Integer errorCode;
    @ApiModelProperty(notes = "Error message handling based on user inputs")
    public String errorMessage;
}
