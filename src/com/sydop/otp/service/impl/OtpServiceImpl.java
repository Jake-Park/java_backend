package com.sydop.otp.service.impl;

import com.sydop.otp.dao.OtpDao;
import com.sydop.otp.model.OtpResult;
import com.sydop.otp.model.OtpUser;
import com.sydop.otp.service.OtpService;
import com.sydop.otp.util.StringUtil;
import com.sydop.otp.util.TOTP;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OtpServiceImpl
implements OtpService {
    private OtpDao otpDao;

    public void setOtpDao(OtpDao otpDao) {
        this.otpDao = otpDao;
    }

    @Transactional
    public OtpResult login(OtpUser vo) throws Exception {
        OtpResult otpResult = new OtpResult();
        OtpUser user = this.otpDao.verifyUser(vo);
        if (user != null) {
            user.setSyncCode(null);
            otpResult.setOk(true);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("token", StringUtil.getRandomString(21));
            map.put("user", user);
            otpResult.setData(map);
            user.setAccessToken((String)map.get("token"));
            this.otpDao.updateOtpUser(user);
        } else {
            otpResult.setOk(false);
            otpResult.setMsg("User not found");
        }
        return otpResult;
    }

    @Transactional
    public OtpResult getSyncCode(String token) throws Exception {
        OtpResult otpResult = new OtpResult();
        OtpUser user = this.otpDao.verifyUserByToken(StringUtil.replace(token, "Bearer ", ""));
        if (user != null) {
            String syncCode = StringUtil.getRandomString(8);
            user.setSyncCode(syncCode);
            this.otpDao.updateOtpUser(user);
            otpResult.setOk(true);
            otpResult.setData((Object)syncCode);
        } else {
            otpResult.setOk(false);
            otpResult.setMsg("Authorisation token is not valid");
        }
        return otpResult;
    }

    @Transactional
    public OtpResult validateSyncCode(Map<String, String> param) throws Exception {
    	OtpResult otpResult = new OtpResult();
        OtpUser user = new OtpUser();        
        user.setSyncCode(param.get("synccode"));
        user = this.otpDao.verifySyncCode(user);
        if (user != null) {        	
            otpResult.setOk(true);
            otpResult.setData(user.getAccessToken());
        } else {
            otpResult.setOk(false);
            otpResult.setMsg("Sync code doesn't match");
        }
            
        return otpResult;
    }

    @Transactional
    public OtpResult setPin(String token, Map<String, String> param) throws Exception {
        OtpResult otpResult = this.verifyUserByToken(token);
        if (otpResult.getData() != null) {
            try {
                OtpUser user = (OtpUser)otpResult.getData();
                if (param.get("pin") == null || StringUtil.nullToVoid((Object)param.get("pin")).length() < 4) {
                    otpResult.setOk(false);
                    otpResult.setMsg("Please provide with more secured PIN");
                } else {
                    user.setPin(param.get("pin"));
                    user.setPinStatus(Integer.valueOf(1));
                    this.otpDao.updateOtpUser(user);
                    otpResult.setOk(true);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                otpResult.setOk(false);
                otpResult.setMsg("Fail to set PIN number");
            }
        }
        otpResult.setData((Object)null);
        return otpResult;
    }
    
    @Transactional
    public OtpResult validatePin(String token, Map<String, String> param) throws Exception {
        OtpResult otpResult = this.verifyUserByToken(StringUtil.replace(token, "Bearer ", ""));
        if (otpResult.getData() != null) {
            OtpUser user = new OtpUser();        
            user.setPin(param.get("pin"));
            user = this.otpDao.validatePin(user);
            if (user != null) {        	
                otpResult.setOk(true);
            } else {
                otpResult.setOk(false);
                otpResult.setMsg("Pin Number doesn't match");
            }
        } else {
            otpResult.setOk(false);
            otpResult.setMsg("Authorisation token is not valid");
        }
        otpResult.setData(null);
        return otpResult;
    }

    @Transactional
    public OtpResult validateOtpNumber(String token, Map<String, String> param) throws Exception {
        OtpResult otpResult = this.verifyUserByToken(token);
        if (otpResult.getData() != null) {
        	String seedKey = ((OtpUser)otpResult.getData()).getPin();
            String otp = null;
            Date date = new Date();
            String getCurrentTimeStamp = String.valueOf(date.getTime()).substring(0, 10);
            getCurrentTimeStamp = param.get("time");
            long testTime = Long.valueOf(getCurrentTimeStamp);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                long T = (testTime - 0) / 30;
                String steps = Long.toHexString(T).toUpperCase();
                while (steps.length() < 16) {
                    steps = "0" + steps;
                }
                String fmtTime = String.format("%1$-11s", testTime);
                String utcTime = df.format(new Date(testTime * 1000));
                otp = TOTP.generateTOTP(seedKey, steps, "6", "HmacSHA1");
                System.out.print("|  " + fmtTime + "  |  " + utcTime + "  | " + steps + " |");
                System.out.println("Seed Key : " + seedKey + " | Otp number : " + String.valueOf(otp) + "| SHA1 |");
                if (!otp.equals(StringUtil.nullToVoid(param.get("otp")))) {
                    otpResult.setOk(false);
                    otpResult.setMsg("Invalid One Time Password.");
                } else {
                    otpResult.setOk(true);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                otpResult.setOk(false);
                otpResult.setMsg("Internal Server Error");
                otpResult.setDevMsg(e.getMessage());
            }
        }
        otpResult.setData((Object)null);
        return otpResult;
    }

    public OtpResult verifyUserByToken(String token) throws Exception {
        OtpResult otpResult = new OtpResult();
        OtpUser otpUser = this.otpDao.verifyUserByToken(StringUtil.replace(token, "Bearer ", ""));
        if (otpUser != null) {
            otpResult.setData((Object)otpUser);
        } else {
            otpResult.setOk(false);
            otpResult.setMsg("Authorisation token is not valid");
        }
        return otpResult;
    }
}
