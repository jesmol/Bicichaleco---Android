package co.atc91.android.utilities.gmaps.gmaps;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.android.gms.maps.model.LatLng;

import android.util.Log;

/**
 * Clase que sirve como API para GOOGLE MAPS V2
 */
public class Navigation {

    /**
     * Las direcciones son para caretera
     */
    public static final String MODE_DRIVING = "driving";

    /**
     * Las direcciones on para peaton
     */
    public static final String MODE_WALKING = "walking";


    /**
     * Hace una peticion http al api de google de la ruta que se desea calcular
     * @param start El punto de inicio
     * @param end El punto de finalizacion
     * @param mode El modo(carretera o peaton)
     * @return El documento de la ruta calculada
     */
    public static Document getDocument(LatLng start, LatLng end, String mode) {
        String url = "http://maps.googleapis.com/maps/api/directions/xml?"
                + "origin=" + start.latitude + "," + start.longitude
                + "&destination=" + end.latitude + "," + end.longitude
                + "&sensor=false&units=metric&mode=driving";

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            InputStream in = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(in);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrona la duracion de un recorrido
     * @param doc El recorrido
     * @return La duracion
     */
    public static String getDurationText(Document doc) {
        NodeList nl1 = doc.getElementsByTagName("duration");
        Node node1 = nl1.item(0);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "text"));
        Log.i("DurationText", node2.getTextContent());
        return node2.getTextContent();
    }

    /**
     * Retorna la duracion raw de un recorrido
     * @param doc El recorrido
     * @return la duracion raw
     */
    public static int getDurationValue(Document doc) {
        NodeList nl1 = doc.getElementsByTagName("duration");
        Node node1 = nl1.item(0);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
        Log.i("DurationValue", node2.getTextContent());
        return Integer.parseInt(node2.getTextContent());
    }

    /**
     * Retorna la distancia de un recorrido
     * @param doc El recorrido
     * @return la distancia del recorrido
     */
    public static String getDistanceText(Document doc) {
        NodeList nl1 = doc.getElementsByTagName("distance");
        Node node1 = nl1.item(0);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "text"));
        Log.i("DistanceText", node2.getTextContent());
        return node2.getTextContent();
    }

    /**
     * Retorna la distancia raw de un recorrido
     * @param doc El recorrido
     * @return la distancia raw del recorrido
     */
    public static int getDistanceValue(Document doc) {
        NodeList nl1 = doc.getElementsByTagName("distance");
        Node node1 = nl1.item(0);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
        Log.i("DistanceValue", node2.getTextContent());
        return Integer.parseInt(node2.getTextContent());
    }

    /**
     * Retorna la direccion de inicio de un recorrido
     * @param doc El recorrido
     * @return la direccion de inicio
     */
    public static String getStartAddress(Document doc) {
        NodeList nl1 = doc.getElementsByTagName("start_address");
        Node node1 = nl1.item(0);
        Log.i("StartAddress", node1.getTextContent());
        return node1.getTextContent();
    }

    /**
     * Retorna la direccion de fin de un recorrido
     * @param doc El recorrido
     * @return la direccion de fin
     */
    public String getEndAddress(Document doc) {
        NodeList nl1 = doc.getElementsByTagName("end_address");
        Node node1 = nl1.item(0);
        Log.i("StartAddress", node1.getTextContent());
        return node1.getTextContent();
    }

    /**
     * Retorna las coordenadas del recorrido
     * @param doc El recorrido
     * @return La lista de coordenadas del recorrido
     */
    public static ArrayList<LatLng> getDirection(Document doc) {
        NodeList nl1, nl2, nl3;
        ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
        nl1 = doc.getElementsByTagName("step");
        if (nl1.getLength() > 0) {
            for (int i = 0; i < nl1.getLength(); i++) {
                Node node1 = nl1.item(i);
                nl2 = node1.getChildNodes();

                Node locationNode = nl2.item(getNodeIndex(nl2, "start_location"));
                nl3 = locationNode.getChildNodes();
                Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
                double lat = Double.parseDouble(latNode.getTextContent());
                Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                double lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));

                locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "points"));
                ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
                for (int j = 0; j < arr.size(); j++) {
                    listGeopoints.add(new LatLng(arr.get(j).latitude, arr.get(j).longitude));
                }

                locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "lat"));
                lat = Double.parseDouble(latNode.getTextContent());
                lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));
            }
        }

        return listGeopoints;
    }

    /**
     * Retorna el indice del primer nodo hijo con el nombre indicado
     * @param nl El nodo en le cual se buscara
     * @param nodename El nombre del nodo hijo
     * @return El indice del nodo hijo o -1 si no se encuentra.
     */
    private static int getNodeIndex(NodeList nl, String nodename) {
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeName().equals(nodename))
                return i;
        }
        return -1;
    }

    /**
     * Decodifica las coordenadas de la ruta del recorrido
     * @param encoded la ruta codificada
     * @return la lista de coordenadas de la ruta del recorrido
     */
    private static ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }

    /**
     * Retorna las indicaciones del recorrido
     * @param doc El recorrido
     * @return la lista de indicadiones
     */
    public static LinkedList<String> getManeuvers(Document doc) {
        LinkedList<String> maneuvers = new LinkedList<>();
        NodeList steps = doc.getElementsByTagName("step");

        for (int i = 0; i < steps.getLength(); i++) {
            Node step = steps.item(i);

            int maneuverIndex = getNodeIndex(step.getChildNodes(), "maneuver");

            if (maneuverIndex >= 0) {
                maneuvers.add(step.getChildNodes().item(maneuverIndex).getTextContent());
            }
        }

        return maneuvers;
    }
}