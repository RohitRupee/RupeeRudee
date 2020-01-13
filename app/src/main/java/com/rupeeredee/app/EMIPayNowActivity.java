package com.rupeeredee.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import ccavconstants.AvenuesParams;
import ccavconstants.ServiceUtility;
import connectivity.ConnectivityReceiver;
import customtools.LightTextView;
import customtools.RegularTextView;
import model.PayNow;
import model.UserRecords;
import services.ApiConstants;
import services.ApiHelper;
import services.SessionManager;

public class EMIPayNowActivity extends AppCompatActivity {
    private LinearLayout foot;
    public static final String PAYMENT_STATUS = "status";
    public static EMIPayNowActivity fa;
    TextView lblLoanNum, lblOutstand,
            lblLoanAmt, txtPayType, lblLoanTerm;
    Button btnProceedToPay;
    String strLoanNum = "", strMobileNumber = "";
    String loanAmount = "", maxpay = "", maxterm = "", address = "", agreementId = "", mendate_day = "",strTotalOutstanding="";
    String orderId = "";
    boolean isConnected;
    SessionManager session;
    String from;
    int paymentId, extentionDays;
    RelativeLayout rlLoanTerm, rlEMIDueDate, rlEMIPeriod, outstand, rlEMIAmount, totaloutstand;
    RelativeLayout containerPayList;
    ImageView imgBack;
    RegularTextView lblMandatForm, outs, totalouts;
    LightTextView lblEMIDuedate, lblEMIPeriod, lblEMIAmount;
    TextView lblOutstandTotal;
    String strPaymentType = "";
    public static EMIPayNowActivity fadd;
    RelativeLayout emiPayble;

