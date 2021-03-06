package ar.edu.utn.frba.dds.model.deserialize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import ar.edu.utn.frba.dds.model.poi.Geolocalizacion;
import ar.edu.utn.frba.dds.model.poi.sucursal.banco.ServicioBanco;
import ar.edu.utn.frba.dds.model.poi.sucursal.banco.SucursalBanco;
import ar.edu.utn.frba.dds.util.time.DateTimeProviderImpl;

public class SucursalBancoDeserializer extends JsonDeserializer<List<SucursalBanco>> {

    @Override
    public List<SucursalBanco> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode nodeElements = jp.getCodec().readTree(jp);
        Iterator<JsonNode> nodeIterator = nodeElements.elements();
        JsonNode node;
        JsonNode nodeServicio;
        SucursalBanco sucursalBanco;
        ServicioBanco servicioBanco;
        String banco;
        String sucursal;
        String gerente;
        Set<ServicioBanco> servicios;
        HashSet<String> palabrasClave = new HashSet<>();
        palabrasClave.add("banco");
        palabrasClave.add("sucursal");
        List<SucursalBanco> sucursales = new ArrayList<>();
        Iterator<JsonNode> nodeServiciosIterator = null;
        while (nodeIterator.hasNext()) {
            node = nodeIterator.next();
            //El constructor por defecto setea el horario por defecto de los bancos (10 a 15 de L a V)
            sucursalBanco = new SucursalBanco(new DateTimeProviderImpl(new DateTime()));
            servicios = new HashSet<>();
            banco = node.get("banco").asText();
            sucursal = node.get("sucursal").asText();
            gerente = node.get("gerente").asText();
            nodeServiciosIterator = node.get("servicios").elements();
            while (nodeServiciosIterator.hasNext()) {
                nodeServicio = nodeServiciosIterator.next();
                servicioBanco = new ServicioBanco();
                servicioBanco.setNombre(nodeServicio.asText());
                servicios.add(servicioBanco);
            }
            sucursalBanco.setBanco(banco);
            sucursalBanco.setSucursal(sucursal);
            sucursalBanco.setGerente(gerente);
            sucursalBanco.setServicios(servicios);
            sucursalBanco.setPalabrasClave(palabrasClave);
            sucursalBanco.setGeolocalizacion(new Geolocalizacion(node.get("x").asDouble(), node.get("y").asDouble()));
            sucursales.add(sucursalBanco);
        }
        return sucursales;
    }
}
