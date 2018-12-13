package com.android16_team.caro_project;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private MessageAdapter messageAdapter;
    private ListView messageListView;
    private MaterialButton btnSend;
    private EditText messageEditText;
    private BluetoothService mChatService;
    private String mConnectedDeviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

//        Toolbar myToolbar = findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);

        messageAdapter = new MessageAdapter(this, R.layout.from_message, new ArrayList<String>());

        messageListView = findViewById(R.id.messageListView);
        messageListView.setAdapter(messageAdapter);
        messageAdapter = null;
        btnSend = findViewById(R.id.btnSend);
        messageEditText = findViewById(R.id.messageEditText);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(MessageCaro.MESSAGE, messageEditText.getText().toString());
                messageEditText.setText("");
            }
        });

        mChatService = BluetoothService.getInstance();
        mChatService.setmHandler(mHandler);
        MessageAdapter mes = (MessageAdapter) messageListView.getAdapter();
        for(MyMessage m : mChatService.getMessages()) {
            mes.add(m.getMessage(), m.isMode());
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = ChatActivity.this;
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
                case Constants.MESSAGE_WRITE: {
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf, 1, writeBuf.length - 1);
                    Log.e("<<WRITE>>", writeMessage);

                    switch (writeBuf[0]) {
                        case MessageCaro.MESSAGE: {
                            MessageAdapter mes = (MessageAdapter) messageListView.getAdapter();
                            mes.add(writeMessage, false);
                            messageListView.smoothScrollToPosition(messageListView.getMaxScrollAmount());
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
                        case MessageCaro.MESSAGE: {
                            MessageAdapter mes = (MessageAdapter) messageListView.getAdapter();
                            mes.add(readMessage, true);
                            messageListView.smoothScrollToPosition(messageListView.getMaxScrollAmount());
                            break;
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

    private void sendMessage(byte messageMode, String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
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
}
