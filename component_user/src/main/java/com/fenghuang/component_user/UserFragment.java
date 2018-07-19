package com.fenghuang.component_user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fenghuang.component_base.base.BaseFragment;
import com.fenghuang.component_base.utils.ViewFinder;
import com.fenghuang.component_user.camera.ActivityScanerCode;
import com.fenghuang.component_user.camera.CameraActivity;


/**
 * @author billy.qi
 * @since 17/12/8 15:30
 */
public class UserFragment extends BaseFragment {
    TextView mTextView;

    @Override
    protected int setView() {
        return R.layout.fragment_user;
    }

    @Override
    protected void init(View view) {
        mTextView = view.findViewById(R.id.tv_name);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),CameraActivity.class));
            }
        });
    }




//    @SuppressLint("SetTextI18n")
//    @Override
//    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
//        Context context = container.getContext();
//        ScrollView scrollView = new ScrollView(context);
//        LinearLayout layout = new LinearLayout(context);
//        scrollView.addView(layout);
//        layout.setOrientation(LinearLayout.VERTICAL);
//        textView = new TextView(context);
//        layout.addView(textView);
//        textView.setGravity(Gravity.CENTER);
//        CC cc = CC.obtainBuilder("ComponentB")
//                .setActionName("getNetworkData")
//                .cancelOnDestroyWith(this)
//                .build();
//        cc.callAsyncCallbackOnMainThread(new IComponentCallback() {
//                    @Override
//                    public void onResult(CC cc, CCResult result) {
//                        String text = "callId=" + cc.getCallId() + "\n" + JsonFormat.format(result.toString());
//                        Toast.makeText(CC.getApplication(), text, Toast.LENGTH_SHORT).show();
//                        log(text);
//                    }
//                });
//        textView.setText(getString(R.string.demo_a_life_cycle_fragment_notice, cc.getCallId()));
//        log = new TextView(context);
//
//        layout.addView(log);
//        return scrollView;
//    }
//
//
//

}
