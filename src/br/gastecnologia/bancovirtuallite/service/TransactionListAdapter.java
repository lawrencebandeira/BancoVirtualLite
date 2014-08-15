package br.gastecnologia.bancovirtuallite.service;

import java.util.List;

import br.gastecnologia.bancovirtuallite.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TransactionListAdapter extends ArrayAdapter<Transaction> 
{
	
	public TransactionListAdapter(Context context, int textViewResourceId,	List<Transaction> objects) 
	{
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		
		   if (convertView == null) 
		   {
			   convertView = View.inflate(this.getContext(), R.layout.transactionlistlayout, null);
           }

           Transaction item = (Transaction) getItem(position);

           TextView text = (TextView) convertView.findViewById(R.id.Type);
           text.setText(item.getType());
           
           text = (TextView) convertView.findViewById(R.id.Value);
           text.setText(item.getValue());

           text = (TextView) convertView.findViewById(R.id.Ag);
           if(item.getType().startsWith("Cart��o"))
           {
        	   text.setText("");
           }
           else
           {
        	   text.setText(this.getContext().getString(R.string.TransactionListAdapter_1) + item.getAg());
           }
           
           text = (TextView) convertView.findViewById(R.id.CC);
           if(item.getType().startsWith("Cart��o"))
           {
        	   text.setText("");
           }
           else
           {
        	   text.setText(this.getContext().getString(R.string.TransactionListAdapter_2) + item.getCC());   
           }
           
           
           return convertView;
   }
}

