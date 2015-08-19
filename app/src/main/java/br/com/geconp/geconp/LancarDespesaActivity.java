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

import br.com.geconp.geconp.async.AsyncDeletaMovDes;
import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.MovimentoDespesa;

public class LancarDespesaActivity extends Activity {

    private List<MovimentoDespesa> lista;
    private ArrayAdapter<MovimentoDespesa> adapter;
    private int adapterLayout= R.layout.item_lan_despesa;
    ListView listas;
    private MovimentoDespesa despesa= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancar_despesa);
        listas= (ListView) findViewById(R.id.listViewDespesa);

        registerForContextMenu(listas);

        listas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                despesa= (MovimentoDespesa)parent.getItemAtPosition(position);
                return false;
            }
        });

    }

    public void excluirDespesa(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Deseja realmente excluir?");
        builder.setIcon(R.drawable.ic_action_remove);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseManager manager= new DatabaseManager(LancarDespesaActivity.this);
                manager.deletarMovDespesa(despesa);
                manager.close();
                carregarLista();
                despesa= null;
            }
        });

        builder.setNegativeButton("Não", null);
        AlertDialog dialog= builder.create();
        builder.setTitle("Confirmação");
        dialog.show();
    }

    public void adicionar(){
        startActivity(new Intent(this, AdicionarLanDesActivity.class));
    }

    public void editar(){
        Intent it= new Intent(this, AttMovDespesaActivity.class);
        it.putExtra("ID", String.valueOf(despesa.getId()));
        it.putExtra("VALOR", String.valueOf(despesa.getValor()));
        it.putExtra("DATA", despesa.getData());
        it.putExtra("DESCRICAO", despesa.getDescricao());
        startActivityForResult(it, 1);
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
        getMenuInflater().inflate(R.menu.menu_lan_despesa, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_lan_add:
                adicionar();
                break;
            case R.id.menu_lan_des_ed:
                editar();
                break;
            case R.id.menu_lan_exc:
                excluirDespesa();
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
        this.lista= manager.listarMovDespesa();
        manager.close();
        this.adapter= new ArrayAdapter<MovimentoDespesa>(this, adapterLayout, lista);
        listas.setAdapter(adapter);
    }



    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();
    }


}
