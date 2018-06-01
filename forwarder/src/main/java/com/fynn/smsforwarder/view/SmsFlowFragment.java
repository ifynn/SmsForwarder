package com.fynn.smsforwarder.view;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.util.Linkify;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fynn.smsforwarder.R;
import com.fynn.smsforwarder.base.BaseFragment;
import com.fynn.smsforwarder.business.AuthCodeCache;
import com.fynn.smsforwarder.business.presenter.DefaultPresenter;
import com.fynn.smsforwarder.common.SmsReceiverManager;
import com.fynn.smsforwarder.model.SmsStorageModel;
import com.fynn.smsforwarder.model.bean.Sms;
import com.fynn.smsforwarder.model.bean.SmsReceiver;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.fynn.appu.util.CharsUtils;
import org.fynn.appu.util.DateHelper;
import org.fynn.appu.util.DensityUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fynn
 */
public class SmsFlowFragment extends BaseFragment<BaseView, SmsStorageModel, DefaultPresenter> {

    private RecyclerView mSmsFlowRecycler;

    private List<Sms> mSmsList = new ArrayList<Sms>();
    private SmsFlowAdapter mSmsFlowAdapter;

    private long totalCount;
    private int pageCount = 10;
    private int currentPage = 0;

    private ViewInteraction interaction;
    private boolean refreshing;

    public SmsFlowFragment() {
        // Required empty public constructor
    }

    public static SmsFlowFragment newInstance(ViewInteraction interaction) {
        SmsFlowFragment fragment = new SmsFlowFragment();
        fragment.interaction = interaction;
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_sms_flow;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mSmsFlowRecycler = findViewById(R.id.recycler_sms_flow);
    }

    @Override
    protected void initActions(Bundle savedInstanceState) {
        totalCount = mPresenter.getDbRecordCount();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);

        mSmsFlowRecycler.setLayoutManager(layoutManager);
        mSmsFlowRecycler.addItemDecoration(
                ItemDivider.newInstance(0xffcccccc, DensityUtils.dip2px(6)));

        mSmsFlowAdapter = new SmsFlowAdapter();
        mSmsFlowRecycler.setAdapter(mSmsFlowAdapter);

        mSmsFlowRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    return;
                }

                recyclerView.invalidateItemDecorations();
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager)
                        recyclerView.getLayoutManager();

                if (manager.getChildCount() <= 0) {
                    return;
                }

                int[] into = new int[2];
                manager.findLastCompletelyVisibleItemPositions(into);

                int last = into[0];

                if (last < into[1]) {
                    last = into[1];
                }

                if (last >= manager.getChildCount() - 1) {
                    fetchNextPage();
                }
            }
        });

        refresh();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            return;
        }

        if (totalCount != mPresenter.getDbRecordCount()) {
            totalCount = mPresenter.getDbRecordCount();
            refresh();
        }
    }

    private void refresh() {
        if (refreshing) {
            return;
        }
        refreshing = true;
        mSmsList.clear();
        currentPage = 0;
        mSmsList.addAll(mPresenter.readSms(currentPage * pageCount, pageCount));
        mSmsFlowAdapter.notifyDataSetChanged();
        refreshing = false;
    }

    private void fetchNextPage() {
        if (refreshing) {
            return;
        }

        if (mSmsList.size() >= totalCount) {
            return;
        }

        refreshing = true;
        currentPage++;
        mSmsList.addAll(mPresenter.readSms(currentPage * pageCount, pageCount));
        mSmsFlowAdapter.notifyDataSetChanged();
        refreshing = false;
    }

    @Override
    protected DefaultPresenter createPresenter() {
        return new DefaultPresenter();
    }

    @Override
    protected SmsStorageModel createModel() {
        return interaction.getModel();
    }

    static class ItemDivider extends RecyclerView.ItemDecoration {

        @ColorInt
        int color;

        int dividerSize;

        Paint dividerPaint;

        public static ItemDivider newInstance(@ColorInt int color, int dividerSize) {
            ItemDivider divider = new ItemDivider();
            divider.color = color;
            divider.dividerSize = dividerSize;

            Paint p = new Paint();
            p.setColor(color);
            divider.dividerPaint = p;

            return divider;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            outRect.bottom = dividerSize;

            StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) parent.getLayoutManager();
            int pos = manager.getPosition(view);

            boolean left = ((StaggeredGridLayoutManager.LayoutParams)
                    view.getLayoutParams()).getSpanIndex() == 0;

            if (left) {
                outRect.right = dividerSize / 2;
                outRect.left = dividerSize;
            } else {
                outRect.left = dividerSize / 2;
                outRect.right = dividerSize;
            }

            if (pos == 0 || pos == 1) {
                outRect.top = dividerSize;
            }
        }
    }

    class SmsFlowAdapter extends RecyclerView.Adapter<SmsFlowAdapter.SmsViewHolder> {

        @Override
        public SmsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_sms_flow_item, parent, false);
            return new SmsViewHolder(item);
        }

        @Override
        public void onBindViewHolder(final SmsViewHolder holder, int position) {
            final Sms s = mSmsList.get(position);
            final StringBuilder msg = new StringBuilder(s.msg);

            SmsReceiver receiver = SmsReceiverManager.getSmsReceiver(s);

            if (receiver != null && receiver.cardSlot >= 0) {
                int slot = receiver.cardSlot;
                msg.append("【").append("卡").append(slot + 1).append("】");
            }

            Pair<String, String> p = AuthCodeCache.get().fetchCode(s);
            boolean hasCode = !CharsUtils.isEmptyAfterTrimming(p.first) &&
                    !CharsUtils.isEmptyAfterTrimming(p.second);

            if (!hasCode) {
                holder.mWatchDetails.setVisibility(View.GONE);
                holder.mMsg.setVisibility(View.VISIBLE);
                holder.mSmsCodeDesc.setVisibility(View.GONE);
                holder.mSmsCode.setVisibility(View.GONE);
            } else {
                holder.mWatchDetails.setVisibility(View.VISIBLE);
                holder.mMsg.setVisibility(View.GONE);
                holder.mSmsCodeDesc.setVisibility(View.VISIBLE);
                holder.mSmsCode.setVisibility(View.VISIBLE);
            }

            if (hasCode) {
                holder.mWatchDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessageDialog(s.address, msg);
                    }
                });

                holder.mSmsCode.setText(p.second);
                holder.mSmsCodeDesc.setText(p.first);
            }

            if (!hasCode) {
                holder.mMsg.setText(msg);
            }

            holder.mFrom.setText(s.address);
            holder.mDate.setText(DateHelper.formatDate(new Date(s.date), "yyyy/MM/dd HH:mm"));
        }

        @Override
        public int getItemCount() {
            return mSmsList.size();
        }

        private void showMessageDialog(String from, StringBuilder text) {
            QMUIDialog.MessageDialogBuilder builder = new QMUIDialog.MessageDialogBuilder(getActivity())
                    .setTitle(from)
                    .setMessage(text)
                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                        }
                    });
            QMUIDialog dialog = builder.create();

            builder.getTextView().setTextIsSelectable(true);
            builder.getTextView().setAutoLinkMask(Linkify.ALL);
            builder.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

            int color = ResourcesCompat.getColor(
                    getResources(), R.color.qmui_config_color_gray_3, null);
            builder.getTextView().setTextColor(color);

            builder.getTitleView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            dialog.show();
        }

        class SmsViewHolder extends RecyclerView.ViewHolder {

            TextView mSmsCode;
            TextView mSmsCodeDesc;
            TextView mFrom;
            TextView mMsg;
            TextView mDate;
            TextView mWatchDetails;

            public SmsViewHolder(View itemView) {
                super(itemView);
                mSmsCode = itemView.findViewById(R.id.tv_sms_code);
                mSmsCodeDesc = itemView.findViewById(R.id.tv_sms_code_desc);
                mFrom = itemView.findViewById(R.id.tv_from);
                mMsg = itemView.findViewById(R.id.tv_msg);
                mDate = itemView.findViewById(R.id.tv_date);
                mWatchDetails = itemView.findViewById(R.id.tv_watch_details);
            }
        }
    }
}
