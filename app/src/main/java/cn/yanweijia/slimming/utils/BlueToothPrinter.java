package cn.yanweijia.slimming.utils;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import net.xprinter.service.XprinterService;
import net.xprinter.utils.DataForSendToPrinterTSC;
import net.xprinter.xpinterface.IMyBinder;
import net.xprinter.xpinterface.UiExecute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.yanweijia.slimming.R;

/**
 * Created by weijia on 2016/11/9.
 * function 连接蓝牙打印机并进行打印
 */

public class BlueToothPrinter {
    private static BluetoothAdapter bluetoothAdapter;
    private static BluetoothDevice btDevice_result = null;  //选中的蓝牙设备
    private static Boolean isSelected = false;  //蓝牙设备是否选过
    private static IMyBinder binder;//IMyBinder接口，所有可供调用的连接和发送数据的方法都封装在这个接口内
    private static boolean isConnect;//用来标识连接状态的一个boolean值
    private static ServiceConnection conn;  //运行时需要绑定Service

    /**
     * 打印到蓝牙打印机
     *
     * @param context 上下文
     * @param text    欲打印内容
     * @return
     */
    public static boolean print(Context context, String text) {
        //打开蓝牙开关,,蓝牙打开失败,则返回失败并退出
        if (!BlueToothPrinter.turnOnBlueTooth(context))
            return false;

        //在弹出对话框Alert.Dialog中显示已配对设备信息(ListView) ,并供用户选择一蓝牙打印机设备
        chooseADeviceAndPrint(context, text);


        return true;
    }

    /**
     * 打印到蓝牙打印机,这个是调试的时候针对已知蓝牙设备地址
     *
     * @param context          上下文
     * @param blueToothAddress 蓝牙设备地址
     * @param text             欲打印文本
     * @return
     */
    public static boolean print(Context context, String blueToothAddress, String text) {
        //打开蓝牙开关,,蓝牙打开失败,则返回失败并退出
        if (!BlueToothPrinter.turnOnBlueTooth(context))
            return false;

        //连接蓝牙,并进行打印
        if (!printToDevice(context, blueToothAddress, text))
            return false;
        return true;
    }

