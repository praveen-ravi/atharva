/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atharva.sharekhan.core;

/**
 *
 * @author Janus
 */
public class MessageHeader {
    int message_length;
    short transaction_code;

    public int getMessage_length() {
        return message_length;
    }

    public short getTransaction_code() {
        return transaction_code;
    }
    public MessageHeader(byte[] bytes){
        message_length = CConstants.getInt32(bytes, 0);
        transaction_code = CConstants.getInt16(bytes, 4);
    }

    public MessageHeader(int message_length, short transaction_code) {
        this.message_length = message_length;
        this.transaction_code = transaction_code;
    }
    
    public byte[] getByteArray(){
        byte[] headerBytes = new byte[6];
        CConstants.setBytes(message_length, headerBytes, 0);
        CConstants.setBytes(transaction_code, headerBytes, 4);
        return headerBytes;
    }
}
