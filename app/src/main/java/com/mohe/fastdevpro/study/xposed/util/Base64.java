package com.mohe.fastdevpro.study.xposed.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Base64 {
    private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    public static String encode(byte[] bArr) {
        int length = bArr.length;
        StringBuffer stringBuffer = new StringBuffer((bArr.length * 3) / 2);
        int i = length - 3;
        int i2 = 0;
        loop0:
        while (true) {
            int i3 = 0;
            while (i2 <= i) {
                int i4 = (((bArr[i2] & 255) << 16) | ((bArr[i2 + 1] & 255) << 8)) | (bArr[i2 + 2] & 255);
                stringBuffer.append(legalChars[(i4 >> 18) & 63]);
                stringBuffer.append(legalChars[(i4 >> 12) & 63]);
                stringBuffer.append(legalChars[(i4 >> 6) & 63]);
                stringBuffer.append(legalChars[i4 & 63]);
                i2 += 3;
                i4 = i3 + 1;
                if (i3 >= 14) {
                    stringBuffer.append(" ");
                } else {
                    i3 = i4;
                }
            }
            break loop0;
        }
        int i5 = 0 + length;
        int i6;
        if (i2 == i5 - 2) {
            i6 = ((bArr[i2 + 1] & 255) << 8) | ((bArr[i2] & 255) << 16);
            stringBuffer.append(legalChars[(i6 >> 18) & 63]);
            stringBuffer.append(legalChars[(i6 >> 12) & 63]);
            stringBuffer.append(legalChars[(i6 >> 6) & 63]);
            stringBuffer.append("=");
        } else if (i2 == i5 - 1) {
            i6 = (bArr[i2] & 255) << 16;
            stringBuffer.append(legalChars[(i6 >> 18) & 63]);
            stringBuffer.append(legalChars[(i6 >> 12) & 63]);
            stringBuffer.append("==");
        }
        return stringBuffer.toString();
    }

    private static int decode(char c) {
        if (c >= 'A' && c <= 'Z') {
            return c - 65;
        }
        if (c >= 'a' && c <= 'z') {
            return (c - 97) + 26;
        }
        if (c >= '0' && c <= '9') {
            return ((c - 48) + 26) + 26;
        }
        if (c == '+') {
            return 62;
        }
        if (c == '/') {
            return 63;
        }
        if (c == '=') {
            return 0;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unexpected code: ");
        stringBuilder.append(c);
        throw new RuntimeException(stringBuilder.toString());
    }

    public static byte[] decode(String str) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            decode(str, byteArrayOutputStream);
            byte[] toByteArray = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                PrintStream printStream = System.err;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error while decoding BASE64: ");
                stringBuilder.append(e.toString());
                printStream.println(stringBuilder.toString());
            }
            return toByteArray;
        } catch (IOException unused) {
            throw new RuntimeException();
        }
    }

    private static void decode(String str, OutputStream outputStream) throws IOException {
        int length = str.length();
        int i = 0;
        while (true) {
            if (i < length && str.charAt(i) <= ' ') {
                i++;
            } else if (i != length) {
                int i2 = i + 2;
                int i3 = i + 3;
                int decode = (((decode(str.charAt(i)) << 18) + (decode(str.charAt(i + 1)) << 12)) + (decode(str.charAt(i2)) << 6)) + decode(str.charAt(i3));
                outputStream.write((decode >> 16) & 255);
                if (str.charAt(i2) != '=') {
                    outputStream.write((decode >> 8) & 255);
                    if (str.charAt(i3) != '=') {
                        outputStream.write(decode & 255);
                        i += 4;
                    } else {
                        return;
                    }
                }
                return;
            } else {
                return;
            }
        }
    }
}
