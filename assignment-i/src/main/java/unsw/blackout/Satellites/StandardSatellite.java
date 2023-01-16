package unsw.blackout.Satellites;

import unsw.utils.Angle;

public class StandardSatellite extends Satellite{

    public StandardSatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position, 2500, 150000, 1, 1, 80);
        this.setAvailableStorage(80);
    }

    /* 
     *  standard satellite move to next minute position
    */
    @Override
    public void nextPostion() {
        double degrees =  this.getVelocity() / (2 * Math.PI * this.getHeight()) * 360;
        this.setPosition(this.getPosition().subtract(Angle.fromDegrees(degrees))); 
    }
    
    @Override
    public boolean isStorageFull(int fileSize) {
        if (this.getFiles().size() < 3 && (this.getAvailableStorage() - fileSize) >= 0) {
            return false;
        }
        return true;
    }

    @Override
    public String storageFull() {
        if (this.getFiles().size() >= 3) {
            return "Max Files Reached";
        } 
        return "Max Storage Reached";
    }
}
