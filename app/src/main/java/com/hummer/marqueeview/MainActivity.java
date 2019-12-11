package com.hummer.marqueeview;

import android.os.CountDownTimer;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private MarqueeView mMarqueeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMarqueeView = findViewById(R.id.marqueeview);
        mMarqueeView.setText("从现在距离2019年结束 倒计时还有 " + 30000 / 1000 + "秒");

        CountDownTimer countDownTimer = new CountDownTimer(20 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished < 10 * 1000) {
                    mMarqueeView.setText("从现在距离2019年结束 倒计时还有 " + millisUntilFinished / 1000 + "秒");
                } else {
                    mMarqueeView.setText("从现在 倒计时还有 " + millisUntilFinished / 1000 + "秒");

                }
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
