# AndroidSerialport
Android 串口通讯,基于android_serialport_api google开源的,简化代码
和单片机串口通讯,总共有2个任务,一个是接受单片机发送指令的线程(协议解析),一个是定时发送指令的线程(心跳包)
因为协议都不同所以要根据协议修改协议的解析
