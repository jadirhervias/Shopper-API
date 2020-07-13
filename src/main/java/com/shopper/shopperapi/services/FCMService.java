package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.FirebaseNotification;
import com.shopper.shopperapi.models.Order;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(FCMService.class);

    @Autowired
    private RestTemplate restTemplate;

    // Notificar a todos los SHOPPERS más cercanos cuando un usuario CUSTOMER realizó una orden
    public Thread sendPushNotificationToShoppers(String senderDeviceFcmKey, List<?> receiverDeviceFcmKeys,
                                               String messageTitle, String messageBody, Order order) {
//    public void sendPushNotificationToShoppers(String senderDeviceFcmKey, List<?> receiverDeviceFcmKeys,
//            String messageTitle, String messageBody, Order order) {

        System.out.println("SEND PUSH NOTIFICATION TO THIS SHOPPERS: ");
        System.out.println("#: " + receiverDeviceFcmKeys.size());
        System.out.println("SHOPPERS: " + receiverDeviceFcmKeys);

        FirebaseNotification firebaseNotification = new FirebaseNotification();
        firebaseNotification.setTitle(messageTitle);
        firebaseNotification.setBody(messageBody);
        firebaseNotification.setNotificationType("Test");

        JSONObject notificationMsg = new JSONObject();
        notificationMsg.put("title", firebaseNotification.getTitle());
        notificationMsg.put("body", firebaseNotification.getBody());
        notificationMsg.put("notificationType", firebaseNotification.getNotificationType());

        JSONObject dataMessage = new JSONObject();
        dataMessage.put("title", firebaseNotification.getTitle());
        dataMessage.put("body", firebaseNotification.getBody());
        dataMessage.put("notificationType", firebaseNotification.getNotificationType());
        dataMessage.put("order", order);

//        receiverDeviceFcmKeys.forEach(key -> {
//            System.out.println("\nCalling fcm Server >>>>>>>");
//            String response = callToFcmServer(notificationMsg, dataMessage, (String) key, senderDeviceFcmKey);
//            System.out.println("Got response from fcm Server : " + response + "\n\n");
//        });

        return notiticationThread(receiverDeviceFcmKeys, notificationMsg, dataMessage, senderDeviceFcmKey);
    }

    private Thread notiticationThread(List<?> receiverDeviceFcmKeys, JSONObject notificationMsg, JSONObject dataMessage, String senderDeviceFcmKey) {
        Runnable not = new Runnable() {
            int num = receiverDeviceFcmKeys.size();
            int index = 0;
            @Override
            public void run() {
                while(index < num) {
                    try {

                        String key = (String) receiverDeviceFcmKeys.get(index);
                        String response = callToFcmServer(notificationMsg, dataMessage, key, senderDeviceFcmKey);
                        logger.info("Got response from fcm Server : " + response + "\n\n");
                        Thread.sleep(10000);
                        index++;

                    } catch (InterruptedException  e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        return new Thread(not);
    }

    // Notificar al usuario CUSTOMER cuando su pedido haya llegado
    public void sendPushNotificationToCustomer(String receiverDeviceFcmKey, String senderDeviceFcmKey,
                                               String messageTitle, String messageBody, Order order) {
        FirebaseNotification firebaseNotification = new FirebaseNotification();
        firebaseNotification.setTitle(messageTitle);
        firebaseNotification.setBody(messageBody);
        firebaseNotification.setNotificationType("Test");

        JSONObject notificationMsg = new JSONObject();
        notificationMsg.put("title", firebaseNotification.getTitle());
        notificationMsg.put("body", firebaseNotification.getBody());
        notificationMsg.put("notificationType", firebaseNotification.getNotificationType());

        JSONObject dataMessage = new JSONObject();
        dataMessage.put("title", firebaseNotification.getTitle());
        dataMessage.put("body", firebaseNotification.getBody());
        dataMessage.put("notificationType", firebaseNotification.getNotificationType());
        dataMessage.put("order", order);

        System.out.println("\nCalling fcm Server >>>>>>>");
        String response = callToFcmServer(notificationMsg, dataMessage, receiverDeviceFcmKey, senderDeviceFcmKey);
        System.out.println("Got response from fcm Server : " + response + "\n\n");
        System.out.println(">>>> CHECK THE CUSTOMER UI!!!!!");
    }

    // Request a FCM Server
    private String callToFcmServer(JSONObject notificationMessage, JSONObject dataMessage, String receiverDeviceFcmKey, String senderDeviceFcmKey) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + FIREBASE_SERVER_KEY);
        httpHeaders.set("Content-Type", "application/json");

        JSONObject json = new JSONObject();
        json.put("data", dataMessage);
        json.put("notification", notificationMessage);
        json.put("to", receiverDeviceFcmKey);
        json.put("from", senderDeviceFcmKey);
        System.out.println("Sending :" + json.toString());

        HttpEntity httpEntity = new HttpEntity<>(json.toString(), httpHeaders);

        return restTemplate.postForObject(FIREBASE_API_URL, httpEntity, String.class);
    }
}
