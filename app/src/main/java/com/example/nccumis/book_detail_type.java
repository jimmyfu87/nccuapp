package com.example.nccumis;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

//查帳下面的
public class book_detail_type extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;
    private final List<Integer> numberArray;
    private final List<String> nameArray;
    private final List<String> percentageArray;
    private final List<Integer> totalArray;


    public book_detail_type (Activity context, List<Integer> numberArrayParam, List<String> nameArrayParam, List<String> percentageArrayParam, List<Integer> totalArrayParam){

        super(context, R.layout.spend_listview_row, nameArrayParam);

        this.context=context;
        this.numberArray = numberArrayParam;
        this.nameArray = nameArrayParam;
        this.percentageArray = percentageArrayParam;
        this.totalArray = totalArrayParam;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.spend_listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView numberTextField = (TextView) rowView.findViewById(R.id.Number);
        TextView nameTextField = (TextView) rowView.findViewById(R.id.TypeName);
        TextView percentageTextField = (TextView) rowView.findViewById(R.id.Percentage);
        TextView totalTextField = (TextView)rowView.findViewById(R.id.Total);

        //this code sets the values of the objects to values from the arrays
        numberTextField.setText(numberArray.get(position).toString());
        nameTextField.setText(nameArray.get(position));
        percentageTextField.setText(percentageArray.get(position));
        totalTextField.setText(totalArray.get(position).toString());

        return rowView;

    };
}
