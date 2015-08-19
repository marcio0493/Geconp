package br.com.geconp.geconp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class HistoricoPoupancaActivity extends ListActivity {
    private static final String[] opcoes= {"Valores Depositados","Valores Retirados"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_historico_poupanca);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.item_his_poupanca, opcoes);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if(id==0){
            Toast.makeText(this, "Position 1", Toast.LENGTH_LONG).show();
            startActivity(new Intent(HistoricoPoupancaActivity.this,HistDepositoActivity.class ));
        }else if(id==1){
            Toast.makeText(this, "Position 2", Toast.LENGTH_LONG).show();
            startActivity(new Intent(HistoricoPoupancaActivity.this,HistRetiradoActivity.class ));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historico_poupanca, menu);
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
