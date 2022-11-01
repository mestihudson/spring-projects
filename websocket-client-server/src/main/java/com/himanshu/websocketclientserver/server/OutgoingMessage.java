package com.himanshu.websocketclientserver.server;

public record OutgoingMessage(String content, String user, int sleepTime, boolean even, String uri) {}
