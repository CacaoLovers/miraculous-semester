package protocol.packets;

import javafx.scene.input.KeyCode;
import protocol.PacketTypes;

import java.io.*;


public class MovePacket extends Packet{

    private byte playerId;
    private byte direction;

    static final byte UP = 0;
    static final byte RIGHT = 1;
    static final byte DOWN = 2;
    static final byte LEFT = 3;

    public MovePacket(byte playerId, byte direction) {
        super(PacketTypes.MOVE_SET);
        this.playerId = playerId;
        this.direction = direction;
    }

    @Override
    public byte[] toByteArray() {
        try (ByteArrayOutputStream writer = new ByteArrayOutputStream()) {
            writer.write(new byte[] {HEADER_1, HEADER_2});
            writer.write(type);
            writer.write(playerId);
            writer.write(direction);
            writer.write(new byte[] {FOOTER_1, FOOTER_2});
            return writer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MovePacket fromByteArray(byte[] data) {
        if(Packet.getType(data) != PacketTypes.MOVE_SET) {
            throw new IllegalArgumentException("Wrong packet type");
        }
        if(data.length < 7) {
            throw new IllegalArgumentException("Unacceptable packet");
        }

        MovePacket packet = new MovePacket(data[2], data[3]);

        return packet;
    }


}
