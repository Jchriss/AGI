package com.example.jbtang.agi.dao.configuration;

import com.example.jbtang.agi.core.Status;

/**
 * Created by jbtang on 11/5/2015.
 */
public class ConfigurationDAO {
    public final String name;
    public final Status.TriggerType type;
    public final int triggerInterval;
    public final int filterInterval;
    public final int silenceCheckTimer;
    public final int receivingAntennaNum;
    public final int totalTriggerCount;
    public final String targetPhoneNum;

    private ConfigurationDAO() {
        this.name = "";
        this.type = Status.TriggerType.SMS;
        this.triggerInterval = 0;
        this.filterInterval = 0;
        this.silenceCheckTimer = 0;
        this.receivingAntennaNum = 0;
        this.totalTriggerCount = 0;
        this.targetPhoneNum = "";
    }

    public ConfigurationDAO(String name, Status.TriggerType type, int triggerInterval, int filterInterval, int silenceCheckTimer,
                            int receivingAntennaNum, int totalTriggerCount, String targetPhoneNum) {
        this.name = name;
        this.type = type;
        this.triggerInterval = triggerInterval;
        this.filterInterval = filterInterval;
        this.silenceCheckTimer = silenceCheckTimer;
        this.receivingAntennaNum = receivingAntennaNum;
        this.totalTriggerCount = totalTriggerCount;
        this.targetPhoneNum = targetPhoneNum;
    }
}
