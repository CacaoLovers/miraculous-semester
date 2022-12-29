package protocol.packets;

import lombok.Data;
import protocol.PacketTypes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class GameOverPacket extends Packet{
    private byte winner;

    public static final byte OWNER = 0;
    public static final byte ENEMY = 1;

    public GameOverPacket(byte winner) {
        super(PacketTypes.GAME_OVER);
        this.winner = winner;
    }

    public byte getWinnerId() {
        return winner;
    }

    @Override
    public byte[] toByteArray() {
        try (ByteArrayOutputStream writer = new ByteArrayOutputStream()) {
            writer.write(new byte[] {HEADER_1, HEADER_2});
            writer.write(type);
            writer.write(winner);

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
