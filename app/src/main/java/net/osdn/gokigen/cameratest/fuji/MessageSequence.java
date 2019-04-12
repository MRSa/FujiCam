package net.osdn.gokigen.cameratest.fuji;

class MessageSequence
{
    static final int HELLO = 0x0000;

    static final int START = 0x1002;
    static final int STOP = 0x1003;

    static final int IMAGE_INFO = 0x1008;
    static final int THUMBNAIL = 0x100a;

    static final int SHUTTER = 0x100e;

    static final int SINGLE_PART = 0x1015;
    static final int DOUBLE_PART = 0x1016;

    static final int FULL_IMAGE = 0x101b;
    static final int CAMERA_REMOTE = 0x101c;

    static final int CAMERA_LAST_IMAGE = 0x9022;

    static final int FOCUS_POINT = 0x9026;
    static final int FOCUS_UNLOCK = 0x9027;

    static final int CAMERA_CAPABILITIES = 0x902b;

    static final int SHUTTER_SPEED = 0x902c;
    static final int APERTURE = 0x902d;
    static final int EXPOSURE_CORRECTION = 0x902e;

    byte[] registration_message()
    {
        return (new byte[] {
                // length byte :  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                // header
                (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xf2, (byte)0xe4, (byte)0x53, (byte)0x8f,
                (byte)0xad, (byte)0xa5, (byte)0x48, (byte)0x5d, (byte)0x87, (byte)0xb2, (byte)0x7f, (byte)0x0b,
                (byte)0xd3, (byte)0xd5, (byte)0xde, (byte)0xd0, (byte)0x02, (byte)0x78, (byte)0xa8, (byte)0xc0,
                // device_name 'GOKIGEN device 0'
                (byte)0x47, (byte)0x00, (byte)0x4f, (byte)0x00, (byte)0x4b, (byte)0x00, (byte)0x49, (byte)0x00,
                (byte)0x47, (byte)0x00, (byte)0x45, (byte)0x00, (byte)0x4e, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
        });
    }

    byte[] hello_message()
    {
        return (new byte[] {
                // HELLO (0x0000)
                0x00, 0x00,
        });
    }

    byte[] start_message()
    {
        return (new byte[] {
                // START (0x1002)
                (byte)0x10, (byte)0x02,
                // index
                (byte)0x00, (byte)0x01,
                // data
                (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00,
        });
    }

    byte[] two_part_message_One()
    {
        return (new byte[] {
                // TWO PART (0x1016)
                (byte)0x10, (byte)0x16,
                // index
                (byte)0x00, (byte)0x01,
                // data
                (byte)0x01, (byte)0xdf, (byte)0x00, (byte)0x00,
        });
    }

    byte[] two_part_message_Two()
    {
        return (new byte[] {
                // TWO PART (0x1016)
                (byte)0x10, (byte)0x16,
                // index
                (byte)0x00, (byte)0x02,
                // data
                (byte)0x05, (byte)0x00, (byte)0x00, (byte)0x00,
        });
    }




}
