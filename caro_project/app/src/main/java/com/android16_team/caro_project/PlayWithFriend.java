package com.android16_team.caro_project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PlayWithFriend extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothService mChatService;
    private String mConnectedDeviceName;

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private DrawView drawView;
    private ImageButton btnMessage;
    private TextView txtCountDownTimer, txtWaitingMsg;

    private float cx;
    private float cy;

    private boolean toggleMode = true;
    private Integer noWaitingMsg = 0;

    private ProgressDialog progressDialog;

    protected boolean isRunning = false;
    protected byte isContinue = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caro_table);
        final Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

//         If the adapter is null, then Bluetooth is not supported


        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            this.finish();
        }

        drawView = findViewById(R.id.drawView);
        drawView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                cx = event.getX();
                cy = event.getY();
                return false;
            }
        });

        drawView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = drawView.check(cx, cy);
                if (msg != null) {
                    sendMessage(MessageCaro.POSITION, msg);
                }
            }
        });

        btnMessage = findViewById(R.id.btnMessage);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noWaitingMsg = 0;
                txtWaitingMsg.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(PlayWithFriend.this, ChatActivity.class);
                startActivityForResult(intent, 123);
            }
        });


        txtCountDownTimer = findViewById(R.id.countTimer);
        CountDownTimer timer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtCountDownTimer.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                progressDialog = new ProgressDialog(PlayWithFriend.this);
                progressDialog.setMessage("Bạn có muốn chơi tiếp không?");
                progressDialog.setTitle("Progress Dialog");
                progressDialog.setIcon(R.drawable.icon_caro);
                isRunning = true;
                progressDialog.setButton(ProgressDialog.BUTTON_POSITIVE, "Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isRunning = false;
                        progressDialog.dismiss();
                        sendMessage(MessageCaro.CONTINUE, "yes");
                    }
                });

                progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isRunning = false;
                        progressDialog.dismiss();
                        sendMessage(MessageCaro.CONTINUE, "no");
                    }
                });
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int max = 10;
                        int cur = 0;
                        while (cur < max && isRunning) {
                            try {
                                Thread.sleep(1000);
                                cur++;
                                if (cur == max) {
                                    isRunning = false;
                                    progressDialog.dismiss();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        }.start();

        txtWaitingMsg = findViewById(R.id.txtWaitingMsg);
        txtWaitingMsg.setVisibility(View.INVISIBLE);
    }

    private void sendMessage(byte messageMode, String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(PlayWithFriend.this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            message = " " + message;
            byte[] send = message.getBytes();
            send[0] = messageMode;
            mChatService.write(send);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult

        //
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

    public void setupChat() {
        mChatService = BluetoothService.getInstance();
        mChatService.setmHandler(mHandler);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = PlayWithFriend.this;
            switch (msg.what) {
//                case Constants.MESSAGE_STATE_CHANGE:
//                    switch (msg.arg1) {
//                        case BluetoothChatService.STATE_CONNECTED:
//                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
//                            mConversationArrayAdapter.clear();
//                            break;
//                        case BluetoothChatService.STATE_CONNECTING:
//                            setStatus(R.string.title_connecting);
//                            break;
//                        case BluetoothChatService.STATE_LISTEN:
//                        case BluetoothChatService.STATE_NONE:
//                            setStatus(R.string.title_not_connected);
//                            break;
//                    }
//                    break;
//                case Constants.MESSAGE_WRITE:
//                    byte[] writeBuf = (byte[]) msg.obj;
//                    //messageWrite(writeBuf, msg.arg1);
//                    break;
//                case Constants.MESSAGE_READ:
//                    byte[] readBuf = (byte[]) msg.obj;
//                    //messageRead(readBuf, msg.arg1);
//                    break;
//                case Constants.MESSAGE_DEVICE_NAME:
//                    // save the connected device's name
//                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
//                    if (null != activity) {
//                        Toast.makeText(activity, "Connected to "
//                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
//                    }
//                    break;
                case Constants.MESSAGE_WRITE: {
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf, 1, writeBuf.length - 1);
                    Log.e("<<WRITE>>", writeMessage);

                    switch (writeBuf[0]) {
                        case MessageCaro.POSITION: {
                            Log.e("<<WRITE-POSITION>>", writeMessage);
                            String[] pos = writeMessage.split(" ");
                            int i = Integer.parseInt(pos[0]);
                            int j = Integer.parseInt(pos[1]);
                            drawView.setCheckedStates(new Node(i, j));
                            if (drawView.isFinish()) {

                            }
                            break;
                        }
                        case MessageCaro.CONTINUE: {
                            Log.e("<<WRITE-CONTINUE>>", writeMessage);
                            if(writeMessage.equals("yes")) {
                                progressDialog = new ProgressDialog(PlayWithFriend.this);
                                progressDialog.setMessage("Chờ đối thủ...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        while(isRunning) {

                                        }
                                    }
                                }).start();
                            } else {
                                PlayWithFriend.this.finish();
                            }
                            break;
                        }
                    }
                }
                break;
                case Constants.MESSAGE_READ: {
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 1, msg.arg1 - 1);
                    Log.e("<<READ>>", readMessage);

                    switch (readBuf[0]) {
                        case MessageCaro.POSITION: {
                            Log.e("<<READ-POSITION>>", readMessage);
                            String[] pos = readMessage.split(" ");
                            int i = Integer.parseInt(pos[0]);
                            int j = Integer.parseInt(pos[1]);
                            drawView.setCheckedStates(new Node(i, j));
                            break;
                        }
                        case MessageCaro.MESSAGE: {
                            Log.e("<<READ-MESSAGE>>", readMessage);
                            noWaitingMsg++;
                            txtWaitingMsg.setVisibility(View.VISIBLE);
                            txtWaitingMsg.setText(noWaitingMsg.toString());
                            break;
                        }
                        case MessageCaro.CONTINUE: {
                            Log.e("<<READ-CONTINUE>>", readMessage);
                            if(readMessage.equals("yes")) {
                                isRunning = false;
                                drawView.resetBoard();
                            } else {
                                isRunning = false;
                                progressDialog.dismiss();
                                PlayWithFriend.this.finish();
                            }
                        }
                    }

                    break;
                }
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }

        mChatService.setmHandler(mHandler);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    //setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
//                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(PlayWithFriend.this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    PlayWithFriend.this.finish();
                }
        }
    }

    /**
     * Establish connection with other device
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bluetooth_chat, menu);
        MenuItem toggleModeItem = menu.findItem(R.id.toggle_mode);
        toggleModeItem.setTitle(toggleMode ? "O" : "X");
        toggleModeItem.setIcon(toggleMode ? R.drawable.ic_o_24dp : R.drawable.ic_x_24dp);
        return true;
    }


//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        MenuItem toggleModeItem = menu.findItem(R.id.toggle_mode);
//        toggleModeItem.setTitle(toggleMode ? "O" : "X");
//        toggleModeItem.setIcon(toggleMode ? R.drawable.ic_o_24dp: R.drawable.ic_x_24dp);
//        super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(PlayWithFriend.this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(PlayWithFriend.this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
            case R.id.toggle_mode: {
                toggleMode = !toggleMode;
                PlayWithFriend.this.supportInvalidateOptionsMenu();
                return true;
            }
        }
        return false;
    }

    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
}


