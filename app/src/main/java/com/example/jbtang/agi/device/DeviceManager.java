package com.example.jbtang.agi.device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manage all the active devices
 * Created by jbtang on 10/13/2015.
 */
public class DeviceManager {
    private Map<String, MonitorDevice> devices;
    private static final DeviceManager instance = new DeviceManager();

    private DeviceManager() {
        devices = new HashMap<>();
    }

    public static DeviceManager getInstance() {
        return instance;
    }

    public List<MonitorDevice> getDevices() {
        return new ArrayList<>(devices.values());
    }

    public MonitorDevice getDevice(String name) {
        return devices.get(name);
    }

    public void add(MonitorDevice device) {
            devices.put(device.getName(), device);
    }

    public void remove(String name) {
        devices.remove(name);
    }

}
