package com.example.realmdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Person;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    EditText nameEditText;
    EditText marksEditText;
    Button saveButton;
    TextView displayTextView;
    Realm realm;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        nameEditText = findViewById(R.id.nameId);
        marksEditText = findViewById(R.id.marksId);
        saveButton = findViewById(R.id.btnSave);
        displayTextView = findViewById(R.id.displayText);

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                readData();
            }
        });
    }
    private void saveData(){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Student student = bgRealm.createObject(Student.class);
                student.setName(nameEditText.getText().toString().trim());
                student.setMarks(Integer.parseInt(marksEditText.getText().toString().trim()));
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Original queries and Realm objects are automatically updated.
//                puppies.size(); // => 0 because there are no more puppies younger than 2 years old
//                managedDog.getAge();   // => 3 the dogs age is updated
                Log.d(TAG,"Data written successfully");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d(TAG,"onError"+error.getMessage());
            }
        });
    }

    private void readData(){
        RealmResults<Student> realmResults = realm.where(Student.class).findAll();
        displayTextView.setText("");
        String data = "";

        for (Student student : realmResults){
            try {
                Log.d(TAG,"ReadData");
                data = data + "\n" +student.toString();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        displayTextView.setText(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
