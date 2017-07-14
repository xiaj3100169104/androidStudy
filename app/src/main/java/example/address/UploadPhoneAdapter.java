package example.address;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UploadPhoneAdapter extends BaseRecyclerViewAdapter<UploadPhone> implements SectionIndexer {
	public UploadPhoneAdapter(Context context, List<UploadPhone> list) {
		super(context, list);
	}


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.adapter_address, parent, false));
    }

    @Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
		UploadPhone up = getData(position);
		logE(String.valueOf(position), up.getTelephone() + "--" + up.getName() + "--" + up.getSortLetters() + "--" + up.isUploaded());
		String name = up.getName();
		setText(holder.tv_first_name, name.substring(name.length() - 1, name.length()));
		setText(holder.tv_name, up.getName());
		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			holder.tvLetter.setVisibility(View.VISIBLE);
			holder.tvLetter.setText(up.getSortLetters());
		} else {
			holder.tvLetter.setVisibility(View.GONE);
		}
		super.setOnItemClickListener(holder, position);
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
		@Bind(R.id.tv_first_name)
		TextView tv_first_name;
		@Bind(R.id.tv_name)
		TextView tv_name;
		@Bind(R.id.catalog)
		TextView tvLetter;
		@Bind(R.id.tv_add)
		TextView tv_add;
		ViewHolder(View view) {
            super(view);
			ButterKnife.bind(this, view);
		}
	}
}
