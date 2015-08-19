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

import br.com.geconp.geconp.async.AsyncAtMovRec;
import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.MovimentoReceita;


public class AttMovReceitaActivity extends Activity {
    private ProgressDialog carregando;
    private String itemTipo, itemConta;
    private EditText txtData, txtValor, txtDescricao;
    static final int DATE_DIALOG_ID = 0;
    private String data;
    private Spinner spCat;
    private Spinner spCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_att_mov_receita);

        spCat= (Spinner)findViewById(R.id.spinnerAttTipoMoRec);
        spCon= (Spinner) findViewById(R.id.spinnerAttConMoRec);

        loadSpinnerData();
        loadSpinnerConta();

        itemSelecionado(spCat, spCon);
        itemTipo= spCat.getSelectedItem().toString();
        //colocar do outro spinner
        itemConta= spCon.getSelectedItem().toString();

        txtData = (EditText) findViewById(R.id.editTextAttDatMovRec);
        txtValor= (EditText)findViewById(R.id.editTextAttValMovRec);
        txtDescricao= (EditText)findViewById(R.id.editTextAttDesMoRec);

        Intent origem= getIntent();
        final String idRec= origem.getStringExtra("ID");
        String valorRecebido= origem.getStringExtra("VALOR");
        String dataRecebido= origem.getStringExtra("DATA");
        String descRecebido= origem.getStringExtra("DESCRICAO");
        txtValor.setText(valorRecebido);
        txtData.setText(dataRecebido);
        txtDescricao.setText(descRecebido);

        Button add= (Button)findViewById(R.id.buttonAttMovRec);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gravar(idRec, itemTipo, itemConta);
            }
        });


    }

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
        spCat.setAdapter(adapter);

    }

    public void loadSpinnerConta(){
        DatabaseManager manager= new DatabaseManager(getApplicationContext());
        List<String> labels= manager.listarConta();
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCon.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        txtData = (EditText) findViewById(R.id.editTextAttDatMovRec);
        txtData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showDialog(DATE_DIALOG_ID);
                txtData.clearFocus();
            }
        });

    }

    private void gravar(String idRec, final String itemTipo, final String itemConta) {
        if(txtValor.getText().toString().equals(".00") || txtValor.getText().toString().equals("") ||
                txtData.getText().toString().equals("") || txtDescricao.getText().toString().equals("")){
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
            MovimentoReceita movimento= new MovimentoReceita(id, valor, d, descricao, itemTipo, itemConta);
            //new AsyncAtu(movimento).execute();

            DatabaseManager manager= new DatabaseManager(AttMovReceitaActivity.this);
            manager.atualizarMovReceita(movimento);

            txtValor.setText("");
            txtData.setText("");
            txtDescricao.setText("");
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle("Sucesso!");
            dialog.setIcon(R.drawable.ic_action_refresh);
            dialog.setMessage(R.string.mensagem_atualizar);
            dialog.setNeutralButton("Ok", null);
            dialog.show();

            setResult(1, new Intent());

            finish();

        }

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_att_mov_receita, menu);
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
        MovimentoReceita movimento;

        private AsyncAtu(MovimentoReceita movimento) {
            this.movimento = movimento;
        }


        @Override
        protected Void doInBackground(Void... params) {
            DatabaseManager manager= new DatabaseManager(AttMovReceitaActivity.this);
            manager.atualizarMovReceita(this.movimento);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            carregando= new ProgressDialog(AttMovReceitaActivity.this);
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
