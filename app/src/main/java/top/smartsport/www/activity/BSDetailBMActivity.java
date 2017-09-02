package top.smartsport.www.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import top.smartsport.www.R;

public class BSDetailBMActivity extends BSDetailActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.adapter_bsss_rl_pay).setVisibility(View.VISIBLE);
        findViewById(R.id.bs_detail_baoming).setVisibility(View.VISIBLE);
    }
}
