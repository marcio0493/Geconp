package br.com.geconp.geconp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.List;

import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.Poupanca;
import br.com.geconp.geconp.model.Receita;


public class ListarPoupancaActivity extends Activity {

    private List<Poupanca> lista;
    private ArrayAdapter<Poupanca> adapter;
    private int adapterLayout= R.layout.item;
    ListView listas;
    private Poupanca poupanca= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_poupanca);

        listas= (ListView) findViewById(R.id.listViewPoupancas);

        registerForContextMenu(listas);

        listas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                poupanca= (Poupanca)parent.getItemAtPosition(position);
                return false;
            }
        });


    }

    public void depositar(){
        Intent it= new Intent(this, DepositarPoupancaActivity.class);
        it.putExtra("ID2", String.valueOf(poupanca.getId()));
        it.putExtra("VALOR2", String.valueOf(poupanca.getValor()));
        it.putExtra("BANCO2", poupanca.getBanco());
        startActivity(it);
    }

    public void retirar(){
        Intent it= new Intent(this, RetirarPoupancaActivity.class);
        it.putExtra("ID3", String.valueOf(poupanca.getId()));
        it.putExtra("VALOR3", String.valueOf(poupanca.getValor()));
        it.putExtra("BANCO3", poupanca.getBanco());
        startActivity(it);
    }



    public void excluir(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Deseja realmente excluir?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseManager manager= new DatabaseManager(ListarPoupancaActivity.this);
                manager.deletarPoupanca(poupanca);
                manager.close();
                carregarLista();
                poupanca= null;
            }
        });

        builder.setNegativeButton("Não", null);
        AlertDialog dialog= builder.create();
        builder.setTitle("Confirmação");
        dialog.show();

        Toast.makeText(this, "Excluído com sucesso", Toast.LENGTH_LONG).show();

     }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_contexto_poupanca, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_pou_depositar:
                depositar();
                break;
            case R.id.menu_pou_excluir:
                excluir();
                break;
            case R.id.menu_pou_sacar:
                retirar();
                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);
    }

    public void carregarLista(){
        DatabaseManager manager= new DatabaseManager(this);
        this.lista= manager.listarPoupanca();
        manager.close();
        this.adapter= new ArrayAdapter<Poupanca>(this, adapterLayout, lista);
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
}
