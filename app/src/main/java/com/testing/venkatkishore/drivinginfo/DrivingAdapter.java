package com.testing.venkatkishore.drivinginfo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Akula on 26-04-2017.
 */
public class DrivingAdapter extends RecyclerView.Adapter<DrivingAdapter.MyViewHolder>{
    private List<Drivinginfolist> drivingList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_starttime, tv_endtime, tv_hours,tv_drivstate;

        public MyViewHolder(View view) {
            super(view);
            tv_starttime = (TextView) view.findViewById(R.id.tx_starttime);
            tv_endtime = (TextView) view.findViewById(R.id.tx_endtime);
            tv_hours = (TextView) view.findViewById(R.id.tx_hourinfo);
            tv_drivstate = (TextView) view.findViewById(R.id.txt_state);
        }
    }


    public DrivingAdapter(List<Drivinginfolist> driveingList) {
        this.drivingList = driveingList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.driving_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Drivinginfolist drivinfo = drivingList.get(position);
        holder.tv_starttime.setText(drivinfo.getStarttime());
        holder.tv_endtime.setText(drivinfo.getEndtime());
        holder.tv_hours.setText(drivinfo.getHours());
        holder.tv_drivstate.setText(drivinfo.getDrivingstate());
    }

    @Override
    public int getItemCount() {
        return drivingList.size();
    }


}
