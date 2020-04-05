package com.personal.addressservice.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")

public class AddressController {

    @RequestMapping("/response")
    public String checkThreeWords() {
        return "Address Controller Working fine";
    }

}
