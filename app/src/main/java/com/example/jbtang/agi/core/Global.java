package com.example.jbtang.agi.core;

import com.example.jbtang.agi.dao.configuration.ConfigurationDBManager;
import com.example.jbtang.agi.device.Device;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by jbtang on 10/7/2015.
 */
public class Global {
    public static class UserInfo {
        public static String user_name = "";

        private UserInfo() {
        }
    }

    public static class ThreadPool {
        public static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        public static final ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

        private ThreadPool() {

        }
    }

    public static class GlobalMsg {
        private String deviceName;
        private int msgType;
        private byte[] bytes;

        public GlobalMsg(String deviceName, int msgType, byte[] bytes) {
            this.deviceName = deviceName;
            this.msgType = msgType;
            this.bytes = bytes;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public int getMsgType() {
            return msgType;
        }

        public byte[] getBytes() {
            return bytes;
        }
    }

    public static class Configuration{
        public static String name;
        public static Status.TriggerType type;
        public static int triggerInterval;
        public static int filterInterval;
        public static int silenceCheckTimer;
        public static int receivingAntennaNum;
        public static int triggerTotalCount;
        public static String targetPhoneNum;

        private Configuration(){
        }
    }

    public static final String TARGET_STMSI = "Target stmsi";
}
