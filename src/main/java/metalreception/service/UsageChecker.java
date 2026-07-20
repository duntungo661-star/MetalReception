package main.java.metalreception.service;

public interface UsageChecker {
    boolean isClientInUse(int clientId);
    boolean isMetalInUse(int metalId);
}
