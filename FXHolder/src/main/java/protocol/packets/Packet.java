package protocol.packets;

import protocol.PacketTypes;

public abstract class Packet {
    protected static final byte HEADER_1 = (byte) 0x0;
    protected static final byte HEADER_2 = (byte) 0x10;

    protected static final byte FOOTER_1 = (byte) 0x11;
    protected static final byte FOOTER_2 = (byte) 0x13;

    protected byte type;

    public Packet(byte type) {
        this.type = type;
    }

    public abstract byte[] toByteArray();

    public static byte getType(byte[] data) {
        if (data[0] != HEADER_1 && data[1] != HEADER_2
                || data[data.length - 1] != FOOTER_2 && data[data.length - 2] != FOOTER_1) {
            throw new IllegalArgumentException("Unknown packet format");
        }
        if(data.length < 5) {
            throw new IllegalArgumentException("Unknown packet format");
        }
        return data[2];
    }
}