    /**
     * 将文本信息打印到蓝牙Device中
     *
     * @param context          上下文
     * @param blueToothAddress 蓝牙设备地址
     * @param text             与打印内容
     * @return
     */
    private static boolean printToDevice(final Context context, final String blueToothAddress, final String text) {

        //bindService的参数conn
        conn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //绑定成功
                binder = (IMyBinder) service;


                //这个接口的实现在连接过程结束后执行（执行于UI线程），onsucess里执行连接成功的代码，onfailed反之；
                binder.connectBtPort(blueToothAddress, new UiExecute() {

                    @Override
                    public void onsucess() {
                        //连接成功后在UI线程中的执行
                        isConnect = true;
                        Toast.makeText(context, "连接成功", Toast.LENGTH_LONG).show();
                        //此处也可以开启读取打印机的数据
                        //参数同样是一个实现的UiExecute接口对象
                        //如果读的过程重出现异常，可以判断连接也发生异常，已经断开
                        //这个读取的方法中，会一直在一条子线程中执行读取打印机发生的数据，
                        //直到连接断开或异常才结束，并执行onfailed
                        binder.acceptdatafromprinter(new UiExecute() {
                            @Override
                            public void onsucess() {
                            }

                            @Override
                            public void onfailed() {
                                isConnect = false;
                                Toast.makeText(context, "连接已断开", Toast.LENGTH_LONG).show();
                            }
                        });
                        //连接打印机打印
                        byte[] data0 = DataForSendToPrinterTSC.cls();
                        byte[] data1 = DataForSendToPrinterTSC.text(10, 10, "0", 0, 2, 2, text + "\n\n\n\n");
                        byte[] data2 = DataForSendToPrinterTSC.feed(4);
                        byte[] data = byteMerger(byteMerger(data0, data1), data2);
                        if (isConnect) {
                            binder.write(data, new UiExecute() {

                                @Override
                                public void onsucess() {
                                    Toast.makeText(context, "发送成功", Toast.LENGTH_LONG)
                                            .show();
                                }

                                @Override
                                public void onfailed() {
                                    Toast.makeText(context, "发送失败", Toast.LENGTH_LONG)
                                            .show();
                                }
                            });
                        }
                        //断开打印机的链接
                        if (isConnect) {//如果是连接状态才执行断开操作
                            binder.disconnectCurrentPort(new UiExecute() {
                                @Override
                                public void onsucess() {
                                    Toast.makeText(context, "断开连接成功", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onfailed() {
                                    Toast.makeText(context, "断开连接失败", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        context.unbindService(conn);
                    }

                    @Override
                    public void onfailed() {
                        //连接失败后在UI线程中的执行
                        isConnect = false;
                        Toast.makeText(context, "连接失败", Toast.LENGTH_LONG).show();
                    }
                });


            }
        };
        //绑定service，获取ImyBinder对象
        Intent intent = new Intent(context, XprinterService.class);
        context.bindService(intent, conn, context.BIND_AUTO_CREATE);


        //下面这个要在Activity结束的时候运行::::已经在断开打印机的代码中执行.
        //context.unbindService(conn);

        return true;
    }


    /**
     * 在弹出对话框Alert.Dialog中显示已配对设备信息(ListView) ,并供用户选择一蓝牙打印机设备
     *
     * @param context Activity的Context
     * @return 选中的Device
     */
    private static void chooseADeviceAndPrint(final Context context, final String text) {
        LinearLayout linearLayoutDialog = new LinearLayout(context);
        linearLayoutDialog.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        ListView listView = new ListView(context);
        listView.setFadingEdgeLength(0);
        List<Map<String, String>> deviceList = new ArrayList<Map<String, String>>();  //建立一个数组存储listview上显示的设备信息


        final Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        //获取所有已配对设备列表
        for (BluetoothDevice bt : pairedDevices) {
            Map<String, String> deviceMap = new HashMap<String, String>();
            deviceMap.put("name", bt.getName());
            deviceMap.put("macAddress", bt.getAddress());
            deviceList.add(deviceMap);
        }
        Toast.makeText(context.getApplicationContext(), "显示已配对设备", Toast.LENGTH_SHORT).show();
        SimpleAdapter adapter = new SimpleAdapter(context, deviceList, android.R.layout.simple_list_item_2, new String[]{"name", "macAddress"}, new int[]{android.R.id.text2, android.R.id.text1});
        listView.setAdapter(adapter);


        linearLayoutDialog.addView(listView);   //在布局中加入ListView

        //对话框
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.chooseADevice)
                .setView(linearLayoutDialog)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setCancelable(false).create();
        dialog.setCanceledOnTouchOutside(false);    //除了dialog以外的地方不能被点击
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Iterator<BluetoothDevice> iterator = pairedDevices.iterator();
                BluetoothDevice btDevice = null;
                for (int i = 0; i <= position && iterator.hasNext(); i++) {
                    btDevice = iterator.next();
                }
                btDevice_result = btDevice;
                isSelected = true;
                //连接蓝牙,并进行打印
                printToDevice(context, btDevice_result.getAddress(), text);
            }
        });
    }


    /**
     * 打开蓝牙开关
     *
     * @param context Activity的Context
     * @return 是否打开成功
     */
    private static boolean turnOnBlueTooth(Context context) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //判断蓝牙开关状态并打开蓝牙开关
        if (!bluetoothAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(turnOn);
            Toast.makeText(context.getApplicationContext(), "蓝牙成功打开", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context.getApplicationContext(), "蓝牙已是打开状态", Toast.LENGTH_LONG).show();
        }
        //再次获取打开状态,打开失败则返回
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled())
            return false;
        else
            return true;
    }

    /**
     * byte数组拼接
     */
    private static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
}
