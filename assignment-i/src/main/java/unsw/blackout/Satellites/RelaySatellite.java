package unsw.blackout.Satellites;

import unsw.utils.Angle;

public class RelaySatellite extends Satellite{
    private int direction;

    public RelaySatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position, 1500, 300000, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
        // here -1 means, the direction is clockwise(1 is anticlockwise)
        if ((position.toDegrees() >= 190 && position.toDegrees() < 345) || 
            (position.toDegrees() >= 140 && position.toDegrees() < 190)) {
            direction = -1;
        } else {
            direction = 1;
        }
        this.setAvailableStorage(0);
    }
    
    /* 
     * Get the next minute position of relay satellite
    */
    @Override
    public void nextPostion() {
        double degrees =  this.getVelocity() / (2 * Math.PI * this.getHeight()) * 360;
        Angle difference = Angle.fromDegrees(degrees);
        if (this.direction == 1) {
            if (this.getPosition().toDegrees() >= 190 && this.getPosition().toDegrees() < 345) {
                // this.setPosition(Angle.fromDegrees(190));
                this.setPosition(this.getPosition().subtract(difference));
                this.direction = -1;
            } else {
                this.setPosition(this.getPosition().add(difference));
            }

        } else if (this.direction == -1) {
            if (this.getPosition().toDegrees() <= 140) {
                this.setPosition(this.getPosition().add(difference));
                this.direction = 1;
            } else {
                this.setPosition(this.getPosition().subtract(difference));
            }
        }
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
    
}
