package unsw.blackout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import unsw.blackout.DeviceALL.*;
import unsw.blackout.DeviceALL.HandheldDevice;
import unsw.blackout.File.File;
import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;
import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.blackout.Satellites.*;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class BlackoutController {

    private List<Device> devices = new ArrayList<Device>();
    private List<Satellite> satellites = new ArrayList<Satellite>();

    public final void createDevice(String deviceId, String type, Angle position) {
        if (type == "HandheldDevice") {
            HandheldDevice newDevice = new HandheldDevice(deviceId, type ,position);
            devices.add(newDevice);
        } else if (type == "LaptopDevice") {
            LaptopDevice newDevice = new LaptopDevice(deviceId, type, position);
            devices.add(newDevice);
        } else if (type == "DesktopDevice") {
            DesktopDevice newDevice = new DesktopDevice(deviceId, type, position);
            devices.add(newDevice);
        }
    }

    public final void removeDevice(String deviceId) {
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getDeviceId() == deviceId) {
                devices.remove(i);
            }
        }
    }

    public final void createSatellite(String satelliteId, String type, double height, Angle position) {
        Satellite satellite;
        if (type == "StandardSatellite") {
            satellite = new StandardSatellite(satelliteId, type, height, position);
            satellites.add(satellite);
        } else if (type == "TeleportingSatellite") {
            satellite = new TeleportingSatellite(satelliteId, type, height, position);
            satellites.add(satellite);
        } else if (type == "RelaySatellite") {
            satellite = new RelaySatellite(satelliteId, type, height, position);
            satellites.add(satellite);
        }
    }

    public final void removeSatellite(String satelliteId) {
        for (int i = 0; i < satellites.size(); i++) {
            if (satellites.get(i).getSatelliteId() == satelliteId) {
                satellites.remove(i);
            }
        }
    }

    public final List<String> listDeviceIds() {
        List<String> deviceIds = new ArrayList<String>();
        for (int i = 0; i < devices.size(); i++) {
            if (deviceIds.add(devices.get(i).getDeviceId())) {
            }
        }
        return deviceIds;
    }

    public final List<String> listSatelliteIds() {
        List<String> satelliteIds = new ArrayList<String>();
        for (int i = 0; i < satellites.size(); i++) {
            if (satelliteIds.add(satellites.get(i).getSatelliteId())) {
            }
        }
        return satelliteIds;
    }

    public final void addFileToDevice(String deviceId, String fileName, String content) {
        File file = new File(fileName, content);
        file.setAvailableSize(content.length());
        findDevice(deviceId).addFile(file);
    }

    public final Device findDevice(String deviceId) {
        for (Device device : devices){
            if (device.getDeviceId() == deviceId) {
                return device;
            }
        }
        return null;
    }

    public final Satellite findSatellite(String satelliteId) {
        for (Satellite satellite : satellites) {
            if (satellite.getSatelliteId() == satelliteId) {
                return satellite;
            }
        }
        return null;
    }

    public final EntityInfoResponse getInfo(String id) {
        EntityInfoResponse response = null;
        Device device = findDevice(id);
        Satellite satellite = findSatellite(id);

        if (device != null) {
            response = device.deviceResponse();
        } else if (satellite != null) {
            response = satellite.satellitResponse();
        }
        return response;
    }


