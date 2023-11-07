package org.example.Model;

import java.time.LocalDateTime;

/**
 * Used to describe the notifications sent to users
 * about the rides.
 */
public class Notification {

    private long notificationID;
    private long recipientId;
    private String message;
    private LocalDateTime timestamp;


}
