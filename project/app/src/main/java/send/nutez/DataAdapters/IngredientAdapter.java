package send.nutez.DataAdapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import send.nutez.R;

public class IngredientAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> data;

    public IngredientAdapter(Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int pos) {
        return data.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.ingredient_element_layout,null,false);
            holder.editPropertyValue = (EditText) convertView.findViewById(R.id.editTextTextPersonName);
            holder.editPropertyValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    data.get(position).put("value", editable.toString());
                    //Log.e("CHANGE", editable.toString() + " " + data.get(position).toString());
                }
            });
            holder.propertyName = (TextView) convertView.findViewById(R.id.ingreedText);
            holder.editPropertyValue.setText(data.get(position).get("value"));
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.propertyName.setText(data.get(position).get("name"));

        convertView.setTag(holder);
        return convertView;
    }

    class ViewHolder {
        EditText editPropertyValue;
        TextView propertyName;
    }
}
