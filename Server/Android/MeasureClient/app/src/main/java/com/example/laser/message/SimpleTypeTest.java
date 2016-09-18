package com.example.laser.message;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;


/**
 * A simple jprotobuf pojo class just for demo.
 * 
 * @author xiemalin
 * @since 1.0.0
 */
public class SimpleTypeTest {

    @Protobuf(fieldType = FieldType.STRING, order = 1, required = true)
    private String name;
  
    @Protobuf
    private Long datetime;

    @Protobuf
    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Protobuf(fieldType = FieldType.INT32, order = 2, required = false)
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
//        StringBuilder imagestr = new StringBuilder();
//        for(int i =0;i < image.length;i++)
//        {
//            imagestr.append(image[i]);
//        }
        return "SimpleTypeTest{" + "name=" + name + ", datetime=" + datetime + ", image=" + Util.Bytes2HexString(image) + ", value=" + value + '}';
    }

   
}

class Util {
public Util() { 
} 

/** 
* 灏嗘寚瀹歜yte鏁扮粍浠�16杩涘埗鐨勫舰寮忔墦鍗板埌鎺у埗鍙� 
* @param hint String 
* @param b byte[] 
* @return void 
*/ 
public static void printHexString(String hint, byte[] b) { 
System.out.print(hint); 
for (int i = 0; i < b.length; i++) { 
String hex = Integer.toHexString(b[i] & 0xFF); 
if (hex.length() == 1) { 
hex = '0' + hex; 
} 
System.out.print(hex.toUpperCase() + " "); 
} 
System.out.println(""); 
} 

/** 
* 
* @param b byte[] 
* @return String 
*/ 
public static String Bytes2HexString(byte[] b) { 
String ret = ""; 
for (int i = 0; i < b.length; i++) { 
String hex = "0x"+Integer.toHexString(b[i] & 0xFF); 
if (hex.length() == 1) { 
hex = '0' + hex; 
} 
ret += hex.toUpperCase(); 
} 
return ret; 
} 

/** 
* 灏嗕袱涓狝SCII瀛楃鍚堟垚涓�涓瓧鑺傦紱 
* 濡傦細"EF"--> 0xEF 
* @param src0 byte 
* @param src1 byte 
* @return byte 
*/ 
public static byte uniteBytes(byte src0, byte src1) { 
byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue(); 
_b0 = (byte)(_b0 << 4); 
byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue(); 
byte ret = (byte)(_b0 ^ _b1); 
return ret; 
} 

/** 
* 灏嗘寚瀹氬瓧绗︿覆src锛屼互姣忎袱涓瓧绗﹀垎鍓茶浆鎹负16杩涘埗褰㈠紡 
* 濡傦細"2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9} 
* @param src String 
* @return byte[] 
*/ 
public static byte[] HexString2Bytes(String src){ 
byte[] ret = new byte[8]; 
byte[] tmp = src.getBytes(); 
for(int i=0; i<8; i++){ 
ret[i] = uniteBytes(tmp[i*2], tmp[i*2+1]); 
} 
return ret; 
} 

} 