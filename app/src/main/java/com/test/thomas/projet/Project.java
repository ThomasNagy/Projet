package com.test.thomas.projet;


import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android .content.Intent;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class Project extends AppCompatActivity {

    EditText NameTxt, EmailTxt, MarketTxt, PhoneTxt;
    List<Contact> Contacts = new ArrayList<Contact>();
    ListView CListView;
    DatabaseHandler dbHandler;
    ImageView contactImgView;
    Uri imageUri= Uri.parse("android.res://drawable/profil.png");
    int Clicktemindex;
    private static final int EDIT = 0, DELETE = 1;
    ArrayAdapter<Contact> CAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        NameTxt = (EditText)findViewById(R.id.TxtName);
        PhoneTxt = (EditText)findViewById(R.id.TxtPhone);
        EmailTxt = (EditText)findViewById(R.id.TxtMail);
        MarketTxt = (EditText)findViewById(R.id.TxtMarket);
        CListView = (ListView) findViewById(R.id.listView);
        dbHandler = new DatabaseHandler(getApplicationContext());
        contactImgView = (ImageView) findViewById(R.id.ImgViewCI);
        registerForContextMenu(CListView);
        CListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Clicktemindex = position;
                return false;
            }
        });

        final TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.creatorTabs);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.ContactTab);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);

        final Button addBtn = (Button) findViewById(R.id.btnAdd);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = new Contact(dbHandler.getContactCount(), String.valueOf(NameTxt.getText()), String.valueOf(PhoneTxt.getText()), String.valueOf(EmailTxt.getText()), String.valueOf(MarketTxt.getText()),imageUri);
                if (!contactExists(contact)) {
                    dbHandler.createContact(contact);
                    Contacts.add(contact);
                    CAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), String.valueOf(NameTxt.getText()) + " has been added to your Contacts", Toast.LENGTH_SHORT).show();
                    tabHost.setCurrentTab(1);
                }else {
                    Toast.makeText(getApplicationContext(), String.valueOf(NameTxt.getText()) + " already exists. Please use a different name. ", Toast.LENGTH_SHORT).show();
                    CAdapter.notifyDataSetChanged();
                }
                }
        });


        NameTxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                addBtn.setEnabled(String.valueOf(NameTxt.getText()).trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        contactImgView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);
            }
        });


        if(dbHandler.getContactCount()!= 0)
                Contacts.addAll(dbHandler.getAllContacts());
            popList();
    }

    public void onCreateContextMenu(ContextMenu menu, View view,ContextMenu.ContextMenuInfo infomenu){

        super.onCreateContextMenu(menu, view, infomenu);

        menu.setHeaderIcon(R.drawable.edit_icon);
        menu.setHeaderTitle("Contact Options");
        menu.add(Menu.NONE, EDIT, 1, "Edit Contact");
        menu.add(Menu.NONE,DELETE,2,"Delete Contact");
    }

    private boolean contactExists(Contact contact){
        String name = contact.getName();
        int contactCount = Contacts.size();

        for(int i = 0; i<contactCount; i ++){
            if(name.compareToIgnoreCase(Contacts.get(i).getName()) == 0)
                return true;
        }
        return false;
    }

    private void enableEdit(Contact contact){

        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setCurrentTab(0);
        NameTxt.setText(contact.getName());
        PhoneTxt.setText(contact.getPhone());
        EmailTxt.setText(contact.getEmail());
        MarketTxt.setText(contact.getMarket());
        imageUri =contact.getImageURI();
        contactImgView.setImageURI(imageUri);
        Button edit = (Button)findViewById(R.id.btnAdd);
        edit.setText("Update");
    }

    public boolean onContextItemSelected(MenuItem item){

        switch (item.getItemId()){
            case EDIT:
                enableEdit(Contacts.get(Clicktemindex));
                break;
            case DELETE:
                dbHandler.deleteContact(Contacts.get(Clicktemindex));
                Contacts.remove(Clicktemindex);
                CAdapter.notifyDataSetChanged();
                break;
        }

        return super.onContextItemSelected(item);

    }


    private void popList(){
        CAdapter = new ContactListAdapter();
        CListView.setAdapter(CAdapter);
    }

    public void onActivityResult(int reqCode,int resCode, Intent data){
        if(resCode == RESULT_OK)   {
            if(reqCode == 1){
                imageUri = data.getData();
                contactImgView.setImageURI(data.getData());
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(),"Configuration selected", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_bold) {
            Toast.makeText(getApplicationContext(),"Configuration selected", Toast.LENGTH_LONG).show();
            return true;
        }
        if(id==R.id.navigate){
            startActivity(new Intent(this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

private class ContactListAdapter extends ArrayAdapter<Contact>{
    public ContactListAdapter(){
        super(Project.this,R.layout.listview_item,Contacts);
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);
        Contact currentContact = Contacts.get(position);

        TextView name = (TextView) view.findViewById(R.id.contactName);
        name.setText(currentContact.getName());
        TextView phone = (TextView) view.findViewById(R.id.phoneNumber);
        phone.setText(currentContact.getPhone());
        TextView email = (TextView) view.findViewById(R.id.email);
        email.setText(currentContact.getEmail());
        TextView market = (TextView) view.findViewById(R.id.marcher);
        market.setText(currentContact.getMarket());
        ImageView ivContactImage = (ImageView)view.findViewById(R.id.ivContactImage);
        ivContactImage.setImageURI(currentContact.getImageURI());

        return view;
    }
    }


}
