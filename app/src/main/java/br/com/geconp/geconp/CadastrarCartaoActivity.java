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

import br.com.geconp.geconp.async.AsyncCartao;
import br.com.geconp.geconp.model.Cartao;


public class CadastrarCartaoActivity extends Activity {
    private static final String[] bandeiras={"Agiplan","American Express", "Aura", "Calcard", "Cartão BNDES", "Credpar","Diners Club International", "Elo", "FlashCard", "Hipercard", "Master Card", "Master Card Maestro", "OuroCard", "PlenoCard", "Policard",
            "Praticard", "Sorocred","Ticket", "Unik","VerdeCard", "Visa", "Visa Electron", "Outro"};
    private String item;
    private EditText txtFatura, txtDataVenc;
    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID_DOIS=1;
    private String data, dataVenc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_cartao);

        Spinner spinner= (Spinner)findViewById(R.id.spinnerCartao);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, bandeiras);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        itemSelecionado(spinner);
        item= spinner.getSelectedItem().toString();

        Button btnCadastrar=(Button) findViewById(R.id.buttonAddCartao);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gravar(item);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        txtFatura= (EditText)findViewById(R.id.editTextFaturaCartao);
        txtDataVenc= (EditText)findViewById(R.id.editTextDataVenCartao);
        txtFatura.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showDialog(DATE_DIALOG_ID);
                txtFatura.clearFocus();
            }
        });

        txtDataVenc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showDialog(DATE_DIALOG_ID_DOIS);
                txtDataVenc.clearFocus();
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
            case DATE_DIALOG_ID_DOIS:
                return new DatePickerDialog(this, mDateSetListenerDois, ano, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            data = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1) + "/"
                    + String.valueOf(year);
            txtFatura.setText(data);
            Log.i("INFO", "DATA 1 = " + data);
        }
    };

    private DatePickerDialog.OnDateSetListener mDateSetListenerDois= new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dataVenc = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1) + "/"
                    + String.valueOf(year);
            txtDataVenc.setText(dataVenc);
            Log.i("INFO", "DATA 2 = " + dataVenc);
        }
    };


    public void gravar(String item){
        EditText txtValor=(EditText)findViewById(R.id.editTextValorCartao);
        EditText txtLimite= (EditText)findViewById(R.id.editTextLimiteCartao);
        EditText txtDescricao= (EditText)findViewById(R.id.editTextDescCartao);

        if(txtValor.getText().toString().equals(".00") || txtValor.getText().toString().equals("") || txtDescricao.getText().toString().equals("") ||
           txtLimite.getText().toString().equals(".00") || txtLimite.getText().toString().equals("") ||
           txtFatura.getText().toString().equals("")){
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle(R.string.cuidado);
            dialog.setIcon(R.drawable.ic_action_warning);
            dialog.setMessage(R.string.campoVazio);
            dialog.setNeutralButton("Ok", null);
            dialog.show();
        }else{
            String val= txtValor.getText().toString();
            float limite= Float.parseFloat(txtLimite.getText().toString());
            String fatura= txtFatura.getText().toString();
            String vencimento= txtDataVenc.getText().toString();
            String aux1= val.replace(",", ".");
            float valor= Float.parseFloat(aux1);
            String descricao= txtDescricao.getText().toString();
            Log.i("RES", "= " + descricao);

            Cartao cartao= new Cartao(valor, descricao, item, limite, fatura, vencimento);
            AsyncCartao async= new AsyncCartao(cartao, this);
            async.execute();
            txtValor.setText(".00");
            txtLimite.setText(".00");
            txtFatura.setText("");
            txtDataVenc.setText("");
            txtDescricao.setText("");

        }

    }

    public void itemSelecionado(Spinner spinner){
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastrar_cartao, menu);
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
