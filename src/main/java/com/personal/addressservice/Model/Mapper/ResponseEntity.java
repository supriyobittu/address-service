package com.personal.addressservice.Model.Mapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Lombook Properties for Response Handling
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
*/

public class ResponseEntity<T> {

    /* Handling of generic base response if required
    @ApiModelProperty(hidden = true)
    public T response;
    @ApiModelProperty(hidden = true)
    public ErrorResponse error;

    public ResponseEntity(T response) {
        this.response = response;
    }

    public ResponseEntity(ErrorResponse error) {
        this.error = error;
    }

     */
        }
