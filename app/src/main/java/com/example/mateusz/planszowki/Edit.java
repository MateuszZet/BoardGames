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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.NumberPicker;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.jar.Attributes;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

//import static com.example.mateusz.planszowki.R.id.Game;

public class Edit extends AppCompatActivity {
    Button Add, Delete;
    EditText Desc;
    ListView List;
    String gkey = "0";

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference games = mDatabase.child("Games");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Add=(Button)findViewById(R.id.Add);
        Delete=(Button)findViewById(R.id.delete);
        List = (ListView)findViewById(R.id.listView);
        Desc=(EditText)findViewById(R.id.Description);



            FirebaseListAdapter<game> adapter2 = new FirebaseListAdapter<game>(this, game.class, R.layout.custom, games) {

                @Override
                protected void populateView(View v, game game, int position) {
                    ((TextView) v.findViewById(R.id.textView)).setText(game.getGamename());
                    ((TextView) v.findViewById(R.id.textView2)).setText(game.getDate());
                    ((TextView) v.findViewById(R.id.textView3)).setText(game.getTime());
                    ((TextView) v.findViewById(R.id.textView4)).setText(game.getDescription());
                    ((TextView) v.findViewById(R.id.textView6)).setText(game.getEmail());
                    ((TextView) v.findViewById(R.id.textView7)).setText(game.getId());
                }


            };

            List.setAdapter(adapter2);

        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String email = ((TextView) view.findViewById(R.id.textView6)).getText().toString();
                String key = ((TextView) view.findViewById(R.id.textView7)).getText().toString();
                if(email.equals(user.getEmail())){
                    gkey = key;
                    Query query = mDatabase.child("Games").orderByChild("id").equalTo(key);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Toast.makeText(Edit.this, "Selected", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{Toast.makeText(Edit.this, "Not your game!", Toast.LENGTH_SHORT).show();
                    gkey = "0";
                }
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                final String description=Desc.getText().toString();

                Query query = mDatabase.child("Games").orderByChild("id").limitToLast(1).equalTo(gkey);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                                String key = nodeDataSnapshot.getKey();
                                String path = "/" + dataSnapshot.getKey() + "/" + key;
                                TreeMap<String, Object> result = new TreeMap();

                                result.put("description", description);
                                mDatabase.child(path).updateChildren(result);
                                    Toast.makeText(Edit.this, "Successfully edited!", Toast.LENGTH_SHORT).show();
                                }

                                else{Toast.makeText(Edit.this, "Nothing to edit!", Toast.LENGTH_SHORT).show();}
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }


                        });

                    }


        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Query query = mDatabase.child("Games").orderByChild("id").limitToLast(1).equalTo(gkey);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                        DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                        String key = nodeDataSnapshot.getKey();
                        String path = "/" + dataSnapshot.getKey() + "/" + key;
                        mDatabase.child(path).removeValue();
                            Toast.makeText(Edit.this, "Successfully deleted!", Toast.LENGTH_SHORT).show();
                        }
                        else{Toast.makeText(Edit.this, "Nothing to delete!", Toast.LENGTH_SHORT).show();}
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }



}


