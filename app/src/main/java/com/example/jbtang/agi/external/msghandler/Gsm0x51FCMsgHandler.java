package com.example.jbtang.agi.external.msghandler;

import com.example.jbtang.agi.external.MonitorApplication;

import java.util.ArrayList;
import java.util.List;

import io.fmaster.GSMNCellInfo;
import io.fmaster.GSMServCellMessage;
import io.fmaster.Message;
import io.fmaster.MessageHandler;

import io.fmaster.Enumeration.BAND_TYPE;
import io.fmaster.GsmMessage.GPRSNcellInfo;
import io.fmaster.GsmMessage.Gsm0x51FCMessage;


public class Gsm0x51FCMsgHandler implements MessageHandler {
    private List<GSMNCellInfo> ncells = new ArrayList<>();

    @Override
    public void handle(Message message) {
        Gsm0x51FCMessage msg = (Gsm0x51FCMessage) message;

        MonitorApplication.data_gsm_ServCell.setEARFCN(msg.getservBcchArfcn());
        MonitorApplication.data_gsm_ServCell.setRXLEV(msg.getservGprsRxLevAvg());

        MonitorApplication.data_gsm_ServCell.setC1(msg.getc1());
        MonitorApplication.data_gsm_ServCell.setC2(msg.getc2());
        MonitorApplication.data_gsm_ServCell.setBand(BAND_TYPE.valueOf(msg.getbcchBand()));
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

        for (GPRSNcellInfo ncell : msg.getCellInfo()) {

            GSMNCellInfo cell = new GSMNCellInfo();
            for (GSMNCellInfo tempcell : ncells) {
                if (tempcell.getEARFCN() == ncell.getbcchArfcn()) {
                    cell.setBSIC(tempcell.getBSIC());

                    break;
                }
            }
            cell.setEARFCN(ncell.getbcchArfcn());
            cell.setRXLEV(ncell.getrxPwr());
            cell.setC1(ncell.getc1());
            cell.setC2(ncell.getc2());

            MonitorApplication.data_gsm_NcellList.AddNcell(cell);
        }

        //send broad
        MonitorApplication.data_gsm_NcellList.setTime(msg.getTime());
        MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.GSM_N_CELL_FLAG, "msg", MonitorApplication.data_gsm_NcellList);

    }
}
