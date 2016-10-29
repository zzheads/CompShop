package com.zzheads.CompShop.dto;

public class EmailProviderDto {
    private String suffix;
    private String name;
    private String host;

    public EmailProviderDto(String suffix, String name, String host) {
        this.suffix = suffix;
        this.name = name;
        this.host = host;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
