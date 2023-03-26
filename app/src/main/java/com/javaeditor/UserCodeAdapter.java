package com.javaeditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserCodeAdapter extends RecyclerView.Adapter<UserCodeAdapter.UserViewHolder> {
    private final Context context;
    private final Activity activity;
    private ArrayList<UserCode> userCodes;

    public UserCodeAdapter(Context context, Activity activity, ArrayList<UserCode> userCodes) {
        this.context = context;
        this.activity = activity;
        this.userCodes = userCodes;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new UserViewHolder(inflater.inflate(R.layout.code_row_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserCode userCode = userCodes.get(position);
        holder.textView.setText(userCode.getTitle());

        holder.mainCodeRowLayout.setOnClickListener(v -> {
            Intent i = new Intent(context, CodeEditorActivity.class);
            i.putExtra("user_code_id", userCode.getId());
            i.putExtra("user_code_title", userCode.getTitle());
            i.putExtra("user_code", userCode.getCode());
            context.startActivity(i);
        });

        try (CodeDatabase cDb = new CodeDatabase(context)) {

            holder.mainCodeRowLayout.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DeleteDialogTheme);

                builder.setIcon(R.drawable.baseline_warning_24).setTitle("Delete code ?");

                builder.setPositiveButton("Yes", ((dialog, which) -> {
                    cDb.deleteOneRow(userCode.getId());
                    activity.recreate();
                    notifyDataSetChanged();
                }));

                builder.setNegativeButton("No", (dialog, which) -> {});

                builder.create();
                builder.show();
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return userCodes.size();
    }

    public void setFilter(ArrayList<UserCode> list) {
        userCodes = new ArrayList<>();
        userCodes.addAll(list);
        notifyDataSetChanged();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainCodeRowLayout;
        TextView textView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            mainCodeRowLayout = itemView.findViewById(R.id.main_code_row_layout);
            textView = itemView.findViewById(R.id.title_text);
        }
    }
}