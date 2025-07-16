package com.cosmo.thousandfaces.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import com.cosmo.thousandfaces.R;
import com.cosmo.thousandfaces.model.ImageModel;
import android.graphics.BitmapFactory;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private final Context context;
    private final List<ImageModel> images;
    private boolean isEditMode = false;

    public ImageAdapter(Context context, List<ImageModel> images) {
        this.context = context;
        this.images = images;
    }
    public void setEditMode(boolean editMode) {
        this.isEditMode = editMode;
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView;
        if (convertView == null) {
            itemView = View.inflate(context, R.layout.item_image, null);
        } else {
            itemView = convertView;
        }

        ImageView imageView = itemView.findViewById(R.id.imageView);
        ImageView deleteIcon = itemView.findViewById(R.id.deleteIcon);

        String path = images.get(position).getImagePath();
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));

        deleteIcon.setVisibility(isEditMode ? View.VISIBLE : View.GONE);

        deleteIcon.setOnClickListener(v -> {
            // Delete from filesystem
            java.io.File file = new java.io.File(path);
            if (file.exists()) {
                file.delete();
            }
            // Remove from list and update
            images.remove(position);
            notifyDataSetChanged();
        });

        return itemView;
    }

}
