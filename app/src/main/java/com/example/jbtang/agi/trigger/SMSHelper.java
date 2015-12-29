package com.example.jbtang.agi.trigger;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import java.util.regex.Pattern;

/**
 * Created by jbtang on 12/3/2015.
 */
public class SMSHelper {

    private static final Pattern COMMA = Pattern.compile(",");
    private static final String TAG = "smsHelper";

    private String SCA;         //短信中心号码
    private String PDU_Type;    //协议数据单元类型
    private String DA;          //接收方SME的地址
    private String DCS;         //参数表示用户数据(UD)采用什么编码方案
    private String PID;         //参数显示SMSC以何种方式处理SM (比如FAX,、Voice等)
    private String UDL;         //用户数据段长度
    private String UD;          //SM数据
    private String MR = "00";     //所有成功的SMS-SUBMIT参考数目(0..255)
    private String VP = "C4";     //参数表示消息在SMSC中不再有效的时长

    private String[] MaxPDU;     //存在多条短信情况，可以允许多条短信发送，所谓的长短信发送功能

    /**
     * @param value absolute phone number without +86. e.g. 18692128345
     */
    private void setDA(String value) {
        if (value == null || value.length() == 0) {     //号码为空
            DA = "00";
        } else {
            int len = value.length();
            value = ParityChange(value);
            //采用国内编码准则？
            DA = String.format("%02X", len) + "A1" + value;
        }
    }

    /**
     * 奇偶互换 (+F)
     *
     * @param str 要被转换的字符串
     * @return 转换后的结果字符串
     */
    private String ParityChange(String str) {
        String result = "";

        //奇字符串 补F
        if (str.length() % 2 != 0) {
            str += "F";
        }
        for (int i = 0; i < str.length(); i += 2) {
            result += str.getBytes()[i + 1];
            result += str.getBytes()[i];
        }

        return result;
    }

    /**
     * 参数显示用户数据编码方案(1个8位组)
     * 7	    6	5	4	3	    2	1	0
     * 编码组              保留	X	X	X
     *
     * @param value 格式，中文，8bit，7bit，显示：用户设备
     */
    private void setDCS(String value) {
        if (value == null || value.length() == 0) {    //号码为空
            DCS = "08";
        } else {
            String[] dcs_s = COMMA.split(value);
            boolean b5 = false;
            boolean b4 = false;
            String codestr = "10";
            String dcodestr = "00";

            for (String dcsstr : dcs_s) {
                if (dcsstr.equals("")) {
                    continue;
                }
                if (dcsstr.contains("压缩")) {
                    b5 = dcsstr.equals("GSM压缩");
                    continue;
                }
                if (dcsstr.contains("中文")) codestr = "10";
                if (dcsstr.contains("8bit")) codestr = "01";
                if (dcsstr.contains("7bit")) codestr = "00";
                if (dcsstr.contains("英文")) codestr = "00";

                if (dcsstr.contains("用户设备")) dcodestr = "11";
                if (dcsstr.contains("SIM卡禁止到终端")) dcodestr = "10";
                if (dcsstr.contains("存储SIM卡")) dcodestr = "01";
                if (dcsstr.contains("闪信")) dcodestr = "00";    //直接到终端

                if (dcsstr.contains("特殊")) b4 = true;
            }
            String result = "00" + (b5 ? "1" : "0") + (b4 ? "1" : "0") + codestr + dcodestr;
            DCS = String.format("%02X", Integer.parseInt(result, 2));
        }
    }

    private String getDCSMeaning() {
        String tStr = String.format("%08b", Integer.parseInt(DCS, 16));
        String result;
        result = tStr.substring(2, 1).equals("1") ? "GSM压缩" : "未压缩";
        result += tStr.substring(3, 1).equals("1") ? (tStr.substring(6, 1).equals("1") ? (tStr.substring(7, 1).equals("1") ? "用户设备" : "SIM卡禁止到终端") : (tStr.substring(7, 1).equals("1") ? "存储SIM卡" : "直接到终端")) : "没有消息类别";
        result += tStr.substring(4, 1).equals("1") ? (tStr.substring(5, 1).equals("1") ? "保留" : "USC2") : ((tStr.substring(5, 1).equals("1") ? "8bit" : "7bit"));
        return result;
    }

