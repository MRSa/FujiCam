package net.osdn.gokigen.cameratest.fuji;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 *   受信したデータを保持するクラス
 *
 */
public class ReceivedDataHolder
{
    private final byte[] data;

    ReceivedDataHolder(byte[] data, int length)
    {
        this.data = Arrays.copyOfRange(data, 0, length);
    }

    ReceivedDataHolder(char[] data, int length)
    {
        byte[] convertedData = toBytes(data);
        this.data = Arrays.copyOfRange(convertedData, 0, length);
    }

    public byte[] getData()
    {
        return (data);
    }

    /**
     *   char[]からbyte[]に変換する
     *    (https://stackoverflow.com/questions/5513144/converting-char-to-byte より)
     */
    private byte[] toBytes(char[] chars)
    {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0x00);
        return (bytes);
    }
}
