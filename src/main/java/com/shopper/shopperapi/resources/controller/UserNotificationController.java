package com.shopper.shopperapi.resources.controller;

import com.shopper.shopperapi.models.DeviceGroup;
import com.shopper.shopperapi.models.User;
import com.shopper.shopperapi.models.UserNotificationDeviceGroup;
import com.shopper.shopperapi.services.UserNotificationService;
import com.shopper.shopperapi.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user-notification")
@CrossOrigin(origins = "*")
public class UserNotificationController {

    /**
     * TODO: Clean notification_device_group when the notification key is dropped
     */

    @Autowired
    UserNotificationService userNotificationService;

    @Autowired
    UserService userService;

    @PostMapping
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> createUserDeviceGroup(
            @RequestBody @Valid DeviceGroup deviceGroup
    ) throws JSONException {

        System.out.println(">>>>>>>>> PAYLOAD: " + deviceGroup.toString());

        // Create Firebase Device Group of the user
        String notificationKey = userNotificationService.createNotificationDeviceGroup(
                deviceGroup.getUserId(), deviceGroup.getRegistrationIds()
        );

        // New Instande of userNotificationGroup
        UserNotificationDeviceGroup userNotificationDeviceGroup = new UserNotificationDeviceGroup();
        userNotificationDeviceGroup.setNotificationKeyName("appUser-" + deviceGroup.getUserId());
        userNotificationDeviceGroup.setNotificationKey(notificationKey);

        // notification_device_group field in user document
        ObjectId userOid = new ObjectId(deviceGroup.getUserId());
        User user = userService.findById(userOid).get();
        Map<String, String> deviceGroupKeys = new HashMap<>();
        deviceGroupKeys.put("notification_key", notificationKey);
        deviceGroupKeys.put("notification_key_name", "appUser-" + deviceGroup.getUserId());
        user.setNotificationDeviceGroup(deviceGroupKeys);
        userService.update(new ObjectId(deviceGroup.getUserId()), user);

        return new ResponseEntity<>(userNotificationDeviceGroup, HttpStatus.OK);
    }

    @PostMapping(value = "/{userId}/add")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> addUserDeviceGroup(
            @PathVariable("userId") String userId,
            @RequestBody Map<String, List<?>> registrationIds
    ) throws JSONException {

        String notificationKeyName = userService.getUserNotificationKeyName(userId);
        String notificationKey = userService.getUserNotificationKey(userId);

        String newNotificationKey = userNotificationService.addtNotificationKeyFcmCall(
                notificationKeyName, registrationIds.get("registration_ids"), notificationKey);

        // ------------------- notificationKey cambia INICIO??
        userService.setUserNotificationKey(userId, notificationKeyName, newNotificationKey);
        // ------------------- notificationKey cambia FIN??

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{userId}/remove")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> removeUserDeviceGroup(
            @PathVariable("userId") String userId,
            @RequestBody Map<String, List<?>> registrationIds
    ) throws JSONException {

        String notificationKeyName = userService.getUserNotificationKeyName(userId);
        String notificationKey = userService.getUserNotificationKey(userId);

        String newNotificationKey = userNotificationService.removeNotificationKey(
                notificationKeyName, registrationIds.get("registration_ids"), notificationKey);

        // ------------------- notificationKey cambia INICIO??
        userService.setUserNotificationKey(userId, notificationKeyName, newNotificationKey);
        // ------------------- notificationKey cambia FIN??

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
