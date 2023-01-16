package unsw.blackout.DeviceALL;

import unsw.utils.Angle;

public class LaptopDevice extends Device{
    private int maxRange;
    public LaptopDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position, 100_000);
    }
    
    public int getMaxRange() {
        return maxRange;
    }
}
