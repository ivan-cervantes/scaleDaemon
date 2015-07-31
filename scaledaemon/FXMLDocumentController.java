/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scaledaemon;

import gnu.io.CommPortIdentifier;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 *
 * @author icervantes
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML private ComboBox comboPort = new ComboBox();
    @FXML private Button buttonDetectPort = new Button();
    @FXML private Button buttonInitiateDaemon = new Button();
    @FXML private ImageView imageLobo;
    @FXML private TableView<ConnectionsVo> tableConnections;
    @FXML private TableColumn<ConnectionsVo, String> id;
    @FXML private TableColumn<ConnectionsVo, String> ip;
    ObservableList<ConnectionsVo> data = FXCollections.observableArrayList();   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        File file = new File("src/resources/img/loboLogo.png");
        Image image = new Image(file.toURI().toString());
        imageLobo.setImage(image);
        this.fillComboPort();
        
        //id.setCellValueFactory(new PropertyValueFactory<>("id"));
        //ip.setCellValueFactory(new PropertyValueFactory<>("ip"));
        
        //tableConnections.setItems(data);
        //data.add(new ConnectionsVo("VENFI-PVB","LOCALHOST"));
        //data.add(new ConnectionsVo("VENFI-PVB2","LOCALHOST"));
        
    }
    
    @FXML
    public void fillComboPort() {
        Enumeration portList = null;
        CommPortIdentifier portId = null;
        Boolean elements = false; 
        portList = CommPortIdentifier.getPortIdentifiers();
        
        while(portList.hasMoreElements()){
            elements = true;
            buttonInitiateDaemon.setDisable(false);
            comboPort.getItems().clear();
            portId = (CommPortIdentifier)portList.nextElement();
            comboPort.getItems().add(portId.getName());
        }
        
        if(!elements) {
            comboPort.getItems().clear();
            comboPort.getItems().add("NO SE DETECTARON PUERTOS");
            comboPort.setValue("NO SE DETECTARON PUERTOS");
            buttonInitiateDaemon.setDisable(true);
        }
    }
    
    @FXML
    public void initiateDaemon() {
        Alert alert;
        CommPortIdentifier portId = null;
        int baudRate = 9600;
        ExecutorService executor = null;
        ServerSocket clientSocket;  
        try {
            clientSocket = new ServerSocket(6500);
                        
            if(comboPort.getValue().toString().equals("") || comboPort.getValue().toString().equals("NO SE DETECTARON PUERTOS")) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Lobo Software");
                alert.setHeaderText(null);
                alert.setContentText("Seleccione un puerto de lectura para la báscula");
                alert.showAndWait();
            } else {
                portId = CommPortIdentifier.getPortIdentifier(comboPort.getValue().toString());
//                Executors.newFixedThreadPool(5);
                new SerialProgram(portId, baudRate);
                
                
                
                while(true) {
//                    data.add(new ConnectionsVo("VENFI-PVB3","LOCALHOST"));
                    Socket connectionSocket = clientSocket.accept();   
                    PrintWriter out = new PrintWriter(connectionSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    //System.out.println(connectionSocket.toString());
                                      
                    while(connectionSocket.isConnected()){
                        out.println(WeightVo.weight);
                        
                    }
                    
                }
            }
        
        } catch (Exception ex) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lobo Software");
            alert.setHeaderText("Se ha presentado un error inesperado");
            alert.setContentText("El error técnico se muestra a continuación:");


            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);
            alert.showAndWait();
        } finally {
            //executor.shutdown();
        }
         
    }
    
}
