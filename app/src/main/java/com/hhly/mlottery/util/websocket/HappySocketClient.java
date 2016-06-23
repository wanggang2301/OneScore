package com.hhly.mlottery.util.websocket;

import android.util.Log;

import com.hhly.mlottery.util.L;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class HappySocketClient extends WebSocketClient {

	private final static String TAG = "HappySocketClient";
	private final static String SERVER_NAME = "happywin";
	private final static String SERVER_PASSWORD = "happywin";

	private Callback webSocketCallback;

	public HappySocketClient(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	public HappySocketClient(URI serverUri, Draft draft, Callback callback) {
		super(serverUri, draft);
		if (callback == null) throw new NullPointerException("HappySocketClient.Callback must be not null");
		this.webSocketCallback = callback;

	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
		L.i(TAG, "websocket close..");

		if(webSocketCallback != null) webSocketCallback.onClose(arg1);
		if(socketResponseCloseListener!=null){
			socketResponseCloseListener.onClose(arg1);
		}
	}

	@Override
	public void onError(Exception exception) {
		L.i(TAG, "___onError____");
		if(webSocketCallback != null) webSocketCallback.onError(exception);
		if (socketResponseErrorListener != null) {
			socketResponseErrorListener.onError(exception);
		}
	}

	@Override
	public void onMessage(String message) {
		L.i(TAG, "___onMessage___");
			if(webSocketCallback != null) webSocketCallback.onMessage(message);
		if (socketResponseMessageListener != null) {
			socketResponseMessageListener.onMessage(message);
		} else {
			Log.e(TAG, "socketResponseMessageListener is null and RollballFragment callback is [[" + webSocketCallback + "]]");
		}

	}

	@Override
	public void onOpen(ServerHandshake arg0) {
		L.i(TAG, "___onOpen___");

		StringBuilder builder = new StringBuilder();
		builder.append("CONNECT\nlogin:").append(SERVER_NAME).append("\npasscode:").append(SERVER_PASSWORD).append("\naccept-version:1.1,1.0\nheart-beat:5000,5000\n").append("\n");
		this.send(builder.toString());
	}

	private SocketResponseMessageListener socketResponseMessageListener;

	public void setSocketResponseMessageListener(SocketResponseMessageListener socketResponseMessageListener) {
		this.socketResponseMessageListener = socketResponseMessageListener;
	}

	public interface SocketResponseMessageListener {
		void onMessage(String message);
	}



	private SocketResponseErrorListener socketResponseErrorListener;

	public void setSocketResponseErrorListener(SocketResponseErrorListener socketResponseErrorListener) {
		this.socketResponseErrorListener = socketResponseErrorListener;
	}

	public interface SocketResponseErrorListener {
		void onError(Exception exception);
	}

	public interface SocketResponseCloseListener {
		void onClose(String message);
	}
	
	private SocketResponseCloseListener socketResponseCloseListener;

	public void setSocketResponseCloseListener(SocketResponseCloseListener socketResponseCloseListener) {
		this.socketResponseCloseListener = socketResponseCloseListener;
	}
	
	public interface Callback {
		void onMessage(String message);
		void onError(Exception exception);
		void onClose(String message);
	}
}
