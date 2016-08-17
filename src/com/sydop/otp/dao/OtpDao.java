package com.sydop.otp.dao;

import java.util.Map;

import com.sydop.otp.model.OtpResult;
import com.sydop.otp.model.OtpUser;

public interface OtpDao {
    public OtpUser verifyUser(OtpUser var1) throws Exception;

    public OtpUser verifyUserByToken(String var1) throws Exception;

    public void updateOtpUser(OtpUser var1) throws Exception;

    public OtpUser verifySyncCode(OtpUser var1) throws Exception;

    public OtpUser verifyPin(OtpUser var1) throws Exception;
    
    public OtpUser validatePin(OtpUser user) throws Exception;
}
