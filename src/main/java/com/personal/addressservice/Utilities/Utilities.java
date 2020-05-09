package com.personal.addressservice.Utilities;

import com.google.gson.Gson;
import com.personal.addressservice.Entity.Address;
import com.personal.addressservice.Model.LatLong;
import com.personal.addressservice.Model.Mapper.ResponseEntity;
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

    private static final Utilities instance = new Utilities();

    //private constructor to avoid client applications to use constructor
    private Utilities(){}

    public static Utilities getInstance(){
        return instance;
    }

     public Boolean checkValidThreeWords(String threeWords) {
        // String regex = "(\\w+(?:\\.\\w+)+)";
        String regex = "(\\w+\\.\\w+\\.\\w+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(threeWords);
        return matcher.matches();
    }
}
