package co.atc91.bicichaleco.interfaz;

import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import co.atc91.bicichaleco.R;
import co.atc91.bicichaleco.mundo.Bicichaleco;
import co.atc91.bicichaleco.mundo.BicichalecoListener;


/**
 * Created by Christian on 10/29/2015.
 *
 * GUI que permite al usuario fijar su destino
 * y ver la ruta que calcula la app.
 */
public class NavigationActivity extends FragmentActivity implements BicichalecoListener, View.OnClickListener {

    /**
     * Las coordenadas del norte de Cali
     */
    private static final LatLng NORTE = new LatLng(3.49126,-76.5161646);

    /**
     * Las coordenadas del sur de Cali
     */
    private static final LatLng SUR = new LatLng(3.3287314,-76.5326428);

    /**
     * Dialogo que se muestra mientras se calcula la ruta
     */
    private ProgressDialog dialog;

    /**
     * El mapa de la actividad
     */
    private GoogleMap map;

    /**
     * Fragmento que contiene el mapa
     */
    private SupportMapFragment fragment;

    /**
     * Los limites bajo los cuales se mostrara el mapa
     */
    private LatLngBounds latlngBounds;

    /**
     * Boton para navegar
     */
    private Button bNavigation;

    /**
     * La ruta que se calcula y se dibuja en el mapa
     */
    private Polyline newPolyline;

    /**
     * El ancho de la pantalla
     */
    private int width;

    /**
     * El alto de la pantalla
     */
    private int height;

    /**
     * El objeto principal de la capa de negocio
     */
    private Bicichaleco bicichaleco;

    /**
     * Inicializa los objetos de la interfaz y la capa de negocio
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);

        getSreenDimentions();
        fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        map = fragment.getMap();

        bNavigation = (Button) findViewById(R.id.bNavigation);
        bNavigation.setOnClickListener(this);

        //Inicializa el mundo
        bicichaleco = Bicichaleco.getInstance(this);
    }

    @Override
    protected void onResume() {

        super.onResume();
        latlngBounds = createLatLngBoundsObject(NORTE, SUR);
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));

    }

    /**
     * Dibuja la ruta calculada en el mapa
     */
    public void handleGetDirectionsResult() {
        List<LatLng> directionPoints = bicichaleco.getNavigationManager().getDirections();

        PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.RED);

        for (int i = 0; i < directionPoints.size(); i++) {
            rectLine.add(directionPoints.get(i));
        }

        if (newPolyline != null) {
            newPolyline.remove();
        }

        newPolyline = map.addPolyline(rectLine);
        latlngBounds = createLatLngBoundsObject(NORTE, SUR);
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));

    }

    /**
     * Estable las dimensiones de la pantalla
     */
    private void getSreenDimentions() {
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
    }

    /**
     * Crea el objeto de los limites del mapa
     * @param firstLocation La esquina superior izquierda del limite
     * @param secondLocation La esquina inferior derecha del limite
     * @return El limite creado
     */
    private LatLngBounds createLatLngBoundsObject(LatLng firstLocation, LatLng secondLocation) {
        if (firstLocation != null && secondLocation != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(firstLocation).include(secondLocation);

            return builder.build();
        }
        return null;
    }

    /**
     * Muestra el dialogo para que el usuario active el bluetooth
     */
    public void enableBluetooth() {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent enableBluetooth = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }
    }

    /**
     * Muestra mensajes de la capa de negocio al usuario
     * @param event El evento que se generó
     */
    public void onEvent(final String event) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), event,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Muestra el dialogo mientras que el sistema calcula la ruta
     */
    public void calcularRuta() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Calculating directions");
        dialog.show();
    }

    /**
     * Cierra el dialogo de calculando ruta y dibuja la ruta
     */
    public void onDirectionsReady() {
        dialog.dismiss();
        handleGetDirectionsResult();
    }

    /**
     * Retorna el contexto de la actividad
     * @return el contexto de la actividad
     */
    public Context getContext() {
        return getContext();
    }

    /**
     * Permite calcular la ruta
     * @param v
     */
    @Override
    public void onClick(View v) {
        calcularRuta();
    }
}
