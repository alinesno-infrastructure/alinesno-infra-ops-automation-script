package com.alinesno.infra.tool.backup.db.operation;

import lombok.Data;

@Data
public class UnhealthyResource {

    private String name ;  // 资源名称
    private String type; // 网站或端口
    private String address;
    private int port;
    private String reason; // 异常原因

    public UnhealthyResource(String name, String type, String address, int port) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.port = port;
    }

    public UnhealthyResource(String name, String type, String address, int port, String reason) {
        this.type = type;
        this.address = address;
        this.port = port;
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "UnhealthyResource{" +
                "type='" + type + '\'' +
                ", address='" + address + '\'' +
                ", port=" + port +
                ", reason='" + reason + '\'' +
                '}';
    }
}