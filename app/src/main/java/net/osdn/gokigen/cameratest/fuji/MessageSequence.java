package net.osdn.gokigen.cameratest.fuji;

class MessageSequence
{

    static final int IMAGE_INFO = 0x1008;
    static final int THUMBNAIL = 0x100a;

    static final int FULL_IMAGE = 0x101b;
    static final int CAMERA_LAST_IMAGE = 0x9022;

    static final int CAMERA_CAPABILITIES = 0x902b;

    static final int SHUTTER_SPEED = 0x902c;
    static final int APERTURE = 0x902d;

    byte[] registration_message()
    {
        return (new byte[] {
                // length byte :  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                // header
                (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xf2, (byte)0xe4, (byte)0x53, (byte)0x8f,
                (byte)0xad, (byte)0xa5, (byte)0x48, (byte)0x5d, (byte)0x87, (byte)0xb2, (byte)0x7f, (byte)0x0b,
                (byte)0xd3, (byte)0xd5, (byte)0xde, (byte)0xd0, // (byte)0x02, (byte)0x78, (byte)0xa8, (byte)0xc0,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,

/**/
                // device_name 'GOKIGEN____012345678'
                (byte)0x47, (byte)0x00, (byte)0x4f, (byte)0x00, (byte)0x4b, (byte)0x00, (byte)0x49, (byte)0x00,
                (byte)0x47, (byte)0x00, (byte)0x45, (byte)0x00, (byte)0x4e, (byte)0x00, (byte)0x5f, (byte)0x00,
                (byte)0x5f, (byte)0x00, (byte)0x5f, (byte)0x00, (byte)0x5f, (byte)0x00, (byte)0x30, (byte)0x00,
                (byte)0x31, (byte)0x00, (byte)0x32, (byte)0x00, (byte)0x33, (byte)0x00, (byte)0x34, (byte)0x00,
                (byte)0x35, (byte)0x00, (byte)0x36, (byte)0x00, (byte)0x37, (byte)0x00, (byte)0x38, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
/**/
        });
    }

    byte[] stop_message()
    {
        return (new byte[] {
                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : STOP (0x1003)
                (byte)0x03, (byte)0x10,

                // message_id (0～1づつ繰り上がる)
                (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00,
        });
    }

    byte[] reset_message()
    {
        return (new byte[] { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, });
    }

    byte[] start_message()
    {
        return (new byte[] {
                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : START (0x1002)  : OpenSession
                (byte)0x02, (byte)0x10,

                // message_id (0～1づつ繰り上がる)
                (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00,

                // data ...
                (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00,
        });
    }

    byte[] start_message2()
    {
        return (new byte[] {
                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : single_part (0x1015) : GetDevicePropValue
                (byte)0x15, (byte)0x10,

                // message_id (0～1づつ繰り上がる)
                (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00,

                // data ...
                (byte)0x12, (byte)0xd2, (byte)0x00, (byte)0x00,

        });
    }

    byte[] start_message3_1()
    {
        return (new byte[] {
                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : two_part (0x1016) : SetDevicePropValue
                (byte)0x16, (byte)0x10,

                // message_id (0～1づつ繰り上がる)
                (byte)0x03, (byte)0x00, (byte)0x00, (byte)0x00,

                // data ...
                (byte)0x01, (byte)0xdf, (byte)0x00, (byte)0x00,

        });
    }

    byte[] start_message3_2()
    {
        return (new byte[] {
                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x02, (byte)0x00,

                // message_header.type : two_part (0x1016) : SetDevicePropValue
                (byte)0x16, (byte)0x10,

                // message_id (0～1づつ繰り上がる...けど two-part messageなので同じ)
                (byte)0x03, (byte)0x00, (byte)0x00, (byte)0x00,

                // data ...
                (byte)0x05, (byte)0x00,

        });
    }

    byte[] start_message4()
    {
        return (new byte[] {
                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : single_part (0x1015)  : GetDevicePropValue
                (byte)0x15, (byte)0x10,

                // message_id (0～1づつ繰り上がる)
                (byte)0x04, (byte)0x00, (byte)0x00, (byte)0x00,

                // data ...
                (byte)0x24, (byte)0xdf, (byte)0x00, (byte)0x00,

        });
    }

    byte[] start_message5_1()
    {
        return (new byte[] {
                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : two_part (0x1016)  : SetDevicePropValue
                (byte)0x16, (byte)0x10,

                // message_id (0～1づつ繰り上がる)
                (byte)0x05, (byte)0x00, (byte)0x00, (byte)0x00,

                // data ...
                (byte)0x24, (byte)0xdf, (byte)0x00, (byte)0x00,

        });
    }

    byte[] start_message5_2()
    {
        return (new byte[] {
                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x02, (byte)0x00,

                // message_header.type : two_part (0x1016)  : SetDevicePropValue
                (byte)0x16, (byte)0x10,

                // message_id (0～1づつ繰り上がる...けど two-part messageなので同じ)
                (byte)0x05, (byte)0x00, (byte)0x00, (byte)0x00,

                // data ...
                (byte)0x07, (byte)0x00, (byte)0x02, (byte)0x00,

        });
    }

    byte[] start_message6()
    {
        return (new byte[] {
                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : single_part (0x1015)  : GetDevicePropValue
                (byte)0x15, (byte)0x10,

                // message_id (0～1づつ繰り上がる...)
                (byte)0x06, (byte)0x00, (byte)0x00, (byte)0x00,

                // data ...
                (byte)0x12, (byte)0xd2, (byte)0x00, (byte)0x00,

        });
    }


    byte[] query_camera_capabilities()
    {
        return (new byte[] {
                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : camera_capabilities (0x902b)
                (byte)0x2b, (byte)0x90,

                // message_id (0～1づつ繰り上がる...)
                (byte)0x07, (byte)0x00, (byte)0x00, (byte)0x00,

        });
    }


    byte[] start_message8()
    {
        return (new byte[] {
                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : single_part (0x1015) : GetDevicePropValue
                (byte)0x15, (byte)0x10,

                // message_id (0～1づつ繰り上がる...)
                (byte)0x08, (byte)0x00, (byte)0x00, (byte)0x00,

                // data ...
                (byte)0x12, (byte)0xd2, (byte)0x00, (byte)0x00,

        });
    }



    byte[] camera_remote_message()
    {
        return (new byte[] {
                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : camera_remote (0x101c)  : InitiateOpenCapture
                (byte)0x1c, (byte)0x10,

                // message_id (0～1づつ繰り上がる...)
                (byte)0x09, (byte)0x00, (byte)0x00, (byte)0x00,

                // data ...
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,

        });
    }


    byte[] status_request_message()
    {
        return (new byte[] {

                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : single_part (0x1015) : GetDevicePropValue
                (byte)0x15, (byte)0x10,

                // message_id (0～1づつ繰り上がる...)
                (byte)0x0A, (byte)0x00, (byte)0x00, (byte)0x00,

                // data ...
                (byte)0x12, (byte)0xd2, (byte)0x00, (byte)0x00,
        });
    }


    byte[] execute_shutter_message()
    {
        return (new byte[] {

                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : shutter (0x100e)
                (byte)0x0e, (byte)0x10,

                // message_id (0～1づつ繰り上がる...
                (byte)0x0B, (byte)0x00, (byte)0x00, (byte)0x00,

                // data ...
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
        });
    }



    byte[] execute_focus_lock(byte pointX, byte pointY)
    {
        return (new byte[] {

                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : focus_point (0x9026)
                (byte)0x26, (byte)0x90,

                // message_id (0～1づつ繰り上がる...
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,

                // data ...
                pointY, pointX, (byte)0x02, (byte)0x03,
                //pointY, pointX, (byte)0x00, (byte)0x00,

        });
    }

    byte[] execute_focus_unlock()
    {
        return (new byte[] {

                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : focus_unlock (0x9027)
                (byte)0x27, (byte)0x90,

                // message_id (0～1づつ繰り上がる...
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
        });
    }

    byte[] update_aperture(boolean isIncrement)
    {
        return (new byte[] {

                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : aperture (0x5007)
                (byte)0x07, (byte)0x50,

                // message_id (0～1づつ繰り上がる...
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,

                // data ...
                (isIncrement)? (byte) 0x01 : (byte)0x00,  (byte)0x00, (byte)0x00, (byte)0x00,
        });

    }

    byte[] update_shutter_speed(boolean isIncrement)
    {
        return (new byte[] {

                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : aperture (0xd240)
                (byte)0x40, (byte)0xd2,

                // message_id (0～1づつ繰り上がる...
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,

                // data ...
                (isIncrement)? (byte) 0x01 : (byte)0x00,  (byte)0x00, (byte)0x00, (byte)0x00,
        });

    }

    byte[] update_exposure(boolean isIncrement)
    {
        return (new byte[] {

                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : aperture (0x902e)
                (byte)0x2e, (byte)0x90,

                // message_id (0～1づつ繰り上がる...
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,

                // data ...
                (isIncrement)? (byte) 0x01 : (byte)0x00,  (byte)0x00, (byte)0x00, (byte)0x00,
        });
    }

    byte[] update_property_1(byte high, byte low)
    {
        return (new byte[] {
                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x01, (byte)0x00,

                // message_header.type : two_part (0x1016)
                (byte)0x16, (byte)0x10,

                // message_id (0～1づつ繰り上がる)
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,

                // command code
               low, high, (byte)0x00, (byte)0x00,
        });
    }

    byte[] update_property_2(byte data0, byte data1)
    {
        return (new byte[] {
                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x02, (byte)0x00,

                // message_header.type : two_part (0x1016)
                (byte)0x16, (byte)0x10,

                // message_id (0～1づつ繰り上がる...けど two-part messageなので同じ)
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,

                // data...
                data1, data0,
        });
    }

    byte[] update_property_2(byte data0, byte data1, byte data2, byte data3)
    {
        return (new byte[] {
                // message_header.index : uint16 (0: terminate, 2: two_part_message, 1: other)
                (byte)0x02, (byte)0x00,

                // message_header.type : two_part (0x1016)
                (byte)0x16, (byte)0x10,

                // message_id (0～1づつ繰り上がる...けど two-part messageなので同じ)
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,

                // data...
                data3, data2, data1, data0,
        });
    }
}
