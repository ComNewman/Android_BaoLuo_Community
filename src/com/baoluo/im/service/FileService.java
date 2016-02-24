package com.baoluo.im.service;

import java.io.File;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import com.baoluo.im.Configs;
import com.baoluo.im.XmppConnection;

public class FileService {
	
	private static FileService fileService;
	
	public FileService getInstance(){
		if(fileService == null){
			fileService = new FileService();
		}
		return fileService;
	}
	
	/**
	 * 发送文件
	 * @param friJid  接收人 Jid
	 * @return
	 */
	public boolean sendFile(String friJid,String filePath){
		FileTransferManager transfer = new FileTransferManager(XmppConnection.getConnection());  
        OutgoingFileTransfer out = transfer.createOutgoingFileTransfer(friJid+Configs.SERVER_SUFFIX);
        File file = new File(filePath);
        try {
			out.sendFile(file, file.getName());
			return true;
		} catch (SmackException e) {
			e.printStackTrace();
		}  
		return false;
	}

}
