package com.mokey.common;

/**
 * @User: jufeng
 * @Date: 18-1-5
 * @Time: 上午10:02
 **/
public class ProxyInfo {
    private String  host;
    private int port = 80;
    private String  username;
    private String password;
    private String scheme;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
