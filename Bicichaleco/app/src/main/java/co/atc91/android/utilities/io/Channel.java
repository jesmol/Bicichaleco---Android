package co.atc91.android.utilities.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;


/**
 * Created by Christian on 8/25/2015.
 * <p/>
 * Clase wrapper para un stream de entrada y un stream de salida que representan un canal de
 * comunicacion bidireccional.
 */
public class Channel implements Serializable {
    // ------------
    // Constantes
    // ------------

    private static final long serialVersionUID = 512699846256589969L;


    // -----------
    // Atributos
    // -----------

    protected DataInputStream datain;

    protected DataOutputStream dataout;

    protected boolean open;


    // -------------
    // Constructor
    // -------------

    public Channel(InputStream in, OutputStream out) {
        datain = new DataInputStream(in);
        dataout = new DataOutputStream(out);

        open = true;
    }

    // ---------
    // Metodos
    // ---------


    public void close() {
        try {
            dataout.close();
            datain.close();
            open = false;
        } catch (IOException e) {
            throw new ChannelException(e.getMessage(), e.getStackTrace());
        }
    }

    public final char readChar() {
        try {
            return datain.readChar();
        } catch (IOException e) {
            throw new ChannelException(e.getMessage(), e.getStackTrace());
        }
    }

    public int read() {
        try {
            return datain.read();
        } catch (IOException e) {
            throw new ChannelException(e.getMessage(), e.getStackTrace());
        }
    }

    public int available() {
        try {
            return datain.available();
        } catch (IOException e) {
            throw new ChannelException(e.getMessage(), e.getStackTrace());
        }
    }

    public final byte readByte() {
        try {
            return datain.readByte();
        } catch (IOException e) {
            throw new ChannelException(e.getMessage(), e.getStackTrace());
        }
    }

    public final int readInt() {
        try {
            return datain.readInt();
        } catch (IOException e) {
            throw new ChannelException(e.getMessage(), e.getStackTrace());
        }
    }

    public final String readUTF() {
        try {
            return datain.readUTF();
        } catch (IOException e) {
            throw new ChannelException(e.getMessage(), e.getStackTrace());
        }
    }

    public void write(int b) {
        try {
            dataout.write(b);
        } catch (IOException e) {
            throw new ChannelException(e.getMessage(), e.getStackTrace());
        }
    }

    public void clear() {
        try {
            datain.skip(datain.available());
        } catch (IOException e) {
            throw new ChannelException(e.getMessage(), e.getStackTrace());
        }
    }

    public void flush() {
        try {
            dataout.flush();
        } catch (IOException e) {
            throw new ChannelException(e.getMessage(), e.getStackTrace());
        }
    }

    public final void writeByte(byte v) {
        try {
            dataout.write((int) v);
        } catch (IOException e) {
            throw new ChannelException(e.getMessage(), e.getStackTrace());
        }
    }

    public final void writeInt(int v) {
        try {
            dataout.writeInt(v);
        } catch (IOException e) {
            throw new ChannelException(e.getMessage(), e.getStackTrace());
        }
    }

    public final void writeUTF(String str) {
        try {
            dataout.writeUTF(str);
        } catch (IOException e) {
            throw new ChannelException(e.getMessage(), e.getStackTrace());
        }
    }

    public final void writeChar(char v) {
        try {
            dataout.writeChar(v);
        } catch (IOException e) {
            throw new ChannelException(e.getMessage(), e.getStackTrace());
        }
    }

    public boolean isOpen() {
        return open;
    }
}

