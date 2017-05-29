package com.example.mateusz.planszowki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.*;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import static com.example.mateusz.planszowki.R.id.Game;

public class Add extends AppCompatActivity implements View.OnClickListener{
    Button btnDate, btnTime, Add;
    EditText Date, Time, Game, Desc;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ListView List;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        List = (ListView)findViewById(R.id.listView);
        btnDate = (Button)findViewById(R.id.btnDate);
        btnTime=(Button)findViewById(R.id.btnTime);
        Add=(Button)findViewById(R.id.Add);
        Game=(EditText)findViewById(R.id.gra);
        Date=(EditText)findViewById(R.id.Date);
        Time=(EditText)findViewById(R.id.Time);
        Desc=(EditText)findViewById(R.id.Description);

        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);

         FirebaseListAdapter<String> adapter = new FirebaseListAdapter<String>(this,
                 String.class, android.R.layout.simple_list_item_1, mDatabase.child("BoardGames")) {
            @Override
            protected void populateView(View v, String string, int position) {

                ((TextView) v.findViewById(android.R.id.text1)).setText(string);
            }


        };

        List.setAdapter(adapter);

        //listener do klikniecia w item
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                Game.setText(data);


            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gamename=Game.getText().toString();
                String date=Date.getText().toString();
                String time=Time.getText().toString();
                String description=Desc.getText().toString();
                String id = mDatabase.push().getKey();
                game game = new game(gamename,date,time,description, user.getEmail(), id);
                mDatabase.child("Games").child(id).setValue(game);
                Toast.makeText(Add.this, "Add new game", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(Add.this, MainActivity.class));


            }
        });


    }



    @Override
    public void onClick(View v) {

        if (v == btnDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            Date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            Time.setText(hourOfDay + ":" + String.format("%02d", minute));
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }


}
