package com.personal.addressservice.Model;

import com.personal.addressservice.Model.LatLong;
import lombok.Data;

@Data
public class ThreeWordsResponse {
    private String country;
    private Square square;
    private String nearestPlace;
    private LatLong coordinates;
    private String words;
    private String language;
    private String map;


}

@Data
class Square {
    private LatLong southwest;
    private LatLong northeast;
}
