package protocol.packets;

import javafx.scene.input.KeyCode;
import protocol.PacketTypes;

import java.io.*;


public class MovePacket extends Packet{
    public static final byte ON_KEY_PRESSED = 1;
    public static final byte ON_KEY_RELEASED = 2;
    private final byte subtype;

    private byte[] keyCodeByteArray;

    public MovePacket(byte subtype) {
        super(PacketTypes.MOVE_SET);
        if(subtype != ON_KEY_PRESSED && subtype != ON_KEY_RELEASED) {
            throw new IllegalArgumentException("Unacceptable subtype");
        }
        this.subtype = subtype;
    }

    public void setKeyCode(KeyCode keyCode) {
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(keyCode);
            keyCodeByteArray = bos.toByteArray();
            if (keyCodeByteArray.length > 255) {
                throw new IllegalArgumentException("Too much data sent");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public KeyCode getKeyCode() {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(keyCodeByteArray);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (KeyCode) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] toByteArray() {
        try (ByteArrayOutputStream writer = new ByteArrayOutputStream()) {
            writer.write(new byte[] {HEADER_1, HEADER_2});
            writer.write(type);
            writer.write(subtype);
            writer.write(keyCodeByteArray);

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
        byte subtype = data[3];
        MovePacket packet = new MovePacket(data[subtype]);

        byte[] keyCodeArray = new byte[data.length - 6];
        System.arraycopy(data, 4, keyCodeArray, 0, data.length - 6);

        packet.keyCodeByteArray = keyCodeArray;

        return packet;
    }

    public byte getSubtype() {
        return subtype;
    }

}
