package br.com.geconp.geconp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.List;

import br.com.geconp.geconp.async.AsyncAtualizaReceita;
import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.Receita;


public class AtualizarReceitaActivity extends Activity {
    private ProgressDialog carregando;
    private EditText txtValor, txtData, txtDescricao;
    Spinner spinner, spinnerConta;
    static final int DATE_DIALOG_ID = 0;
    private String data;
    private String itemTipo, itemConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_receita);

        spinner= (Spinner)findViewById(R.id.spinnerTipoAttRec);
        spinnerConta= (Spinner) findViewById(R.id.spinnerContaAttRec);

        loadSpinnerData();
        loadSpinnerConta();

        itemSelecionado(spinner, spinnerConta);
        itemTipo= spinner.getSelectedItem().toString();
        //colocar do outro spinner
        itemConta= spinnerConta.getSelectedItem().toString();

        txtData = (EditText) findViewById(R.id.editTextAttDataRec);
        txtValor= (EditText)findViewById(R.id.editTextAtValRec);
        txtDescricao= (EditText)findViewById(R.id.editTextAttDescRec);

        Intent origem= getIntent();
        final String idRec= origem.getStringExtra("ID");
        String valorRcebido= origem.getStringExtra("VALOR");
        String dataRecebido= origem.getStringExtra("DATA");
        String descRecebido= origem.getStringExtra("DESCRICAO");
        txtValor.setText(valorRcebido);
        txtData.setText(dataRecebido);
        txtDescricao.setText(descRecebido);


        Button add= (Button)findViewById(R.id.buttonAttReceita);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gravar(idRec);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        txtData = (EditText) findViewById(R.id.editTextAttDataRec);
        txtData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showDialog(DATE_DIALOG_ID);
                txtData.clearFocus();
            }
        });
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        Calendar calendar= Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, ano, mes, dia);
        }
        return null;

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            data = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1) + "/"
                    + String.valueOf(year);
            txtData.setText(data);

            Log.i("INFO", "DATA = " + data);
        }
    };


    public void itemSelecionado(final Spinner spinner, final Spinner spinnerConta){
        Log.i("TAG", "Selecionado");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemTipo = parent.getItemAtPosition(position).toString();
                Log.i("TAG", "pegar valor Tipo" + itemTipo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        spinnerConta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemConta = parent.getItemAtPosition(position).toString();
                Log.i("TAG", "pegar valor Conta" + itemConta);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void loadSpinnerData(){
        DatabaseManager manager= new DatabaseManager(getApplicationContext());
        List<String> labels= manager.listarCategoriasReceita();
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    public void loadSpinnerConta(){
        DatabaseManager manager= new DatabaseManager(getApplicationContext());
        List<String> labels= manager.listarConta();
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConta.setAdapter(adapter);

    }

    public void gravar(String idRec){
        if(txtValor.getText().toString().equals(".00") || txtValor.getText().toString().equals("") || txtData.getText().toString().equals("") || txtDescricao.getText().toString().equals("")){
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle(R.string.cuidado);
            dialog.setIcon(R.drawable.ic_action_warning);
            dialog.setMessage(R.string.campoVazio);
            dialog.setNeutralButton("Ok", null);
            dialog.show();
        }else{
            //Bloco de formatação do valor
            String aux1 = txtValor.getText().toString();
            String aux3= aux1.replace(",", ".");
            float valor = Float.parseFloat(aux3);

            String d = txtData.getText().toString();
            Log.i("INFO3", "Data Chegou aqui= " + d);
            String descricao = txtDescricao.getText().toString();

            long id= Long.parseLong(idRec);
            Receita receita= new Receita(id, valor, d, descricao, itemTipo, itemConta);
            //new AsyncAtu(receita).execute();
            DatabaseManager manager= new DatabaseManager(AtualizarReceitaActivity.this);
            manager.atualizarReceita(receita);

            limparCampos();
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle("Sucesso!");
            dialog.setIcon(R.drawable.ic_action_refresh);
            dialog.setMessage(R.string.mensagem_atualizar);
            dialog.setNeutralButton("Ok", null);
            dialog.show();


            Log.i("LOG", "LISTA 4");
            setResult(1, new Intent());
            Log.i("LOG", "LISTA 5");
            finish();
        }

    }

    public void limparCampos(){
        txtValor.setText(".00");
        txtData.setText("");
        txtDescricao.setText("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_atualizar_receita, menu);
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

    private class AsyncAtu extends AsyncTask<Void, Void, Void>{
        Receita receita;
        private AsyncAtu(Receita receita) {
                this.receita= receita;
        }

        @Override
        protected Void doInBackground(Void... params) {
            DatabaseManager manager= new DatabaseManager(AtualizarReceitaActivity.this);
            manager.atualizarReceita(this.receita);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            carregando= new ProgressDialog(AtualizarReceitaActivity.this);
            carregando.setTitle("Aguarde");
            carregando.setMessage("Atualizando dados...");
            carregando.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            carregando.dismiss();
        }
    }


}
