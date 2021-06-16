package com.submission.jwork_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelesaiJobActivity extends AppCompatActivity {

    TextView staticInvoiceId, staticJobseeker, staticInvoiceDate, staticPaymentType, staticInvoiceStatus, staticReferralCode, staticJob, staticTotalFee, staticJobName;
    TextView tvJobseekerName, tvInvoiceDate, tvPaymentType, tvInvoiceStatus, tvRefCode, tvJobFee, tvTotalFee;
    Button btnCancel, btnFinish;
    View viewAtas, viewBawah;

    private static int jobSeekerId;
    private int jobSeekerInvoiceId;
    private String date;
    private String paymentType;
    private int totalFee;
    private static String jobSeekerName;
    private static String jobName;
    private static int jobFee;
    private String invoiceStatus;
    private String refCode;
    private JSONObject bonus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selesai_job);

        staticInvoiceId = findViewById(R.id.invoice_id);
        staticJobseeker = findViewById(R.id.static_jobseeker);
        staticInvoiceDate = findViewById(R.id.staticInvoiceDate);
        staticPaymentType = findViewById(R.id.staticPaymentType);
        staticInvoiceStatus = findViewById(R.id.staticInvoiceStatus);
        staticReferralCode = findViewById(R.id.staticReferralCode);
        staticJob = findViewById(R.id.staticJob);
        staticJobName = findViewById(R.id.jobName);
        staticTotalFee = findViewById(R.id.static_total_fee);
        tvJobseekerName = findViewById(R.id.jobseeker_name);
        tvInvoiceDate = findViewById(R.id.invoice_date);
        tvPaymentType = findViewById(R.id.payment_type);
        tvInvoiceStatus = findViewById(R.id.invoice_status);
        tvRefCode = findViewById(R.id.refCode);
        tvJobFee = findViewById(R.id.jobFee);
        tvTotalFee = findViewById(R.id.totalFee);
        btnCancel = findViewById(R.id.btnCancel);
        btnFinish = findViewById(R.id.btnFinish);
        viewAtas = findViewById(R.id.viewBatasAtas);
        viewBawah = findViewById(R.id.viewBatasBawah);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jobSeekerId = extras.getInt("jobseekerid");
        }

        staticInvoiceId.setText("There are no ongoing orders");
        staticJobseeker.setVisibility(View.INVISIBLE);
        staticInvoiceDate.setVisibility(View.INVISIBLE);
        staticPaymentType.setVisibility(View.INVISIBLE);
        staticInvoiceStatus.setVisibility(View.INVISIBLE);
        staticReferralCode.setVisibility(View.INVISIBLE);
        staticJob.setVisibility(View.INVISIBLE);
        staticJobName.setVisibility(View.INVISIBLE);
        staticTotalFee.setVisibility(View.INVISIBLE);
        tvJobseekerName.setVisibility(View.INVISIBLE);
        tvInvoiceDate.setVisibility(View.INVISIBLE);
        tvPaymentType.setVisibility(View.INVISIBLE);
        tvInvoiceStatus.setVisibility(View.INVISIBLE);
        tvRefCode.setVisibility(View.INVISIBLE);
        tvJobFee.setVisibility(View.INVISIBLE);
        tvTotalFee.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
        btnFinish.setVisibility(View.INVISIBLE);
        viewAtas.setVisibility(View.INVISIBLE);
        viewBawah.setVisibility(View.INVISIBLE);

        fetchJob();
        buttonSelect();
    }

    private void fetchJob() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.isEmpty()) {
                    Toast.makeText(SelesaiJobActivity.this, "No job applied!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SelesaiJobActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    staticInvoiceId.setText("Invoice ID: ");
                    staticJobseeker.setVisibility(View.VISIBLE);
                    staticInvoiceDate.setVisibility(View.VISIBLE);
                    staticPaymentType.setVisibility(View.VISIBLE);
                    staticInvoiceStatus.setVisibility(View.VISIBLE);
                    staticReferralCode.setVisibility(View.VISIBLE);
                    staticJob.setVisibility(View.VISIBLE);
                    staticJobName.setVisibility(View.VISIBLE);
                    staticTotalFee.setVisibility(View.VISIBLE);
                    tvJobseekerName.setVisibility(View.VISIBLE);
                    tvInvoiceDate.setVisibility(View.VISIBLE);
                    tvPaymentType.setVisibility(View.VISIBLE);
                    tvInvoiceStatus.setVisibility(View.VISIBLE);
                    tvRefCode.setVisibility(View.VISIBLE);
                    tvJobFee.setVisibility(View.VISIBLE);
                    tvTotalFee.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                    btnFinish.setVisibility(View.VISIBLE);
                    viewAtas.setVisibility(View.VISIBLE);
                    viewBawah.setVisibility(View.VISIBLE);
                    tvJobseekerName.setText(jobSeekerName);
                }
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    for (int i = 0; i < jsonResponse.length(); i++) {
                        JSONObject jsonInvoice = jsonResponse.getJSONObject(i);
                        invoiceStatus = jsonInvoice.getString("invoiceStatus");
                        jobSeekerInvoiceId = jsonInvoice.getInt("id");
                        date = jsonInvoice.getString("date");
                        paymentType = jsonInvoice.getString("paymentType");
                        totalFee = jsonInvoice.getInt("totalFee");
                        refCode = "---";
                        try {
                            bonus = jsonInvoice.getJSONObject("bonus");
                            refCode = bonus.getString("referralCode");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        staticInvoiceId.setText("Invoice ID: "  + jobSeekerInvoiceId);
                        tvInvoiceDate.setText(date.substring(0, 10));
                        tvPaymentType.setText(paymentType);
                        tvTotalFee.setText(String.valueOf(totalFee));
                        tvInvoiceStatus.setText(invoiceStatus);
                        tvRefCode.setText(refCode);

                        JSONObject jsonCustomer = jsonInvoice.getJSONObject("jobseeker");
                        jobSeekerName = jsonCustomer.getString("name");
                        tvJobseekerName.setText(jobSeekerName);

                        JSONArray jsonJobs = jsonInvoice.getJSONArray("jobs");
                        for (int j = 0; j < jsonJobs.length(); j++) {
                            JSONObject jsonJobObj = jsonJobs.getJSONObject(j);
                            jobName = jsonJobObj.getString("name");
                            staticJobName.setText(jobName);
                            jobFee = jsonJobObj.getInt("fee");
                            tvJobFee.setText(String.valueOf(jobFee));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        JobFetchRequest fetchRequest = new JobFetchRequest(String.valueOf(jobSeekerId), responseListener);
        RequestQueue queue = Volley.newRequestQueue(SelesaiJobActivity.this);
        queue.add(fetchRequest);
    }

    private void buttonSelect() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> cancelListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Intent intent = new Intent(SelesaiJobActivity.this, MainActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Toast.makeText(SelesaiJobActivity.this, "Your job has been canceled!", Toast.LENGTH_LONG).show();
                JobBatalRequest batalRequest = new JobBatalRequest(String.valueOf(jobSeekerInvoiceId), cancelListener);
                RequestQueue queue = Volley.newRequestQueue(SelesaiJobActivity.this);
                queue.add(batalRequest);
            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> doneListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Intent intent = new Intent(SelesaiJobActivity.this, MainActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Toast.makeText(SelesaiJobActivity.this, "Your job is finished!", Toast.LENGTH_LONG).show();
                JobSelesaiRequest selesaiRequest = new JobSelesaiRequest(String.valueOf(jobSeekerInvoiceId), doneListener);
                RequestQueue queue = Volley.newRequestQueue(SelesaiJobActivity.this);
                queue.add(selesaiRequest);
            }
        });
    }
}