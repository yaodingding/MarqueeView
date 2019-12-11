package com.hummer.marqueeview;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private MarqueeView mMarqueeView;
    private MarqueeView mMarqueeView2, mMarqueeView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMarqueeView = findViewById(R.id.marqueeview);
        mMarqueeView2 = findViewById(R.id.marqueeview2);
        mMarqueeView3 = findViewById(R.id.marqueeview3);
        String second = "100";
        String text = String.format("实现跑马灯倒计时效果 倒计时还有%s秒", second);
        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(this, android.R.style.TextAppearance_Material);
        spannableString.setSpan(foregroundColorSpan, text.indexOf(second), text.indexOf(second) + second.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mMarqueeView.setText(spannableString);
        mMarqueeView2.setText(spannableString);
        mMarqueeView3.setText(spannableString);
        CountDownTimer countDownTimer = new CountDownTimer(100 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String second = String.valueOf(millisUntilFinished / 1000);
                String text = String.format("实现跑马灯倒计时效果 倒计时还有%s秒", second);
                SpannableString spannableString = new SpannableString(text);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
                spannableString.setSpan(foregroundColorSpan, text.indexOf(second), text.indexOf(second) + second.length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mMarqueeView.setText(spannableString);
                mMarqueeView2.setText(spannableString);
                mMarqueeView3.setText(spannableString);

            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
        mMarqueeView.setClickListener(new MarqueeView.OnClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(MainActivity.this, "点击了", Toast.LENGTH_LONG).show();
            }
        });
    }
}
