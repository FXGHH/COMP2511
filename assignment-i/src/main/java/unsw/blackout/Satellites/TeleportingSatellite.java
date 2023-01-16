package unsw.blackout.Satellites;

import unsw.utils.Angle;

public class TeleportingSatellite extends Satellite{
    private int direction;

    public TeleportingSatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position, 1000, 200000, 15, 10, 200);
        // here -1 means, the direction is clockwise(1 is anticlockwise)
        this.direction = 1;
        this.setAvailableStorage(200);
    }

    /* 
     * Get the next minute position of teleporting satellite
    */
    @Override
    public void nextPostion() {
        double degrees =  this.getVelocity() / (2 * Math.PI * this.getHeight()) * 360;
        double nextPosition;
        if (this.direction == 1) {
            nextPosition = this.getPosition().toDegrees() + degrees;
            if (nextPosition >= 180) {
                this.setPosition(Angle.fromDegrees(0));
                // change move direction
                this.direction = -1;
            } else {
                this.setPosition(Angle.fromDegrees(nextPosition));
            }

        } else if (this.direction == -1) {
            nextPosition = this.getPosition().toDegrees() - degrees;
            if (nextPosition <= 180) {
                this.setPosition(Angle.fromDegrees(0));
                // change move direction
                this.direction = 1;
            } else {
                this.setPosition(Angle.fromDegrees(nextPosition));
            }
        }
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public boolean isStorageFull(int fileSize) {
        if ((getAvailableStorage() - fileSize) >= 0) {
            return false;
        }
        return true;
    }

    @Override
    public String storageFull() {
        return "Max Storage Reached";
    }

    
    
}
