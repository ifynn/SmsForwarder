package com.fynn.smsforwarder.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fynn.smsforwarder.R;
import com.fynn.smsforwarder.base.BaseFragment;
import com.fynn.smsforwarder.business.SmsFlowPresenter;
import com.fynn.smsforwarder.model.SmsStorageModel;
import com.fynn.smsforwarder.model.bean.Sms;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fynn
 */
public class SmsFlowFragment extends BaseFragment<SmsFlowFragment, SmsStorageModel, SmsFlowPresenter> {

    private RecyclerView mSmsFlowRecycler;

    private List<Sms> mSmsList = new ArrayList<Sms>();

    public SmsFlowFragment() {
        // Required empty public constructor
    }

    public static SmsFlowFragment newInstance() {
        SmsFlowFragment fragment = new SmsFlowFragment();
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
        mSmsFlowRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mSmsFlowRecycler.addItemDecoration(ItemDivider.newInstance(0xffeeeeee));
    }

    @Override
    protected SmsFlowPresenter createPresenter() {
        return new SmsFlowPresenter();
    }

    @Override
    protected SmsStorageModel createModel() {
        return new SmsStorageModel();
    }

    class SmsFlowAdapter extends RecyclerView.Adapter<SmsFlowAdapter.SmsViewHolder> {

        @Override
        public SmsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_sms_flow_item, parent, false);
            return new SmsViewHolder(item);
        }

        @Override
        public void onBindViewHolder(SmsViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mSmsList.size();
        }

        class SmsViewHolder extends RecyclerView.ViewHolder {

            public SmsViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    static class ItemDivider extends RecyclerView.ItemDecoration {

        @ColorInt
        int color;

        public static ItemDivider newInstance(@ColorInt int color) {
            ItemDivider divider = new ItemDivider();
            divider.color = color;
            return divider;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }
}
