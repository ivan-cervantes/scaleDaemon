package scaledaemon;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author icervantes
 */
public class ConnectionsVo {
    private final SimpleStringProperty id;
    private final SimpleStringProperty ip;

    public ConnectionsVo(String id, String ip) {
        this.id = new SimpleStringProperty(id);
        this.ip = new SimpleStringProperty(ip);
    }
    
    public String getId() {
        return id.get();
    }
    
    public void setId(String id) {
        this.id.set(id);
    }

    public String getIp() {
        return ip.get();
    }
    
    public void setIp(String ip) {
        this.ip.set(ip);
    }
    
    
}
