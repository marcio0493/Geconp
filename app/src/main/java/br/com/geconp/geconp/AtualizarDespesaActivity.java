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

import br.com.geconp.geconp.async.AsyncAtualizaDespesa;
import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.Despesa;


public class AtualizarDespesaActivity extends Activity {
    private ProgressDialog carregando;
    private EditText txtValor, txtData, txtDescricao;
    Spinner spinnerTipoDespesa, spinnerContaDespesa;
    static final int DATE_DIALOG_ID = 0;
    private String data;
    private String itemTipo, itemConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_despesa);

        spinnerTipoDespesa= (Spinner)findViewById(R.id.spinnerTipoAttDes);
        spinnerContaDespesa= (Spinner) findViewById(R.id.spinnerContaAttDes);

        loadSpinnerData();
        loadSpinnerConta();

        itemSelecionado(spinnerTipoDespesa, spinnerContaDespesa);
        itemTipo=spinnerTipoDespesa.getSelectedItem().toString();
        itemConta=spinnerContaDespesa.getSelectedItem().toString();

        txtData = (EditText) findViewById(R.id.editTextAttDataDes);
        txtValor= (EditText)findViewById(R.id.editTextAtValDes);
        txtDescricao= (EditText)findViewById(R.id.editTextAttDescDes);

        Intent origem= getIntent();
        final String idPag= origem.getStringExtra("ID");
        String valorPagado= origem.getStringExtra("VALOR");
        String dataPagado= origem.getStringExtra("DATA");
        String descPagado= origem.getStringExtra("DESCRICAO");
        txtValor.setText(valorPagado);
        txtData.setText(dataPagado);
        txtDescricao.setText(descPagado);

        Button btn= (Button)findViewById(R.id.buttonAttDespesa);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gravar(idPag);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        txtData = (EditText) findViewById(R.id.editTextAttDataDes);
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


    public void itemSelecionado(final Spinner spinnerTipo, final Spinner spinnerConta){
        Log.i("TAG2", "selecionado");
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemTipo= parent.getItemAtPosition(position).toString();
                Log.i("TAG2", "pegou valor" + itemTipo);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerConta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemConta= parent.getItemAtPosition(position).toString();
                Log.i("TAG2", "pegou valor" + itemConta);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void loadSpinnerData(){
        DatabaseManager manager= new DatabaseManager(getApplicationContext());
        List<String> labels= manager.listarCategoriasDespesa();
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDespesa.setAdapter(adapter);

    }

    public void loadSpinnerConta(){
        DatabaseManager manager= new DatabaseManager(getApplicationContext());
        List<String> labels= manager.listarConta();
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerContaDespesa.setAdapter(adapter);

    }

    public void gravar(String idPag) {
        if (txtValor.getText().toString().equals(".00") || txtValor.getText().equals("") || txtData.getText().equals("") || txtDescricao.getText().equals("")) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.cuidado);
            dialog.setIcon(R.drawable.ic_action_warning);
            dialog.setMessage(R.string.campoVazio);
            dialog.setNeutralButton("Ok", null);
            dialog.show();
        } else {
            String aux1 = txtValor.getText().toString();
            String aux2 = aux1.replace(",", ".");

            float valor = Float.parseFloat(aux2);

            String d = txtData.getText().toString();
            String descricao = txtDescricao.getText().toString();
            long id= Long.parseLong(idPag);

            Despesa despesa= new Despesa(id, valor, d, descricao, itemTipo, itemConta);
            //new AsyncAtu(despesa).execute();
            DatabaseManager databaseManager= new DatabaseManager(AtualizarDespesaActivity.this);
            databaseManager.atualizarDespesa(despesa);
            limparCampos();
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

    public void limparCampos(){
        txtValor.setText(".00");
        txtData.setText("");
        txtDescricao.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_atualizar_despesa, menu);
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
        Despesa despesa;

        private AsyncAtu(Despesa despesa) {
            this.despesa = despesa;
        }

        @Override
        protected Void doInBackground(Void... params) {
            DatabaseManager databaseManager= new DatabaseManager(AtualizarDespesaActivity.this);
            databaseManager.atualizarDespesa(this.despesa);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            carregando= new ProgressDialog(AtualizarDespesaActivity.this);
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
