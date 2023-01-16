package unsw.archaic_fs.exceptions;

import java.nio.file.FileAlreadyExistsException;

public class UNSWFileAlreadyExistsException extends FileAlreadyExistsException{

    public UNSWFileAlreadyExistsException(String file) {
        super(file);
    }
    
}