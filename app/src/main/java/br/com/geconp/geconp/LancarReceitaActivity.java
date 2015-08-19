package br.com.geconp.geconp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import br.com.geconp.geconp.async.AsyncDeletaMovRec;
import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.MovimentoReceita;


public class LancarReceitaActivity extends Activity {
    private List<MovimentoReceita> lista;
    private ArrayAdapter<MovimentoReceita> adapter;
    private int adapterLayout= R.layout.item_lan_receita;
    ListView listas;
    private MovimentoReceita receita= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancar_receita);
        listas= (ListView) findViewById(R.id.listViewReceitas);

        registerForContextMenu(listas);

        listas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                receita= (MovimentoReceita)parent.getItemAtPosition(position);
                return false;
            }
        });

    }

    public void excluirReceita(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Confirmação");
        builder.setIcon(R.drawable.ic_action_remove);
        builder.setMessage("Deseja realmente excluir?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseManager manager= new DatabaseManager(LancarReceitaActivity.this);
                manager.deletarMovReceita(receita);
                manager.close();
                carregarLista();
                receita= null;
            }
        });

        builder.setNegativeButton("Não", null);
        AlertDialog dialog= builder.create();
        dialog.show();
    }

    public void adicionar(){
        startActivity(new Intent(this, AdicionarLanReceActivity.class));
    }

    @Override
    public void recreate() {
        super.recreate();
        carregarLista();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            carregarLista();
        }else{
            carregarLista();
        }
    }

    public void carregarLista(){
        DatabaseManager manager= new DatabaseManager(this);
        this.lista= manager.listarMovReceita();
        manager.close();
        this.adapter= new ArrayAdapter<MovimentoReceita>(this, adapterLayout, lista);
        listas.setAdapter(adapter);
    }

    public void editar(){
        Intent it= new Intent(this, AttMovReceitaActivity.class);
        it.putExtra("ID", String.valueOf(receita.getId()));
        it.putExtra("VALOR", String.valueOf(receita.getValor()));
        it.putExtra("DATA", String.valueOf(receita.getData()));
        it.putExtra("DESCRICAO", String.valueOf(receita.getDescricao()));
        startActivityForResult(it, 1);

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
    protected void onResume() {
        super.onResume();
        carregarLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_lan_receita, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_lan_rec_add:
                adicionar();
                break;
            case R.id.menu_lan_rec_exc:
                excluirReceita();
                break;
            case R.id.menu_lan_rec_ed:
                editar();
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
}
