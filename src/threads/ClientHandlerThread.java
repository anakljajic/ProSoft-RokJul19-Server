/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import controller.Controller;
import domain.Gazdinstvo;
import domain.Poljoprivrednik;
import domain.Stado;
import domain.Zivotinja;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import transfer.RequestObject;
import transfer.ResponseObject;
import util.Operation;

/**
 *
 * @author student1
 */
public class ClientHandlerThread extends Thread {

    private Socket socket;

    public ClientHandlerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        RequestObject request = null;
        ResponseObject response = null;
        while (!socket.isClosed()) {
            try {
                request = receiveRequest();

                switch (request.getOperation()) {
                    case Operation.OPERATION_LOGIN:
                        response = operationLogIn(request);
                        break;
                    case Operation.OPERATION_GET_ALL_POLJOPRIVREDNICI:
                        response = operationGetAllPoljoprivrednici(request);
                        break;
                    case Operation.OPERATION_GET_ALL_GAZDINSTVA:
                        response = operationGetAllGazdinstva(request);
                        break;
                    case Operation.OPERATION_GET_ALL_ZIVOTINJE:
                        response = operationGetAllZivotinje(request);
                        break;
                    case Operation.OPERATION_SAVE_GAZDINSTVO:
                        response = operationSaveGazdinstvo(request);
                        break;
                    case Operation.OPERATION_SAVE_STADA:
                        response = operationSaveStada(request);
                        break;
                }
                sendResponse(response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public RequestObject receiveRequest() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        return (RequestObject) in.readObject();
    }

    public void sendResponse(ResponseObject response) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(response);
        out.flush();
    }

    public ResponseObject operationLogIn(RequestObject request) {
        ResponseObject response = null;
        Map<String, String> data = (Map) request.getData();
        String username = data.get("username");
        String password = data.get("password");

        try {
            response = new ResponseObject();
            Poljoprivrednik p = Controller.getInstance().logIn(username, password);
            response.setData(p);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }
        return response;
    }
//

    private ResponseObject operationGetAllPoljoprivrednici(RequestObject request) {
        ResponseObject response = null;

        try {
            response = new ResponseObject();
            List<Poljoprivrednik> p = Controller.getInstance().getAllPoljoprivrednici();
            response.setData(p);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }
        return response;
    }
//
//    private ResponseObject operationSaveProduct(RequestObject request) {
//        ResponseObject response = null;
//        Product product=(Product)request.getData();
//        
//        try {
//            response = new ResponseObject();
//            Controller.getInstance().saveProduct(product);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            response.setException(ex);
//        }
//        return response;
//    }
//
//    private ResponseObject operationGetAllProducts(RequestObject request) {
//        ResponseObject response = null;
//        
//        try {
//            response = new ResponseObject();
//            List<Product> products= Controller.getInstance().getAllProducts();
//            response.setData(products);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            response.setException(ex);
//        }
//        return response;
//    }
//
//    private ResponseObject operationSaveInvoice(RequestObject request) {
//         ResponseObject response = null;
//         Invoice invoice=(Invoice)request.getData();
//        
//        try {
//            response = new ResponseObject();
//            invoice= Controller.getInstance().saveInvoice(invoice);
//            response.setData(invoice);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            response.setException(ex);
//        }
//        return response;
//    }

    public Socket getSocket() {
        return socket;
    }

    private ResponseObject operationGetAllZivotinje(RequestObject request) {
        ResponseObject response = null;

        try {
            response = new ResponseObject();
            List<Zivotinja> z = Controller.getInstance().getAllZivotinje();
            response.setData(z);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }
        return response;
    }

    private ResponseObject operationGetAllGazdinstva(RequestObject request) {
        ResponseObject response = null;

        try {
            response = new ResponseObject();
            List<Gazdinstvo> g = Controller.getInstance().getAllGazdinstva();
            response.setData(g);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }
        return response;
    }

    private ResponseObject operationSaveGazdinstvo(RequestObject request) {
        ResponseObject response = null;
        Gazdinstvo g = (Gazdinstvo) request.getData();

        try {
            response = new ResponseObject();
            Controller.getInstance().sacuvajGazdinstvo(g);
            response.setData(g);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }
        return response;
    }

    private ResponseObject operationSaveStada(RequestObject request) {
        ResponseObject response = null;
        List<Stado> s = (List<Stado>) request.getData();

        try {
            response = new ResponseObject();
            Controller.getInstance().sacuvajStada(s);
            response.setData(s);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        }
        return response;
    }

}
