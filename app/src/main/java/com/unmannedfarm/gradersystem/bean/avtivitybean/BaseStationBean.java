package com.unmannedfarm.gradersystem.bean.avtivitybean;

public class BaseStationBean {

    private String ip;
    private String port;
    private String account;
    private String password;
    private String mountedPoint;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMountedPoint() {
        return mountedPoint;
    }

    public void setMountedPoint(String mountedPoint) {
        this.mountedPoint = mountedPoint;
    }

    @Override
    public String toString() {
        return "BaseStationBean{" +
                "ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", mountedPoint='" + mountedPoint + '\'' +
                '}';
    }
}