    //////////////////////////////////////////////////////////////////////////
    //发送方PDU_Type格式：
    //Bit No.	7	6	    5	   4	3	2	1	0
    //          RP	UDHI	SRR	   VPF		RD	MTI
    //          0	0	    1	   1	0	0	0	1
    //////////////////////////////////////////////////////////////////////////
    //接收方格式：
    //Bit No.	7	6	    5	    4   3	2	    1	0
    //          RP	UDHI	SRI				MMS	    MTI
    //          0	0	    0	    0	0   1	    0	0
    //////////////////////////////////////////////////////////////////////////
    // RP:应答路径（Reply Paht）
    // UDHI：  用户数据头标识（User Data Header
    // SRR：    请求状态报告（Status Report
    // VPF：    有效期格式（Validity Period Format）
    // RD：      拒绝复本（Reject Duplicate）
    // MMS：   有更多的信息需要发送（More Messages to Send），此值仅被 SMSC 设置
    // MTI：    信息类型指示（Message Type Indicator）
    //////////////////////////////////////////////////////////////////////////
    /// 短信发送格式：
    /// SCA	PDUType	MR	DA	PID	DCS	VP[VPDateTime]	UDL	UD
    /// 短信接收格式：
    /// SCA	PDUType	OA	PID	DCS	SCTS	UDL	UD
    ///
    //////////////////////////////////////////////////////////////////////////
    private String getPDU_Type_Value() {
        String result = String.format("%08b", Integer.parseInt(PDU_Type, 16));

        String ret = (result.substring(0, 1).equals("1") ? "有应答路径" : "无应答路径");
        ret += "|" + (result.substring(1, 1).equals("1") ? "有用户头" : "无用户头");
        ret += "|" + (result.substring(2, 1).equals("1") ? "有报告将返回" : "无报告不返回");
        ret += "|" + (result.substring(3, 2).equals("1") ? "无有效期" : (result.substring(3, 2).equals("1") ? "增加有效期" : (result.substring(3, 2).equals("1") ? "相对有效期" : "绝对有效期")));
        ret += "|" + (result.substring(5, 1).equals("1") ? "无等待消息拒绝重复" : "有等待消息接受重复");
        ret += "|" + (result.substring(6, 2).equals("1") ? "接收短信" : (result.substring(6, 2).equals("1") ? "发送报告" : result.substring(6, 2).equals("1") ? "状态报告" : "保留"));

        return ret;
    }

    private void setPDU_Type_Value(String value) {
        //设置用户头，VP时间，状态报告，发送标记
        String[] sstr = COMMA.split(value);
        boolean bRP = false;         //默认无路径
        boolean bUDHI = false;     //默认无用户头
        boolean bSRR = true;       //默认要状态报告
        boolean bRD = false;
        boolean bVPF = false;      //默认相对日期
        boolean bMTI = true;       //默认发送短信

        for (String s : sstr) {
            if (s.contains("应答路径")) bRP = s.substring(0, 1).equals("有");
            if (s.contains("用户头")) bUDHI = s.substring(0, 1).equals("有");
            if (s.contains("返回")) bSRR = s.substring(0, 1).equals("有");
            if (s.contains("重复")) bRD = s.substring(0, 1).equals("有");
            if (s.contains("有效期")) bVPF = s.substring(0, 1).equals("无");
            if (s.contains("发送")) bMTI = s.substring(0, 2).equals("发送");
        }

        PDU_Type = (bRP ? "1" : "0") + (bUDHI ? "1" : "0") + (bSRR ? "1" : "0") + (bVPF ? "00" : "10") + (bRD ? "1" : "0") + (bMTI ? "01" : "10");
        PDU_Type = String.format("%02X", Integer.parseInt(PDU_Type, 2));
    }

