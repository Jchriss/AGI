package com.example.jbtang.agi.dao.devices;

import com.example.jbtang.agi.core.Status;

/**
 * Created by jbtang on 10/8/2015.
 */
public class DeviceDAO {
    public final String name;
    public final String ip;
    public final int dataPort;
    public final int messagePort;
    public final Status.BoardType type;

    private DeviceDAO() {
        this.name = "";
        this.ip = "";
        this.dataPort = 0;
        this.messagePort = 0;
        this.type = null;
    }

    public DeviceDAO(String name, String ip, int dataPort, int messagePort, Status.BoardType type) {
        this.name = name;
        this.ip = ip;
        this.dataPort = dataPort;
        this.messagePort = messagePort;
        this.type = type;
    }
}
