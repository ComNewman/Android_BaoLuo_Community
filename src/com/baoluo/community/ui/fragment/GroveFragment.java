package com.baoluo.community.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.ZoomView;
import com.baoluo.im.ui.AtyDormList;

/**
 * 小树林
 * 
 * @author xiangyang.fu
 * 
 */
@SuppressLint("NewApi")
public class GroveFragment extends Fragment implements OnClickListener {

	private View view;
	private Button btnDorm;
//	private ZoomView mZoomView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frg_grove, container, false);
		initView();
		return view;
	}

	private void initView() {
//		mZoomView    = (ZoomView)view.findViewById(R.id.zoomView);
//		mZoomView.smoothZoomTo(1,0,0);
		btnDorm = (Button) view.findViewById(R.id.btn_drom);
		btnDorm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.btn_drom:
			startActivity(new Intent(getActivity(),AtyDormList.class));
			break;
		}
	}

}
