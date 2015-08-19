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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.geconp.geconp.async.AsyncDeletaDespesa;
import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.Despesa;
import br.com.geconp.geconp.model.MovimentoDespesa;


public class ContasPagarActivity extends Activity {
    private List<Despesa> lista;
    private ArrayAdapter<Despesa> adapter;
    private int adapterLayout= R.layout.item_pagar;
    ListView listas;
    private Despesa despesa= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas_pagar);

        listas= (ListView) findViewById(R.id.listViewContPagar);

        registerForContextMenu(listas);

        listas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                despesa= (Despesa)parent.getItemAtPosition(position);
                return false;
            }
        });

    }

    public void excluir(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Deseja realmente excluir?");
        builder.setIcon(R.drawable.ic_action_remove);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseManager manager= new DatabaseManager(ContasPagarActivity.this);
                manager.deletarDespesa(despesa);
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

    public void lancar(){
        float valor= despesa.getValor();
        String data= despesa.getData();
        String descricao= despesa.getDescricao();
        String tipo= despesa.getTipo();
        String conta= despesa.getConta();

        MovimentoDespesa movimento= new MovimentoDespesa(valor, data, descricao, tipo, conta);
        DatabaseManager manager= new DatabaseManager(this);
        manager.lancarDespesa(movimento);
        manager.deletarDespesa(despesa);
        manager.close();
        Toast.makeText(this, "Pago com sucesso", Toast.LENGTH_LONG).show();
        carregarLista();
    }

    public void adicionar(){
        startActivity(new Intent(this, DespesaActivity.class));
    }

    public void atualizar(){
        Intent it= new Intent(this, AtualizarDespesaActivity.class);
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
        getMenuInflater().inflate(R.menu.menu_con_pagar, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_con_pag_add:
                adicionar();
                break;
            case R.id.menu_con_pag_exc:
                excluir();
                break;
            case R.id.menu_con_pag_atl:
                atualizar();
                break;
            case R.id.menu_con_pag_lancar:
                lancar();
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

    public void carregarLista(){
        DatabaseManager manager= new DatabaseManager(this);
        this.lista= manager.listarDespesa();
        manager.close();
        this.adapter= new ArrayAdapter<Despesa>(this, adapterLayout, lista);
        listas.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contas_pagar, menu);
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
