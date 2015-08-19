package br.com.geconp.geconp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.geconp.geconp.async.AsyncDeletaCartao;
import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.Cartao;

public class ListarCartaoActivity extends Activity {

    private List<Cartao> lista;
    private ArrayAdapter<Cartao> adapter;
    private int adapterLayout= R.layout.item_cartao;
    ListView listas;
    private Cartao cartao= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_cartao);

        listas= (ListView) findViewById(R.id.listViewCartoes);

        registerForContextMenu(listas);

        listas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cartao= (Cartao)parent.getItemAtPosition(position);
                return false;
            }
        });

    }

    public void comprar(){
        Intent it= new Intent(this, ComprarCartaoActivity.class);
        it.putExtra("ID2", String.valueOf(cartao.getId()));
        it.putExtra("VALOR2", String.valueOf(cartao.getValor()));
        it.putExtra("DESC2", cartao.getDescricao());
        it.putExtra("BANDEIRA2", cartao.getBandeira());
        startActivity(it);
    }

    public void pagar(){
        Intent it= new Intent(this, PagarCartaoActivity.class);
        it.putExtra("ID3", String.valueOf(cartao.getId()));
        it.putExtra("VALOR3", String.valueOf(cartao.getValor()));
        it.putExtra("BANDEIRA3", cartao.getBandeira());
        startActivity(it);
    }

    public void excluir(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Deseja realmente excluir?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseManager manager= new DatabaseManager(ListarCartaoActivity.this);
                manager.deletarCartao(cartao);
                carregarLista();
                cartao= null;
            }
        });

        builder.setNegativeButton("Não", null);
        AlertDialog dialog= builder.create();
        builder.setTitle("Confirmação");
        dialog.show();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_contexto_cartao, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_cartao_comprar:
                comprar();
                break;
            case R.id.menu_excluir_cartao:
                excluir();
                break;
            case R.id.menu_cartao_pagar:
                pagar();
                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void recreate() {
        super.recreate();
        carregarLista();
    }

    public void carregarLista(){
        DatabaseManager manager= new DatabaseManager(this);
        this.lista= manager.listarCartao();
        manager.close();
        this.adapter= new ArrayAdapter<Cartao>(this, adapterLayout, lista);
        listas.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        carregarLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listar_cartao, menu);
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
