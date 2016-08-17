package com.sydop.otp.service;

import com.sydop.otp.model.OtpResult;
import com.sydop.otp.model.OtpUser;
import java.util.Map;

public interface OtpService {
    public OtpResult login(OtpUser var1) throws Exception;

    public OtpResult getSyncCode(String var1) throws Exception;

    public OtpResult validateSyncCode(Map<String, String> var2) throws Exception;

    public OtpResult setPin(String var1, Map<String, String> var2) throws Exception;
    
    public OtpResult validatePin(String token, Map<String, String> map) throws Exception;

    public OtpResult validateOtpNumber(String var1, Map<String, String> var2) throws Exception;

    public OtpResult verifyUserByToken(String var1) throws Exception;
}
