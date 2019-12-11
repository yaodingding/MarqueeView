# MarqueeView
# 跑马灯执行过程中 由于文案更新不会导致跑马灯暂停。
1、marquee_textsize 字体大小。
2、marquee_textcolor字体颜色。
3、marquee_speed执行速度。
`<com.hummer.marqueeview.MarqueeView
         android:id="@+id/marqueeview"
         android:layout_width="match_parent"
         android:background="#00ff00"
         app:marquee_textsize="23sp"
         app:marquee_textcolor="#ff0000"
         app:marquee_speed="3dp"
         android:layout_height="wrap_content" />`