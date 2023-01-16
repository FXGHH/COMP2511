package unsw.blackout.DeviceALL;

import unsw.utils.Angle;

public class DesktopDevice extends Device{
    private int maxRange;
    public DesktopDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position, 200_000);
    }

    public int getMaxRange() {
        return maxRange;
    }
}
