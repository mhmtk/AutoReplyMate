package com.mhmt.autoreplymate.activities;

import java.util.ArrayList;

import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.mhmt.autoreplymate.R;

/**
 * @author Mehmet Kologlu
 * @version June 14, 2015
 *
 * insipired by Satheesh's comment on
 * http://stackoverflow.com/questions/9450058/using-checkbox-to-filter-contacts-and-get-phone-number/10105655#10105655
 */
public class ContactPicker extends ActionBarActivity {

    static ContentResolver cr;
    String[] phoneNos;
    String[] listContacts;
    ArrayList<String> selectedContacts = new ArrayList<String>();
    Button selectButton;
    Activity thisActivity;

    SparseBooleanArray checked;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_picker);
        thisActivity = this;

        listView = (ListView)findViewById(R.id.contactpicker_contactsList);

        populateContactList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, listContacts);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    private void populateContactList(){
        Uri myContacts = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(myContacts,
                projection, null, null,
                ContactsContract.Contacts.SORT_KEY_PRIMARY + " ASC"  // order
        );
        phoneNos = new String[cursor.getCount()];
        listContacts = new String[cursor.getCount()];
        int i =0;
        if(cursor.moveToFirst())
        {
            do
            {
                String phoneNo = cursor.getString(cursor
                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNos[i] = phoneNo;
                listContacts[i] = cursor.getString(cursor
                        .getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME)) + "\n" +
                        phoneNo;
                i++;
            }
            while(cursor.moveToNext());
        }
        cursor.close();
    }

    private void doneSelected(){
        checked = listView.getCheckedItemPositions();

        for (int i = 0; i < listView.getCount(); i++)
            if (checked.get(i)) {
                selectedContacts.add(phoneNos[i]); // TODO trim - ( ) and white space
                //you can you this array list to next activity
                      /* do whatever you want with the checked item */
            }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("selected_contacts", selectedContacts);

        Intent contactIntent = new Intent();
        contactIntent.putExtras(bundle);
        setResult(1, contactIntent);
        thisActivity.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.contactpicker_action_done:
                doneSelected();
                return true;
            case R.id.contactpicker_action_selectAll:
                setAll(true);
                return true;
            case R.id.contactpicker_action_deselectAll:
                setAll(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Sets all the checkboxes in the listView to the given value
     * @param value Value to set
     */
    private void setAll(boolean value) {
        for (int i = 0; i < listView.getCount(); i++) {
            listView.setItemChecked(i, value);
        }
    }
}