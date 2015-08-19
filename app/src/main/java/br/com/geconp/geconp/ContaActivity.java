package br.com.geconp.geconp;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import br.com.geconp.geconp.async.AsyncConta;
import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.Conta;


public class ContaActivity extends Activity {
    Spinner spinner;
    String itemConta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);

        spinner= (Spinner) findViewById(R.id.spinnerTipoConta);
        loadDataConta();

        itemSelecionado(spinner);
        itemConta= spinner.getSelectedItem().toString();

        Button add= (Button) findViewById(R.id.buttonAddConta);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gravar(itemConta);
            }
        });
    }

    private void itemSelecionado(final Spinner spinner){
        Log.i("SPINNER", "Chegou aqui");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemConta= parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    private void gravar(String item){
        Log.i("SELECIONADO", "Item: " + item);
        EditText txtNomeConta= (EditText)findViewById(R.id.editTextNomeConta);
        EditText txtSaldoConta= (EditText)findViewById(R.id.editTextSaldoConta);

        if(txtNomeConta.getText().toString().equals("") || txtSaldoConta.getText().toString().equals(".00") || txtSaldoConta.getText().toString().equals("")){
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle(R.string.cuidado);
            dialog.setIcon(R.drawable.ic_action_warning);
            dialog.setMessage(R.string.campoVazio);
            dialog.setNeutralButton("Ok", null);
            dialog.show();
        }else{
            String nome= txtNomeConta.getText().toString();
            String aux1= txtSaldoConta.getText().toString();
            String aux2= aux1.replace(",", ".");
            float saldo= Float.parseFloat(aux2);

            Log.i("CONTA", "Item: " + item);
            Conta conta= new Conta(nome, item, saldo);
            AsyncConta async= new AsyncConta(conta, this);
            async.execute();

            txtNomeConta.setText("");
            txtSaldoConta.setText("");
        }
    }

    public void loadDataConta(){
        DatabaseManager manager= new DatabaseManager(getApplicationContext());
        List<String> labels= manager.listarCategoriaConta();
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
