package protocol.packets;

import protocol.PacketTypes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BombPacket extends Packet{
    private byte playerId;


    public BombPacket(byte playerId) {
        super(PacketTypes.BOMB);
        this.playerId = playerId;
    }

    @Override
    public byte[] toByteArray() {
        try (ByteArrayOutputStream writer = new ByteArrayOutputStream()) {
            writer.write(new byte[] {HEADER_1, HEADER_2});
            writer.write(type);
            writer.write(playerId);

            writer.write(new byte[] {FOOTER_1, FOOTER_2});
            return writer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BombPacket fromByteArray(byte[] data) {
        if(Packet.getType(data) != PacketTypes.BOMB) {
            throw new IllegalArgumentException("Wrong packet type");
        }
        return new BombPacket(data[3]);
    }
}
