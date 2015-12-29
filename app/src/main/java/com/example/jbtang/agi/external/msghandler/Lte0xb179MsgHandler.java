package com.example.jbtang.agi.external.msghandler;

import com.example.jbtang.agi.external.MonitorApplication;

import io.fmaster.LTENcellInfo;
import io.fmaster.Message;
import io.fmaster.MessageHandler;
import io.fmaster.LteMessage.Lte0xb179MeasNcell;
import io.fmaster.LteMessage.Lte0xb179Message;

public class Lte0xb179MsgHandler implements MessageHandler {

    @Override
    public void handle(Message message) {

        Lte0xb179Message msg = (Lte0xb179Message) message;
        if (msg.getEARFCN() == 0) {
            return;
        }
        boolean sendFlag = false;
        if (MonitorApplication.data_ServCell.getPCI() != msg.getSerPCI()) {
            MonitorApplication.data_ServCell.reset();
            MonitorApplication.data_PwrInfo.reset();
            MonitorApplication.data_NcellList.reset();

            sendFlag = true;
        }

        MonitorApplication.data_ServCell.setEARFCN(msg.getEARFCN());
        MonitorApplication.data_ServCell.setPCI(msg.getSerPCI());
        //send broad
        //if (msg.getTime() - MonitorApplication.data_ServCell.getTime() >= ServCellMessage.UPDATE_INTERVAL) {
        MonitorApplication.data_ServCell.setTime(msg.getTime());
        MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.SERVER_CELL_FLAG, "msg", MonitorApplication.data_ServCell);
        //}

        float rsrp = msg.getSerFilteredRSRP();
        float rsrq = msg.getSerFilteredRSRQ();

        if (rsrp < 0) {
            MonitorApplication.data_PwrInfo.setRSRP(rsrp);
        }
        if (rsrq < 0) {
            MonitorApplication.data_PwrInfo.setRSRQ(rsrq);
        }
        //send broad
        //if (msg.getTime() - MonitorApplication.data_PwrInfo.getTime() >= PwrInfoMessage.UPDATE_INTERVAL) {
        MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.POWER_INFO_FLAG, "msg", MonitorApplication.data_PwrInfo);
        //}


        MonitorApplication.data_PwrInfo.setRSRP_NB2(Float.NaN);
        if (msg.getMeasNcells().size() > 1) {
            sendFlag = true;
            MonitorApplication.data_PwrInfo.setRSRP_NB1(msg.getMeasNcells().get(0).getRSRP());
            MonitorApplication.data_PwrInfo.setRSRP_NB2(msg.getMeasNcells().get(1).getRSRP());
        } else if (msg.getMeasNcells().size() > 0) {
            sendFlag = true;
            MonitorApplication.data_PwrInfo.setRSRP_NB1(msg.getMeasNcells().get(0).getRSRP());

        }

        if (sendFlag) {
            //MonitorApplication.data_PwrInfo.setTime(msg.getTime());
            MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.POWER_INFO_FLAG, "msg", MonitorApplication.data_PwrInfo);
        }

        if (msg.getMeasNcells().size() > 0) {
            MonitorApplication.data_NcellList.reset();
            //add servCell in Cell list
            LTENcellInfo cell = new LTENcellInfo();
            cell.setEARFCN(MonitorApplication.data_ServCell.getEARFCN());
            cell.setPCI(MonitorApplication.data_ServCell.getPCI());
            cell.setRSRP(MonitorApplication.data_PwrInfo.getRSRP());
            cell.setRSRQ(MonitorApplication.data_PwrInfo.getRSRQ());
            MonitorApplication.data_NcellList.AddNcell(cell);
        }

        for (Lte0xb179MeasNcell ncell : msg.getMeasNcells()) {
            LTENcellInfo cell = new LTENcellInfo();
            cell.setEARFCN(msg.getEARFCN());
            cell.setPCI(ncell.getPci());
            cell.setRSRP(ncell.getRSRP());
            cell.setRSRQ(ncell.getRSRQ());
            MonitorApplication.data_NcellList.AddNcell(cell);
        }

        //send broad
        if (sendFlag) {
            MonitorApplication.data_NcellList.setTime(msg.getTime());
            MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.N_CELL_FLAG, "msg", MonitorApplication.data_NcellList);
        }


    }

}
