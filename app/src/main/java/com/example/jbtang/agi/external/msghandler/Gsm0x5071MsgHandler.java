package com.example.jbtang.agi.external.msghandler;

import com.example.jbtang.agi.external.MonitorApplication;

import java.util.ArrayList;
import java.util.List;


import io.fmaster.GSMNCellInfo;
import io.fmaster.Message;
import io.fmaster.MessageHandler;
import io.fmaster.GsmMessage.Gsm0x5071Message;
import io.fmaster.GsmMessage.GsmCellInfo;


public class Gsm0x5071MsgHandler implements MessageHandler {
    private List<GSMNCellInfo> ncells = new ArrayList<>();

    @Override
    public void handle(Message message) {
        Gsm0x5071Message msg = (Gsm0x5071Message) message;

        MonitorApplication.data_gsm_ServCell.setTime(msg.getTime());
        MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.GSM_SERVER_CELL_FLAG, "msg", MonitorApplication.data_gsm_ServCell);
        ncells.clear();
        if (MonitorApplication.data_gsm_NcellList.getNcells() != null) {
            ncells.addAll(MonitorApplication.data_gsm_NcellList.getNcells());
        }
        if (msg.getCellInfo().size() > 0) {
            MonitorApplication.data_gsm_NcellList.reset();
            //add servCell in Cell list
            GSMNCellInfo cell = new GSMNCellInfo();
            cell.setEARFCN(MonitorApplication.data_gsm_ServCell.getEARFCN());
            cell.setBSIC(MonitorApplication.data_gsm_ServCell.getBSIC());
            cell.setRXLEV(MonitorApplication.data_gsm_ServCell.getRXLEV());
            cell.setC1(MonitorApplication.data_gsm_ServCell.getC1());
            cell.setC2(MonitorApplication.data_gsm_ServCell.getC2());
            MonitorApplication.data_gsm_NcellList.AddNcell(cell);
        }


        for (GsmCellInfo ncell : msg.getCellInfo()) {
            GSMNCellInfo cell = new GSMNCellInfo();
            cell.setEARFCN(ncell.getArfcn());
            cell.setBSIC((short) (ncell.getncc() * 10 + ncell.getbcc()));
            for (GSMNCellInfo tempcell : ncells) {
                if (tempcell.getEARFCN() == ncell.getArfcn()) {
                    cell.setC1(tempcell.getC1());
                    cell.setC2(tempcell.getC2());
                    break;
                }
            }

            cell.setRXLEV((byte) ncell.getRxPwr());

            MonitorApplication.data_gsm_NcellList.AddNcell(cell);
        }

        //send broad
        MonitorApplication.data_gsm_NcellList.setTime(msg.getTime());
        MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.GSM_N_CELL_FLAG, "msg", MonitorApplication.data_gsm_NcellList);

    }
}
