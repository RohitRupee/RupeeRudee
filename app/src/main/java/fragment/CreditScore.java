package fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.rupeeredee.app.MainActivity;
import com.rupeeredee.app.R;

import java.util.HashMap;
import java.util.Random;

import credit_score_view.OldCreditSesameView;
import customtools.MediumTextView;
import model.UserRecords;
import services.ApiConstants;
import services.ApiHelper;
import services.SessionManager;

public class CreditScore extends Fragment {
    SessionManager sessionManager;
    private OldCreditSesameView oldCreditSesameView;
    private String strMobileNumber = "";
    private Random random = new Random();
    RelativeLayout linShow;
    LinearLayout linHide;
    MediumTextView txtDate;
    public static String strStatus="";
    Button btnApply;
    MediumTextView txtNewcredit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.credit_score, container, false);
        oldCreditSesameView = (OldCreditSesameView) view.findViewById(R.id.sesame_view);
        linHide=view.findViewById(R.id.nocredit);
        linShow=view.findViewById(R.id.showcreddit);
        txtDate=view.findViewById(R.id.txtdate);
        btnApply=view.findViewById(R.id.btnCalculate);
        txtNewcredit=view.findViewById(R.id.txtNewcredit);
        sessionManager = new SessionManager(getActivity());
        strMobileNumber = sessionManager.getUserRecord().get(UserRecords.mobile);
        new CheckCreditScore(strMobileNumber).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goTomain=new Intent(getActivity(),MainActivity.class);
                startActivity(goTomain);
                getActivity().finish();
            }
        });
        return view;
    }

    class CheckCreditScore extends AsyncTask<Void, HashMap<String, String>, HashMap<String, String>> {

        String strMobileNumber = "";


        public CheckCreditScore(String strMobileNumber) {
            this.strMobileNumber = strMobileNumber;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ApiConstants.showProgress(getActivity(), "Getting your score...");
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... voids) {
            ApiHelper helper = new ApiHelper();
            return helper.showCreditScore(sessionManager.getUserRecord().get(UserRecords.strid),sessionManager.getToken("token"),strMobileNumber);
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            super.onPostExecute(hashMap);
            ApiConstants.dismissProgress();
            String status = hashMap.get("Status");
            String messasge = hashMap.get(ApiConstants.strMessage);


            if (hashMap != null && hashMap.size() > 0) {

                if (status != null && status.equals("true")) {
                    String strCreditScore = hashMap.get("creditScore");
                    strStatus = hashMap.get("creditstatus");
                    String strCreditDate=hashMap.get("createdDate");
                    txtDate.setText("This credit score is courtsey Experian as on "+strCreditDate);

                    oldCreditSesameView.setSesameValues(Integer.parseInt(strCreditScore));
                    if(strStatus.equalsIgnoreCase("NTC"))
                    {
                        linShow.setVisibility(View.GONE);
                        linHide.setVisibility(View.VISIBLE);
                        txtNewcredit.setVisibility(View.GONE);
                    }
                    else
                    {
                        linShow.setVisibility(View.VISIBLE);
                        linHide.setVisibility(View.GONE);
                        txtNewcredit.setVisibility(View.VISIBLE);
                    }

                }
                else if (status.equals("false")) {

                    linShow.setVisibility(View.GONE);
                    linHide.setVisibility(View.VISIBLE);
                    txtNewcredit.setVisibility(View.GONE);



                }
                else {
                    ApiConstants.showAlertMessage(getActivity(), messasge);
                    // ApiConstants.showAlertMessage(getActivity(), messasge);

                }
            } else {
                ApiConstants.showAlertMessage(getActivity(), messasge);

            }

        }
    }

}
