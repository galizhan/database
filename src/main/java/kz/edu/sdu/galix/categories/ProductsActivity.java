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
import android.widget.TableRow;
import android.widget.TextView;

public class ProductsActivity extends AppCompatActivity {
    DBHelper dbh;
    SQLiteDatabase db;
    long cat_id,id,changableId;
    TableLayout tbl;
    final int MENU_UPDATE = 1;
    final int MENU_DELETE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        dbh = new DBHelper(this);
        db = dbh.getWritableDatabase();
        Intent i = getIntent();
        cat_id = i.getLongExtra("cat_id",0);
        tbl = (TableLayout) findViewById(R.id.tbl2);
        Log.d("newq",""+cat_id);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh2(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh2(null);
    }

    public void create2(View v){
        Intent i = new Intent(this,CreateActivity.class);
        i.putExtra("cat_id",cat_id);
        i.putExtra("activity","products");
        i.putExtra("action","insert");
        startActivity(i);
    }
    public void refresh2(View v){
        tbl.removeAllViews();
        TableLayout.LayoutParams tbllp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        tbllp.bottomMargin = 10;
        tbllp.leftMargin = 5;
        Cursor c = db.rawQuery("select * from products where cat_id=? ",new String[]{cat_id+""});
        if(c.moveToFirst()){
            do {
                id = c.getLong(c.getColumnIndex("_id"));
                String name = c.getString(c.getColumnIndex("name"));
                TextView tv = new TextView(this);

                tv.setTextSize(25);
                tv.setText(id + ". " + name  + "    >");
                tv.setBackgroundColor(getResources().getColor(R.color.colorGray));
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
                i.putExtra("activity","products");
                startActivity(i);
                break;
            case MENU_DELETE:
                db.delete("products", "_id=?", new String[]{changableId+""});
                refresh2(null);
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
