package com.shopper.shopperapi.utils.distance;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DistanceCalculated {
    public static Double distanceCoord(Double lat1, Double lng1, Double lat2, Double lng2){

    	Double rdWord = 6372.795477598;

    	Double dlat = Math.toRadians((lat2) - (lat1));
    	Double dlon = Math.toRadians((lng2) - (lng1));
    	Double sindLat = Math.sin(dlat / 2);
    	Double sindLng = Math.sin(dlon / 2);
    	Double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng,2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
    	Double va2 = 2 * Math.atan2(Math.sqrt(va1),Math.sqrt(1 - va1));

    	Double distancia = rdWord * va2;
    	distancia = distancia * 1000;
    	Double redondeado = new BigDecimal(distancia).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

    	return redondeado;
    }
}
