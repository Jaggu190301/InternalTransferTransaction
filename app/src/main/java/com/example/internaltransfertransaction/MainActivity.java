package com.example.internaltransfertransaction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText date, coilID, tonnage, pageRefNo, remarks;

    private MaterialAutoCompleteTextView  from, to, driverNameAutoCompleteTextView, VehicleNumAutoComplete;

    private Calendar calendar;

    private LinearLayout containerLayout;

    private List<String> driverNames = new ArrayList<>();
    private List<String> VehicleNo = new ArrayList<>();
    private List<String> Location=new ArrayList<>();

    private Map<String, Integer> driverIds = new HashMap<>();
    private Map<String, Integer> vehicleIds = new HashMap<>();
    private Button submitButton;

    private Button RowAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        driverNameAutoCompleteTextView = findViewById(R.id.DriverName);
        VehicleNumAutoComplete = findViewById(R.id.VehicleNo);
        date = findViewById(R.id.date);
        calendar = Calendar.getInstance();
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        coilID = findViewById(R.id.CoilID);
        tonnage = findViewById(R.id.tonnage);
        pageRefNo = findViewById(R.id.PageRefNo);
        remarks = findViewById(R.id.Remarks);



        submitButton = findViewById(R.id.Submit);
        RowAdd = findViewById(R.id.Add);
        containerLayout = findViewById(R.id.containerLayout);

        fetchDriverNames();

        fetchVehicleNo();

        fetchLocation();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitButton.setEnabled(false);
                displayInputValues();
            }
        });

        RowAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewSetOfFields();
            }
        });


    }

    private void fetchLocation(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://external.balajitransports.in/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService=retrofit.create(ApiService.class);
        Call<List<Location>>call=apiService.getLocation();

        call.enqueue(new Callback<List<com.example.internaltransfertransaction.Location>>() {
            @Override
            public void onResponse(Call<List<com.example.internaltransfertransaction.Location>> call, Response<List<com.example.internaltransfertransaction.Location>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Location location : response.body()) {
                        Location.add(location.getLocation());
                    }

                    // Set up ArrayAdapter for the MaterialAutoCompleteTextView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            MainActivity.this, android.R.layout.simple_dropdown_item_1line, Location
                    );

                    // Set the adapter for the MaterialAutoCompleteTextView
                    from.setAdapter(adapter);
                    to.setAdapter(adapter);
                } else {
                    Log.e("MainActivity", "Failed to fetch driver names from API");
                }
            }

            @Override
            public void onFailure(Call<List<com.example.internaltransfertransaction.Location>> call, Throwable t) {
                Log.e("MainActivity", "API request failed to Get Location", t);
            }
        });
    }

    private void fetchVehicleNo(){
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://external.balajitransports.in/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService=retrofit.create(ApiService.class);
        Call<List<Vehicle>>call=apiService.getVehicle();

        call.enqueue(new Callback<List<Vehicle>>() {
            @Override
            public void onResponse(Call<List<Vehicle>> call, Response<List<Vehicle>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Vehicle vehicle : response.body()) {
                        VehicleNo.add(vehicle.getVehicle());

                        // Store the vehicle ID for later use in the POST request
                        vehicleIds.put(vehicle.getVehicle(), vehicle.getVehicleID());
                    }

                    // Set up ArrayAdapter for the MaterialAutoCompleteTextView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            MainActivity.this, android.R.layout.simple_dropdown_item_1line, VehicleNo
                    );

                    // Set the adapter for the MaterialAutoCompleteTextView
                    VehicleNumAutoComplete.setAdapter(adapter);
                } else {
                    Log.e("MainActivity", "Failed to fetch driver names from API");
                }
            }

            @Override
            public void onFailure(Call<List<Vehicle>> call, Throwable t) {
                Log.e("MainActivity", "API request failed to get Vehicle", t);
            }
        });
    }

    private void fetchDriverNames(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://external.balajitransports.in/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService=retrofit.create(ApiService.class);
        Call<List<Driver>>call=apiService.getDrivers();

        call.enqueue(new Callback<List<Driver>>() {
            @Override
            public void onResponse(Call<List<Driver>> call, Response<List<Driver>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Driver driver : response.body()) {
                        String combinedName = driver.getName() + " - " + getLastFourDigits(driver.getDLNo());
                        driverNames.add(combinedName);

                        driverIds.put(combinedName, driver.getDriverID());
                    }

                    // Set up ArrayAdapter for the MaterialAutoCompleteTextView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            MainActivity.this, android.R.layout.simple_dropdown_item_1line, driverNames
                    );

                    // Set the adapter for the MaterialAutoCompleteTextView
                    driverNameAutoCompleteTextView.setAdapter(adapter);
                } else {
                    Log.e("MainActivity", "Failed to fetch driver names from API"+response.code() + ", Message: " + response.message());


                }
            }

            @Override
            public void onFailure(Call<List<Driver>> call, Throwable t) {
                Log.e("MainActivity", "API request failed to get DriverName", t);
            }
        });
    }

    private String getLastFourDigits(String input){
        if (input != null && input.length() >= 4) {
            return input.substring(input.length() - 4);
        }
        return input;
    }



    public  void displayInputValues(){
        String selectedDriverName = driverNameAutoCompleteTextView.getText().toString();
        String dateValue = date.getText().toString();
        String VehicleNo = VehicleNumAutoComplete.getText().toString();
        String fromValue = from.getText().toString();
        String toValue = to.getText().toString();
        String coilIDValue = coilID.getText().toString();
        String tonnageValue = tonnage.getText().toString();
        String pageRefNoValue = pageRefNo.getText().toString();
        String remarksValue = remarks.getText().toString();

        String driverId = String.valueOf(driverIds.get(selectedDriverName));
        String vehicleId =  String.valueOf(vehicleIds.get(VehicleNo));

        // Create Retrofit instance for the submit API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://external.balajitransports.in/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ApiService apiService = retrofit.create(ApiService.class);
        PostDataModel postDataModel=new PostDataModel(driverId,dateValue,vehicleId,fromValue,toValue,coilIDValue,tonnageValue,pageRefNoValue,remarksValue);

        // Make the POST request
       Call<ResponseBody> call=apiService.postData(postDataModel);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                submitButton.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String successMessage = response.body().string();
                        Log.d("MainActivity", "Response: " + successMessage);
                        Toast.makeText(MainActivity.this,successMessage,Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("MainActivity", "Failed to get a successful response");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                submitButton.setEnabled(true);

                Log.e("MainActivity", "Failed to make a POST request", t);

            }
        });

        // Display values in the log
        Log.d("MainActivity", "Driver Name: " + selectedDriverName);
        Log.d("MainActivity", "Date: " + dateValue);
        Log.d("MainActivity", "Date: " + VehicleNo);
        Log.d("MainActivity", "From: " + fromValue);
        Log.d("MainActivity", "To: " + toValue);
        Log.d("MainActivity", "Coil ID: " + coilIDValue);
        Log.d("MainActivity", "Tonnage: " + tonnageValue);
        Log.d("MainActivity", "Page Ref No: " + pageRefNoValue);
        Log.d("MainActivity", "Remarks: " + remarksValue);

        driverNameAutoCompleteTextView.setText("");
        date.setText("");
        VehicleNumAutoComplete.setText("");
        from.setText("");
        to.setText("");
        coilID.setText("");
        tonnage.setText("");
        pageRefNo.setText("");
        remarks.setText("");
    }

    private void addNewSetOfFields(){
        for (int i = 0; i < 4; i++) {
            // Create TextInputLayout

            TextInputLayout textInputLayout = new TextInputLayout(this);
            LinearLayout.LayoutParams textInputLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textInputLayoutParams.setMargins(10, 0, 10, 0);
            textInputLayout.setLayoutParams(textInputLayoutParams);

            // Create TextInputEditText
            TextInputEditText textInputEditText = new TextInputEditText(this);
            textInputEditText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT);

            // Set hint based on the iteration
            switch (i) {
                case 0:
                    textInputLayout.setHint("CoilID");
                    textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_book_24, 0);
                    textInputEditText.setId(View.generateViewId());
                    break;
                case 1:
                    textInputLayout.setHint("PageRefNo");
                    textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_book_24, 0);
                    textInputEditText.setId(View.generateViewId());
                    break;
                case 2:
                    textInputLayout.setHint("Tonnage");
                    textInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_book_24, 0);
                    textInputEditText.setId(View.generateViewId());
                    break;
                case 3:
                    textInputLayout.setHint("Remarks");
                    textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_book_24, 0);
                    textInputEditText.setId(View.generateViewId());
                    break;
            }

            // Add TextInputEditText to TextInputLayout
            textInputLayout.addView(textInputEditText);

            // Add TextInputLayout to the container layout
            containerLayout.addView(textInputLayout);
        }

    }

    public void showDatePicker(View view) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        calendar.set(Calendar.YEAR, selectedYear);
                        calendar.set(Calendar.MONTH, selectedMonth);
                        calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                        updateDateEditText();
                    }
                },
                year, month, dayOfMonth);

        datePickerDialog.show();
    }

    // Helper method to update the date TextInputEditText with the selected date
    private void updateDateEditText() {
        String myFormat = "yyyy/MM/dd"; // Choose the desired date format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(calendar.getTime()));
    }

    private void handleErrorResponse(Response<ApiResponse> response) {
        try {
            String errorBody = response.errorBody().string();
            Log.e("MainActivity", "Response Error: " + errorBody);
            Toast.makeText(MainActivity.this, "Failed to submit data. Check logs for details.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading error response", e);
            Toast.makeText(MainActivity.this, "Failed to read error response", Toast.LENGTH_SHORT).show();
        }
    }

    }