    TextView emipaybleText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emi_pay_now_activity);
        fadd = this;
        checkconnection();
        session = new SessionManager(this);

        Intent intent = getIntent();
      //  strLoanNum = intent.getStringExtra(PayNow.strAgreementNumber);
        strPaymentType = intent.getStringExtra("LoanType");
        strMobileNumber = intent.getStringExtra(PayNow.strMobileNumber);
        agreementId = intent.getStringExtra(PayNow.strAgreementId);
        from = intent.getStringExtra(ApiConstants.EXTRA_FROM);
        paymentId = intent.getIntExtra(ApiConstants.EXTRA_PAYMENTID, 0);
        extentionDays = intent.getIntExtra(ApiConstants.EXTRA_EXTENTION_DAYS, 0);
        mendate_day = intent.getStringExtra("mendate_day");



        lblLoanNum = findViewById(R.id.lblLoanNum);
        lblOutstand = findViewById(R.id.lblOutstand);
        lblLoanAmt = findViewById(R.id.lblLoanAmt);
        lblLoanTerm = findViewById(R.id.lblLoanTerm);
        outstand = findViewById(R.id.outstand);
        outs = findViewById(R.id.outs);
        totalouts = findViewById(R.id.totalouts);
        totaloutstand = findViewById(R.id.totaloutstand);
        rlEMIDueDate = findViewById(R.id.rlEMIDueDate);
        rlEMIPeriod = findViewById(R.id.rlEMIPeriod);
        lblEMIDuedate = findViewById(R.id.lblEMIDuedate);
        lblEMIPeriod = findViewById(R.id.lblEMIPeriod);
        rlEMIAmount = findViewById(R.id.rlEMIAmount);
        lblEMIAmount = findViewById(R.id.lblEMIAmount);
        txtPayType = findViewById(R.id.txtPayType);
        emiPayble = findViewById(R.id.emiPayble);
        emipaybleText=findViewById(R.id.emipaybleText);

        foot=findViewById(R.id.foot);
        lblOutstandTotal=findViewById(R.id.lblOutstandTotal);
        if (strPaymentType.equalsIgnoreCase("FullPayment")) {
            txtPayType.setText("EMI PAYMENT");
            rlEMIDueDate.setVisibility(View.VISIBLE);
            rlEMIPeriod.setVisibility(View.VISIBLE);
            outstand.setVisibility(View.GONE);
            rlEMIAmount.setVisibility(View.GONE);
            totaloutstand.setVisibility(View.GONE);
            if (isConnected)
                new GetFullPayment(agreementId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();


        } else if (strPaymentType.equalsIgnoreCase("PartPayment")) {
            txtPayType.setText("EMI PRE PAYMENT");
            rlEMIDueDate.setVisibility(View.VISIBLE);
            rlEMIPeriod.setVisibility(View.GONE);
            outstand.setVisibility(View.GONE);
            rlEMIAmount.setVisibility(View.VISIBLE);
            totaloutstand.setVisibility(View.VISIBLE);
            if (isConnected)
                new GetPrePayment(agreementId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

        } else if (strPaymentType.equalsIgnoreCase("LoanClosure")) {
            txtPayType.setText("LOAN CLOSURE");
            rlEMIDueDate.setVisibility(View.GONE);
            rlEMIPeriod.setVisibility(View.GONE);
            outs.setText("Foreclosure Amount");
            outstand.setVisibility(View.VISIBLE);
            rlEMIAmount.setVisibility(View.GONE);
            totaloutstand.setVisibility(View.VISIBLE);
            totalouts.setText("Total Outstanding");
            if (isConnected)
                new GetClosurePayment(agreementId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }

        imgBack = findViewById(R.id.imgBack);
        lblMandatForm = findViewById(R.id.lblMandatForm);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rlLoanTerm = findViewById(R.id.rlLoanTerm);
        containerPayList = findViewById(R.id.containerPayList);

        btnProceedToPay = findViewById(R.id.btnProceedToPay);

        btnProceedToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected)
                  //  System.out.println("FFFFF"+orderId+" "+strLoanNum+" "+agreementId+" "+extentionDays+" "+String.valueOf(paymentId)+" "+strTotalOutstanding+" "+session.getUserRecord().get(UserRecords.strid));

                new InsertPaymentRecord(orderId, strLoanNum, agreementId, "" + extentionDays, paymentId, strTotalOutstanding).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                else
                    Toast.makeText(EMIPayNowActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkconnection();
        ApiConstants.dismissProgress();
        if (data != null && requestCode == ApiConstants.PG_CODE) {
            String trackingId = data.getStringExtra(AvenuesParams.TRACKING_ID);
            String bankRef = data.getStringExtra(AvenuesParams.BANK_REFERENCE);
            String paidAmount = data.getStringExtra(AvenuesParams.AMOUNT);
            String status = data.getStringExtra(AvenuesParams.ORDER_STATUS);
            if (status != null && status.equalsIgnoreCase("success")) {
                if (isConnected) {
                    new UpdatePaymentRecord(orderId, trackingId, bankRef, strLoanNum,
                            agreementId, paidAmount, paymentId, status).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    Toast.makeText(this, "You are not connected to the internet", Toast.LENGTH_SHORT).show();
                }
            } else {
                showCustomizedAlert(getResources().getString(R.string.payment_fail));
            }
        }
    }
    class UpdatePaymentRecord extends AsyncTask<Void, Boolean, Boolean> {

        String orderId, trackingId, referenceNo, agreementNo, agreementId, amount, orderStatus;
        int paymentid;

        public UpdatePaymentRecord(String orderId, String trackingId, String referenceNo, String agreementNo,
                                   String agreementId, String amount, int paymentid, String orderStatus) {
            this.orderId = orderId;
            this.trackingId = trackingId;
            this.referenceNo = referenceNo;
            this.agreementNo = agreementNo;
            this.agreementId = agreementId;
            this.amount = amount;
            this.paymentid = paymentid;
            this.orderStatus = orderStatus;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ApiConstants.showProgress(EMIPayNowActivity.this, "Processing your request...");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            ApiHelper helper = new ApiHelper();
            return helper.updatePaymentRecord(orderId, trackingId, referenceNo, agreementNo,
                    agreementId, amount, paymentid, orderStatus);
        }

        @Override
        protected void onPostExecute(Boolean status) {
            super.onPostExecute(status);
            ApiConstants.dismissProgress();
            if (status != null && status == true) {
                showSuccessAlert("Your payment done successfully.\n" +
                        "Your transaction id is " + trackingId);
            } else if (status != null && status == false) {
                showCustomizedAlert(getResources().getString(R.string.payment_fail));
            } else {
                Toast.makeText(EMIPayNowActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void showSuccessAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(EMIPayNowActivity.this, R.style.myDialog));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_error, null);
        builder.setView(dialogView);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnConfirm);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);

        TextView txtmessage = (TextView) dialogView.findViewById(R.id.txtdata);
        final AlertDialog dialog = builder.create();

        txtmessage.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (from != null && from.equalsIgnoreCase("Dashboard")) {
                    finish();
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                } else {
                    Intent in = new Intent();
                    in.putExtra(PAYMENT_STATUS, true);
                    setResult(PayNowActivity.PAYMENT_RESPONSE, in);
                    finish();
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                }
            }
        });

        btnCancel.setVisibility(View.GONE);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void showCustomizedAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(EMIPayNowActivity.this, R.style.myDialog));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_error, null);
        builder.setView(dialogView);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnConfirm);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);

        TextView txtmessage = (TextView) dialogView.findViewById(R.id.txtdata);
        final AlertDialog dialog = builder.create();

        txtmessage.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnCancel.setVisibility(View.GONE);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    public void checkconnection() {
        isConnected = ConnectivityReceiver.isConnected();
    }

    class GetFullPayment extends AsyncTask<Void, HashMap<String, String>, HashMap<String, String>> {

        String agreementId;

        public GetFullPayment(String agreementId) {
            this.agreementId = agreementId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ApiConstants.showProgress(EMIPayNowActivity.this, "Processing your request...");
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... voids) {
            ApiHelper helper = new ApiHelper();
            return helper.checkFullPaymentStatus(session.getUserRecord().get(UserRecords.crmId), session.getUserRecord().get(UserRecords.strid), session.getToken("token"));
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            super.onPostExecute(hashMap);
            ApiConstants.dismissProgress();
            if (hashMap != null && hashMap.size() > 1) {


                String strStatus = hashMap.get("Status");
                if (strStatus.equalsIgnoreCase("true")) {
                    String strLoanAmount = hashMap.get("loanAmount");
                    String strLoanTenure = hashMap.get("loanTenure");
                    strLoanNum = hashMap.get("loanNo");
                    orderId = ApiConstants.generateOrderidorInsertion(6, strLoanNum);
                    String strEMIDate = hashMap.get("emiDate");
                    String strEMIPeriod = hashMap.get("emiPeriod");
                    String strEMIAmount = hashMap.get("emiAmount");
                    strTotalOutstanding=strEMIAmount;
                    String strLoanType = hashMap.get("LoanType");
                    lblLoanNum.setText(strLoanNum);
                    lblLoanAmt.setText("\u20B9 " + ApiConstants.roundOffString(strLoanAmount));
                    lblLoanTerm.setText(strLoanTenure + " days");
                    lblEMIDuedate.setText(strEMIDate);
                    if(strEMIAmount.equalsIgnoreCase("0"))
                    {
                        foot.setVisibility(View.GONE);
                        emiPayble.setVisibility(View.GONE);
                    }
                    else
                    {
                        foot.setVisibility(View.VISIBLE);
                        emiPayble.setVisibility(View.VISIBLE);
                        emipaybleText.setText("\u20B9 " +strEMIAmount);

                    }
                    lblEMIAmount.setText("\u20B9 " + ApiConstants.roundOffString(strEMIAmount));
                    lblEMIPeriod.setText(strEMIPeriod);
                    lblOutstand.setText("\u20B9 " + ApiConstants.roundOffString(strLoanAmount));

                }
                else
                {
                    ApiConstants.showAlertMessage(EMIPayNowActivity.this, ApiConstants.strMessage);

                    foot.setVisibility(View.GONE);
                    lblLoanNum.setText("--");
                    lblLoanAmt.setText("--");
                    lblLoanTerm.setText("--");
                    lblEMIDuedate.setText("--");
                    lblEMIAmount.setText("--");
                    lblEMIPeriod.setText("--");
                    lblOutstandTotal.setText("--");

                }

            } else if (hashMap != null && hashMap.size() == 1) {
                String logoutState = session.getTokenState("token_state");
                if (logoutState.equalsIgnoreCase("1")) {
                    ApiConstants.showSessionLogout(EMIPayNowActivity.this, "Session timeout", session);
                } else {
                    ApiConstants.showAlertMessage(EMIPayNowActivity.this, ApiConstants.strMessage);
                }
            } else {
                ApiConstants.showAlertMessage(EMIPayNowActivity.this, ApiConstants.strMessage);
                foot.setVisibility(View.GONE);
                lblLoanNum.setText("--");
                lblLoanAmt.setText("--");
                lblLoanTerm.setText("--");
                lblEMIDuedate.setText("--");
                lblEMIAmount.setText("--");
                lblEMIPeriod.setText("--");
                lblOutstandTotal.setText("--");
            }
        }
    }

    //PRe
    class GetPrePayment extends AsyncTask<Void, HashMap<String, String>, HashMap<String, String>> {

        String agreementId;

        public GetPrePayment(String agreementId) {
            this.agreementId = agreementId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ApiConstants.showProgress(EMIPayNowActivity.this, "Processing your request...");
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... voids) {
            ApiHelper helper = new ApiHelper();
            return helper.checkEMIPrePaymentStatus(session.getUserRecord().get(UserRecords.crmId), session.getUserRecord().get(UserRecords.strid), session.getToken("token"));
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            super.onPostExecute(hashMap);
            ApiConstants.dismissProgress();
            if (hashMap != null && hashMap.size() > 1) {


                String strStatus = hashMap.get("Status");
                if (strStatus.equalsIgnoreCase("true")) {


                    emiPayble.setVisibility(View.GONE);

                    String strLoanAmount = hashMap.get("loanAmount");
                    String strLoanTenure = hashMap.get("loanTenure");
                    strLoanNum = hashMap.get("loanNo");
                    orderId = ApiConstants.generateOrderidorInsertion(6, strLoanNum);
                    String strEMIDate = hashMap.get("emiDate");
                    strTotalOutstanding = hashMap.get("totalOutstanding");
                    foot.setVisibility(View.VISIBLE);



                    lblLoanNum.setText(strLoanNum);
                    lblLoanAmt.setText("\u20B9 " + ApiConstants.roundOffString(strLoanAmount));
                    lblLoanTerm.setText(strLoanTenure + " days");

                    lblEMIAmount.setText("\u20B9 " + ApiConstants.roundOffString(strTotalOutstanding));
                    lblEMIDuedate.setText(strEMIDate);


                    lblOutstandTotal.setText("\u20B9 " + ApiConstants.roundOffString(strTotalOutstanding));

                }
                else
                {
                    ApiConstants.showAlertMessage(EMIPayNowActivity.this, ApiConstants.strMessage);

                    foot.setVisibility(View.GONE);
                    lblLoanNum.setText("--");
                    lblLoanAmt.setText("--");
                    lblLoanTerm.setText("--");
                    lblEMIAmount.setText("--");
                    lblEMIDuedate.setText("--");
                    lblOutstandTotal.setText("--");

                }

            } else if (hashMap != null && hashMap.size() == 1) {
                String logoutState = session.getTokenState("token_state");
                if (logoutState.equalsIgnoreCase("1")) {
                    ApiConstants.showSessionLogout(EMIPayNowActivity.this, "Session timeout", session);
                } else {
                    ApiConstants.showAlertMessage(EMIPayNowActivity.this, ApiConstants.strMessage);
                }
            } else {
                ApiConstants.showAlertMessage(EMIPayNowActivity.this, ApiConstants.strMessage);

                foot.setVisibility(View.GONE);
                lblLoanNum.setText("--");
                lblLoanAmt.setText("--");
                lblLoanTerm.setText("--");
                lblEMIAmount.setText("--");
                lblEMIDuedate.setText("--");
                lblOutstandTotal.setText("--");

            }
        }
    }



    //Closure
    class GetClosurePayment extends AsyncTask<Void, HashMap<String, String>, HashMap<String, String>> {

        String agreementId;

        public GetClosurePayment(String agreementId) {
            this.agreementId = agreementId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ApiConstants.showProgress(EMIPayNowActivity.this, "Processing your request...");
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... voids) {
            ApiHelper helper = new ApiHelper();
            return helper.checkLoanClosure(session.getUserRecord().get(UserRecords.crmId), session.getUserRecord().get(UserRecords.strid), session.getToken("token"));
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            super.onPostExecute(hashMap);
            ApiConstants.dismissProgress();
            if (hashMap != null && hashMap.size() > 1) {


                String strStatus = hashMap.get("Status");
                if (strStatus.equalsIgnoreCase("true")) {
                    String strLoanAmount = hashMap.get("loanAmount");
                    String strLoanTenure = hashMap.get("loanTenure");
                    strLoanNum = hashMap.get("loanNo");
                    orderId = ApiConstants.generateOrderidorInsertion(6, strLoanNum);
                    String strEMIDate = hashMap.get("emiDate");
                    strTotalOutstanding = hashMap.get("totalOutstanding");
                    foot.setVisibility(View.VISIBLE);

                    emiPayble.setVisibility(View.GONE);


                    lblLoanNum.setText(strLoanNum);
                    lblLoanAmt.setText("\u20B9 " + ApiConstants.roundOffString(strLoanAmount));
                    lblLoanTerm.setText(strLoanTenure + " days");

                    lblEMIAmount.setText("\u20B9 " + ApiConstants.roundOffString(strTotalOutstanding));
                    lblEMIDuedate.setText(strEMIDate);


                    lblOutstandTotal.setText("\u20B9 " + ApiConstants.roundOffString(strTotalOutstanding));
                    lblOutstand.setText("\u20B9 " + ApiConstants.roundOffString(strTotalOutstanding));
                }
                else
                {
                    ApiConstants.showAlertMessage(EMIPayNowActivity.this, ApiConstants.strMessage);

                    foot.setVisibility(View.GONE);
                    lblLoanNum.setText("--");
                    lblLoanAmt.setText("--");
                    lblLoanTerm.setText("--");
                    lblEMIAmount.setText("--");
                    lblEMIDuedate.setText("--");
                    lblOutstandTotal.setText("--");
                    lblOutstand.setText("--");

                }


            } else if (hashMap != null && hashMap.size() == 1) {
                String logoutState = session.getTokenState("token_state");
                if (logoutState.equalsIgnoreCase("1")) {
                    ApiConstants.showSessionLogout(EMIPayNowActivity.this, "Session timeout", session);
                } else {
                    ApiConstants.showAlertMessage(EMIPayNowActivity.this, ApiConstants.strMessage);
                }
            } else {
                ApiConstants.showAlertMessage(EMIPayNowActivity.this, ApiConstants.strMessage);

                foot.setVisibility(View.GONE);
                lblLoanNum.setText("--");
                lblLoanAmt.setText("--");
                lblLoanTerm.setText("--");
                lblEMIAmount.setText("--");
                lblEMIDuedate.setText("--");
                lblOutstandTotal.setText("--");
                lblOutstand.setText("--");
            }
        }
    }

