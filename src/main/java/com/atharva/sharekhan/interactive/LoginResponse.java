/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atharva.sharekhan.interactive;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Janus
 */
public class LoginResponse {
    public short status_code;
    public String message;
    public String client_info;

    public LoginResponse(byte[] bytes){
        ByteBuffer buffer3 = ByteBuffer.allocate(2);
        buffer3.order(ByteOrder.LITTLE_ENDIAN);
        System.arraycopy(bytes, 6, buffer3.array(), 0, buffer3.array().length);
        status_code = buffer3.getShort();
        byte[] msgByte= new byte[250];
        System.arraycopy(bytes, 8, msgByte, 0, msgByte.length);
        message = new String(msgByte);
        byte[] clientinfo= new byte[75];
        System.arraycopy(bytes, 258, clientinfo, 0, clientinfo.length);
        client_info = new String(clientinfo);
                   
    }
    public short getStatus_code() {
        return status_code;
    }

    public String getMessage() {
        return message;
    }

    public String getClient_info() {
        return client_info;
    }
    
}
