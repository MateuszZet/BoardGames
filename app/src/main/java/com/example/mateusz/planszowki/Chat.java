package com.example.mateusz.planszowki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Chat extends AppCompatActivity {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference message = mDatabase.child("Chat");

    private Button btnSend;
    private EditText txtMessage;


    ListView List;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        List = (ListView)findViewById(R.id.listView);
        btnSend = (Button)  findViewById(R.id.send);
        txtMessage = (EditText)findViewById(R.id.editText);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=txtMessage.getText().toString();
                String id = mDatabase.push().getKey();
                mDatabase.child("Chat").child(id).child("username").setValue(user.getEmail());
                mDatabase.child("Chat").child(id).child("ms").setValue(message);
                txtMessage.setText("");

            }
        });

        FirebaseListAdapter<message> adapter = new FirebaseListAdapter<message>(this, message.class,
                android.R.layout.two_line_list_item, message) {
            @Override
            protected void populateView(View v, message message, int position) {

                ((TextView) v.findViewById(android.R.id.text1)).setText(message.getUsername());
                ((TextView) v.findViewById(android.R.id.text2)).setText(message.getMs());
            }


        };

        List.setAdapter(adapter);

        //listener do klikniecia w item
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String email = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                Toast.makeText(Chat.this, "Send message to: " + email, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));
            }
        });
    }
}