    /// <summary>
    /// 参数显示消息中心以何种方式处理消息内容
    /// （比如FAX,Voice）(1个8位组)
    /// </summary>
    private void setPID_Value(String value) {
        if (value == null || value.length() == 0) {     //参数为空，默认短信发送，相对格式
            PID = "00";
        } else {
            if (value.equals("正常短信")) PID = "00";

            if (value.equals("覆盖短信1")) PID = "41";
            if (value.equals("覆盖短信2")) PID = "42";
            if (value.equals("覆盖短信3")) PID = "43";
            if (value.equals("覆盖短信4")) PID = "44";
            if (value.equals("覆盖短信5")) PID = "45";
            if (value.equals("覆盖短信6")) PID = "46";
            if (value.equals("覆盖短信7")) PID = "47";

            if (value.equals("定位短信")) PID = "40";
            if (value.equals("回复短信")) PID = "5F";
        }
    }

    /// <summary>
    /// 用户数据(0-140个8位组)
    ///
    /// 编码过程自动计算了UDL的长度？？？
    ///
    /// </summary>
    private String getUD_Value() {
        //需要考虑用户头的问题，解决用户头消息
        //否则解码容易出现问题？

        int len = Integer.parseInt(UDL, 16) * 2;
        String result = "";
        String resultAdd = "";
        String UDtemp = UD;

        if (getPDU_Type_Value().contains("有用户头")) {
            //取出相应的用户头，随后组装内容去
            int lenUDHL = Integer.parseInt(UD.substring(0, 2), 16);
            len = len - (lenUDHL + 1) * 2;
            UDtemp = UD.substring((lenUDHL + 1) * 2, len);

            String UDHL = UD.substring(2, lenUDHL * 2);

            if (UDHL.substring(0, 2).equals("00")) {
                //是分拆短信
                int lenF = Integer.parseInt(UDHL.substring(2, 2), 16);
                int iMr = Integer.parseInt(UDHL.substring(4, 2), 16);
                int iTotal = Integer.parseInt(UDHL.substring(6, 2), 16);
                int iCur = Integer.parseInt(UDHL.substring(8, 2), 16);

                resultAdd = iCur + "/" + iTotal + " ";
            }
            //解析其中的含义吗？
        }

        String DCS_Meaning = getDCSMeaning();
        switch (DCS_Meaning.substring(DCS_Meaning.length() - 4, 4)) {
            case "USC2":
                //四个一组，每组译为一个USC2字符
                for (int i = 0; i < len; i += 4) {
                    String temp = UDtemp.substring(i, 4);

                    int byte1 = Integer.parseInt(temp, 16);

                    result += (char) byte1;
                }
                break;
            case "7bit":
                result = PDU7bitDecoder(UDtemp);
                break;
            case "8bit":
                //两个一组，每组直接编码，可能无法显示结果？！
                result = PDU8BitDecode(UDtemp);
                break;
            default:
                break;
        }
        return resultAdd + result;
    }

    /// <summary>
    /// PDU7bit的解码，供UserData的get访问器调用
    /// </summary>
    /// <param name="len">用户数据长度</param>
    /// <param name="userData">数据部分PDU字符串</param>
    /// <returns></returns>
    private String PDU7bitDecoder(String userData) {

        byte[] oldBin = new byte[userData.length() / 2];

        String b7BinStr = "";

        for (int i = 0; i < userData.length(); i++) {
            oldBin[i / 2] = (byte) Integer.parseInt(userData.substring(i, 2), 16);
            String tStr = String.format("%08b", (int) oldBin[i / 2]);
            b7BinStr = tStr + b7BinStr;
            i++;
        }

        String Newb7Str = "";

        do {
            Newb7Str += (char) Integer.parseInt(b7BinStr.substring(b7BinStr.length() - 7, 7), 2);
            b7BinStr = b7BinStr.substring(0, b7BinStr.length() - 7);
        } while (b7BinStr.length() > 6);

        if (b7BinStr.length() > 0) {
            int left = 8 - b7BinStr.length();
            String pad = "";
            while (left-- > 0) {
                pad += "0";
            }
            b7BinStr += pad;
            Newb7Str += String.format("%x", Integer.parseInt(b7BinStr, 2));
        }
        return Newb7Str;
    }

