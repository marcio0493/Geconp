package br.com.geconp.geconp;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.Receita;

public class BuscaReceitaActivity extends Activity {
    private static final String[] opcoes={"Categoria", "Conta", "Data", "Valor"};
    private String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_receita);

        Spinner spinner= (Spinner)findViewById(R.id.spinnerBuscaDadoConReceita);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opcoes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        itemSelecionado(spinner);
        item= spinner.getSelectedItem().toString();

        ImageButton button= (ImageButton)findViewById(R.id.imageButtonBuscaConRec);
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
        EditText txtBusca= (EditText)findViewById(R.id.editTextDadoBuscaReceita);
        if(txtBusca.getText().toString().equals("")){
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle(R.string.cuidado);
            dialog.setIcon(R.drawable.ic_action_warning);
            dialog.setMessage(R.string.campoVazio);
            dialog.setNeutralButton("Ok", null);
            dialog.show();
        }else{
            String dado= txtBusca.getText().toString();
            TextView textBusca= (TextView)findViewById(R.id.txtViewBusConRec);
            textBusca.setText("");
            DatabaseManager manager;
            List<Receita> busca;
            StringBuilder sb;
            if(item.equals("Categoria")){
                manager= new DatabaseManager(this);
                busca= manager.buscaCategoria(dado);
                sb= new StringBuilder();
                for(Receita receita: busca){
                    sb.append(String.valueOf(receita)).append("\n");
                }

                textBusca.setText(sb.toString());

                textBusca.setVisibility(View.VISIBLE);
            }else if(item.equals("Conta")){
                manager= new DatabaseManager(this);
                busca= manager.buscaConta(dado);
                sb= new StringBuilder();

                for(Receita receita: busca){
                    sb.append(String.valueOf(receita)).append("\n");
                }
                textBusca.setText(sb.toString());
                textBusca.setVisibility(View.VISIBLE);
            }else if(item.equals("Data")){

                manager= new DatabaseManager(this);
                busca= manager.buscaData(dado);
                sb= new StringBuilder();

                for(Receita receita: busca){
                    sb.append(String.valueOf(receita)).append("\n");
                }
                textBusca.setText(sb.toString());
                textBusca.setVisibility(View.VISIBLE);

            }else if(item.equals("Valor")){
                String aux1= txtBusca.getText().toString();
                String aux2= aux1.replace(",", ".");
                float valor= Float.parseFloat(aux2);

                manager= new DatabaseManager(this);
                busca= manager.buscaValor(valor);
                sb= new StringBuilder();

                for(Receita receita: busca){
                    sb.append(String.valueOf(receita)).append("\n");
                }
                textBusca.setText(sb.toString());
                textBusca.setVisibility(View.VISIBLE);

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_busca_receita, menu);
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
