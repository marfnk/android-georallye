package org.marfnk.georallye.adapter;

import org.marfnk.georallye.R;
import org.marfnk.georallye.data.Constants;
import org.marfnk.georallye.models.Quest;
import org.marfnk.georallye.services.QuestManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuListAdapter extends BaseAdapter {
    private Context context;
    private QuestManager questManager;
    private LayoutInflater inflater;

    public MenuListAdapter(Context pContext, QuestManager qm) {
        context = pContext;
        questManager = qm;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.drawer_list_item, parent, false);

        TextView txtTitle = (TextView) itemView.findViewById(R.id.drawer_itemName);
        txtTitle.setTypeface(Constants.TEXT_TYPEFACE);
        ImageView imgIcon = (ImageView) itemView.findViewById(R.id.drawer_icon);

        Quest thisQuest = questManager.getActivatedQuestByPosition(position);
        txtTitle.setText(thisQuest.getTitle());
        if (thisQuest.isCompleted()) {
            imgIcon.setImageResource(R.drawable.quest_complete);
        } else {
            imgIcon.setImageResource(R.drawable.quest_incomplete);
        }
        
        return itemView;
    }

    @Override
    public int getCount() {
        return questManager.getActivatedQuests().size();
    }

    @Override
    public Object getItem(int position) {
        return questManager.getActivatedQuestByPosition(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
