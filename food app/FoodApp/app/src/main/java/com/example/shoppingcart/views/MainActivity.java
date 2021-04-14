package com.example.shoppingcart.views;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoppingcart.R;

public class MainActivity extends Activity implements OnClickListener
{
    EditText RegistrationNumber,Name,Phone;
    Button Insert,Delete,Update,View,ViewAll;
    SQLiteDatabase db;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RegistrationNumber=(EditText)findViewById(R.id.RegistrationNumber);
        Name=(EditText)findViewById(R.id.Name);
        Phone=(EditText)findViewById(R.id.Phone);
        Insert=(Button)findViewById(R.id.Insert);
        Delete=(Button)findViewById(R.id.Delete);
        Update=(Button)findViewById(R.id.Update);
        View=(Button)findViewById(R.id.View);
        ViewAll=(Button)findViewById(R.id.ViewAll);

        Insert.setOnClickListener(this);
        Delete.setOnClickListener(this);
        Update.setOnClickListener(this);
        View.setOnClickListener(this);
        ViewAll.setOnClickListener(this);

        // Creating database and table
        db=openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);");
    }
    public void onClick(View view)
    {

        if(view==Insert)
        {

            Insert.setOnClickListener(v -> {
                Intent intent = new Intent(this,MainActivity2.class);
                startActivity(intent);
                // Checking for empty fields
                if(RegistrationNumber.getText().toString().trim().length()==0||
                        Name.getText().toString().trim().length()==0||
                        Phone.getText().toString().trim().length()==0)
                {
                    showMessage("Error", "Please enter all values");
                    return;
                }
                db.execSQL("INSERT INTO student VALUES('"+RegistrationNumber.getText()+"','"+Name.getText()+
                        "','"+Phone.getText()+"');");
                showMessage("Success", "Record added");

                Toast.makeText(MainActivity.this, "Details Saved Successfully.", Toast.LENGTH_SHORT).show();
                clearText();
            });
        }
        // Deleting a record from the Student table
        if(view==Delete)
        {
            // Checking for empty roll number
            if(RegistrationNumber.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter RegistrationNumber");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+RegistrationNumber.getText()+"'", null);
            if(c.moveToFirst())
            {
                db.execSQL("DELETE FROM student WHERE rollno='"+RegistrationNumber.getText()+"'");
                showMessage("Success", "Record Deleted");
            }
            else
            {
                showMessage("Error", "Invalid RegistrationNumber");
            }
            clearText();
        }
        // Updating a record in the Student table
        if(view==Update)
        {
            // Checking for empty roll number
            if(RegistrationNumber.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter RegistrationNumber");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+RegistrationNumber.getText()+"'", null);
            if(c.moveToFirst()) {
                db.execSQL("UPDATE student SET name='" + Name.getText() + "',marks='" + Phone.getText() +
                        "' WHERE rollno='"+RegistrationNumber.getText()+"'");
                showMessage("Success", "Record Modified");
            }
            else {
                showMessage("Error", "Invalid RegistrationNumber");
            }
            clearText();
        }
        // Display a record from the Student table
        if(view==View)
        {
            // Checking for empty roll number
            if(RegistrationNumber.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter RegistrationNumber");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+RegistrationNumber.getText()+"'", null);
            if(c.moveToFirst())
            {
                Name.setText(c.getString(1));
                Phone.setText(c.getString(2));
            }
            else
            {
                showMessage("Error", "Invalid RegistrationNumber");
                clearText();
            }
        }
        // Displaying all the records
        if(view==ViewAll)
        {
            Cursor c=db.rawQuery("SELECT * FROM student", null);
            if(c.getCount()==0)
            {
                showMessage("Error", "No records found");
                return;
            }
            StringBuffer buffer=new StringBuffer();
            while(c.moveToNext())
            {
                buffer.append("RegistrationNumber: "+c.getString(0)+"\n");
                buffer.append("Name: "+c.getString(1)+"\n");
                buffer.append("Phone: "+c.getString(2)+"\n\n");
            }
            showMessage("Student Details", buffer.toString());
        }
    }
    public void showMessage(String title,String message)
    {
        Builder builder=new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {
        RegistrationNumber.setText("");
        Name.setText("");
        Phone.setText("");
        RegistrationNumber.requestFocus();
    }


}