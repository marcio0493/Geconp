package br.com.geconp.geconp;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.Despesa;


public class BuscaDespesaActivity extends Activity {
    private static final String[] opcoes={"Categoria", "Conta", "Data", "Valor"};
    private String item;
    List<Despesa> busca;
    private Despesa despesa=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_despesa);

        Spinner spinner= (Spinner)findViewById(R.id.spinnerBuscaDadoConDespesa);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opcoes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        itemSelecionado(spinner);
        item= spinner.getSelectedItem().toString();

        ImageButton button= (ImageButton)findViewById(R.id.imageButtonBuscaConDes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar(item);
            }
        });


    }

    public void itemSelecionado(final Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item= parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void buscar(String item){
        final EditText txtBusca= (EditText)findViewById(R.id.editTextDadoBuscaDespesa);
        if(txtBusca.getText().toString().equals("")){
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle(R.string.cuidado);
            dialog.setIcon(R.drawable.ic_action_warning);
            dialog.setMessage(R.string.campoVazio);
            dialog.setNeutralButton("Ok", null);
            dialog.show();
        }else{
            String dado= txtBusca.getText().toString();
            final TextView textBusca= (TextView)findViewById(R.id.txtViewBusConDes);
            textBusca.setText("");
            DatabaseManager manager;
            StringBuilder sb;
            if(item.equals("Categoria")){
                manager= new DatabaseManager(this);
                busca= manager.buscaCategoriaDespesa(dado);
                sb= new StringBuilder();
                for(Despesa despesa: busca){
                    sb.append(String.valueOf(despesa)).append("\n");
                }
                textBusca.setText(sb.toString());
                textBusca.setVisibility(View.VISIBLE);

            }else if(item.equals("Conta")){
                manager= new DatabaseManager(this);
                busca= manager.buscaContaDespesa(dado);
                sb= new StringBuilder();

                for(Despesa despesa: busca){
                    sb.append(String.valueOf(despesa)).append("\n");
                }
                textBusca.setText(sb.toString());
                textBusca.setVisibility(View.VISIBLE);

            }else if(item.equals("Data")){

                manager= new DatabaseManager(this);
                busca= manager.buscaDataDespesa(dado);
                sb= new StringBuilder();

                for(Despesa despesa: busca){
                    sb.append(String.valueOf(despesa)).append("\n");
                }
                textBusca.setText(sb.toString());
                textBusca.setVisibility(View.VISIBLE);

            }else if(item.equals("Valor")){
                String aux1= txtBusca.getText().toString();
                String aux2= aux1.replace(",", ".");
                float valor= Float.parseFloat(aux2);

                manager= new DatabaseManager(this);
                busca= manager.buscaValorDespesa(valor);
                sb= new StringBuilder();

                for(Despesa despesa: busca){
                    sb.append(String.valueOf(despesa)).append("\n");
                }
                textBusca.setText(sb.toString());
                textBusca.setVisibility(View.VISIBLE);
            }


        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_con_busca_despesa, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_ex_bus_des:
                excluir();
                break;
            case R.id.menu_att_bus_des:
                atualizar();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void excluir(){
        Toast.makeText(BuscaDespesaActivity.this, "ONCLICK 2", Toast.LENGTH_LONG).show();
        Despesa despesa= new Despesa();
    }

    public void atualizar(){
        Toast.makeText(BuscaDespesaActivity.this, "ONCLICK 3", Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_busca_despesa, menu);
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
