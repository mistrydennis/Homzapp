package com.example.mbg;

/**
 * Base activity that can be used by device acting as client
 */
public abstract class BaseRemoteActivity extends BaseConnectActivity {

	@Override
	public final void onNewMessage(byte[] bytes) {
		// client is only interested in success or failure of command
		boolean success = false;
		if(bytes != null) {
			success = Boolean.valueOf(bytes.toString());
		}
		onDeviceResponse(success);
	}
	
	/**
	 * Callback that will be invoked after command is executed on remote device
	 * @param success TRUE is command is supported by server FALSE otherwise
	 */
	public abstract void onDeviceResponse(boolean success);

}
