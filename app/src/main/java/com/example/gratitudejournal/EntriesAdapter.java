package com.example.gratitudejournal;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;
import com.example.myapplication.Entry;
import android.text.format.DateFormat;

public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.ViewHolder> {

    private Context context;
    private List<Entry> entries;

    public EntriesAdapter(Context context, List<Entry> entries) {
        this.context = context;
        this.entries = entries;
    }

    // Clean all elements of the recycler
    public void clear() {
        entries.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Entry> list) {
        entries.addAll(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Entry entry = entries.get(position);
        holder.bind(entry);
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvDate;
        private Button btnMood;
        private TextView tvEntry;
        private TextView tvLoadMore;
        private View vBar;
        private Date lastDate;
//        private Boolean messageDisplayed = false;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnMood = itemView.findViewById(R.id.btnMood);
            tvEntry = itemView.findViewById(R.id.tvEntry);
            tvLoadMore = itemView.findViewById(R.id.tvLoadMore);
            vBar = itemView.findViewById(R.id.vBar);
            itemView.setOnClickListener(this);
        }

        public void bind(Entry entry) {
            // Bind the entry data to the view elements
//            if(entry.equals(entries.get(0)) && !messageDisplayed) {
//                Log.i("EntriesAdapter", "This is the first entry" + entry.getCreatedAt() + entry.getText());
//                tvLoadMore.setText("Load more entries!");
//                lastDate = entry.getCreatedAt();
//                messageDisplayed = true;
//            }
            Date date = entry.getCreatedAt();
            String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
            String day          = (String) DateFormat.format("dd",   date); // 20
            String monthString  = (String) DateFormat.format("MMM",  date); // Jun
            String monthNumber  = (String) DateFormat.format("MM",   date); // 06
            String year         = (String) DateFormat.format("yyyy", date); // 2013
            //String time = String.valueOf(date.getDay()) + " " + String.valueOf(date.getMonth()) + " " + String.valueOf(date.getDate()) + " " + String.valueOf(date.getYear());
            // String time = entry.getCreatedAt().toString();
            String time = (monthString + " " + day + ", " + year);
            tvDate.setText(time);
            btnMood.setText(entry.getMood());

            // get the user's current journal entry
            String text = null;
            try {
                text = entry.fetchIfNeeded().getString("text");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvEntry.setText(text);


            // get the user's mood selection
            String mood = null;
            try {
                mood = entry.fetchIfNeeded().getString("mood");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (mood.equals("skip")) {
                mood = "No mood selected";
            }

            // set the mood as the text for the button
            btnMood.setText(mood);

            // change the colors of the mood and save buttons depending on the mood the user
            // has selected
            if(mood.equals("Amazing")){
                btnMood.setBackgroundColor(0xFFF8CC7F);
            }

            else if(mood.equals("Good")){
                btnMood.setBackgroundColor(0xFFD2B08C);
            }

            else if(mood.equals("Okay")){
                btnMood.setBackgroundColor(0xFF808080);
            }

            else if(mood.equals("Bad")){
                btnMood.setBackgroundColor(0xFF374C56);
            }

            else if(mood.equals("Terrible")){
                btnMood.setBackgroundColor(0xFF04202F);
            }

            else if(mood.equals("No mood selected")) {
                btnMood.setBackgroundColor(0xFF7294a3);
            }

//            tvLoadMore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.i("EntriesAdapter", "CLICKED!!");
//                    ParseUser currentUser = ParseUser.getCurrentUser();
//                    ParseQuery<Entry> query = ParseQuery.getQuery(Entry.class);
//                    query.setLimit(20);
//                    query.whereMatches("user", currentUser.getObjectId());
//                    query.whereGreaterThan("createdAt", lastDate);
//                    query.addDescendingOrder("createdAt");
//                    query.findInBackground(new FindCallback<Entry>() {
//                        @Override
//                        public void done(List<Entry> entries, ParseException e) {
//                            // check for errors
//                            if (e != null) {
//                                Log.e("ole", "Issue with getting posts", e);
//                                return;
//                            }
//                            if (entries.size() == 0) {
//                                Toast.makeText(context, "No more entries", Toast.LENGTH_LONG).show();
//                                return;
//                            }
//                            // for debugging purposes let's print every post description to logcat
//                            for (Entry entry : entries) {
//                                Log.i("ole", "infinite scroll entry");
//                            }
//                            Date currentDate = entries.get(0).getCreatedAt();
//                            Intent intent = new Intent(context, ScrollActivity.class);
//                            String dayOfTheWeek1 = (String) DateFormat.format("EEEE", currentDate); // Thursday
//                            String day1         = (String) DateFormat.format("dd",   currentDate); // 20
//                            String monthString1  = (String) DateFormat.format("MMM",  currentDate); // Jun
//                            String monthNumber1  = (String) DateFormat.format("MM",   currentDate); // 06
//                            String year1         = (String) DateFormat.format("yyyy", currentDate); // 2013
//                            intent.putExtra("year", year1);
//                            intent.putExtra("month", monthNumber1);
//                            intent.putExtra("dayOfMonth", day1);
//                            intent.putExtra("index",entries.size());
//                            context.startActivity(intent);
//                            //finish();
//                        }
//                    });
//                }
//            });
        }

        @Override
        public void onClick(View v) {
            // do nothing, is that an option?
        }
    }

    }
