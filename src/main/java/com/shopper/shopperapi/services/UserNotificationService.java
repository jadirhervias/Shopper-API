package com.shopper.shopperapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.shopper.shopperapi.utils.firebase.DeviceGroupsOperations.*;

@Service
public class UserNotificationService {

    private final String HANDLE_DEVICE_GROUP_API_URL = "https://android.googleapis.com/gcm/notification";
    private final String FIREBASE_SERVER_KEY = "AAAAnRLCoVc:APA91bGDZREuBwRlRe7spQZpx6mP719_GHKkUzNCUVqUNQoN0grqvRNb3A2zLssVGtMH9y1svaVEqPc9I3uC6fpQhhJOVK-bkgSwWSzcuZhgtpOnDyio5dgtq9CGEZV_Zcc7hwUedAmy";
    private final String SENDER_ID = "674624610647";

    @Autowired
    private RestTemplate restTemplate;

    public String createNotificationDeviceGroup(String userid, List<String> registrationIds)
            throws JSONException {

        // HTTP request header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + FIREBASE_SERVER_KEY);
        httpHeaders.set("project_id", SENDER_ID);
        httpHeaders.set("Content-Type", "application/json");

        // HTTP request
        JSONObject data = new JSONObject();
        data.put("operation", CREATE_DEVICE_GROUP.getOperation());
        data.put("notification_key_name", "appUser-" + userid);
        data.put("registration_ids", new JSONArray(registrationIds));

        System.out.println(">>>>>>>>>> " + data.toString());

        HttpEntity httpEntity = new HttpEntity<>(data.toString(), httpHeaders);

        String responseString =  restTemplate.postForObject(HANDLE_DEVICE_GROUP_API_URL, httpEntity, String.class);

        // Parse the JSON string and return the notification key
        JSONObject response = new JSONObject(responseString);
        return response.getString("notification_key");
    }

    // Una operación correcta devuelve una notification_key.
    // Guarda esta notification_key y el notification_key_name correspondiente para utilizarlos en operaciones subsiguientes.
    public String addtNotificationKeyFcmCall(
            String notificationKeyName, List<?> registrationIds, String notificationKey)
            throws JSONException {

        // HTTP request header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + FIREBASE_SERVER_KEY);
        httpHeaders.set("project_id", SENDER_ID);
        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.set("Accept", "application/json");

        // HTTP request
        JSONObject data = new JSONObject();
        data.put("operation", ADD_NOTIFICATION_KEY.getOperation());
        data.put("notification_key_name", notificationKeyName);
        data.put("registration_ids", new JSONArray(registrationIds));
        data.put("notification_key", notificationKey);

        HttpEntity httpEntity = new HttpEntity<>(data.toString(), httpHeaders);

        String responseString =  restTemplate.postForObject(HANDLE_DEVICE_GROUP_API_URL, httpEntity, String.class);

        // Parse the JSON string and return the notification key
        JSONObject response = new JSONObject(responseString);
        return response.getString("notification_key");
    }

    public String removeNotificationKey(
            String notificationKeyName, List<?> registrationIds, String notificationKey)
            throws JSONException {

        // HTTP request header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + FIREBASE_SERVER_KEY);
        httpHeaders.set("project_id", SENDER_ID);
        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.set("Accept", "application/json");

        // HTTP request
        JSONObject data = new JSONObject();
        data.put("operation", REMOVE_NOTIFICATION_KEY.getOperation());
        data.put("notification_key_name", notificationKeyName);
        data.put("registration_ids", new JSONArray(registrationIds));
        data.put("notification_key", notificationKey);

        HttpEntity httpEntity = new HttpEntity<>(data.toString(), httpHeaders);

        String responseString =  restTemplate.postForObject(HANDLE_DEVICE_GROUP_API_URL, httpEntity, String.class);

        // Parse the JSON string and return the notification key
        JSONObject response = new JSONObject(responseString);
        return response.getString("notification_key");
    }
}
