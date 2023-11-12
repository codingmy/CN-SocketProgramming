public class config {
    private String ipAddress=null;
    private String portNum=null;

    // constructor
    public config(){}

    //getter
    public String getIpAddress() {
        return ipAddress;
    }

    public String getPortNum() {
        return portNum;
    }

    //setter
    public void setIpAddress(String inputIpAddress) {
        this.ipAddress=inputIpAddress;
    }

    public void setPortNum(String inputPortNum) {
        this.portNum=inputPortNum;
    }
    
}
