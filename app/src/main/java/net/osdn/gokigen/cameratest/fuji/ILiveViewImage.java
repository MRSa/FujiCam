package net.osdn.gokigen.cameratest.fuji;

import android.graphics.Bitmap;

public interface ILiveViewImage
{
    void updateImage(ReceivedDataHolder receivedData);
    void updateImage(Bitmap bitmap);

}
