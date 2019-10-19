package com.casper.testdrivendevelopment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewBookActivity extends AppCompatActivity {

    private EditText editTextBookTitle,editTextBookPrice;
    private Button buttonOk,buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);

        editTextBookTitle=findViewById(R.id.edit_text_book_title);
        editTextBookPrice=findViewById(R.id.edit_text_book_price);
        buttonOk=findViewById(R.id.button_ok);
        buttonCancel=findViewById(R.id.button_cancel);

        final int insertPosition=getIntent().getIntExtra("position",0);
        String book_title=getIntent().getStringExtra("book_title");
        double book_price=getIntent().getDoubleExtra("book_price",0);
        if(book_title!=null){
            editTextBookTitle.setText(book_title);
            editTextBookPrice.setText(book_price+"");
        }

        buttonOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.putExtra("position",insertPosition);
                intent.putExtra("book_title",editTextBookTitle.getText().toString().trim());
                intent.putExtra("book_price",Double.parseDouble(editTextBookPrice.getText().toString()));
                setResult(RESULT_OK,intent);
                NewBookActivity.this.finish();
            }
        });
        buttonCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NewBookActivity.this.finish();
            }
        });
    }
}
