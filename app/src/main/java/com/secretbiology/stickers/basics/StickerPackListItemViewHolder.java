package com.secretbiology.stickers.basics;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class StickerPackListItemViewHolder extends RecyclerView.ViewHolder {

    View container;
    TextView titleView;
    TextView publisherView;
    TextView filesizeView;
    ImageView addButton;
    LinearLayout imageRowView;

    StickerPackListItemViewHolder(final View itemView) {
        super(itemView);
        container = itemView;
        titleView = itemView.findViewById(R.id.sticker_pack_title);
        publisherView = itemView.findViewById(R.id.sticker_pack_publisher);
        filesizeView = itemView.findViewById(R.id.sticker_pack_filesize);
        addButton = itemView.findViewById(R.id.add_button_on_list);
        imageRowView = itemView.findViewById(R.id.sticker_packs_list_item_image_list);
    }
}