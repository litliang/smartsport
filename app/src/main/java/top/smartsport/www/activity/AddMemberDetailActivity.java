package top.smartsport.www.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;

/**
 * Created by Administrator on 2017/8/17.
 */
@ContentView(R.layout.activity_add_member_detail)
public class AddMemberDetailActivity extends BaseActivity {
    @ViewInject(R.id.et_team_name)
    private TextView name;
    @ViewInject(R.id.weizhi)
    private TextView position;
    @ViewInject(R.id.haoma)
    private TextView number;
    @ViewInject(R.id.btn_add)
    private Button add;
    private String team_id;

    @Override
    protected void initView() {
        team_id = getIntent().getStringExtra("team_id");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMember(new FunCallback() {
                    @Override
                    public void onSuccess(Object result, List object) {
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Object result, List object) {

                    }

                    @Override
                    public void onCallback(Object result, List object) {

                    }
                });
            }
        });
        back();
    }
    private void addMember(FunCallback func) {
            MapBuilder m = MapBuilder.build().add("action", "editMyTeam");
            m.add("team_id", team_id);
            m.add("name",name.getText().toString());
            m.add("type", 0);
            m.add("number", number.getText().toString());
            m.add("position", position.getText().toString());
            callHttp(m.get(), func);
    }


}