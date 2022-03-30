package lk.hashcode.encryption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.button);
        EditText editText=findViewById(R.id.edittext);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Chats");
                Adapter adapter=new Adapter();
                String key="vishwa";
                String text=editText.getText().toString();
                String encrypHexa = "";
                int keyItr = 0;
                for (int i = 0; i < text.length(); i++) {

                    int temp = text.charAt(i) ^ key.charAt(keyItr);

                    encrypHexa += String.format("%02x", (byte)temp);
                    keyItr++;
                    if(keyItr >= key.length()){

                        keyItr = 0;
                    }

                }
                adapter.setMessage(encrypHexa);
                reference.push().setValue(adapter);
                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();

            }
        });


        Query query = FirebaseDatabase.getInstance().getReference().child("Chats");
        FirebaseListOptions<Adapter> options = new FirebaseListOptions.Builder<Adapter>()
                .setLayout(R.layout.item)
                .setLifecycleOwner(MainActivity.this)
                .setQuery(query, Adapter.class)
                .build();
        FirebaseListAdapter adapter =new FirebaseListAdapter(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                TextView message=v.findViewById(R.id.text);
                Adapter adapterclass = (Adapter) model;
                String key="vishwa";
                String hexToDeci = "";
                for (int i = 0; i < adapterclass.getMessage().length()-1; i+=2) {
                    String output = adapterclass.getMessage().substring(i, (i+2));
                    int decimal = Integer.parseInt(output, 16);
                    hexToDeci += (char)decimal;
                }


                String decrypText = "";
                int keyItr = 0;
                for (int i = 0; i < hexToDeci.length(); i++) {
                    int temp = hexToDeci.charAt(i) ^ key.charAt(keyItr);

                    decrypText += (char)temp;
                    keyItr++;
                    if(keyItr >= key.length()){
                        keyItr = 0;
                    }

                }


                message.setText(decrypText);

            }
        };
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }
}