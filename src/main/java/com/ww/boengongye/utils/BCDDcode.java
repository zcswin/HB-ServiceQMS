package com.ww.boengongye.utils;

public class BCDDcode {
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;

        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }

        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }

        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;

        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }

            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }

            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }


    public static int highestOneBit(int i) {
        // HD, Figure 3-1
        i |= (i >>  1);
        i |= (i >>  2);
        i |= (i >>  4);
        i |= (i >>  8);
        i |= (i >> 16);
        return i - (i >>> 1);
    }

    public String GetMobileNo(byte[] mobileArray)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++)
        {
            byte bb=mobileArray[i];
            sb.append(((byte)(bb >> 4)));
            sb.append(((byte)((byte)(bb << 4) >> 4)));
        }
        return sb.toString();
    }

    public byte[] ConvertToBCD6(String mobileNo)
    {
        byte[] mobileArray=new byte[6];
        if (mobileNo.length() != 12) return mobileArray;
        for (int i = 0; i < 6; i++)
        {
            mobileArray[i] = Byte.parseByte(mobileNo.substring(i*2, 2), 16);
        }
        return mobileArray;
    }


    /**
     * 异或运算和
     * @param bytes
     * @return
     */
    public static byte[] byteOrbyte(byte[] bytes){
        byte[] orbyte = new byte[1];
        byte value = bytes[0];
        for (int i = 1; i < bytes.length; i++) {
            value = (byte) (value^bytes[i]);
        }
        orbyte[0] = value;
        return orbyte;
    }

    //苏标校验码算法
    public static String makeChecksum(String data) {
        if (data == null || data.equals("")) {
            return "";
        }
        int total = 0;
        int len = data.length();
        int num = 0;
        while (num < len) {
            String s = data.substring(num, num + 2);
            System.out.println(s);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        /**
         * 用256求余最大是255，即16进制的FF
         */
        int mod = total % 256;
        String hex = Integer.toHexString(mod);
        len = hex.length();
        // 如果不够校验位的长度，补0,这里用的是两位校验
        if (len < 2) {
            hex = "0" + hex;
        }

        return hex.toUpperCase();
    }

    public static void main(String[] args) {
//        Date now = new Date();
//        SimpleDateFormat f = new SimpleDateFormat("今天是" + "yyyy年MM月dd日 E kk点mm分");
//        byte[] result = str2Bcd(f.format(now));
//
//        System.out.println(Arrays.toString(result));
//
//        byte[] result2 = str2Bcd("00 06 21 01 96 85 01 88 25 10 50 50 3B D7");
//
//        System.out.println(Arrays.toString(result2));
////        byte[] result3 ={'a','b','3B'};


        System.out.println(makeChecksum("00 06 21 01 96 85 01 88 25 10 50 50 3B D7"));
    }



}
