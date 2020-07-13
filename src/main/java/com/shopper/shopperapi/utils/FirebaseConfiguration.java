package com.shopper.shopperapi.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.shopper.shopperapi.models.*;
import com.shopper.shopperapi.services.FCMService;
import com.shopper.shopperapi.services.OrderService;
import com.shopper.shopperapi.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static com.shopper.shopperapi.utils.notification.NotificationMessages.*;
import static com.shopper.shopperapi.utils.notification.OrderState.*;

@Configuration
public class FirebaseConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfiguration.class);

    @Autowired
    private FCMService fcmService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    private Thread thread;

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

                    logger.info("event");
                    logger.info("Order from Firebase: " + dataSnapshot.getValue(Order.class));
                    logger.info("Previous Order ID: " + prevChildKey);

                    Order newOrder = dataSnapshot.getValue(Order.class);

                    logger.info("ID order: " + newOrder.getId());
                    String customerId = newOrder.getCustomer().getId();

                    logger.info("ID customer: " + customerId);
                    logger.info("CUSTOMER EMAIL: " + newOrder.getCustomer().getEmail());
                    logger.info("CUSTOMER FIRST NAME: " + newOrder.getCustomer().getFirstName());
                    logger.info("ORDER SHOP ID: " + newOrder.getShopId());

                    if (newOrder.getShopper() != null) {
                        logger.info("ID shopper: " + newOrder.getShopper().getId());
                    }

                    if (isOrderPendingState(newOrder.getState())) {
                        List<String> shoppersDeviceGroupKeys = orderService.shoperList(newOrder.getShopId());
                        logger.info("A QUE SHOPPERS LES LLEGARA LA NOTIFICAION: " + shoppersDeviceGroupKeys);

                        thread =
                        fcmService.sendPushNotificationToShoppers(userService.getUserNotificationKey(customerId),
                            shoppersDeviceGroupKeys, MESSAGE_TITLE.getMessage(),
                            NEW_ORDER_MESSAGE_BODY.getMessage(), newOrder);

                        thread.start();
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {

                    logger.info("Order (state) changed");
                    Order changedOrder = dataSnapshot.getValue(Order.class);
                    logger.info("The Order ID is: " + changedOrder.getId());

                    int state = changedOrder.getState();
                    logger.info("The Order state is: " + state);

                    String customerId = changedOrder.getCustomer().getId();
                    String shopperId = changedOrder.getCustomer().getId();

                    switch (state) {
                        // Orden tomada
                        case 1:
                            fcmService.sendPushNotificationToCustomer(
                                    userService.getUserNotificationKey(customerId),
                                    userService.getUserNotificationKey(shopperId),
                                    MESSAGE_TITLE.getMessage() + userService.getUserFirstName(customerId),
                                    ORDER_TAKEN_MESSAGE_BODY.getMessage(), changedOrder);

                            thread.interrupt();
                            logger.info("Thread murio? " + thread.isAlive());

                            break;
                        // Orden llegó
                        case 2:
                            fcmService.sendPushNotificationToCustomer(
                                    userService.getUserNotificationKey(customerId),
                                    userService.getUserNotificationKey(shopperId),
                                    MESSAGE_TITLE.getMessage() + userService.getUserFirstName(customerId),
                                    ORDER_ARRIVED_MESSAGE_BODY.getMessage(), changedOrder);
                            break;
                        // Orden completada / recibida
                        case 3:
                            logger.info(">>> CASE 3");
                            logger.info(">>> ORDER OBJ");
                            logger.info("" + changedOrder);
                            orderService.deleteFirebaseOrder(changedOrder);
                            break;
                        // Orden cancelada
                        case 4:
                            List<String> shoppersDeviceGroupKeys = Collections.singletonList(userService.getUserNotificationKey(shopperId));
                            fcmService.sendPushNotificationToShoppers(
                                    userService.getUserNotificationKey(customerId),
                                    shoppersDeviceGroupKeys, MESSAGE_TITLE.getMessage()
                                            + userService.getUserFirstName(customerId)
                                            + " ha hecho un pedido",
                                    ORDER_ARRIVED_MESSAGE_BODY.getMessage(), changedOrder);
                            break;
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    logger.info("An Order was removed");
                    Order removedOrder = dataSnapshot.getValue(Order.class);
                    logger.info("The Order ID " + removedOrder.getId() + " has been deleted");
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    logger.info("Error del order child event listener");
                }
            });

        } catch (Exception e) {
            logger.warn("Exception: " + e);
            e.printStackTrace();
        }
    }

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
