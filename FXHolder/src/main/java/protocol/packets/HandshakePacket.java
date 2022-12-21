package protocol.packets;

import protocol.PacketTypes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HandshakePacket extends Packet{
    public HandshakePacket() {
        super(PacketTypes.START);
    }

    @Override
    public byte[] toByteArray() {
        try (ByteArrayOutputStream writer = new ByteArrayOutputStream()) {
            writer.write(new byte[] {HEADER_1, HEADER_2});
            writer.write(type);


            writer.write(new byte[] {FOOTER_1, FOOTER_2});
            return writer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static HandshakePacket fromByteArray(byte[] data) {
        return new HandshakePacket();
    }
}
