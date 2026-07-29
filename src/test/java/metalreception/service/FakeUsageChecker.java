package metalreception.service;

public class FakeUsageChecker implements UsageChecker {

    private boolean clientInUse = false;
    private boolean metalInUse = false;

    public void setClientInUse(boolean value) {
        this.clientInUse = value;
    }

    public void setMetalInUse(boolean value) {
        this.metalInUse = value;
    }

    @Override
    public boolean isClientInUse(int clientId) {
        return clientInUse;
    }

    @Override
    public boolean isMetalInUse(int metalId) {
        return metalInUse;
    }
}