package com.example.bustedapp;

public class Wifi {
    private String ssid, macadd, percentage, encryption, status, date, time;

    public Wifi(){

    }

    public Wifi(String ssid, String macadd, String percentage, String encryption, String status, String date, String time) {
        this.ssid = ssid;
        this.macadd = macadd;
        this.percentage = percentage;
        this.encryption = encryption;
        this.status = status;
        this.date = date;
        this.time = time;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getMacadd() {
        return macadd;
    }

    public void setMacadd(String macadd) {
        this.macadd = macadd;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}


