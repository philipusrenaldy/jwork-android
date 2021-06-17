package com.submission.jwork_android.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.submission.jwork_android.R;
import com.submission.jwork_android.request.ApplyJobRequest;
import com.submission.jwork_android.request.BonusRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ApplyJobActivity extends AppCompatActivity {
    private int jobseekerID;
    private int jobID;
    private String jobName;
    private String jobCategory;
    private double jobFee;

    ApplyJobRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_job);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jobseekerID = extras.getInt("jobseekerId");
            jobID = extras.getInt("job_id");
            jobName = extras.getString("job_name");
            jobCategory = extras.getString("job_category");
            jobFee = extras.getInt("job_fee");
        }

        EditText etRefCode = findViewById(R.id.referral_code);
        TextView tvRefCode = findViewById(R.id.textCode);
        TextView tvJobName = findViewById(R.id.job_name);
        TextView tvJobCategory = findViewById(R.id.job_category);
        TextView tvJobFee = findViewById(R.id.job_fee);
        TextView tvTotalFee = findViewById(R.id.total_fee);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Button btnCount = findViewById(R.id.btnCount);
        Button btnApply = findViewById(R.id.btnApply);

        btnApply.setVisibility(View.INVISIBLE);
        tvRefCode.setVisibility(View.INVISIBLE);
        etRefCode.setVisibility(View.INVISIBLE);

        tvJobName.setText(jobName);
        tvJobCategory.setText(jobCategory);
        tvJobFee.setText("Rp. " + jobFee);
        tvTotalFee.setText("Rp. 0");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = findViewById(checkedId);
                switch (checkedId) {
                    case R.id.ewallet:
                        tvRefCode.setVisibility(View.VISIBLE);
                        etRefCode.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bank:
                        tvRefCode.setVisibility(View.INVISIBLE);
                        etRefCode.setVisibility(View.INVISIBLE);
                        btnCount.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        btnCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedId = radioGroup.getCheckedRadioButtonId();
                switch (checkedId) {
                    case R.id.ewallet:
                        String refCode = etRefCode.getText().toString();
                        Response.Listener<String> bonusResponse = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (refCode.isEmpty()) {
                                    Toast.makeText(ApplyJobActivity.this, "No referral code applied!", Toast.LENGTH_SHORT).show();
                                    tvTotalFee.setText("Rp. " + jobFee);
                                } else {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        int extraFee = jsonResponse.getInt("extraFee");
                                        int minTotalFee = jsonResponse.getInt("minTotalFee");
                                        boolean bonusStatus = jsonResponse.getBoolean("active");

                                        if (!bonusStatus) {
                                            Toast.makeText(ApplyJobActivity.this, "This bonus is invalid!", Toast.LENGTH_SHORT).show();
                                        } else if (bonusStatus) {
                                            if (jobFee < extraFee || jobFee < minTotalFee) {
                                                Toast.makeText(ApplyJobActivity.this, "Referral code invalid!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(ApplyJobActivity.this, "Referral code applied!", Toast.LENGTH_SHORT).show();
                                                tvTotalFee.setText("Rp. " + (jobFee + extraFee));
                                                btnCount.setVisibility(View.INVISIBLE);
                                                btnApply.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(ApplyJobActivity.this, "Referral code not Exist!", Toast.LENGTH_SHORT).show();
                                        tvTotalFee.setText("Rp. " + jobFee);
                                    }
                                }

                            }
                        };
                        BonusRequest bonusRequest = new BonusRequest(refCode, bonusResponse);
                        RequestQueue queue = Volley.newRequestQueue(ApplyJobActivity.this);
                        queue.add(bonusRequest);
                        break;

                    case R.id.bank:
                        tvTotalFee.setText("Rp. " + jobFee);
                        btnCount.setVisibility(View.INVISIBLE);
                        btnApply.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioId = radioGroup.getCheckedRadioButtonId();
                ApplyJobRequest request = null;

                final Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                Toast.makeText(ApplyJobActivity.this, "Applied!", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(ApplyJobActivity.this, "Apply failed!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ApplyJobActivity.this, "Apply failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                if (selectedRadioId == R.id.bank) {
                    request = new ApplyJobRequest(String.valueOf(jobID), String.valueOf(jobseekerID), responseListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(ApplyJobActivity.this);
                    requestQueue.add(request);
                } else if (selectedRadioId == R.id.ewallet) {
                    String refCode = etRefCode.getText().toString();
                    request = new ApplyJobRequest(String.valueOf(jobID), String.valueOf(jobseekerID), refCode, responseListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(ApplyJobActivity.this);
                    requestQueue.add(request);
                }
            }
        });
    }
}