package br.com.geconp.geconp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.Poupanca;


public class RetirarPoupancaActivity extends Activity {
    static final int DATE_DIALOG_ID = 0;
    private String data;
    private EditText txtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retirar_poupanca);

        Intent origem= getIntent();
        final String txtId= origem.getStringExtra("ID3");
        final String txtValor= origem.getStringExtra("VALOR3");
        final String txtBanco= origem.getStringExtra("BANCO3");

        txtData= (EditText)findViewById(R.id.editTextDataRetirado);
        txtData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showDialog(DATE_DIALOG_ID);
            }
        });


        Button btnRetirar=(Button)findViewById(R.id.buttonDiminuirPoupanca);
        btnRetirar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retirar(txtId, txtValor, txtBanco);
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

    public void retirar(String txtId, String txtValor, String txtBanco){
        EditText txtRetirado= (EditText)findViewById(R.id.editTextValorRetiradoPou);

        if(txtRetirado.getText().toString().equals(".00") || txtRetirado.getText().toString().equals("") || txtData.getText().toString().equals("")){
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle(R.string.cuidado);
            dialog.setIcon(R.drawable.ic_action_warning);
            dialog.setMessage(R.string.campoVazio);
            dialog.setNeutralButton("Ok", null);
            dialog.show();
        }else{
            String aux1= txtRetirado.getText().toString();
            String aux2= aux1.replace(",", ".");

            float v= Float.parseFloat(txtValor);
            float valorRetirado= Float.parseFloat(aux2);
            float subtrair= v - valorRetirado;
            long id= Long.parseLong(txtId);

            DatabaseManager manager= new DatabaseManager(this);
            Poupanca poupanca= new Poupanca(id, subtrair);
            manager.atualizarPoupanca(poupanca);
            manager.historicoRetirado(id,valorRetirado, txtBanco, data);
            manager.close();
            txtRetirado.setText(".00");
            txtData.setText("");

            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle("Sucesso!");
            dialog.setIcon(R.drawable.ic_action_accept);
            dialog.setMessage(R.string.mensagem_retirar);
            dialog.setNeutralButton("Ok", null);
            dialog.show();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_retirar_poupanca, menu);
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
