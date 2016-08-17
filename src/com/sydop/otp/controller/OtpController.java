package com.sydop.otp.controller;

import com.sydop.otp.model.OtpResult;
import com.sydop.otp.model.OtpUser;
import com.sydop.otp.service.OtpService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/otp")
public class OtpController {
    @Autowired
    private OtpService otpService;

    @RequestMapping(value={"/login"}, method={RequestMethod.POST})
    @ResponseBody
    public OtpResult login(@RequestBody OtpUser otpUser) throws Exception {
        return this.otpService.login(otpUser);
    }

    @RequestMapping(value={"/synccode"}, method={RequestMethod.GET})
    @ResponseBody
    public OtpResult getSyncCode(@RequestHeader(value="Authorization") String token) throws Exception {
        return this.otpService.getSyncCode(token);
    }

    @RequestMapping(value={"/synccode"}, method={RequestMethod.POST})
    @ResponseBody
    public OtpResult validateSyncCode(@RequestBody Map<String, String> param) throws Exception {
        return this.otpService.validateSyncCode(param);
    }

    @RequestMapping(value={"/pin"}, method={RequestMethod.POST})
    @ResponseBody
    public OtpResult setPin(@RequestHeader(value="Authorization") String token, @RequestBody Map<String, String> param) throws Exception {
        return this.otpService.setPin(token, param);
    }
    
    @RequestMapping(value={"/validpin"}, method={RequestMethod.POST})
    @ResponseBody
    public OtpResult validatePin(@RequestHeader(value="Authorization") String token, @RequestBody Map<String, String> param) throws Exception {
        return this.otpService.validatePin(token, param);
    }    

    @RequestMapping(value={"/otp"}, method={RequestMethod.POST})
    @ResponseBody
    public OtpResult getOtp(@RequestHeader(value="Authorization") String token, @RequestBody Map<String, String> param) throws Exception {
        return this.otpService.validateOtpNumber(token, param);
    }
}