    /// <summary>
    /// 输入：31323334353637383930
    /// 输出：1234567890
    /// </summary>
    /// <param name="userData"></param>
    /// <returns></returns>
    private String PDU8BitDecode(String userData) {
        if (userData.length() % 2 == 1) {
            return "error length";
        }
        byte[] pdu8 = new byte[userData.length() / 2];

        for (int i = 0; i < userData.length() / 2; i++) {
            pdu8[i] = (byte) Integer.parseInt(userData.substring(i * 2, 2), 16);
        }

        String ret = "";
        try {
            ret = new String(pdu8, "UTF-8");
        } catch (Exception e) {
            Log.d(TAG, "PDU8BitDecode failed");
        }
        return ret;
    }

    private void setUD_Value(String value) {
        byte[] Bytes = new byte[]{};

        String DCS_Meaning = getDCSMeaning();
        switch (DCS_Meaning.substring(DCS_Meaning.length() - 4, 4)) {
            case "USC2":
                //USC2 编码格式
                UD = "";

                try {
                    Bytes = value.getBytes("UTF8");
                } catch (Exception e) {
                    Log.d(TAG, "setUD_Value failed");
                }

                for (byte b : Bytes) {
                    UD += String.format("%02X", b);
                }
                UDL = String.format("%02X", UD.length() / 2);

                break;
            case "7bit":

                //USC2 编码格式
                UD = "";
                UD = PDU7bitEncoder(value);
                UDL = String.format("%02X", value.length());
                break;
            case "8bit":
                //两个一组，每组直接编码，可能无法显示结果？！
                UD = PDU8BitEncode(value);
                UDL = String.format("%02X", UD.length() / 2);
                break;
            default:
                break;
        }

    }

    /// <summary>
    /// 7bit 编码规则
    /// </summary>
    /// <param name="msg"></param>
    /// <returns></returns>
    private String PDU7bitEncoder(String msg) {
        byte[] b7data = msg.getBytes();

        String b7BinStr = "";

        String[] b7Str = new String[b7data.length];

        int iCount = 0;

        for (; iCount < b7data.length; ) {
            b7Str[iCount] = String.format("%08b", (int) b7data[iCount]);
            b7Str[iCount] = b7Str[iCount].substring(1, 7);
            b7BinStr = b7Str[iCount] + b7BinStr;
            iCount++;
        }

        String Newb7Str = "";

        do {
            Newb7Str += String.format("%X", Integer.parseInt(b7BinStr.substring(b7BinStr.length() - 8, 8), 2));
            b7BinStr = b7BinStr.substring(0, b7BinStr.length() - 8);
        } while (b7BinStr.length() > 7);

        if (b7BinStr.length() > 0) {
            int left = 8 - b7BinStr.length();
            String pad = "";
            while (left-- > 0) {
                pad += "0";
            }
            b7BinStr += pad;
            Newb7Str += String.format("%x", Integer.parseInt(b7BinStr, 2));
        }
        return Newb7Str;
    }

    /// <summary>
    /// 输入字符串：1234567890
    /// 输出字符串：31323334353637383930
    /// </summary>
    /// <param name="userData"></param>
    /// <returns></returns>
    private String PDU8BitEncode(String userData) {
        byte[] pdu8 = new byte[]{};
        try {
            pdu8 = userData.getBytes("UTF8");
        } catch (Exception e) {
            Log.d(TAG, "PDU8BitEncode failed");
        }
        String result = "";

        for (byte cc : pdu8) {
            result += String.format("%02X", cc);
        }

        return result;
    }

