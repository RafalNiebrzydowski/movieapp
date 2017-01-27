package com.example.rafal.movieapp.adapter.serialAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafal.movieapp.R;
import com.example.rafal.movieapp.utility.Utility;
import com.example.rafal.movieapp.data.MovieContract;

/**
 * Created by rafal on 13.01.2017.
 */

public class SerialAdapter extends RecyclerView.Adapter<SerialAdapter.MovieViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    private View emptyView;

    final private SerialAdapterOnClickHandler mClickHandler;


    public interface SerialAdapterOnClickHandler {
        void onClickSerial(long _id);
    }
    public SerialAdapter(Context mContext, SerialAdapterOnClickHandler mClickHandler, View emptyView) {
        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
        this.emptyView = emptyView;
    }
    public SerialAdapter(Context mContext, SerialAdapterOnClickHandler mClickHandler) {
        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
    }
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_item_list_main_fragment, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.titleSerialTextView.setText(mCursor.getString(mCursor.getColumnIndex(MovieContract.Serial.COLUMN_TITLE)));
        if(mCursor.getBlob(mCursor.getColumnIndex(MovieContract.Serial.COLUMN_IMAGE))!=null)
            holder.posterSerialImageView.setImageBitmap(BitmapFactory.decodeByteArray(mCursor.getBlob(mCursor.getColumnIndex(MovieContract.Serial.COLUMN_IMAGE)), 0,mCursor.getBlob(mCursor.getColumnIndex(MovieContract.Serial.COLUMN_IMAGE)).length));

        if(mCursor.getInt(mCursor.getColumnIndex(MovieContract.Serial.COLUMN_FAVORITE))==1) {
            holder.favoriteSerialimageView.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent));
        }
        else {
            holder.favoriteSerialimageView.setColorFilter(ContextCompat.getColor(mContext, R.color.nofavorite));
        }
    }

    @Override
    public int getItemCount() {
        if(mCursor==null)
        return 0;
        else
            return mCursor.getCount();
    }
    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        if (emptyView!=null)
        emptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView titleSerialTextView;
        final ImageView posterSerialImageView;
        final ImageView favoriteSerialimageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            titleSerialTextView = (TextView) itemView.findViewById(R.id.title_movie_textView);
            posterSerialImageView = (ImageView) itemView.findViewById(R.id.poster_movie_imageView);
            favoriteSerialimageView = (ImageView) itemView.findViewById(R.id.favorite);
            itemView.setOnClickListener(this);
            favoriteSerialimageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    mCursor.moveToPosition(pos);
                    if(mCursor.getInt(mCursor.getColumnIndex(MovieContract.Serial.COLUMN_FAVORITE))==1){
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MovieContract.Serial.COLUMN_FAVORITE, 0);
                        String selection = MovieContract.Serial.COLUMN_ID + "= ?";
                        Utility.removeFromFavorite(mContext, Integer.toString(mCursor.getInt(mCursor.getColumnIndex(MovieContract.Serial.COLUMN_ID))), view,contentValues, selection,MovieContract.Serial.CONTENT_URI);
                        favoriteSerialimageView.setColorFilter(ContextCompat.getColor(mContext, R.color.nofavorite));

                    }
                    else {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MovieContract.Serial.COLUMN_FAVORITE, 1);
                        String selection = MovieContract.Serial.COLUMN_ID + "= ?";
                        Utility.addToFavorite(mContext, Integer.toString(mCursor.getInt(mCursor.getColumnIndex(MovieContract.Serial.COLUMN_ID))), view, contentValues, selection,MovieContract.Serial.CONTENT_URI);
                        favoriteSerialimageView.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent));
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long _id = mCursor.getLong(mCursor.getColumnIndex(MovieContract.Serial.COLUMN_ID));
            mClickHandler.onClickSerial(_id);
        }
    }
}
