package com.hummer.marqueeview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Function:    跑马灯 使用RecyclerView 实现
 * Author:      yaodingding
 * Create:      2019-12-03
 * Modtime:     2019-12-03
 */
public class MarqueeView extends RecyclerView {
    private Context mContext;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ValueAnimator mValueAnimator;
    private CharSequence mText;
    private float mTextSize = 30;
    private int mTextColor = Color.parseColor("#000000");
    private float mMarqueeSpeed = 5;
    private int mItemCount = 1;
    private OnClickListener mClickListener;
    private int mWidth;

    public MarqueeView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public MarqueeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initTypedArray(context, attrs);
        initView(context);
    }

    public MarqueeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs);
        initView(context);
    }


    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Marquee_Text_View);
        try {
            mTextSize = ta.getDimension(R.styleable.Marquee_Text_View_marquee_textsize, 20);
            mTextColor = ta.getColor(R.styleable.Marquee_Text_View_marquee_textcolor, Color.parseColor("#000000"));
            mMarqueeSpeed = ta.getDimension(R.styleable.Marquee_Text_View_marquee_speed, 5);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (ta != null) {
                ta.recycle();
            }
        }
    }


    private void initView(Context context) {
        this.mContext = context;
        mLinearLayoutManager = new LinearLayoutManager(mContext, HORIZONTAL, false);
        setLayoutManager(mLinearLayoutManager);
        addItemDecoration(new ItemDecoration(350));
        mAdapter = new RecyclerView.Adapter<ViewHolder>() {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.widget_marquee_view_item, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                holder.mTextView.setText(mText);
            }

            @Override
            public int getItemCount() {
                return mItemCount;
            }
        };
        setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public void setText(CharSequence text) {
        this.mText = text;
        startMarquee();
    }

    private void startMarquee() {
        if (mWidth != 0 && mWidth < measureTextWidth(mText)) {
            startAnim();
            mItemCount = Integer.MAX_VALUE;
        } else {
            mItemCount = 1;
            cancleAni();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
    }

    private void startAnim() {
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            return;
        }
        mValueAnimator = ValueAnimator.ofInt(0, Integer.MAX_VALUE);
        mValueAnimator.addUpdateListener(new AnimatorUpdateListener(this));
        mValueAnimator.addListener(new AnimatorListener(this));
        mValueAnimator.setDuration(Integer.MAX_VALUE);
        mValueAnimator.start();
    }

    private void cancleAni() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
    }

    private float measureTextWidth(CharSequence text) {
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(mTextSize);
        float layoutWidth = StaticLayout.getDesiredWidth(text, textPaint);
        return layoutWidth;
    }

    private void marqueeText() {
        scrollBy((int) mMarqueeSpeed, 0);
    }

    private void resetMarqueeText() {
        scrollTo(0, 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_text);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            mTextView.setTextColor(mTextColor);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onClick();
                    }
                }
            });
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancleAni();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void setClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    private class ItemDecoration extends RecyclerView.ItemDecoration {

        private final int space;

        public ItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                                   @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.right = space;
        }
    }

    public static class AnimatorListener implements Animator.AnimatorListener {
        private final WeakReference<MarqueeView> mWeakmarqueeTextView;

        public AnimatorListener(MarqueeView marqueeTextView) {
            mWeakmarqueeTextView = new WeakReference<>(marqueeTextView);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            resetMarqueeText();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            resetMarqueeText();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            resetMarqueeText();
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            resetMarqueeText();
        }

        private void resetMarqueeText() {
            if (mWeakmarqueeTextView != null && mWeakmarqueeTextView.get() != null) {
                MarqueeView textView = mWeakmarqueeTextView.get();
                textView.resetMarqueeText();
            }
        }
    }

    public static class AnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private final WeakReference<MarqueeView> mWeakmarqueeTextView;

        public AnimatorUpdateListener(MarqueeView marqueeTextView) {
            mWeakmarqueeTextView = new WeakReference<>(marqueeTextView);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (mWeakmarqueeTextView != null && mWeakmarqueeTextView.get() != null) {
                MarqueeView textView = mWeakmarqueeTextView.get();
                textView.marqueeText();
            }
        }
    }

    public interface OnClickListener {
        public void onClick();
    }
}
