package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.db.Todo;
import com.example.myapplication.db.TodoDao;
import com.example.myapplication.db.TodoDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateActivity extends AppCompatActivity implements LocationListener {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button buttonCreateTodo;
    private Button buttonSelectDate;
    private TextView textViewCurrentLocation;
    private String selectedDate = "";
    private String location = "";

    private TodoDao dao;
    private Calendar calendar;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        dao = TodoDatabase.getInstance(getApplicationContext()).getTodoDao();
        calendar = Calendar.getInstance();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonCreateTodo = findViewById(R.id.buttonCreateTodo);
        buttonSelectDate = findViewById(R.id.buttonSelectDate);
        textViewCurrentLocation = findViewById(R.id.textViewCurrentLocation);

        buttonCreateTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTodo();
            }
        });

        buttonSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        checkLocationPermissionAndRequestIfNeeded();
    }

    private void createTodo() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || selectedDate.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String date = dateFormat.format(calendar.getTime());

        Todo todo = new Todo(title, description, selectedDate, location);
        dao.insert(todo);

        Toast.makeText(this, "Todo created successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                selectedDate = dateFormat.format(calendar.getTime());

                buttonSelectDate.setText(selectedDate);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @SuppressLint("MissingPermission")
    private void checkLocationPermissionAndRequestIfNeeded() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        this.location = "Latitude: " + latitude + ", Longitude: " + longitude;
        textViewCurrentLocation.setText(this.location);
        getAddressFromLocation(latitude, longitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String street = address.getAddressLine(0);
                String locality = address.getLocality();
                String adminArea = address.getAdminArea();
                String country = address.getCountryName();
                location = street + ", " + locality + ", " + adminArea + ", " + country;
                textViewCurrentLocation.setText(location);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