//Insert Payment
class InsertPaymentRecord extends AsyncTask<Void, Boolean, Boolean> {

    String orderId, agreementNo, agreementId, extDays, total;
    int paymentid;

    public InsertPaymentRecord(String orderId, String agreementNo,
                               String agreementId, String extDays, int paymentid, String total) {
        this.orderId = orderId;
        this.agreementNo = agreementNo;
        this.agreementId = agreementId;
        this.extDays = extDays;
        this.paymentid = paymentid;
        this.total = total;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ApiConstants.showProgress(EMIPayNowActivity.this, "Processing your request...");
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        ApiHelper helper = new ApiHelper();
        return helper.insertPaymentRecordEMI(orderId, agreementNo, agreementId, extDays, paymentid, total, session.getUserRecord().get(UserRecords.strid));
    }

    @Override
    protected void onPostExecute(Boolean status) {
        super.onPostExecute(status);
        ApiConstants.dismissProgress();
        if (status != null && status == true) {

            if (paymentid == ApiConstants.FULL_PAYMENT_CODE) {
                int maxAmount = Math.round(Float.parseFloat(total));

                String vAccessCode = ApiConstants.ACCESS_CODE;
                String vMerchantId = ApiConstants.MERCHANT_ID;
                String vCurrency = ApiConstants.CURRENCY;
                String vAmount = ServiceUtility.chkNull(maxAmount).toString().trim();
                if (!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")) {
                    Intent intent = new Intent(EMIPayNowActivity.this, WebViewActivity.class);
                    intent.putExtra("come","emi_payment");
                    intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(ApiConstants.ACCESS_CODE).toString().trim());
                    intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(ApiConstants.MERCHANT_ID).toString().trim());
                    intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId).toString().trim());
                    intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(ApiConstants.CURRENCY).toString().trim());
                    intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(maxAmount).toString().trim());

                    intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(ApiConstants.REDIRECT_URL).toString().trim());
                    intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(ApiConstants.REDIRECT_URL).toString().trim());
                    intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(ApiConstants.RSA_URL).toString().trim());
                    intent.putExtra(AvenuesParams.BILLING_TEL, ServiceUtility.chkNull(strMobileNumber).toString().trim());

                    intent.putExtra(AvenuesParams.BILLING_NAME, ServiceUtility.chkNull(session.getUserRecord().get(UserRecords.firstName) + " " + session.getUserRecord().get(UserRecords.lastName)).toString().trim());
                    intent.putExtra(AvenuesParams.BILLING_EMAIL, ServiceUtility.chkNull(session.getUserRecord().get(UserRecords.emailId)).toString().trim());
                    intent.putExtra(AvenuesParams.SUB_ACCOUNT_ID, ServiceUtility.chkNull(ApiConstants.SUB_ACCOUNT_VALUE).toString().trim());

                    startActivityForResult(intent, ApiConstants.PG_CODE);
                    //finish();
                }
            } else {
                int amountToPass = Math.round(Float.parseFloat(total));
                String vAccessCode = ApiConstants.ACCESS_CODE;
                String vMerchantId = ApiConstants.MERCHANT_ID;
                String vCurrency = ApiConstants.CURRENCY;
                String vAmount = ServiceUtility.chkNull(amountToPass).toString().trim();
                if (!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")) {
                    Intent intent = new Intent(EMIPayNowActivity.this, WebViewActivity.class);
                    intent.putExtra("come","emi_payment");

                    intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(ApiConstants.ACCESS_CODE).toString().trim());
                    intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(ApiConstants.MERCHANT_ID).toString().trim());
                    intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId).toString().trim());
                    intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(ApiConstants.CURRENCY).toString().trim());
                    intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(amountToPass).toString().trim());

                    intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(ApiConstants.REDIRECT_URL).toString().trim());
                    intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(ApiConstants.REDIRECT_URL).toString().trim());
                    intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(ApiConstants.RSA_URL).toString().trim());
                    intent.putExtra(AvenuesParams.BILLING_TEL, ServiceUtility.chkNull(strMobileNumber).toString().trim());
                    intent.putExtra(AvenuesParams.BILLING_NAME, ServiceUtility.chkNull(session.getUserRecord().get(UserRecords.firstName) + " " + session.getUserRecord().get(UserRecords.lastName)).toString().trim());
                    intent.putExtra(AvenuesParams.BILLING_EMAIL, ServiceUtility.chkNull(session.getUserRecord().get(UserRecords.emailId)).toString().trim());
                    intent.putExtra(AvenuesParams.SUB_ACCOUNT_ID, ServiceUtility.chkNull(ApiConstants.SUB_ACCOUNT_VALUE).toString().trim());

                    startActivityForResult(intent, ApiConstants.PG_CODE);
                    //finish();
                }
            }
        } else if (status != null && status == false) {
            ApiConstants.showAlertMessage(EMIPayNowActivity.this, "unable to process your request");

        } else {
            ApiConstants.showAlertMessage(EMIPayNowActivity.this, ApiConstants.strMessage);

        }
    }
}

}
