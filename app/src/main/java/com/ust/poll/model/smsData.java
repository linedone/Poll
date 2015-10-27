package com.ust.poll.model;

/**
 * Created by Ken on 26/10/2015.
 */


import java.io.Serializable;

public class smsData implements Serializable {

    private static final long serialVersionUID = 1L;
    private String phoneNo;
    private String message;
    private String captcha;
    private String formName;
    private String formValue;

    public smsData(){

    }

    public smsData(String phoneNo, String message, String captcha, String formName, String formValue){
        this.phoneNo = phoneNo;
        this.message = message;
        this.captcha = captcha;
        this.formName = formName;
        this.formValue = formValue;

    }

    public void setPhoneno( String phoneNo ){
        this.phoneNo = phoneNo;
    }

    public String getphoneNo( ){
        return phoneNo;
    }

    public void setmessage( String message ){
        this.message = message;
    }

    public String getmessage( ){
        return message;
    }

    public void setcaptcha( String captcha ){
        this.captcha = captcha;
    }

    public String getcaptcha( ){
        return captcha;
    }

    public void setformName( String formName ){
        this.formName = formName;
    }

    public String getformName( ){
        return formName;
    }

    public void setformValue( String formValue ){
        this.formValue = formValue;
    }

    public String getformValue( ){
        return formValue;
    }



























}
