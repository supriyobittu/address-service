package com.personal.addressservice.Client;

import com.google.gson.Gson;
import com.personal.addressservice.Entity.Address;
import com.personal.addressservice.Model.LatLong;
import com.personal.addressservice.Model.Mapper.ErrorResponse;
import com.personal.addressservice.Model.Mapper.ResponseEntity;
import com.personal.addressservice.Model.ThreeWordsResponse;
import com.personal.addressservice.Utilities.Utilities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class ThreeWordsService   {
        private String baseUrl;
        private String authKey;

        @Value("${application.properties.threeWordsUrl}")
        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        @Value("${application.properties.APIkey}")
        public void setAuthKey(String authKey) {
            this.authKey = authKey;
        }

        public Object getThreeWordsCoordinates(String threeWords) throws IOException {
            try
            {
                if (Utilities.getInstance().checkValidThreeWords(threeWords)) {
                    String threeWordsUrl = baseUrl + threeWords + "&key=" + authKey;
                    System.out.println(threeWordsUrl);
                    URL urlForGetRequest = new URL(threeWordsUrl);
                    String readLine = null;
                    HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(connection.getInputStream()));
                        StringBuffer response = new StringBuffer();
                        while ((readLine = in.readLine()) != null) {
                            response.append(readLine);
                        }
                        in.close();
                        ThreeWordsResponse threeWordsResponse = new Gson().fromJson(response.toString(), ThreeWordsResponse.class);
                        LatLong coordinates = threeWordsResponse.getCoordinates();
                        return coordinates;
                    } else {
                        return "Bad Request. Please enter correct three words address";
                    }
                }else {
                    return "Please enter valid Three Words Address with a Dot in between the words";
                }

            }catch(Exception e){
                e.printStackTrace();
                return e.getLocalizedMessage();
            }
        }

//        public ResponseEntity setUserAddress(Address address) throws IOException {
//            try {
//                LatLong coordinates = (LatLong) getThreeWordsCoordinates(address.getThreeWordAddress());
//                address.setLatitude(coordinates.getLat());
//                address.setLongitude(coordinates.getLng());
//                return  address;
//            }catch (Exception e) {
//                return new ErrorResponse(404, (String) getThreeWordsCoordinates(address.getThreeWordAddress()));
//            }
//        }
}