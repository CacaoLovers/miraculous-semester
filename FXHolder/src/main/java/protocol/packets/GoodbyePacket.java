package protocol.packets;

import protocol.PacketTypes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GoodbyePacket extends Packet{
    public GoodbyePacket() {
        super(PacketTypes.GOODBYE);
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

    public static GoodbyePacket fromByteArray(byte[] data) {
        if(Packet.getType(data) != PacketTypes.GOODBYE) {
            throw new IllegalArgumentException("Wrong packet type");
        }
        return new GoodbyePacket();
    }
}
