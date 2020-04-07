package com.personal.addressservice.Utilities;

import com.google.gson.Gson;
import com.personal.addressservice.Model.LatLong;
import com.personal.addressservice.Model.ThreeWordsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
public class Utilities {

    private String baseUrl;
    private String authKey;

    private static final Utilities instance = new Utilities();

    //private constructor to avoid client applications to use constructor
    private Utilities(){}

    public static Utilities getInstance(){
        return instance;
    }

    @Value("${application.properties.threeWordsUrl}")
    public void setBaseUrl(String baseUrl) {
        instance.baseUrl = baseUrl;
    }

    @Value("${application.properties.APIkey}")
    public void setAuthKey(String authKey) {
        instance.authKey = authKey;
    }

    public Object checkThreeWords(String threeWords) throws IOException {
        try
        {
            if (checkValidThreeWords(threeWords)) {
                String threeWordsUrl = baseUrl + threeWords + "&key=" + authKey;
                System.out.println(threeWordsUrl);
                URL urlForGetRequest = new URL(threeWordsUrl);
                String readLine = null;
                HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
                conection.setRequestMethod("GET");
                int responseCode = conection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conection.getInputStream()));
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

    private Boolean checkValidThreeWords(String threeWords) {
        // String regex = "(\\w+(?:\\.\\w+)+)";
        String regex = "(\\w+\\.\\w+\\.\\w+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(threeWords);
        return matcher.matches();
    }

}
