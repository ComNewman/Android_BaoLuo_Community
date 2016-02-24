package com.baoluo.community.support.task;

import java.lang.reflect.Type;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.Configs;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.T;

/**
 * 回调更新视图
 * 
 * @author tao.lai
 * 
 */
public class UpdateViewHelper {

	public static class UpdateViewListener {
		private Class<?> cls;
		private Type type;

		public UpdateViewListener(Class<?> objCls) {
			this.cls = objCls;
		}

		public UpdateViewListener(Type type) {
			this.type = type;
		}

		public UpdateViewListener(){

		}

		public void onComplete(Object obj) {

		}

		/**
		 * 异步操起前执行
		 */
		public void onPreExecute() {

		}

		/**
		 * 异步操作完成执行
		 */
		public void onCompleteExecute(String responseStr) {
			if (Configs.RESPONSE_ERROR.equals(responseStr)) {
				L.i("UpdateViewHelper", "服务器响应失败...");
				onFail();
				return;
			}
			Object obj = null;
			if(cls != null){
				obj = GsonUtil.getInstance().str2Obj(responseStr, this.cls);
			}else if(this.type != null){
				obj = GsonUtil.getInstance().String2List(responseStr,this.type);
			}else{
				obj = responseStr;
			}
			if(obj == null && (cls != null || this.type != null)){
//				T.showShort(GlobalContext.getInstance(),"服务器响应失败...");
				L.e("UpdateViewHelper", "服务器响应失败...");
				onFail();
				return;
			}else{
				onComplete(obj);
			}
		}

		public void onFail() {

		}
	}
}
