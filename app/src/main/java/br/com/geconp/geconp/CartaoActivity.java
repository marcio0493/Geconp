package br.com.geconp.geconp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import br.com.geconp.geconp.imgadapter.ImageCartaoAdapter;
import br.com.geconp.geconp.model.Cartao;


public class CartaoActivity extends Activity {
    private Intent intent;
    private int[] imagens= {R.mipmap.ic_cartao, R.mipmap.ic_cartao, R.mipmap.ic_cartao};
    private String[] textos= {"Cadastrar Cartão", "Histórico", "Listar Cartões"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartao);

        GridView gridview = (GridView) findViewById(R.id.gridviewCartao);
        gridview.setAdapter(new ImageCartaoAdapter(this, imagens, textos));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    startActivity(new Intent(CartaoActivity.this, CadastrarCartaoActivity.class));
                }else if(position==1){
                    startActivity(new Intent(CartaoActivity.this, HistoricoCartaoActivity.class));
                }else if(position==2){
                    startActivity(new Intent(CartaoActivity.this, ListarCartaoActivity.class));
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cartao, menu);
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
