package br.com.geconp.geconp;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

import br.com.geconp.geconp.dao.DatabaseManager;


public class HistDepositoActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseManager manager= new DatabaseManager(this);
        SQLiteDatabase db = manager.getReadableDatabase();

        Cursor cursor = db.query("histDeposito",
                null,
                null,//" nome = ? ", //selection
                null,
                null,
                null, null);

        String[] de= {"banco", "valor", "data"};
        int[] para={R.id.txtViewHistBanco, R.id.txtViewHistValor, R.id.txtViewHistData};
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this,
                        R.layout.activity_hist_deposito, cursor,de,para,0);

        setListAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hist_deposito, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
