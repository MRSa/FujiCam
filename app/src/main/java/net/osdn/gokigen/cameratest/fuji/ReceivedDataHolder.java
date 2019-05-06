package net.osdn.gokigen.cameratest.fuji;

import java.util.Arrays;

/**
 *   受信したデータを保持するクラス
 *
 */
public class ReceivedDataHolder
{
    private final byte[] data;

    ReceivedDataHolder(byte[] data, int length) {
        this.data = Arrays.copyOfRange(data, 0, length);
    }

    public byte[] getData() {
        return (data);
    }

}
