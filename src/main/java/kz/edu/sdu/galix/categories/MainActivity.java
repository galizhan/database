package kz.edu.sdu.galix.categories;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button btn;
    TableLayout tbl;
    DBHelper dbh;
    SQLiteDatabase db;
    long id,changableId;
    final int MENU_UPDATE = 1;
    final int MENU_DELETE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn);
        dbh = new DBHelper(this);
        db = dbh.getWritableDatabase();

        tbl = (TableLayout) findViewById(R.id.tbl);

    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh(null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refresh(null);
    }

    public void create(View v){
        Intent i = new Intent(this,CreateActivity.class);
        i.putExtra("action","insert");
        i.putExtra("activity","categories");
        startActivity(i);
    }
    public void refresh(View v){
        tbl.removeAllViews();
        TableLayout.LayoutParams tbllp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        tbllp.bottomMargin = 10;
        tbllp.leftMargin = 5;
        Cursor c = db.query("categories", null, null, null, null, null, "name");
        if(c.moveToFirst()){
            do {
                long id = c.getLong(c.getColumnIndex("_id"));
                String name = c.getString(c.getColumnIndex("name"));
                TextView tv = new TextView(this);
                tv.setTextSize(25);
                tv.setText(id + ". " + name +"    >");
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String n = ((TextView)v).getText().toString();
                        Intent i2 = new Intent(v.getContext(),ProductsActivity.class);
                        i2.putExtra("cat_id",Long.parseLong(n.split("\\.")[0]));
                        startActivity(i2);


                    }
                });
                tv.setTextColor(getResources().getColor(R.color.colorBlack));

                tbl.addView(tv, tbllp);
                registerForContextMenu(tv);
            }while(c.moveToNext());
        }
    c.close();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        String contactStr = ((TextView)v).getText().toString();
        changableId = Long.parseLong(contactStr.split("\\.")[0]);

        menu.add(0, MENU_UPDATE, 0, "Update");
        menu.add(0, MENU_DELETE, 0, "Delete");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case MENU_UPDATE:
                Intent i = new Intent(this, CreateActivity.class);
                i.putExtra("action", "update");
                i.putExtra("id", changableId);
                i.putExtra("activity","categories");
                startActivity(i);
                break;
            case MENU_DELETE:
                db.delete("categories", "_id=?", new String[]{changableId+""});
                refresh(null);
                break;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbh.close();
    }
}
