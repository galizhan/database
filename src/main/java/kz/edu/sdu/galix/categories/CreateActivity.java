package kz.edu.sdu.galix.categories;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class CreateActivity extends AppCompatActivity {
    DBHelper dbh;
    SQLiteDatabase db;
    EditText ed;
    String activity,action;
    long cat_id,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        dbh = new DBHelper(this);
        db = dbh.getWritableDatabase();
        ed = (EditText) findViewById(R.id.ed1);
        Intent intent = getIntent();
        activity = intent.getStringExtra("activity");
        action = intent.getStringExtra("action");
        cat_id = intent.getLongExtra("cat_id",0);

        if(action.equals("update")){
            id = intent.getLongExtra("id",0);
            Cursor c = db.query(activity, null, "_id=?",
                    new String[]{id+""}, null, null, null);
            if(c.moveToFirst()){
                ed.setText(c.getString(c.getColumnIndex("name")));
            }

        }
        }
    public void save(View v){
        ContentValues cv = new ContentValues();
        Log.d("newq",""+cat_id);
        switch (activity){
            case "categories":

                cv.put("name",ed.getText().toString());
                if(action.equals("update")){
                    db.update("categories", cv, "_id="+id, null);
                }
                else{
                db.insert("categories",null,cv);

                }
                break;
            case "products":
                //Ya znaiu chto mojno bylo sdelat legche no poskolku ya eto uje sdelal
                //ne hochu nichego menyat
                cv.put("name",ed.getText().toString());
                if(action.equals("update")){
                    db.update("products", cv, "_id="+id, null);
                }
                else{
                    cv.put("cat_id",cat_id);
                    db.insert("products",null,cv);
                }
                break;
        }

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbh.close();
    }
}
