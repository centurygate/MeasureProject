package handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import measurebussiness.model.Measure;
import message.MeasureRecord;
import message.PushMeasureDataMessage;
import message.PushSingleMeasureMessage;
import message.RequestCmdHeader;
import runner.Main;
import session.Session;

import java.util.regex.Pattern;

/**
 * Created by free on 2016/9/3.
 */
class Util
{
    public Util()
    {
    }

    /**
     * 将指定byte数组以16进制的形式打印到控制台
     *
     * @param hint String
     * @param b    byte[]
     * @return void
     */
    public static void printHexString(String hint, byte[] b)
    {
        System.out.print(hint);
        for (int i = 0; i < b.length; i++)
        {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase() + " ");
        }
        System.out.println("");
    }

    /**
     * @param b byte[]
     * @return String
     */
    public static String Bytes2HexString(byte[] b)
    {
        String ret = "";
        for (int i = 0; i < b.length; i++)
        {
            String hex = "0x" + Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    /**
     * 将两个ASCII字符合成一个字节；
     * 如："EF"--> 0xEF
     *
     * @param src0 byte
     * @param src1 byte
     * @return byte
     */
    public static byte uniteBytes(byte src0, byte src1)
    {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    /**
     * 将指定字符串src，以每两个字符分割转换为16进制形式
     * 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
     *
     * @param src String
     * @return byte[]
     */
    public static byte[] HexString2Bytes(String src)
    {
        byte[] ret = new byte[8];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < 8; i++)
        {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }
}

public class PushPacketMeasureDataToCenterServerHandler extends ChannelInboundHandlerAdapter
{
    //	private Channel channel ;
//
//	public PushPacketMeasureDataToCenterServerHandler(Channel channel)
//	{
//		this.channel = channel;
//	}
    private String BytetoStr(byte[] databyte)
    {
        int len = databyte.length;
        char hexchar[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuilder strbuilder = new StringBuilder();
        for (int i = 0; i < len; i++)
        {
            char low = hexchar[((byte) databyte[i] & 0xf)];
            char high = hexchar[((byte) databyte[i] >> 4 & 0xf)];
            strbuilder.append(high).append(low);
        }
        return strbuilder.toString();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        String setaddrokstr = "FA048181";
        String setaddrerrstr = "FA848102FF";

        String modifydis_okstr = "FA048B77";
        String modifydis_errstr = "FA848B01F6";

        String setintervalok_okstr = "FA04857D";
        String setintervalerror_paramstr = "FA848501FC";
        String setintervalerrstr = "FA848501FA";


        String setstartpos_okstr = "FA04887A";
        String setstartpos_errstr = "FA848801F9";

        String setragnge_okstr = "FA048979";
        String setragnge_errstr = "FA848901F8";

        String setfreq_okstr = "FA048A78";
        String setfreq_errstr = "FA048A78";

        String setresolution_okstr = "FA048C76";
        String setresolution_errstr = "FA848C01F5";

        String setonpowerwork_okstr = "FA048D75";
        String setonpowerwork_errstr = "FA848D01F4";

        //take care of the device address
        String single_measure_low_precision_okstr = ".*0682(3[0-9]){3}2E(3[0-9]){3}.{2}$";
        String single_measure_low_precision_errstr = ".*0682ERR--(3[0-9]){2}.{2}$";

        String single_measure_high_precision_okstr = ".*0682(3[0-9]){3}2E(3[0-9]){4}.{2}$";
        String single_measure_high_precision_errstr = ".*0682ERR---(3[0-9]){2}.{2}$";

        String consecutive_measure_low_precision_okstr = ".*0683(3[0-9]){3}2E(3[0-9]){3}.{2}$";
        String consecutive_measure_low_precision_errstr = ".*0683ERR--(3[0-9]){2}.{2}$";

        String consecutive_measure_high_precision_okstr = ".*0683(3[0-9]){3}2E(3[0-9]){4}.{2}$";
        String consecutive_measure_high_precision_errstr = ".*0683ERR---(3[0-9]){2}.{2}$";

        String ctl_laser_power_okstr = "*.068501.{2}$";
        String ctl_laser_power_errstr = "*.068500.{2}$";

        //first receive the byte data into an array,
        //secondly, convert the byte data into string format to match the case
        int datalen = ((ByteBuf) msg).readableBytes();
        byte[] databyte = new byte[datalen];
        ((ByteBuf) msg).readBytes(databyte);
        String datastr = BytetoStr(databyte).toUpperCase();
        double dis = 0.0d;
        int startpos = 6 + 2;
        PushMeasureDataMessage pushMeasureDataMessage = new PushMeasureDataMessage();
        RequestCmdHeader cmdHeader = new RequestCmdHeader();
        cmdHeader.setClientID(Session.clientID);
        pushMeasureDataMessage.setCmdHeader(cmdHeader);
        MeasureRecord measureRecord = new MeasureRecord();
        long recordtime = System.currentTimeMillis();
        byte[] addrbyte = new byte[6];

        for (int i = 0; i < addrbyte.length;i++)
        {
            addrbyte[i] = databyte[i];
        }
        String addr = BytetoStr(addrbyte);
        measureRecord.setDeviceAddr(addr);
        measureRecord.setTime(recordtime);
        System.out.println("+==================================================================+");
        if (msg instanceof ByteBuf)
        {
            if (datastr.equals(setaddrokstr))
            {
                System.out.println("Set Device Addr OK");
            }
            else if (datastr.equals(setaddrerrstr))
            {
                System.out.println("Set Device Addr ERROR");
            }
            else if (datastr.equals(modifydis_okstr))
            {
                System.out.println("Modify Distance OK");
            }
            else if (datastr.equals(modifydis_errstr))
            {
                System.out.println("Modify Distance ERROR");
            }
            else if (datastr.equals(setintervalok_okstr))
            {
                System.out.println("Set Interval OK");
            }
            else if (datastr.equals(setintervalerror_paramstr))
            {
                System.out.println("Set Interval Error: (params wrong)");
            }
            else if (datastr.equals(setintervalerrstr))
            {
                System.out.println("Set Interval Error");
            }
            else if (datastr.equals(setstartpos_okstr))
            {
                System.out.println("Set Position OK");
            }
            else if (datastr.equals(setstartpos_errstr))
            {
                System.out.println("Set Position ERROR");
            }
            else if (datastr.equals(setragnge_okstr))
            {
                System.out.println("Set Range OK");
            }
            else if (datastr.equals(setragnge_errstr))
            {
                System.out.println("Set Range ERROR");
            }
            else if (datastr.equals(setfreq_okstr))
            {
                System.out.println("Set Frequency OK");
            }
            else if (datastr.equals(setfreq_errstr))
            {
                System.out.println("Set Frequency ERROR");
            }
            else if (datastr.equals(setresolution_okstr))
            {
                System.out.println("Set Resolution OK");
            }
            else if (datastr.equals(setresolution_errstr))
            {
                System.out.println("Set Resolution ERROR");
            }
            else if (datastr.equals(setonpowerwork_okstr))
            {
                System.out.println("Set On Power work OK");
            }
            else if (datastr.equals(setonpowerwork_errstr))
            {
                System.out.println("Set On Power work ERROR");
            }
            else if (Pattern.compile(single_measure_low_precision_okstr).matcher(datastr).matches())
            {
                System.out.println("Single Measure Low Precision OK");
                //calculate the distance from the byte array, see detail on the document "protocal of laser device"
                dis = (databyte[0 + startpos] - 0x30) * 100;
                dis = dis + (databyte[1 + startpos] - 0x30) * 10;
                dis = dis + (databyte[2 + startpos] - 0x30);
                dis = dis + (databyte[4 + startpos] - 0x30) * 0.1;
                dis = dis + (databyte[5 + startpos] - 0x30) * 0.01;
                dis = dis + (databyte[6 + startpos] - 0x30) * 0.001;
                measureRecord.setDistance(dis);
                pushMeasureDataMessage.setSingleMeasureRecord(measureRecord);
                Main.channel.writeAndFlush(pushMeasureDataMessage);

            }
            else if (Pattern.compile(single_measure_low_precision_errstr).matcher(datastr).matches())
            {
                System.out.println("Single Measure Low Precision ERROR");
            }
            else if (Pattern.compile(single_measure_high_precision_okstr).matcher(datastr).matches())
            {
                System.out.println("Single Measure High Precision OK");
                //calculate the distance from the byte array, see detail on the document "protocal of laser device"
                dis = (databyte[0 + startpos] - 0x30) * 100;
                dis = dis + (databyte[1 + startpos] - 0x30) * 10;
                dis = dis + (databyte[2 + startpos] - 0x30);
                dis = dis + (databyte[4 + startpos] - 0x30) * 0.1;
                dis = dis + (databyte[5 + startpos] - 0x30) * 0.01;
                dis = dis + (databyte[6 + startpos] - 0x30) * 0.001;
                dis = dis + (databyte[7 + startpos] - 0x30) * 0.0001;
                measureRecord.setDistance(dis);
                pushMeasureDataMessage.setSingleMeasureRecord(measureRecord);
                Main.channel.writeAndFlush(pushMeasureDataMessage);
            }
            else if (Pattern.compile(single_measure_high_precision_errstr).matcher(datastr).matches())
            {
                System.out.println("Single Measure High Precision ERROR");
            }
            //--------------------------------------------------------------------------------------------------
            else if (Pattern.compile(consecutive_measure_low_precision_okstr).matcher(datastr).matches())
            {
                System.out.println("Consecutive Measure Low Precision OK");
                //calculate the distance from the byte array, see detail on the document "protocal of laser device"
                dis = (databyte[0 + startpos] - 0x30) * 100;
                dis = dis + (databyte[1 + startpos] - 0x30) * 10;
                dis = dis + (databyte[2 + startpos] - 0x30);
                dis = dis + (databyte[4 + startpos] - 0x30) * 0.1;
                dis = dis + (databyte[5 + startpos] - 0x30) * 0.01;
                dis = dis + (databyte[6 + startpos] - 0x30) * 0.001;
                measureRecord.setDistance(dis);
                pushMeasureDataMessage.setSingleMeasureRecord(measureRecord);
                Main.channel.writeAndFlush(pushMeasureDataMessage);

            }
            else if (Pattern.compile(consecutive_measure_low_precision_errstr).matcher(datastr).matches())
            {
                System.out.println("Consecutive Measure Low Precision ERROR");
            }
            else if (Pattern.compile(consecutive_measure_high_precision_okstr).matcher(datastr).matches())
            {
                System.out.println("Consecutive Measure High Precision OK");
                //calculate the distance from the byte array, see detail on the document "protocal of laser device"
                dis = (databyte[0 + startpos] - 0x30) * 100;
                dis = dis + (databyte[1 + startpos] - 0x30) * 10;
                dis = dis + (databyte[2 + startpos] - 0x30);
                dis = dis + (databyte[4 + startpos] - 0x30) * 0.1;
                dis = dis + (databyte[5 + startpos] - 0x30) * 0.01;
                dis = dis + (databyte[6 + startpos] - 0x30) * 0.001;
                dis = dis + (databyte[7 + startpos] - 0x30) * 0.0001;
                measureRecord.setDistance(dis);
                pushMeasureDataMessage.setSingleMeasureRecord(measureRecord);
                Main.channel.writeAndFlush(pushMeasureDataMessage);
            }
            else if (Pattern.compile(consecutive_measure_high_precision_errstr).matcher(datastr).matches())
            {
                System.out.println("Consecutive Measure High Precision ERROR");
            }
            else if (datastr.equals(ctl_laser_power_okstr))
            {
                System.out.println("Control Lase Power ON/OFF OK");
            }
            else if (datastr.equals(ctl_laser_power_errstr))
            {
                System.out.println("Control Lase Power ON/OFF ERROR");
            }
            else
            {
                System.out.println(datastr);
            }
            System.out.println("+==================================================================+");
        }
        else
        {
            System.out.println("===============ctx.fireChannelRead(msg)");
            ctx.fireChannelRead(msg);
        }
    }
}
