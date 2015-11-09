package co.atc91.bicichaleco.mundo;

import android.content.Context;

/**
 * Created by Christian on 8/25/2015.
 * <p/>
 * Contrato que debe implementar el observador del administrador de canales
 */
public interface BicichalecoListener {
    /**
     * Activa el servicio bluetooth en el terminal android
     */
    void enableBluetooth();

    Context getContext();

    void onDirectionsReady();


    /**
     * Es invocado cada vez que se genera un evento en los canales de comunicacion
     *
     * @param event El evento que se generó
     */
    void onEvent(String event);
}