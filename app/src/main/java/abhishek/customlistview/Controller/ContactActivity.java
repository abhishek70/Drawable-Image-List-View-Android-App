package abhishek.customlistview.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import abhishek.customlistview.DatabaseHelper.DatabaseHandler;
import abhishek.customlistview.Model.Contact;
import abhishek.customlistview.R;

public class ContactActivity extends Activity {

    public String addEditKey;
    public LinearLayout addLinearLayout, editLinearLayout;
    public EditText addName, addMobile, addEmail;
    public Button addBtn, saveBtn;
    public DatabaseHandler db;
    public int contactId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Initialize the database handler to create the database
        db = new DatabaseHandler(this);
        addLinearLayout = (LinearLayout) findViewById(R.id.updateView);
        editLinearLayout = (LinearLayout) findViewById(R.id.addView);
        addName = (EditText) findViewById(R.id.addName);
        addMobile = (EditText) findViewById(R.id.addMobile);
        addEmail = (EditText) findViewById(R.id.addEmail);
        addBtn = (Button) findViewById(R.id.addBtn);
        saveBtn = (Button) findViewById(R.id.saveBtn);


        addEditKey = getIntent().getStringExtra("ADDEDITKEY");

        if(addEditKey.equals("Add")) {
            addLinearLayout.setVisibility(View.GONE);
            editLinearLayout.setVisibility(View.VISIBLE);
        } else {
            contactId = Integer.parseInt(getIntent().getStringExtra("CONTACTID"));
            editLinearLayout.setVisibility(View.GONE);
            addLinearLayout.setVisibility(View.VISIBLE);
            Contact contact = db.GetContact(contactId);
            addName.setText(contact.name);
            addMobile.setText(contact.phone);
            addEmail.setText(contact.email);
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContactData();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContactData();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Resetting all the fields
    public void resetField(View view) {
        addName.setText("");
        addMobile.setText("");
        addEmail.setText("");
    }

    // Function for adding contact data
    public void addContactData() {
        if(isValidName()) {

            String name = addName.getText().toString();
            String phone = addMobile.getText().toString();
            String email = addEmail.getText().toString();

            long insertId = db.AddContact(new Contact(name,phone,email));
            if(insertId!=-1) {
                Toast.makeText(this, "Contact has been added successfully", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Error while adding contact", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Function for updating the contact data
    public void saveContactData() {
        if(isValidName()) {

            String name = addName.getText().toString();
            String phone = addMobile.getText().toString();
            String email = addEmail.getText().toString();

            boolean success = db.UpdateContact(new Contact(contactId,name,phone,email));
            if(success) {
                Toast.makeText(this, "Contact has been updated successfully", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Error while adding contact", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Function for validation of the fields
    public boolean isValidName() {
        boolean success = false;
        String name = addName.getText().toString();
        addName.setError(null);
        if(name.length()>0) {
            success = true;
        } else {
            success = false;
            addName.setError("Enter your name");
        }
        return success;
    }
}
