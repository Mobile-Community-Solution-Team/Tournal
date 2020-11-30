package com.kelompokmcs.tournal.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kelompokmcs.tournal.Adapter.ChatAdapter;
import com.kelompokmcs.tournal.Model.Chat;
import com.kelompokmcs.tournal.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvChat;
    private Button btnSend;
    private Socket socket;
    private int groupId;
    private EditText etMessage;
    private ChatAdapter adapter;
    private ArrayList<Chat> chatArrayList;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rvChat = findViewById(R.id.rv_chat);
        btnSend = findViewById(R.id.btn_send);
        etMessage = findViewById(R.id.et_message);
        chatArrayList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        groupId = getIntent().getIntExtra("groupId",-1);

        toolbar.setTitle("");
        toolbarTitle.setText(getIntent().getStringExtra("groupName"));
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        connectToSocket();
        setupRecyclerView();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = GoogleSignIn.getLastSignedInAccount(ChatActivity.this).getId();
                String userName = GoogleSignIn.getLastSignedInAccount(ChatActivity.this).getDisplayName();
                String messageContent = etMessage.getText().toString().trim();

                socket.emit("newMessage",userId,userName,messageContent,String.valueOf(groupId));
                Chat chat = new Chat(userId,R.drawable.ic_user_avatar,userName,messageContent, new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime()));

                addItemToRecyclerView(chat);
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new ChatAdapter(this,chatArrayList);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(adapter);
    }

    private void connectToSocket() {
        try {
            socket = IO.socket("http://ec2-100-25-163-107.compute-1.amazonaws.com:3000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();

        while (!socket.connected()) {
            Log.d("Socket.io", "connecting...");
        }

        String userName = GoogleSignIn.getLastSignedInAccount(ChatActivity.this).getDisplayName();
        socket.emit("subscribe",userName,String.valueOf(groupId));
        socket.on("updateChat", onUpdateChat); // To update if someone send a message to chatroom
    }

    Emitter.Listener onUpdateChat = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        JSONObject message = new JSONObject((String) args[0]);
                        String userId = message.getString("userId");
                        String userName = message.getString("userName");
                        String messageContent = message.getString("messageContent");
                        String groupId = message.getString("groupId");

                        Chat chat = new Chat(userId,R.drawable.ic_user_avatar,userName,messageContent, new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime()));

                        addItemToRecyclerView(chat);
                    } catch (JSONException e) {
                        Toast.makeText(ChatActivity.this, "Gagal menerima pesan", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    private void addItemToRecyclerView(Chat chat) {
        chatArrayList.add(chat);
        adapter.setChatArrayList(chatArrayList);
        adapter.notifyDataSetChanged();

        etMessage.setText("");

        rvChat.scrollToPosition(chatArrayList.size() - 1);
    }

}
