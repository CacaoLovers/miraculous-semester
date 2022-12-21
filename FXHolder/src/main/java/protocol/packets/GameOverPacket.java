package protocol.packets;

import lombok.Data;
import protocol.PacketTypes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class GameOverPacket extends Packet{
    private byte winnerId;

    public GameOverPacket(byte winnerId) {
        super(PacketTypes.GAME_OVER);
        this.winnerId = winnerId;
    }

    public byte getWinnerId() {
        return winnerId;
    }

    @Override
    public byte[] toByteArray() {
        try (ByteArrayOutputStream writer = new ByteArrayOutputStream()) {
            writer.write(new byte[] {HEADER_1, HEADER_2});
            writer.write(type);
            writer.write(winnerId);

            writer.write(new byte[] {FOOTER_1, FOOTER_2});
            return writer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameOverPacket fromByteArray(byte[] data) {
        if(Packet.getType(data) != PacketTypes.GAME_OVER) {
            throw new IllegalArgumentException("Wrong packet type");
        }
        return new GameOverPacket(data[3]);
    }
}
