package com.example.jbtang.agi.external.msghandler;

import com.example.jbtang.agi.external.MonitorApplication;

import java.util.ArrayList;
import java.util.List;

import io.fmaster.GSMNCellInfo;
import io.fmaster.GSMServCellMessage;
import io.fmaster.Message;
import io.fmaster.MessageHandler;

import io.fmaster.Enumeration.BAND_TYPE;
import io.fmaster.GsmMessage.Gsm0x5137Message;
import io.fmaster.GsmMessage.GsmNcellEle;


public class Gsm0x5137MsgHandler implements MessageHandler {
    private List<GSMNCellInfo> ncells = new ArrayList<>();

    @Override
    public void handle(Message message) {
        Gsm0x5137Message msg = (Gsm0x5137Message) message;
        MonitorApplication.data_gsm_ServCell.setEARFCN(msg.getArfcn());
        MonitorApplication.data_gsm_ServCell.setRXLEV(msg.getservReLevAvg());
        MonitorApplication.data_gsm_ServCell.setC1(msg.getservC1());
        MonitorApplication.data_gsm_ServCell.setC2(msg.getservC2());
        MonitorApplication.data_gsm_ServCell.setBand(BAND_TYPE.valueOf(msg.getBand()));
        if (msg.getTime() - MonitorApplication.data_gsm_ServCell.getTime() > GSMServCellMessage.UPDATE_INTERVAL) {
            MonitorApplication.data_gsm_ServCell.setTime(msg.getTime());
            MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.GSM_SERVER_CELL_FLAG, "msg", MonitorApplication.data_gsm_ServCell);
        }
        ncells.clear();
        ncells.addAll(MonitorApplication.data_gsm_NcellList.getNcells());
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

        for (GsmNcellEle ncell : msg.getCellInfo()) {
            GSMNCellInfo cell = new GSMNCellInfo();

            for (GSMNCellInfo tempcell : ncells) {
                if (tempcell.getEARFCN() == ncell.getArfcn()) {
                    cell.setBSIC(tempcell.getBSIC());

                    break;
                }
            }
            cell.setEARFCN(ncell.getArfcn());
            cell.setRXLEV(ncell.getrxLevAvg());
            cell.setC1(ncell.getnCell1());
            cell.setC1(ncell.getnCell2());

            MonitorApplication.data_gsm_NcellList.AddNcell(cell);
        }

        //send broad
        MonitorApplication.data_gsm_NcellList.setTime(msg.getTime());
        MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.GSM_N_CELL_FLAG, "msg", MonitorApplication.data_gsm_NcellList);

    }
}
