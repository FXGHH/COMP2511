package unsw.blackout.DeviceALL;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import unsw.blackout.File.File;
import unsw.blackout.Satellites.Satellite;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

public class Device {

    private List<Device> devices = new ArrayList<>();
    private List<File> files = new ArrayList<>();

    private String deviceId;
    private String type;
    private Angle position;
    private double height;
    private int maxRange;

    public Device(String deviceId, String type, Angle position, int maxRange){
        this.deviceId = deviceId;
        this.type = type;
        this.position = position;
        this.height = RADIUS_OF_JUPITER;
        this.maxRange = maxRange;
    }

    public Device() {
    }


    public void addFile(File file) {
        files.add(file);
    }

    public EntityInfoResponse deviceResponse() {
        Map<String, FileInfoResponse> fileMap = new HashMap<>();
        for (File file : files) {
            fileMap.put(file.getFileName(), getFileResponse(file.getFileName()));
        }
        EntityInfoResponse newResponse = new EntityInfoResponse(this.deviceId, this.position, this.height, this.type, fileMap);
        return newResponse;
    }

    public FileInfoResponse getFileResponse(String fileName) {
        File file = getFile(fileName, this.files);
        FileInfoResponse newResponse = new FileInfoResponse(fileName, file.getAvailableContent(), file.getContent().length(), file.hasTransferCompleted());
        return newResponse;
    }

    private File getFile(String fileName, List<File> filesList) {
        for (File file : filesList) {
            if (file.getFileName() == fileName);
            return file;
        }
        return null;
    }

    // check if the satellite within range of device
    public boolean SatelliteinRangeToDevice(Satellite satellite) {
        if (MathsHelper.isVisible(satellite.getHeight(), satellite.getPosition(), getPosition())) {
            if (MathsHelper.getDistance(satellite.getHeight(), satellite.getPosition(), getPosition()) <= maxRange) {
                return true;
            }
        }
        return false;
    }

    public String getDeviceId() {
        return deviceId;
    }


    public String getType() {
        return type;
    }


    public Angle getPosition() {
        return position;
    }


    public double getHeight() {
        return height;
    }

    public List<Device> getDevices() {
        return devices;
    }


    public List<File> getFiles() {
        return files;
    }


    public int getMaxRange() {
        return maxRange;
    }

    
    public boolean checkFileExist(String fileName) {
        for (File f : files) {
            if (f.getFileName() == fileName) {
                return true;
            }
        }
        return false;
    }

    public File getFile(String fileName) {
        for (File f : files) {
            if (f.getFileName() == fileName) {
                return f;
            }
        }
        return null;
    }

    public String getFileContent(String fileName) {
        return this.getFile(fileName).getContent();
    }

    public void satelliteTeleportsToDevice(Satellite from, File file) {
        if (from.checkTeleport()) {
            for (File deviceFile : this.getFiles()) {
                if (deviceFile.getFileName() == file.getFileName()) {
                    deviceFile.setAvailableContent(deviceFile.getContent() + deviceFile.getLeftContent().replace("t", ""));
                    deviceFile.setAvailableSize(deviceFile.getContent().length());
                    break;
                }
            }
        }
        
    }
}
