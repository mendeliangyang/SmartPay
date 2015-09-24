package com.smart.mockbankfx;

import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController implements Initializable {

    @FXML
    private TextArea label;
    @FXML
    private Button button;

    private StringBuffer labelMessage = new StringBuffer();

    boolean isStart = false;

    @FXML
    private void handleButtonAction(ActionEvent event) {
//        System.out.println("You clicked me!");
//        label.setText("Hello World!");
        if (isStart) {
            labelMessage.delete(0, labelMessage.length());
            label.setText(labelMessage.toString());
            return;
        }
        try {
            System.out.println("start server......");
            SetMessage("start server......");
            ClientProcessPool = Executors.newCachedThreadPool();

            ServerSinglePool = Executors.newSingleThreadExecutor();
            server = new ServerSocket(serverPort);

            ServerSocketAccept ssa = new ServerSocketAccept(server, this);
            ServerSinglePool.execute(ssa);

            SetMessage("start server success.");

            isStart = true;
            button.setText("clear message");

        } catch (Exception e) {
            System.out.println("Error:" + e);
            labelMessage.append("Error:").append(e).append("\n");
            SetMessage(labelMessage.toString());
        }
    }

    public void SetMessage(String strMsg) {
        labelMessage.append(strMsg).append("\r\n");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //javaFX operations should go here
                label.setText(labelMessage.toString());
            }
        });
    }

    static ServerSocket server = null;
    static int serverPort = 4000;
    static ExecutorService ServerSinglePool;
    static ExecutorService ClientProcessPool;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }
}
