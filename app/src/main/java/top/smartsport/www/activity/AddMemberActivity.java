package top.smartsport.www.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.view.annotation.ContentView;

import top.smartsport.www.R;
import top.smartsport.www.adapter.TeamMemberAdapter;
import top.smartsport.www.base.BaseActivity;

/**
 * Created by Administrator on 2017/8/17.
 */
@ContentView(R.layout.activity_add_member)
public class AddMemberActivity extends BaseActivity {


    @Override
    protected void initView() {
        back();
        ListView mListView = (ListView) findViewById(R.id.lv_team);
        View foot = getLayoutInflater().inflate(R.layout.team_member_item,null);
        mListView.addFooterView(foot);
        final TeamMemberAdapter adapter = new TeamMemberAdapter();
        mListView.setAdapter(adapter);
        TextView tvAddMember = (TextView) foot.findViewById(R.id.tv_add_member);
        tvAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addMember();
            }
        });
    }

}
