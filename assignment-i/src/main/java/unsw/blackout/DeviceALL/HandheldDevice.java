package unsw.blackout.DeviceALL;

import unsw.utils.Angle;

public class HandheldDevice extends Device{
    private int maxRange;
    public HandheldDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position, 50_000);
    }
    
    public int getMaxRange() {
        return maxRange;
    }
}
