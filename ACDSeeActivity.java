package hxy.ttt.acdseeview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import hxy.ttt.com.acdseeview.R;

/**
 * Created by Admin on 2016/3/17.
 * 本例子图片加载使用xUtils框架，可自由实现
 */
public class ACDSeeActivity extends Activity implements ViewPager.OnPageChangeListener{

    private LinearLayout layout;
    private ViewPager viewPager;
    private ImageView[] dots;

    protected final String ACDSeeData = "ACDSeeData";
    protected final String ACDSeeItem = "ACDSeeItem";
    protected final String ACDSeeAdot = "ACDSeeAdot";

    private ACDSeeListener listener;
    private List<String> datas = new ArrayList<>();
    private Boolean showDot = true;
    private int selectedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        String[] urls = intent.getStringArrayExtra(ACDSeeData);
        showDot = intent.getBooleanExtra(ACDSeeAdot, true);
        selectedItem = intent.getIntExtra(ACDSeeItem, 0);
        if (urls != null && urls.length > 0){
            for (String str: urls) {
                datas.add(str);
            }
        }
    }

    private void initView() {
        setContentView(R.layout.activity_acdsee);
        viewPager = (ViewPager) findViewById(R.id.activity_acdsee_vp);
        viewPager.setAdapter(new ACDSeeAdapter(datas));
        viewPager.setOnPageChangeListener(this);

        layout = (LinearLayout) findViewById(R.id.activity_acdsee_ll);
        if (datas.size()>1){
            if (selectedItem > 0 && selectedItem < datas.size()){
                viewPager.setCurrentItem(selectedItem);
            }else{
                selectedItem = 0;
            }
            if (showDot){
                initDot();
            }
        }

        listener = new ACDSeeListener() {
            @Override
            public void OnAction() {
                ACDSeeActivity.this.finish();
            }
        };
    }

    private void initDot() {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 4, 5, 4);

        layout.removeAllViews();
        dots = new ImageView[datas.size()];
        for (int i = 0; i < datas.size(); i++) {
            ImageView dot = new ImageView(this);
            dot.setLayoutParams(layoutParams);
            dots[i] = dot;
            dots[i].setTag(i);
            dots[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int position = (Integer) v.getTag();
                    viewPager.setCurrentItem(position);
                }
            });

            if (i == selectedItem) {
                dots[i].setBackgroundResource(R.drawable.ic_dot1);
            } else {
                dots[i].setBackgroundResource(R.drawable.ic_dot2);
            }

            layout.addView(dots[i]);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
    @Override
    public void onPageScrollStateChanged(int state) { }
    @Override
    public void onPageSelected(int position) {
        setDot(position);
    }

    private void setDot(int position) {
        if (dots == null || position<0 || position>=dots.length || !showDot) return;
        for (int i = 0; i < dots.length; i++) {
            if (position == i) {
                dots[i].setBackgroundResource(R.drawable.ic_dot1);
            } else {
                dots[i].setBackgroundResource(R.drawable.ic_dot2);
            }
        }
    }

    private class ACDSeeAdapter extends CommonPagerAdapter {

        public ACDSeeAdapter(List data) {
            super(data);
        }

        @Override
        public Object getItemType(int position) {
            return position;
        }

        @NonNull
        @Override
        public AdapterItem onCreateItem(Object type) {
            return new ACDSeeItem();
        }
    }

    private class ACDSeeItem implements AdapterItem {

        private ImageView iv;
        private RelativeLayout layout;
        private BitmapUtils bitmapUtils;

        @Override
        public int getLayoutResId() {
            return R.layout.acdsee_item;
        }

        @Override
        public void onBindViews(View root) {
            bitmapUtils = new BitmapUtils(root.getContext());
            layout = (RelativeLayout) root.findViewById(R.id.acdsee_item_layout);
            iv = (ImageView) root.findViewById(R.id.acdsee_item_iv);
        }

        @Override
        public void onSetViews() {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                // ACDSeeActivity.this.finish();
                if(listener != null){
                    listener.OnAction();
                }
                }
            });
        }

        @Override
        public void onUpdateViews(Object model, int position) {
            bitmapUtils.display(iv, (String)model);
        }
    }

    public interface ACDSeeListener {
        public void OnAction();
    }
}
