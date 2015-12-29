package com.example.jbtang.agi.external.msghandler;

import com.example.jbtang.agi.external.MonitorApplication;

import io.fmaster.LTENcellInfo;
import io.fmaster.Message;
import io.fmaster.MessageHandler;
import io.fmaster.LteMessage.Lte0xb192Message;
import io.fmaster.LteMessage.Lte0xb192NcellMeasRsp;

public class Lte0xb192MsgHandler implements MessageHandler {

    @Override
    public void handle(Message message) {
        Lte0xb192Message msg = (Lte0xb192Message) message;
        boolean sendFlag = false;
        MonitorApplication.data_PwrInfo.setRSRP_NB2(Float.NaN);
        if (msg.getNcells().size() > 1) {
            sendFlag = true;
            MonitorApplication.data_PwrInfo.setRSRP_NB1(msg.getNcells().get(0).getInstRsrp());
            MonitorApplication.data_PwrInfo.setRSRP_NB2(msg.getNcells().get(1).getInstRsrp());
        } else if (msg.getNcells().size() > 0) {
            sendFlag = true;
            MonitorApplication.data_PwrInfo.setRSRP_NB1(msg.getNcells().get(0).getInstRsrp());
        }

        if (sendFlag) {
            //MonitorApplication.data_PwrInfo.setTime(msg.getTime());
            MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.POWER_INFO_FLAG, "msg", MonitorApplication.data_PwrInfo);
        }

        if (msg.getNcells().size() > 0) {
            MonitorApplication.data_NcellList.reset();
            //add servCell in Cell list
            LTENcellInfo cell = new LTENcellInfo();
            cell.setEARFCN(MonitorApplication.data_ServCell.getEARFCN());
            cell.setPCI(MonitorApplication.data_ServCell.getPCI());
            cell.setRSRP(MonitorApplication.data_PwrInfo.getRSRP());
            cell.setRSRQ(MonitorApplication.data_PwrInfo.getRSRQ());
            MonitorApplication.data_NcellList.AddNcell(cell);
        }

        for (Lte0xb192NcellMeasRsp ncell : msg.getNcells()) {
            LTENcellInfo cell = new LTENcellInfo();
            cell.setEARFCN(msg.geteArfcn());
            cell.setPCI(ncell.getPci());
            cell.setRSRP(ncell.getInstRsrp());
            cell.setRSRQ(ncell.getInstRsrq());
            MonitorApplication.data_NcellList.AddNcell(cell);
        }

        //send broad
        if (sendFlag) {
            MonitorApplication.data_NcellList.setTime(msg.getTime());
            MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.N_CELL_FLAG, "msg", MonitorApplication.data_NcellList);
        }
    }

}
