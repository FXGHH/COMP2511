package unsw.archaic_fs.exceptions;

import java.io.FileNotFoundException;

public class UNSWFileNotFoundException extends FileNotFoundException{
    public UNSWFileNotFoundException(String file) {
        super(file);
    }
}