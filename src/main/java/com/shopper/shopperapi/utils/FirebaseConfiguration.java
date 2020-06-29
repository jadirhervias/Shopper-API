package com.shopper.shopperapi.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.shopper.shopperapi.models.*;
import com.shopper.shopperapi.services.FCMService;
import com.shopper.shopperapi.services.OrderService;
import com.shopper.shopperapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shopper.shopperapi.utils.notification.NotificationMessages.*;

@Configuration
public class FirebaseConfiguration {

    @Autowired
    private FCMService fcmService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    // Clase para obtener instancia de Firebase Realtime Database
    @Bean
    public DatabaseReference firebaseDatabase() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @Value("${application.firebase.database.url}")
    private String databaseUrl;

    @Value("${application.firebase.config.path}")
    private String configPath;

    @PostConstruct
    public void init() {
//        throws IOException {

        /**
         * Using Firebase service account
         */

//        FileInputStream serviceAccount = new FileInputStream("path/to/serviceAccountKey.json");

        try {
            InputStream serviceAccount = FirebaseConfiguration.class.getClassLoader().getResourceAsStream(configPath);

            if (serviceAccount == null) {
                System.out.println("Service Account credentials not found!");
            }

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(databaseUrl)
                    .build();

            FirebaseApp.initializeApp(options);

            FirebaseDatabase.getInstance().getReference().child("orders").addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                    System.out.println("EVENT");

                    System.out.println("HAS CHILD: " + dataSnapshot.hasChild(dataSnapshot.getKey()));
                    System.out.println(dataSnapshot);

//                    System.out.println("DESCRIPTION: " + dataSnapshot.child("value").child());

                    System.out.println("generic type: " + dataSnapshot.getValue());

                    System.out.println("CASTING MODEL TEST: " + dataSnapshot.getValue(Order.class));

//                    Object orderMap = dataSnapshot.getValue();
//                    orderMap.

                    System.out.println("Previous Order ID: " + prevChildKey);

//                    Map<String, Object> orderMap = new HashMap<>();
//                    orderMap.put(dataSnapshot.getKey(), dataSnapshot.getValue(true));
//
//                    System.out.println("ORDER MAP: " + orderMap);

//                    Order newOrder = new Order();
//                    Order newOrder = (Order) dataSnapshot.getValue();

                    System.out.println("OBJ CAN BE ORDER INSTANCE?: " + Order.class.equals(dataSnapshot.getValue()));
                    Order newOrder = dataSnapshot.getValue(Order.class);

                    System.out.println("ID order: " + newOrder.getId());

                    String customerId = newOrder.getCustomer().getId();
                    System.out.println("ID customer: " + customerId);
                    System.out.println("CUSTOMER EMAIL: " + newOrder.getCustomer().getEmail());
                    System.out.println("CUSTOMER FIRST NAME: " + newOrder.getCustomer().getFirstName());

                    if (newOrder.getShopper() != null) {
                        System.out.println("ID shopper: " + newOrder.getShopper().getId());
                    }

                    List<String> shoppersDeviceGroupKeys = orderService.shoperList(newOrder.getShopId());

                    fcmService.sendPushNotificationToShoppers(userService.getUserNotificationKey(customerId),
                            shoppersDeviceGroupKeys, MESSAGE_TITLE.getMessage(),
                            NEW_ORDER_MESSAGE_BODY.getMessage(), newOrder);
//                newOrder.getFirebaseDbReferenceKey()
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                    System.out.println("Order (state) changed");
                    Order changedOrder = dataSnapshot.getValue(Order.class);
                    System.out.println("The Order ID is: " + changedOrder.getId());

                    int state = changedOrder.getState();
                    System.out.println("The Order state is: " + state);

//                    String customerId = changedOrder.getCustomer().getId();
                    String customerId = changedOrder.getCustomer().getId();

//                    String shopperId = changedOrder.getCustomer().getId();
                    String shopperId = changedOrder.getCustomer().getId();


                    switch (state) {
                        // Orden tomada
                        case 1:
                            fcmService.sendPushNotificationToCustomer(
                                    userService.getUserNotificationKey(customerId),
                                    userService.getUserNotificationKey(shopperId),
                                    MESSAGE_TITLE.getMessage() + userService.getUserFirstName(customerId),
                                    ORDER_TAKEN_MESSAGE_BODY.getMessage(), changedOrder);
//                        changedOrder.getFirebaseDbReferenceKey()

                        // Orden llegó
                        case 2:
                            fcmService.sendPushNotificationToCustomer(
                                    userService.getUserNotificationKey(customerId),
                                    userService.getUserNotificationKey(shopperId),
                                    MESSAGE_TITLE.getMessage() + userService.getUserFirstName(customerId),
                                    ORDER_ARRIVED_MESSAGE_BODY.getMessage(), changedOrder);
//                        changedOrder.getFirebaseDbReferenceKey()

                        // Orden completada
                        case 3:
                            // ...

                        // Orden cancelada
                        case 4:
                            List<String> shoppersDeviceGroupKeys = Collections.singletonList(userService.getUserNotificationKey(shopperId));
//                        shoppersDeviceGroupKeys.add(userService.getUserNotificationKey(shopperId));
                            fcmService.sendPushNotificationToShoppers(
                                    userService.getUserNotificationKey(customerId),
                                    shoppersDeviceGroupKeys, MESSAGE_TITLE.getMessage()
                                            + userService.getUserFirstName(customerId)
                                            + " ha hecho un pedido",
                                    ORDER_ARRIVED_MESSAGE_BODY.getMessage(), changedOrder);
//                        changedOrder.getFirebaseDbReferenceKey()

                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    System.out.println("An Order was removed");
                    Order removedOrder = dataSnapshot.getValue(Order.class);
                    System.out.println("The Order ID " + removedOrder + " has been deleted");
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Error del order child event listener");
                }
            });

        } catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
    }
}
