package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rupeeredee.app.R;

import java.util.ArrayList;

import customtools.RegularTextView;
import model.EMISchedule;


public class EMIScheduleAdapter extends ArrayAdapter<EMISchedule> {

    ArrayList<EMISchedule> pastItem;
    Context context;

    public EMIScheduleAdapter(Context context, ArrayList<EMISchedule> pastItem) {
        super(context, R.layout.row_emi_details, pastItem);
        this.context = context;
        this.pastItem = pastItem;
    }
    // View lookup cache
    private static class ViewHolder {
        TextView txtEMI,txtDueDate,txtEMIAmount,txtInterest,txtServiceFees,txtClosingPrinciple;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        EMISchedule dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_emi_details, parent, false);
            viewHolder.txtEMI = convertView.findViewById(R.id.txtEMI);
            viewHolder.txtDueDate =  convertView.findViewById(R.id.txtDueDate);
            viewHolder.txtEMIAmount = convertView.findViewById(R.id.txtEMIAmount);
            viewHolder.txtInterest = convertView.findViewById(R.id.txtInterest);
            viewHolder.txtServiceFees = convertView.findViewById(R.id.txtServiceFees);
            viewHolder.txtClosingPrinciple = convertView.findViewById(R.id.txtClosingPrinciple);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        viewHolder.txtEMI.setText(dataModel.getEmiPeriod());
        viewHolder.txtDueDate.setText(dataModel.getEmiDate());
        viewHolder.txtEMIAmount.setText("\u20B9"+dataModel.getEmiAmount());
        viewHolder.txtInterest.setText("\u20B9"+dataModel.getInterest());
        viewHolder.txtServiceFees.setText("\u20B9"+dataModel.getServiceFees());
        viewHolder.txtClosingPrinciple.setText("\u20B9"+dataModel.getClosingPrincipal());

        // Return the completed view to render on screen
        return convertView;
    }
}