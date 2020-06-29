package com.shopper.shopperapi.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.shopper.shopperapi.models.FirebaseNotification;
import com.shopper.shopperapi.models.Order;
import com.shopper.shopperapi.models.OrderShopper;
import com.shopper.shopperapi.services.FCMService;
import com.shopper.shopperapi.services.OrderService;
import com.shopper.shopperapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;
//import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.shopper.shopperapi.utils.notification.NotificationMessages.*;
import static com.shopper.shopperapi.utils.notification.OrderState.*;

@Configuration
public class FirebaseConfiguration {

    // Clase para obtener instancia de Firebase Realtime Database
    @Bean
//    @DependsOn(value = {"firebaseInitialization"})
    public DatabaseReference firebaseDatabase() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @Value("${application.firebase.database.url}")
    private String databaseUrl;

    @Value("${application.firebase.config.path}")
    private String configPath;

    @Autowired
    private FCMService fcmService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

//    @Bean("firebaseInitialization")
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
                    System.out.println(dataSnapshot);
                    System.out.println("order: " + dataSnapshot.getValue());

                    Order newOrder = dataSnapshot.getValue(Order.class);

                    System.out.println("Previous Order ID: " + prevChildKey);
                    System.out.println("ID order: " + newOrder.getId());

                    String customerId = newOrder.getCustomer().getId();
                    System.out.println("ID customer: " + customerId);

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
                    Order changedOrder = dataSnapshot.getValue(Order.class);
                    System.out.println("The Order ID is: " + changedOrder.getId());

                    int state = changedOrder.getState();
                    System.out.println("The Order state is: " + state);

                    String customerId = changedOrder.getCustomer().getId();
                    String shopperId = changedOrder.getCustomer().getId();

                    switch (state) {
                        // Orden tomada
                        case 1:
                            fcmService.sendPushNotificationToCustomer(
                                    userService.getUserNotificationKey(customerId),
                                    userService.getUserNotificationKey(shopperId), MESSAGE_TITLE.getMessage(),
                                    ORDER_TAKEN_MESSAGE_BODY.getMessage(), changedOrder);
//                        changedOrder.getFirebaseDbReferenceKey()

                            // Orden llegó
                        case 2:
                            fcmService.sendPushNotificationToCustomer(
                                    userService.getUserNotificationKey(customerId),
                                    userService.getUserNotificationKey(shopperId), MESSAGE_TITLE.getMessage(),
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
                                    shoppersDeviceGroupKeys, MESSAGE_TITLE.getMessage(),
                                    ORDER_ARRIVED_MESSAGE_BODY.getMessage(), changedOrder);
//                        changedOrder.getFirebaseDbReferenceKey()

                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
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

    /*+
        TODO: CHECK
     */
//    @Bean("orderEventListenerAdding")
//    @PostConstruct
//    @DependsOn(value = {"firebaseDatabaseReference"})
//    @DependsOn(value = {"firebaseInitialization"})
//    public ChildEventListener orderAddChildEventListener() {
//
//        DatabaseReference ordersRef = firebaseDatabase().child("orders");
//
//        return ordersRef.addChildEventListener(new ChildEventListener() {
//
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
//                Order newOrder = dataSnapshot.getValue(Order.class);
//
//                System.out.println("Previous Order ID: " + prevChildKey);
//                System.out.println("ID order: " + newOrder.getId());
//
//                String customerId = newOrder.getCustomer().getId();
//                System.out.println("ID customer: " + customerId);
//
//                if (newOrder.getShopper() != null) {
//                    System.out.println("ID shopper: " + newOrder.getShopper().getId());
//                }
//
//                List<String> shoppersDeviceGroupKeys = orderService.shoperList(newOrder.getShopId());
//
//                fcmService.sendPushNotificationToShoppers(userService.getUserNotificationKey(customerId),
//                        shoppersDeviceGroupKeys, MESSAGE_TITLE.getMessage(),
//                        NEW_ORDER_MESSAGE_BODY.getMessage(), newOrder);
////                newOrder.getFirebaseDbReferenceKey()
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
//                Order changedOrder = dataSnapshot.getValue(Order.class);
//                System.out.println("The Order ID is: " + changedOrder.getId());
//
//                int state = changedOrder.getState();
//                System.out.println("The Order state is: " + state);
//
//                String customerId = changedOrder.getCustomer().getId();
//                String shopperId = changedOrder.getCustomer().getId();
//
//                switch (state) {
//                    // Orden tomada
//                    case 1:
//                        fcmService.sendPushNotificationToCustomer(
//                                userService.getUserNotificationKey(customerId),
//                                userService.getUserNotificationKey(shopperId), MESSAGE_TITLE.getMessage(),
//                                ORDER_TAKEN_MESSAGE_BODY.getMessage(), changedOrder);
////                        changedOrder.getFirebaseDbReferenceKey()
//
//                    // Orden llegó
//                    case 2:
//                        fcmService.sendPushNotificationToCustomer(
//                                userService.getUserNotificationKey(customerId),
//                                userService.getUserNotificationKey(shopperId), MESSAGE_TITLE.getMessage(),
//                                ORDER_ARRIVED_MESSAGE_BODY.getMessage(), changedOrder);
////                        changedOrder.getFirebaseDbReferenceKey()
//
//                    // Orden completada
//                    case 3:
//                        // ...
//
//                    // Orden cancelada
//                    case 4:
//                        List<String> shoppersDeviceGroupKeys = Collections.singletonList(userService.getUserNotificationKey(shopperId));
////                        shoppersDeviceGroupKeys.add(userService.getUserNotificationKey(shopperId));
//                        fcmService.sendPushNotificationToShoppers(
//                                userService.getUserNotificationKey(customerId),
//                                shoppersDeviceGroupKeys, MESSAGE_TITLE.getMessage(),
//                                ORDER_ARRIVED_MESSAGE_BODY.getMessage(), changedOrder);
////                        changedOrder.getFirebaseDbReferenceKey()
//
//                }
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Order removedOrder = dataSnapshot.getValue(Order.class);
//                System.out.println("The Order ID " + removedOrder + " has been deleted");
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("Error del order child event listener");
//            }
//        });
//    }

    /*+
        TODO: CHECK
     */
//    @Bean
//    public void orderAddValueEventListener() {
//        DatabaseReference ordersRef = firebaseDatabase().child("orders");
//
//        ordersRef.child("test").addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
}
