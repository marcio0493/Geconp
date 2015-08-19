package br.com.geconp.geconp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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

import br.com.geconp.geconp.async.AsyncDespesa;
import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.Despesa;


public class DespesaActivity extends Activity {
    private String itemTipo, itemConta;
    private EditText txtValor, txtData, txtDescricao;
    private  Spinner spinnerTipoDespesa, spinnerContaDespesa;
    static final int DATE_DIALOG_ID = 0;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        //coloca o spinner para tipos
        spinnerTipoDespesa= (Spinner)findViewById(R.id.spinnerTipoDespesa);
        //coloca spinner para conta
        spinnerContaDespesa= (Spinner)findViewById(R.id.spinnerContaDespesa);
        loadSpinnerDataDespesa();
        loadSpinnerDataConta();

        itemSelecionado(spinnerTipoDespesa, spinnerContaDespesa);
        itemTipo=spinnerTipoDespesa.getSelectedItem().toString();
        itemConta=spinnerContaDespesa.getSelectedItem().toString();

        Button adicionar= (Button) findViewById(R.id.buttonAddDespesa);
        adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG2", "clicado");
                Log.i("TAG2", "Tipo: " + itemTipo);
                Log.i("TAG2", "Conta: " + itemConta);
                gravar(itemTipo, itemConta);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        txtData = (EditText) findViewById(R.id.editTextDataDespesa);

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

    public void loadSpinnerDataDespesa(){
        DatabaseManager manager= new DatabaseManager(getApplicationContext());
        List<String> labels= manager.listarCategoriasDespesa();
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDespesa.setAdapter(adapter);
    }

    public void loadSpinnerDataConta(){
        DatabaseManager manager= new DatabaseManager(getApplicationContext());
        List<String> labels= manager.listarConta();
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerContaDespesa.setAdapter(adapter);
    }


    public void gravar(final String itemTipo, final String itemConta){
        txtValor = (EditText) findViewById(R.id.editTextValorDespesa);
        txtDescricao = (EditText) findViewById(R.id.editTextDescricaoDespesa);

        if(txtValor.getText().toString().equals(".00") || txtValor.getText().toString().equals("") || txtData.getText().toString().equals("") || txtDescricao.getText().toString().equals("")){
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle(R.string.cuidado);
            dialog.setIcon(R.drawable.ic_action_warning);
            dialog.setMessage(R.string.campoVazio);
            dialog.setNeutralButton("Ok", null);
            dialog.show();
        }else{
            String aux1= txtValor.getText().toString();
            String aux2= aux1.replace(",", ".");

            float valor= Float.parseFloat(aux2);

            String d= txtData.getText().toString();
            String descricao= txtDescricao.getText().toString();
            //String[] param= d.split("/");
            //String[] param= d.split(Pattern.quote("/"));
            String um= d.substring(1);
            String dois= um.substring(1,2);
            //String tres= d.substring(5,8);

            Log.i("PRIMEIRO", "String 1" + um);
            //Log.i("SEGUNDO", "String 2" + dois);
            //Log.i("TERCEIRO", "String 3" + tres);
            limparCampos();

            Despesa despesa= new Despesa(valor, d, descricao, itemTipo, itemConta);
            //DatabaseManager databaseManager= new DatabaseManager(DespesaActivity.this);
            //databaseManager.cadastrarDespesa(despesa);

            AsyncDespesa asyn= new AsyncDespesa(despesa, this);
            asyn.execute();

        }

    }

    public void limparCampos(){
        txtValor.setText(".00");
        txtData.setText("");
        txtDescricao.setText("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_despesa, menu);
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
