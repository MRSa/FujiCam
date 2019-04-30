package net.osdn.gokigen.cameratest.fuji;

import java.util.Arrays;

class ReceivedData
{
    private final byte[] data;

    ReceivedData(byte[] data, int length)
    {
        //this.data = new byte[length];
        this.data = Arrays.copyOfRange(data, 0, length);
    }

    byte[] getData()
    {
        return (data);
    }
}
