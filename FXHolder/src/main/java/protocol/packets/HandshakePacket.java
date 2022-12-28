package protocol.packets;

import javafx.scene.input.KeyCode;
import protocol.PacketTypes;

import java.io.*;

public class HandshakePacket extends Packet{

    private byte playerId;

    public HandshakePacket(byte playerId) {
        super(PacketTypes.START);
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



    public static HandshakePacket fromByteArray(byte[] data) {
        return new HandshakePacket(data[3]);
    }
}
