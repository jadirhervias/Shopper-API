package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.FirebaseNotification;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FCMService {
    private final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
    private final String FIREBASE_SERVER_KEY = "AAAAnRLCoVc:APA91bGDZREuBwRlRe7spQZpx6mP719_GHKkUzNCUVqUNQoN0grqvRNb3A2zLssVGtMH9y1svaVEqPc9I3uC6fpQhhJOVK-bkgSwWSzcuZhgtpOnDyio5dgtq9CGEZV_Zcc7hwUedAmy";

    @Autowired
    private RestTemplate restTemplate;

    // Notificar a todos los SHOPPERS más cercanos cuando un usuario CUSTOMER realizó una orden
    public void sendPushNotificationToShoppers(List<?> keys, String messageTitle, String messageBody) {
        FirebaseNotification firebaseNotification = new FirebaseNotification();
        firebaseNotification.setTitle(messageTitle);
        firebaseNotification.setBody(messageBody);
        firebaseNotification.setNotificationType("Test");

        JSONObject msg = new JSONObject();
        msg.put("title", firebaseNotification.getTitle());
        msg.put("body", firebaseNotification.getBody());
        msg.put("notificationType", firebaseNotification.getNotificationType());

        keys.forEach(key -> {
            System.out.println("\nCalling fcm Server >>>>>>>");
            String response = callToFcmServer(msg, key.toString());
            System.out.println("Got response from fcm Server : " + response + "\n\n");
        });
    }

    // Notificar al usuario CUSTOMER cuando su pedido haya llegado
    public void sendPushNotificationToCustomer(String receiverDeviceFcmKey, String messageTitle, String messageBody) {
        FirebaseNotification firebaseNotification = new FirebaseNotification();
        firebaseNotification.setTitle(messageTitle);
        firebaseNotification.setBody(messageBody);
        firebaseNotification.setNotificationType("Test");

        JSONObject msg = new JSONObject();
        msg.put("title", firebaseNotification.getTitle());
        msg.put("body", firebaseNotification.getBody());
        msg.put("notificationType", firebaseNotification.getNotificationType());

        System.out.println("\nCalling fcm Server >>>>>>>");
        String response = callToFcmServer(msg, receiverDeviceFcmKey);
        System.out.println("Got response from fcm Server : " + response + "\n\n");
        System.out.println(">>>> CHECK THE CUSTOMER UI!!!!!");
    }

    // Request a FCM Server
    private String callToFcmServer(JSONObject message, String receiverDeviceFcmKey) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + FIREBASE_SERVER_KEY);
        httpHeaders.set("Content-Type", "application/json");

        JSONObject json = new JSONObject();
        json.put("data", message);
        json.put("notification", message);
        json.put("to", receiverDeviceFcmKey);
        System.out.println("Sending :" + json.toString());

        HttpEntity httpEntity = new HttpEntity<>(json.toString(), httpHeaders);

        return restTemplate.postForObject(FIREBASE_API_URL, httpEntity, String.class);
    }
}
