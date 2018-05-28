
package android_serialport_api;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 该类的包名不能随便改动
 */
public class SerialPort {

	private static final String TAG = "SerialPort";
	private FileDescriptor mFd;
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;

	public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {

		//检查权限
		if (!device.canRead() || !device.canWrite()) {
			Log.d(TAG, "SerialPort: 没有操作串口的权限");
			try {
				//权限丢失从新获取
				Process su;
				su = Runtime.getRuntime().exec("/system/bin/su");
				String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
						+ "exit\n";
				su.getOutputStream().write(cmd.getBytes());
				if ((su.waitFor() != 0) || !device.canRead()
						|| !device.canWrite()) {
					throw new SecurityException();
				}
			} catch (Exception e) {
				Log.d(TAG, "SerialPort: 获取权限是出错");
				e.printStackTrace();
				throw new SecurityException();
			}
		}
		Log.d(TAG, "SerialPort: 有操作串口的权限");
		mFd = open(device.getAbsolutePath(), baudrate, flags);
		if (mFd == null) {
			Log.d(TAG, "native打开方法返回null");
			throw new IOException();
		}
		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);
	}

	public InputStream getInputStream() {
		return mFileInputStream;
	}

	public OutputStream getOutputStream() {
		return mFileOutputStream;
	}


	private native static FileDescriptor open(String path, int baudrate, int flags);
	public native void close();
	static {
		System.loadLibrary("serial_port");
	}
}
