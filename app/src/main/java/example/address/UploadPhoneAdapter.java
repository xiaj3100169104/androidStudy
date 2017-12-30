package example.address;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.AdapterAddressBinding;

import java.util.ArrayList;

public class UploadPhoneAdapter extends BaseRecyclerViewAdapter<UploadPhone> implements SectionIndexer {
	public UploadPhoneAdapter(Context context, ArrayList<UploadPhone> list) {
		super(context, list);
	}


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		AdapterAddressBinding bd = DataBindingUtil.inflate(mInflater, R.layout.adapter_address, parent, false);
		return new ViewHolder(bd);
	}

    @Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
		UploadPhone up = getData(position);
		logE(String.valueOf(position), up.getTelephone() + "--" + up.getName() + "--" + up.getSortLetters() + "--" + up.isUploaded());
		String name = up.getName();
		holder.bd.tvFirstName.setText(name.substring(name.length() - 1, name.length()));
		holder.bd.tvName.setText(up.getName());
		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			holder.bd.catalog.setVisibility(View.VISIBLE);
			holder.bd.catalog.setText(up.getSortLetters());
		} else {
			holder.bd.catalog.setVisibility(View.GONE);
		}
		holder.bd.executePendingBindings();

		super.setOnItemClickListener(holder.itemView, position);
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    /**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getItemCount(); i++) {
			String sortStr = ((UploadPhone) list.get(i)).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}


    public class ViewHolder extends RecyclerView.ViewHolder {
		AdapterAddressBinding bd;
		ViewHolder(AdapterAddressBinding bd) {
            super(bd.getRoot());
			this.bd = bd;
		}
	}
}
