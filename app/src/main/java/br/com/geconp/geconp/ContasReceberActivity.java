package br.com.geconp.geconp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.geconp.geconp.async.AsyncDeletaReceita;
import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.MovimentoReceita;
import br.com.geconp.geconp.model.Receita;


public class ContasReceberActivity extends Activity {
    private List<Receita> lista;
    private ArrayAdapter<Receita> adapter;
    private int adapterLayout= R.layout.item;
    ListView listas;
    private Receita receita= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas_receber);
        listas= (ListView)findViewById(R.id.listViewContReceber);
        registerForContextMenu(listas);

        listas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                receita= (Receita)parent.getItemAtPosition(position);
                return false;
            }
        });
    }

    public void excluirReceita(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Deseja realmente excluir?");
        builder.setIcon(R.drawable.ic_action_remove);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseManager manager= new DatabaseManager(ContasReceberActivity.this);
                manager.deletarReceita(receita);
                manager.close();
                carregarLista();
                receita= null;
            }
        });

        builder.setNegativeButton("Não", null);
        AlertDialog dialog= builder.create();
        builder.setTitle("Confirmação");
        dialog.show();

    }

    public void addReceita(){
        startActivity(new Intent(this, ReceitaActivity.class));
    }

    public void atualizar(){
        Intent it= new Intent(this, AtualizarReceitaActivity.class);
        it.putExtra("ID", String.valueOf(receita.getId()));
        it.putExtra("VALOR", String.valueOf(receita.getValor()));
        it.putExtra("DATA", receita.getData());
        it.putExtra("DESCRICAO", receita.getDescricao());
        startActivityForResult(it, 1);
    }

    public void lancarReceita(){
        float valor= (float) receita.getValor();
        String data= receita.getData();
        String descricao= receita.getDescricao();
        String tipo= receita.getTipo();
        String conta= receita.getConta();
        MovimentoReceita movimento= new MovimentoReceita(valor, data, descricao, tipo, conta);

        DatabaseManager manager= new DatabaseManager(this);
        manager.lancarReceita(movimento);
        manager.deletarReceita(receita);
        manager.close();
        Toast.makeText(this, "Recebido com sucesso", Toast.LENGTH_LONG).show();
        carregarLista();
    }


    @Override
    protected void onStart() {
        super.onStart();
        carregarLista();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        carregarLista();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        carregarLista();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_con_receber, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_con_re_add:
                addReceita();
                break;
            case R.id.menu_con_re_atl:
                atualizar();
                break;
            case R.id.menu_con_re_excluir:
                excluirReceita();
                break;
            case R.id.menu_con_re_lancar:
                lancarReceita();
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
        Log.i("LOG", "RECREATE");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== 1){
            Log.i("LOG", "LISTA 3");
            carregarLista();
        }else{
            carregarLista();
        }
    }

    public void carregarLista(){
        Log.i("LOG", "LISTA 1");
        DatabaseManager manager= new DatabaseManager(this);
        this.lista= manager.listarReceita();
        Log.i("LOG", "LISTA 2");
        manager.close();
        this.adapter= new ArrayAdapter<Receita>(this, adapterLayout, lista);
        listas.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();
    }

    @Override
    public WindowManager getWindowManager() {
        Log.i("LOG", "WINDOWMANAGER");
        return super.getWindowManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contas_receber, menu);
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
