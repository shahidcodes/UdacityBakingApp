package ml.shahidkamal.udacitybakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.shahidkamal.udacitybakingapp.R;
import ml.shahidkamal.udacitybakingapp.RecipeDetailsActivity;
import ml.shahidkamal.udacitybakingapp.StepsActivity;
import ml.shahidkamal.udacitybakingapp.model.Steps;
import ml.shahidkamal.udacitybakingapp.utils.Constants;

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.StepsListViewHolder> {


    Context context;
    List<Steps> stepsList;

    public StepsListAdapter(Context context, List<Steps> stepsList){
        this.stepsList = stepsList;
        this.context = context;
    }

    @NonNull
    @Override
    public StepsListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_steps_list, viewGroup, false);
        return new StepsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsListViewHolder stepsListViewHolder, final int i) {
        final Steps steps = stepsList.get(i);
        stepsListViewHolder.stepName.setText(steps.getShortDescription());
        stepsListViewHolder.stepsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeDetailsActivity activity = (RecipeDetailsActivity) context;
                activity.changeStepsFragment(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    class StepsListViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.card_step_list)
        CardView stepsCard;
        @BindView(R.id.tv_step_name)
        TextView stepName;

        public StepsListViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
