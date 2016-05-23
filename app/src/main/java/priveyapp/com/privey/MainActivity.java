package priveyapp.com.privey;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Firebase mFirebaseRef;
    EditText mEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);

        mFirebaseRef = new Firebase("https://privey.firebaseio.com");
        mEditText = (EditText) this.findViewById(R.id.message_text);


    }

    public void onSendButtonClick(View v) {
        String message = mEditText.getText().toString();
        Map<String,Object> values = new HashMap<>();
        values.put("name", "puf");
        values.put("message", message);
        mFirebaseRef.push().setValue(values);
        mEditText.setText("");
    }

}
