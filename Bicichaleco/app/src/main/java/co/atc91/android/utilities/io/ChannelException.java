package co.atc91.android.utilities.io;


@SuppressWarnings("serial")
/**
 * Created by Christian on 8/25/2015.
 *
 * Lanzada cuando existe un problema al comunicarse entre el servidor y el cliente.
 */
public class ChannelException extends RuntimeException {
    public ChannelException(String mensaje) {
        super(mensaje);
    }

    public ChannelException(String mensaje, StackTraceElement[] stack) {
        super(mensaje);

        setStackTrace(stack);
    }
}