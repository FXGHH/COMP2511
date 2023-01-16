package unsw.blackout.File;

public class File {
    private String fileName;
    private String content;
    private int size;
    private String availableContent;
    private int availableSize;
    private String from;
    private String to;

    public File(String filename, String content) {
        this.fileName = filename;
        this.content = content;
        this.size = content.length();
        this.from = "";
        this.to = "";
        this.availableContent = "";
        this.availableSize = 0;
    }

    public int getSize() {
        return size;
    }

    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getAvailableContent() {
        return availableContent;
    }

    public void setAvailableContent(String availableContent) {
        this.availableContent = availableContent;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setAvailableContent() {
        availableContent = content.substring(0, availableSize);
    }

    public int getAvailableSize() {
        return availableContent.length();
    }

    public void setAvailableSize(int availableSize) {
        if (availableSize > content.length()) {
            availableSize = content.length();
        }
        this.availableSize = availableSize;
        setAvailableContent();
    }

    public boolean hasTransferCompleted() {
        return content.length() == availableSize;
    }
    
    public String getLeftContent() {
        return this.getContent().substring(this.getAvailableSize());
    }
    
}
