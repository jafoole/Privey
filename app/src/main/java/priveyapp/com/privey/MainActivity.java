package priveyapp.com.privey;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.FirebaseApp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends ListActivity {

    private Firebase mFirebaseRef;
//    private Firebase mMessageRef;
    EditText mMessageEditText;
    ListView mListView;
    private String mUsername;

    private FirebaseListAdapter<ChatMessage> mListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);

        mFirebaseRef = new Firebase("**************");
        mMessageEditText = (EditText) this.findViewById(R.id.message_text);


        mListAdapter = new FirebaseListAdapter<ChatMessage>(mFirebaseRef, ChatMessage.class, R.layout.message_layout,this) {
            @Override
            protected void populateView(View v, ChatMessage model) {
                ((TextView)v.findViewById(R.id.username_text_view)).setText(model.getName());
                ((TextView)v.findViewById(R.id.message_text_view)).setText(model.getMessage());
            }
        };
        setListAdapter(mListAdapter);

    }

    public void onSendButtonClick(View v) {
        String message = mMessageEditText.getText().toString();
        mMessageEditText.setText("");
    }

    public void onLoginButtonClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enter your email address and password").setTitle("Log in");

        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_signin, null));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                AlertDialog dlg = (AlertDialog) dialog;
                final String email = ((TextView)dlg.findViewById(R.id.email)).getText().toString();
                final String password = ((TextView)dlg.findViewById(R.id.password)).getText().toString();

                mFirebaseRef.createUser(email, password, new Firebase.ResultHandler() {

                    @Override
                    public void onSuccess() {
                        mFirebaseRef.authWithPassword(email, password, null);
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        mFirebaseRef.authWithPassword(email, password, null);
                    }
                });

            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();


        mFirebaseRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    mUsername = ((String)authData.getProviderData().get("email"));
                    findViewById(R.id.login).setVisibility(View.INVISIBLE);
                }
                else {
                    mUsername = null;
                    findViewById(R.id.login).setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
