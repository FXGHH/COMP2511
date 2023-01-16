package unsw.blackout.Satellites;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import unsw.blackout.DeviceALL.Device;
import unsw.blackout.File.File;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class Satellite {
    private List<Satellite> Satellites = new ArrayList<Satellite>();
    private List<File> files = new ArrayList<>();
    private String satelliteId;
    private String type;
    private double height;
    private Angle position;

    private int velocity;
    private int maxRange;
    private int bandWidthIn;
    private int bandWidthOut;
    private int uploadingNum;
    private int downloadingNum;
    private int availableStorage;
    private int maxMemory;

    public Satellite(String satelliteId, String type, double height, Angle position, int velocity, 
                    int maxRange, int bandWidthIn, int bandWidthOut, int maxMemory) {
                        
        this.satelliteId = satelliteId;
        this.type = type;
        this.height = height;
        this.position = position;

        this.velocity = velocity;
        this.maxRange = maxRange;
        this.bandWidthIn = bandWidthIn;
        this.bandWidthOut = bandWidthOut;
        this.uploadingNum = 0;
        this.downloadingNum = 0;
        this.availableStorage = 0;
        this.maxMemory = maxMemory;
    }
    
    public EntityInfoResponse satellitResponse() {
        Map<String, FileInfoResponse> fileMap = new HashMap<>();
        for (File file : files) {
            fileMap.put(file.getFileName(), getFileResponse(file.getFileName()));
        }
        EntityInfoResponse newResponse = new EntityInfoResponse(this.satelliteId, this.position, 
                                                                this.height, this.type, fileMap);
        return newResponse;
    }

    public FileInfoResponse getFileResponse(String fileName) {
        File file = getFile(fileName, this.files);
        FileInfoResponse newResponse = new FileInfoResponse(fileName, file.getAvailableContent(), 
                                                            file.getContent().length(), file.hasTransferCompleted());
        return newResponse;
    }

    private File getFile(String fileName, List<File> filesList) {
        for (File file : filesList) {
            if (file.getFileName() == fileName);
            return file;
        }
        return null;
    }

    // check if the device in range to satellite
    public boolean deviceInRangetoSatellite(Device device) {
        if (getType() == "StandardSatellite") {
            if (device.getType() == "DesktopDevice") {
                return false;
            }
        }
        if (MathsHelper.isVisible(height, position, device.getPosition())) {
            if (MathsHelper.getDistance(height, position, device.getPosition()) <= maxRange) {
                return true;
            }
        }
        return false;
    }
  
    // check if the satellite in range to satellite
    public boolean SatelliteInRangetoSatellite(Satellite satellite) {
        if (MathsHelper.isVisible(height, position, satellite.getHeight(), satellite.getPosition())) {
          if (MathsHelper.getDistance(height, position, satellite.getHeight(), getPosition()) <= maxRange) {
                return true;
            }
        }
        return false;
    }

    public void satelliteTeleportsToSallite(Satellite from, File file) {
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
    
    public String getType() {
        return type;
    }

    public double getHeight() {
        return height;
    }   

    public Angle getPosition() {
        return position;
    }

    public void setPosition(Angle position) {
        this.position = position;
    }

    public String getSatelliteId() {
        return satelliteId;
    }

    public List<Satellite> getSatellites() {
        return Satellites;
    }

    public List<File> getFiles() {
        return files;
    }

    public int getVelocity() {
        return velocity;
    }

    public int getMaxRange() {
        return maxRange;
    }


    public int getMaxMemory() {
        return maxMemory;
    }

    public int getBandWidthIn() {
        return bandWidthIn;
    }

    public int getBandWidthOut() {
        return bandWidthOut;
    }

    public void nextPostion(){

    };

    public int getUploadingNum() {
        return uploadingNum;
    }

    public void setUploadingNum(int uploadingNum) {
        this.uploadingNum = uploadingNum;
    }

    public int getDownloadingNum() {
        return downloadingNum;
    }

    public void setDownloadingNum(int downloadingNum) {
        this.downloadingNum = downloadingNum;
    }

    public int getAvailableStorage() {
        return availableStorage;
    }

    public void setAvailableStorage(int availableStorage) {
        this.availableStorage = availableStorage;
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

    public boolean canDownload() {
        if (downloadingNum + uploadingNum >= bandWidthIn) {
            return false;
        }
        return true;
    }

    public boolean canUpload() {
        if (downloadingNum + uploadingNum >= bandWidthOut) {
            return false;
        }
        return true;
    }

    public boolean isStorageFull(int fileSize) {
        return false;
    }

    public String storageFull() {
        return "";
    }

    public void addFile(File f) {
        files.add(f);
    }

    public void addSendFile(File file) {
        this.addFile(file);
        this.setAvailableStorage(this.getAvailableStorage() - file.getSize());
        this.setDownloadingNum(this.getDownloadingNum() + 1); 
    }

    public boolean checkTeleport() {
        if (this.getPosition().toDegrees() == 0) {
            return true;
        }
        return false;
    }

    public int downloadSpeed() {
        return bandWidthIn / downloadingNum;
    }

    public int uploadSpeed() {
        return bandWidthOut / uploadingNum;
    }
    
}
