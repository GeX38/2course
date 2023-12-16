package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editTextDepartureDate, editTextArrivalDate;
    private Button btnSelectDepartureDate, btnSelectReturnDate, btnSearch;
    private TextView textViewSelectedDates;
    private Spinner spinnerDepartureCity, spinnerArrivalCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextDepartureDate = findViewById(R.id.editTextDepartureDate);
        editTextArrivalDate = findViewById(R.id.editTextArrivalDate);
        btnSelectDepartureDate = findViewById(R.id.btnSelectDepartureDate);
        btnSelectReturnDate = findViewById(R.id.btnSelectReturnDate);
        textViewSelectedDates = findViewById(R.id.textViewSelectedDates);
        btnSearch = findViewById(R.id.buttonSearch);
        spinnerDepartureCity = findViewById(R.id.spinnerDepartureCity);
        spinnerArrivalCity = findViewById(R.id.spinnerArrivalCity);

        btnSelectDepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editTextDepartureDate);
            }
        });

        btnSelectReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editTextArrivalDate);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDepartureCity.setAdapter(adapter);
        spinnerArrivalCity.setAdapter(adapter);

        spinnerDepartureCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                checkDepartureArrivalMatch();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        spinnerArrivalCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                checkDepartureArrivalMatch();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    performSearch();
                }
            }
        });
    }

    public void showDatePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                        String formattedDate = dateFormat.format(selectedDate.getTime());
                        editText.setText(formattedDate);
                    }
                },
                year, month, day
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void checkDepartureArrivalMatch() {
        if (spinnerDepartureCity.getSelectedItem() != null && spinnerArrivalCity.getSelectedItem() != null) {
            String departureCity = spinnerDepartureCity.getSelectedItem().toString();
            String arrivalCity = spinnerArrivalCity.getSelectedItem().toString();

            if (departureCity.equals(arrivalCity)) {
                Toast.makeText(this, "Город вылета не может совпадать с городом прилета", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInputs() {
        String departureDate = editTextDepartureDate.getText().toString();
        String arrivalDate = editTextArrivalDate.getText().toString();

        if (departureDate.isEmpty() || arrivalDate.isEmpty()) {
            Toast.makeText(this, "Введите дату вылета и прилета", Toast.LENGTH_SHORT).show();
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            Calendar departureCalendar = Calendar.getInstance();
            departureCalendar.setTime(dateFormat.parse(departureDate));

            Calendar arrivalCalendar = Calendar.getInstance();
            arrivalCalendar.setTime(dateFormat.parse(arrivalDate));

            if (arrivalCalendar.before(departureCalendar)) {
                Toast.makeText(this, "Дата прилета не может быть раньше даты вылета", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Некорректный формат даты", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void performSearch() {
        String departureCity = spinnerDepartureCity.getSelectedItem().toString();
        String arrivalCity = spinnerArrivalCity.getSelectedItem().toString();
        String departureDate = editTextDepartureDate.getText().toString();
        String arrivalDate = editTextArrivalDate.getText().toString();

        String searchResult = "Город вылета: " + departureCity + "\n" +
                "Город прилета: " + arrivalCity + "\n" +
                "Дата вылета: " + departureDate + "\n" +
                "Дата прилета: " + arrivalDate;

        textViewSelectedDates.setText(searchResult);
    }
}