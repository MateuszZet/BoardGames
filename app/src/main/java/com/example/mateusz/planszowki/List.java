package com.example.mateusz.planszowki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.NumberPicker;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.mateusz.planszowki.R.id.text2;

public class List extends AppCompatActivity {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference games = mDatabase.child("Games");


    ListView List;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        List = (ListView)findViewById(R.id.listView);


            FirebaseListAdapter<game> adapter2 = new FirebaseListAdapter<game>(this, game.class, android.R.layout.two_line_list_item, games) {

                @Override
                protected void populateView(View v, game game, int position) {

                    ((TextView) v.findViewById(android.R.id.text1)).setText(game.getGamename() + "\n" + game.getDate() + "\n" + game.getTime() + "\n" + game.getDescription());
                    ((TextView) v.findViewById(android.R.id.text2)).setText(game.getEmail());
                }


            };

            List.setAdapter(adapter2);
        //listener to click in item
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String email = ((TextView) view.findViewById(android.R.id.text2)).getText().toString();
        String data = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
        Toast.makeText(List.this, "Send message to: " + email, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
        intent.putExtra(Intent.EXTRA_SUBJECT, data);
        intent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(intent, ""));
    }
});

    }
}
