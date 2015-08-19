package br.com.geconp.geconp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;

import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.Poupanca;


public class AdicionarPoupancaActivity extends Activity {
    EditText txtBanco, txtValor, txtData;
    static final int DATE_DIALOG_ID = 0;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_poupanca);

        Button add= (Button)findViewById(R.id.buttonAddContaPoupanca);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gravar();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtData= (EditText)findViewById(R.id.editTextDataPoupanca);

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

    public void gravar(){
        txtBanco= (EditText)findViewById(R.id.editTextBancoPoupanca);
        txtValor=(EditText)findViewById(R.id.editTextValorPoupanca);
        if(txtBanco.getText().toString().equals("") || txtValor.getText().toString().equals(".00") || txtValor.getText().toString().equals("") ||
           txtData.getText().toString().equals("") ){
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle(R.string.cuidado);
            dialog.setIcon(R.drawable.ic_action_warning);
            dialog.setMessage(R.string.campoVazio);
            dialog.setNeutralButton("Ok", null);
            dialog.show();
        }else{
            String banco= txtBanco.getText().toString();
            String val= txtValor.getText().toString();
            String aux1= val.replace(",", ".");
            Log.i("NEW", "replace "+ aux1);

            String d= txtData.getText().toString();
            float valor= Float.parseFloat(aux1);
            Log.i("NEW", "Valor float " + valor);

            Poupanca poupanca= new Poupanca(banco, valor, d);
            DatabaseManager manager= new DatabaseManager(this);
            manager.addPoupanca(poupanca);
            txtBanco.setText("");
            txtValor.setText(".00");
            txtData.setText("");

            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle("Sucesso!");
            dialog.setIcon(R.drawable.ic_action_accept);
            dialog.setMessage(R.string.add_sucesso);
            dialog.setNeutralButton("Ok", null);
            dialog.show();
        }

    }
}
