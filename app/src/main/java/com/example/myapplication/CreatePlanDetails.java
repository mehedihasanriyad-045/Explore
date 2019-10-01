package com.example.myapplication;


        import androidx.appcompat.app.AppCompatActivity;

        import android.app.DatePickerDialog;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.DatePicker;
        import android.widget.EditText;

        import java.util.Calendar;

public class CreatePlanDetails extends AppCompatActivity {
    EditText tourDateField;
    DatePickerDialog.OnDateSetListener setListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan_details);
        tourDateField=(EditText)findViewById(R.id.tourDateField);
        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DATE);


        tourDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreatePlanDetails.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        tourDateField.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }
}