    /**
     * @param phone:     目的手机号码
     * @param text:      短信内容
     * @param DCSFormat  GSM压缩|未压缩，中文|8bit|7bit，特殊，用户设备|SIM卡禁止到终端|存储SIM卡|闪信(直接到终端)
     * @param SendFormat 【（有|无）应答路径】，【（有|无）用户头】，【（有|无）返回状态报告】，【发送】
     * @param SendType   正常短信|覆盖短信1-7|定位短信|回复短信
     * @param PDUNums    是否是一条短信的PDU，还是多条短信的PDU，作为返回值
     * @return
     */
    private String sms_Send_PDU_Encoder(String phone, String text, String DCSFormat, String SendFormat, String SendType, int PDUNums) {
        SCA = "00"; //采用默认的短消息中心，发送不填，为无短消息中心
        setDA(phone);

        //GSM压缩|未压缩，中文|8bit|7bit，特殊，用户设备|SIM卡禁止到终端|存储SIM卡|闪信(直接到终端)
        setDCS(DCSFormat);

        //if (text.Length > 70)
        //{
        //需要特殊处理，解析为长短信，需要分拆短信？
        //throw (new Exception("短信字数超过70"));

        //判断格式为 中文，7bit，8bit 等，根据长短信，还剩余 140-6 = 134 字节可用 自动拆分几条短信
        //  USC2的话 按照 67字符拆分
        //  8比特 按照 134字节拆分
        //  7比特 按照 134 * 8 /7 = 153 字节拆分 要求全部是英文字母

        int smsTimes = 1;
        int divNum = text.length();
        String DCS_Meaning = getDCSMeaning();

        switch (DCS_Meaning.substring(DCS_Meaning.length() - 4, 4)) {
            case "USC2":
                if (divNum > 70) divNum = 67;
                else divNum = 70;
                break;
            case "8bit":
                if (divNum > 140) divNum = 134;
                else divNum = 140;
                break;
            case "7bit":
                if (divNum > 160) divNum = 153;
                else divNum = 160;
                break;
        }

        smsTimes = text.length() / divNum;

        if (text.length() % divNum != 0) smsTimes = smsTimes + 1;

        MaxPDU = new String[smsTimes];

        String UDHLStr = "";

        //如果是长短信，需要自动修订用户头信息
        //【（有|无）应答路径】，【（有|无）用户头】，【（有|无）返回状态报告】，【发送】
        if (smsTimes > 1) {
            setPDU_Type_Value(SendFormat + ",有用户头");
        } else {
            setPDU_Type_Value(SendFormat);
        }

        //正常短信|覆盖短信1-7|定位短信|回复短信
        setPID_Value(SendType);

        for (int i = 0; i < smsTimes; i++) {

            //最后设置
            String nPD = text.substring(i * divNum, ((i * divNum + divNum) > text.length()) ? text.length() - i * divNum : divNum);
            setUD_Value(nPD);

            if (smsTimes > 1) {
                UDHLStr = "05000305" + String.format("%02X", smsTimes) + String.format("%02X", i + 1);
                //短信发送格式：
                //SCA	PDUType	MR	DA	PID	DCS	VP[VPDateTime]	UDL	UD
                int iLen = Integer.parseInt(UDL, 16);
                iLen = iLen + 6;

                MaxPDU[i] = SCA + PDU_Type + MR + DA + PID + DCS + VP + String.format("%02X", iLen) + UDHLStr + UD;
            } else
                MaxPDU[0] = SCA + PDU_Type + MR + DA + PID + DCS + VP + UDL + UD;
        }

        PDUNums = smsTimes;

        return MaxPDU[0];

        //}
        //else
        //{
        //    //【（有|无）应答路径】，【（有|无）用户头】，【（有|无）返回状态报告】，【发送】
        //    PDU_Type_Value = SendFormat;

        //    //正常短信|覆盖短信1-7|定位短信|回复短信
        //    PID_Value = SendType;

        //    //最后设置
        //    UD_Value = text;

        //    PDUNums = 1;    //就1条短信！

        //    //短信发送格式：
        //    //SCA	PDUType	MR	DA	PID	DCS	VP[VPDateTime]	UDL	UD
        //    return SCA + PDU_Type + MR + DA + PID + DCS + VP + UDL + UD;
        //}
    }


}
