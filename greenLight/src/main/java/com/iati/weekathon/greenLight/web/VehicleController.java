package com.iati.weekathon.greenLight.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Liron on 27/04/2014.
 */
@RequestMapping(value = "/vehicles")
@Controller
public class VehicleController {


    private final static Logger log = LoggerFactory.getLogger(VehicleController.class);


    @RequestMapping(value = "/location", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> sendVehicleLocation(@RequestParam(value = "x", required = false, defaultValue = "0") int x,
                                            @RequestParam(value = "y", required = false, defaultValue = "0") int y) {

        Map<String, Object> model = new HashMap<String, Object>();

        model.put("Message", "Received Vehicle location: X="+x+", Y="+y);
        //model.put("errMsg","");
        return model;

    }

}