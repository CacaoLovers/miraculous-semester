package protocol.packets;


import protocol.PacketTypes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DestroyBlockPacket extends Packet{
    private byte blockId;

    public DestroyBlockPacket(byte blockId) {
        super(PacketTypes.DESTROY_BLOCK);
        this.blockId = blockId;
    }

    @Override
    public byte[] toByteArray() {
        try (ByteArrayOutputStream writer = new ByteArrayOutputStream()) {
            writer.write(new byte[] {HEADER_1, HEADER_2});
            writer.write(type);
            writer.write(blockId);

            writer.write(new byte[] {FOOTER_1, FOOTER_2});
            return writer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static DestroyBlockPacket fromByteArray(byte[] data) {
        if(Packet.getType(data) != PacketTypes.DESTROY_BLOCK) {
            throw new IllegalArgumentException("Wrong packet type");
        }
        return new DestroyBlockPacket(data[3]);
    }

    public byte getBlockId() {
        return blockId;
    }
}
