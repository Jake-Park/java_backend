package com.sydop.otp.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="USER")
public class OtpUser {
    @Id
    private String id;
    private String password;
    @Column(name="sync_code")
    private String syncCode;
    private String pin;
    @Column(name="pin_status")
    private Integer pinStatus;
    @Column(name="access_token")
    private String accessToken;
    @Transient
    private String otp;
    private Date createdAt;
    private Date modifiedAt;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSyncCode() {
        return this.syncCode;
    }

    public void setSyncCode(String syncCode) {
        this.syncCode = syncCode;
    }

    public String getPin() {
        return this.pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Integer getPinStatus() {
        return this.pinStatus;
    }

    public void setPinStatus(Integer pinStatus) {
        this.pinStatus = pinStatus;
    }

    public String getOtp() {
        return this.otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return this.modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String toString() {
        return "OtpUser [id=" + this.id + ", password=" + this.password + ", syncCode=" + this.syncCode + ", pin=" + this.pin + ", pinStatus=" + this.pinStatus + ", createdAt=" + this.createdAt + ", modifiedAt=" + this.modifiedAt + "]";
    }
}
