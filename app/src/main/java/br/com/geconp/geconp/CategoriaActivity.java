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

import br.com.geconp.geconp.async.AsyncCatConta;
import br.com.geconp.geconp.async.AsyncCatDespesa;
import br.com.geconp.geconp.async.AsyncCategoria;


public class CategoriaActivity extends Activity {
    private static final String[] categorias={"Receita", "Despesa", "Conta"};
    private String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        //coloca o spinner para as categorias
        final Spinner spinner= (Spinner) findViewById(R.id.spinnerTipoCategoria);
        ArrayAdapter<String> adapterCategoria= new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, categorias);
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterCategoria);

        itemSelecionado(spinner);
        item= spinner.getSelectedItem().toString();

        Button add= (Button)findViewById(R.id.buttonAddCategoria);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gravar(item);
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

    public void gravar(String item){
        EditText txtNome= (EditText)findViewById(R.id.editTextNomeCategoria);
        if(txtNome.getText().toString().equals("")){
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle(R.string.cuidado);
            dialog.setIcon(R.drawable.ic_action_warning);
            dialog.setMessage(R.string.campoVazio);
            dialog.setNeutralButton("Ok", null);
            dialog.show();

        }else{
            String nome= txtNome.getText().toString();
            if(item.equals("Receita")){
                //Toast.makeText(this, "RECEITA", Toast.LENGTH_LONG).show();
                Log.i("RES", "= " + item);
                Log.i("RES", "= "+ nome);
                AsyncCategoria async= new AsyncCategoria(nome, item, this);
                async.execute();
                txtNome.setText("");


            }else if(item.equals("Despesa")){
                //Toast.makeText(this, "DESPESA", Toast.LENGTH_LONG).show();
                Log.i("RES", "= " + item);
                Log.i("RES", "= "+ nome);
                AsyncCatDespesa async= new AsyncCatDespesa(nome, item, this);
                async.execute();
                txtNome.setText("");

            }else{
                AsyncCatConta async= new AsyncCatConta(nome, this);
                async.execute();
                txtNome.setText("");
            }
        }

    }
}