//////////////////////////////////// Task 2 ////////////////////////////////
    public void simulate() {
        for (Satellite salSatellite : satellites) {
            salSatellite.nextPostion();
        }

        for (Device device : devices) {
            deviceFileTransfer(device);
        }

        for (Satellite satellite : satellites) {
            SatelliteFiletransfer(satellite);
        }

    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    public final List<String> communicableEntitiesInRange(String id) {
        ArrayList<String> inRange = new ArrayList<String>();
        Device device = findDevice(id);
        Satellite satellite = findSatellite(id);

        if (device != null) {
            inRange = (deviceAddInRangeSatelliate(device));

            if (hasRelay(inRange)) {
                ArrayList<String> allRelay = getAllRely(inRange);
                inRange.addAll(allRelay);
                inRange.addAll(addAllOtherSatellite(allRelay));
            }
            inRange.remove(device.getDeviceId());
            
        } else if (satellite != null) {
            inRange = satelliteAddInRangeEntities(satellite);
            inRange.add(satellite.getSatelliteId());

            if (hasRelay(inRange)) {
                ArrayList<String> allRelay = getAllRely(inRange);
                inRange.addAll(allRelay);
                inRange.addAll(addAllOtherSatellite(allRelay));
            }
            inRange.addAll(addAllDevice(inRange, satellite));
            inRange.remove(satellite.getSatelliteId());
        }
        HashSet<String> removeDuplict = new HashSet<String>(inRange);
        ArrayList<String> result = new ArrayList<String>(removeDuplict);
        result.remove(id);
        return result;
    }

    public ArrayList<String> addAllDevice(ArrayList<String> allSatellites, Satellite targetSallite) {
        ArrayList<String> allDevices = new ArrayList<>();
        for (String s : allSatellites) {
            Satellite satellite = findSatellite(s);
            
            if (s == targetSallite.getSatelliteId() || satellite.getType() == "RelaySatellite") {
                
                for (Device d : devices) {
                    if (!allDevices.contains(d.getDeviceId()) && satellite.deviceInRangetoSatellite(d)) {
                        allDevices.add(d.getDeviceId());
                    }
                }
            }
            
        }
        return allDevices;
    }

    // add other type of satellite(not include Relay satellite)
    public ArrayList<String> addAllOtherSatellite(ArrayList<String> relays) {
        ArrayList<String> EntitiesTorRely = new ArrayList<>();
        for (String r : relays) {
            Satellite relay = findSatellite(r);
            for (Satellite s : satellites) {
                if (!EntitiesTorRely.contains(s.getSatelliteId()) && relay.SatelliteInRangetoSatellite(s)) {
                    EntitiesTorRely.add(s.getSatelliteId());
                }
            }
        }
        return EntitiesTorRely;
    }

    public boolean hasRelay(ArrayList<String> InRangeSatellites) {
        for (String satellite : InRangeSatellites) {
            Satellite s = findSatellite(satellite);
            if (s.getType() == "RelaySatellite") {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getAllRely(ArrayList<String> InRangeSatellites) {
        ArrayList<String> deviceTorRely = new ArrayList<>();
        for (String satellite : InRangeSatellites) {
            Satellite s = findSatellite(satellite);
            if (s.getType() == "RelaySatellite") {
                deviceTorRely.add(s.getSatelliteId());
            }
        }
        return findAllRely(deviceTorRely);
    }

    public ArrayList<String> findAllRely(ArrayList<String> relays) {
        ArrayList<String> allRelay = new ArrayList<>();
        for (int i = 0; i < relays.size(); i ++) {
            boolean newAdd = false;
            Satellite relay = findSatellite(relays.get(i));
            for (Satellite s : satellites) {
                if (!relays.contains(s.getSatelliteId()) && relay.SatelliteInRangetoSatellite(s) 
                    && s.getType() == "RelaySatellite" && !allRelay.contains(s.getSatelliteId())) {
                    allRelay.add(s.getSatelliteId());
                    newAdd = true;
                }
            }
            if (newAdd == true) {
                i = 0;
            }
        }
        allRelay.addAll(relays);
        return allRelay;
    }

    // add all in range satellite to devcie
    public ArrayList<String> deviceAddInRangeSatelliate(Device device) {
        ArrayList<String> inRange = new ArrayList<>();
        for (Satellite satellite : satellites) {
            if (device.SatelliteinRangeToDevice(satellite)) {
                inRange.add(satellite.getSatelliteId());
            }
        }
        return inRange;
    }

    // add all in range Entities to satellite
    public ArrayList<String> satelliteAddInRangeEntities(Satellite satellite) {
        ArrayList<String> inRange = new ArrayList<>();
        for (Satellite s : satellites) {
            if (satellite.SatelliteInRangetoSatellite(s)) {
                inRange.add(s.getSatelliteId());
            }
        }
        inRange.remove(satellite.getSatelliteId());
        return inRange;
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        Device fromDevice = findDevice(fromId);
        Device toDevice = findDevice(toId);
        Satellite fromSatellite = findSatellite(fromId);
        Satellite toSatellite = findSatellite(toId);

        if (fromDevice != null) {
            // satellite is out range to the device
            if (!fromDevice.SatelliteinRangeToDevice(toSatellite)) {
                return;
            }
            if (fromDevice.checkFileExist(fileName) == false || 
                fromDevice.getFile(fileName).hasTransferCompleted() == false) {

                throw new VirtualFileNotFoundException(fileName);
            }
            // If the file already exist on the "to" satellite
            if (toSatellite.checkFileExist(fileName) == true) {
                throw new VirtualFileAlreadyExistsException(fileName);
            }
            // If the "to" setallite cannot be downloaded
            if (!toSatellite.canDownload()) {
                throw new VirtualFileNoBandwidthException(toSatellite.getSatelliteId());
            }
            // aim setallite is full
            if (toSatellite.isStorageFull(fromDevice.getFileContent(fileName).length())) {
                throw new VirtualFileNoStorageSpaceException(toSatellite.storageFull());
            }

            File target = new File(fileName, fromDevice.getFileContent(fileName));
            target.setFrom(fromId);
            target.setTo(toId);
            toSatellite.addSendFile(target);

        // file is from a satellite
        } else if (fromSatellite != null) {

            // If the file does not exist on the "from" satellite or it is a partial file on the source satellite
            if (fromSatellite.checkFileExist(fileName) == false 
                || fromSatellite.getFile(fileName).hasTransferCompleted() == false) {
                throw new VirtualFileNotFoundException(fileName);
            }

            // If the source setallite cannot upload more file
            if (!fromSatellite.canUpload()) {
                throw new VirtualFileNoBandwidthException(fromSatellite.getSatelliteId());
            }

            // If the file is sent to a satellite
            if (toSatellite != null) {

                // check "to" satellite in range
                if (!fromSatellite.SatelliteInRangetoSatellite(toSatellite)) {
                    return;
                }

                // If "to" satellite already has this file
                if (toSatellite.checkFileExist(fileName) != false) {
                    throw new VirtualFileAlreadyExistsException(fileName);
                }
                
                // If the "to" satellite cannot download
                if (!toSatellite.canDownload()) {
                    throw new VirtualFileNoBandwidthException(toSatellite.getSatelliteId());
                }

                // If the "to" setallite is full
                if (toSatellite.isStorageFull(fromSatellite.getFile(fileName).getContent().length())) {
                    throw new VirtualFileNoStorageSpaceException(toSatellite.storageFull());
                }

                File target = new File(fileName, fromSatellite.getFile(fileName).getContent());
                target.setFrom(fromId);
                target.setTo(toId);
                toSatellite.addSendFile(target);
            // If the file is sent to a device
            } else if (toDevice != null) {

                // Check Is the destination device in range
                if (!fromSatellite.deviceInRangetoSatellite(toDevice)) {
                    return;
                }
                
                // If the file already exist on the "to" device
                if (toDevice.checkFileExist(fileName) != false) {
                    throw new VirtualFileAlreadyExistsException(fileName);
                }

                File target = new File(fileName, fromSatellite.getFile(fileName).getContent());
                target.setFrom(fromId);
                target.setTo(toId);
                toDevice.addFile(target);
            }
            
            fromSatellite.setUploadingNum(fromSatellite.getUploadingNum() + 1);
        }
    
    }

    public void deviceFileTransfer(Device device) {
        for (File file : device.getFiles()) {
            if (!file.hasTransferCompleted()) {
                Satellite satellite = findSatellite(file.getFrom());
                // if the transfer is out range
                if (!device.SatelliteinRangeToDevice(satellite)) {
                    break;
                }
                file.setAvailableSize(file.getAvailableSize() + satellite.uploadSpeed());
                // check if the file has transfer completed
                if (file.hasTransferCompleted()) {
                    satellite.setUploadingNum(satellite.getUploadingNum() - 1);
                }
            }
        }
    }


    public void SatelliteFiletransfer(Satellite satellite) {
        for  (File file : satellite.getFiles()) {
            if (!file.hasTransferCompleted()) {
                Device toDevice = findDevice(file.getFrom());
                Satellite toSatellite = findSatellite(file.getFrom());
                if (toDevice != null) {
                    if (!toDevice.SatelliteinRangeToDevice(satellite)) {
                        toDevice.satelliteTeleportsToDevice(satellite, file);
                        break;
                    }
                    file.setAvailableSize(file.getAvailableSize() + satellite.downloadSpeed());
                    if (file.hasTransferCompleted()) {
                        satellite.setDownloadingNum(satellite.getDownloadingNum() - 1);
                    }
                }
                if (toSatellite != null) {
                    if (!satellite.SatelliteInRangetoSatellite(satellite)) {
                        toSatellite.satelliteTeleportsToSallite(satellite, file);
                        break;
                    }
                    int limitation;
                    if (satellite.downloadSpeed() > toSatellite.uploadSpeed()) {
                        limitation = toSatellite.uploadSpeed();
                    } else {
                        limitation = satellite.downloadSpeed();
                    }
                    file.setAvailableSize(file.getAvailableSize() + limitation);
                    if (file.hasTransferCompleted()) {
                        satellite.setDownloadingNum(satellite.getDownloadingNum() - 1);
                        toSatellite.setUploadingNum(toSatellite.getUploadingNum() - 1);
                    }
                }
            }
        }
    }   

    public final void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        createDevice(deviceId, type, position);
        // TODO: Task 3
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // TODO: Task 3
        // If you are not completing Task 3 you can leave this method blank :)
    }

}
