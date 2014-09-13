package abhishek.customlistview.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import abhishek.customlistview.CustomAdapter.ContactAdapter;
import abhishek.customlistview.DatabaseHelper.DatabaseHandler;
import abhishek.customlistview.Model.Contact;
import abhishek.customlistview.R;


public class ContactListActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    public ListView contactListView;
    public ArrayList<Contact> contactList = new ArrayList<Contact>();
    public DatabaseHandler db;
    public ContactAdapter contactAdapter;
    public SwipeRefreshLayout SwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        // SwipeRefresh functionality implemented
        SwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.SwipeRefresh);
        SwipeRefresh.setOnRefreshListener(this);
        SwipeRefresh.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //setTitle("Contact List");
        db = new DatabaseHandler(this);
        contactListView = (ListView) findViewById(R.id.contactListView);
        fetchDataFromDatabase();
    }

    /*
    @Override
    public void onResume() {
        super.onResume();
        fetchDataFromDatabase();

    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add) {

            Intent intent = new Intent(this,ContactActivity.class);
            intent.putExtra("ADDEDITKEY","Add");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Function for fetching the data from the database
    public void fetchDataFromDatabase() {
        contactList.clear();
        ArrayList<Contact> data = db.GetContacts();

        if(data.size()!=0) {
            for (int i = 0; i < data.size(); i++) {
                int id = data.get(i).id;
                String name = data.get(i).name;
                String phone = data.get(i).phone;
                String email = data.get(i).email;

                Contact contact = new Contact();
                contact.setId(id);
                contact.setPhone(phone);
                contact.setName(name);
                contact.setEmail(email);

                contactList.add(contact);
            }
        }
        db.close();

        // Customize Contact Adapter for displaying each row in the ListView
        contactAdapter = new ContactAdapter(this,R.layout.activity_contact_list_row,contactList);
        contactListView.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();

    }

    // Refresh Handler
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchDataFromDatabase();
                stopSwipeRefresh();
            }
        }, 5 * 1000);
    }

    private void stopSwipeRefresh() {
        SwipeRefresh.setRefreshing(false);
    }
}
