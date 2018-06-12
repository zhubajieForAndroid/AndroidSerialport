# AndroidSerialport 
Android 串口通讯,基于android_serialport_api google开源的,简化代码
项目中只使用到SerialPort类和3个so文件
public static SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            //串口文件和波特率
            mSerialPort = new SerialPort(new File("/dev/ttyS2"), 19200, 0);
        }
        return mSerialPort;
}
直接指定串口名和波特率,可以节省很多代码

和单片机串口通讯,总共有2个任务,一个是接受单片机发送指令的线程(协议解析),一个是定时发送指令的线程(心跳包),
在打开串口的时候2个任务就开启了
因为协议都不同所以要根据协议修改协议的解析

# 在接受到完成的一包协议后是通过广播的方式发送出去的
                    // 一个完整包即产生
                    Intent intent = new Intent();
                    intent.setAction(Constants.DATA);
                    intent.putExtra("response", resole);
                    intent.putExtra("size", resole.length);
                    SerialportApplication.getContext().sendBroadcast(intent);
# 注册广播接受者
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final byte[] buffer = intent.getByteArrayExtra("response");
                final int size = intent.getIntExtra("size", 0);
                onDataReceived(buffer, size);
            }
        };
        IntentFilter filter = new IntentFilter(Constants.DATA);
        registerReceiver(mBroadcastReceiver, filter);

