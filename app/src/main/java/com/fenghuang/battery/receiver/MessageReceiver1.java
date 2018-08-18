package com.fenghuang.battery.receiver;

import android.content.Context;
import android.content.Intent;
import com.tencent.android.tpush.XGLocalMessage;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;


public class MessageReceiver1 extends XGPushBaseReceiver {
	private Intent intent = new Intent("com.jpp.mall.activity.UPDATE_LISTVIEW");
	public static final String LogTag = "TPushReceiver";

	private void show() {
//		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}


	private long code;
	// 通知展示
	@Override
	public void onNotifactionShowedResult(Context context,
			XGPushShowedResult notifiShowedRlt) {
		if (context == null || notifiShowedRlt == null) {
			return;
		}
		//Logger.e(LogTag,"onNotifactionShowedResult" + notifiShowedRlt.toString());
		//设置在通知发出的时候的音频
		//新建本地通知
		// APP自主处理消息的过程...
		XGLocalMessage localMessage = new XGLocalMessage();
		localMessage.setTitle(localMessage.getTitle());
		localMessage.setContent(notifiShowedRlt.getContent());
		localMessage.setRing(1);
		localMessage.setType(1);
		localMessage.setVibrate(1);
//		localMessage.setNotificationId(1);
//		localMessage.setAction_type(1);
//		localMessage.setStyle_id(0);
		localMessage.setActivity("com.jpp.mall.acitivity.NotifyCenterActivity");
		XGPushManager.addLocalNotification(context, localMessage);
		show();

	}

	@Override
	public void onUnregisterResult(Context context, int errorCode) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "反注册成功";
		} else {
			text = "反注册失败" + errorCode;
		}

	}

	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"设置成功";
		} else {
			text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
		}

	}


	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"删除成功";
		} else {
			text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
		}

	}

	// 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
		String text = "";
		if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
			// 通知在通知栏被点击啦。。。。。
			// APP自己处理点击的相关动作
			// 这个动作可以在activity的onResume也能监听，请看第3点相关内容
			text = "通知被打开 :" + message;
		} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			// 通知被清除啦。。。。
			// APP自己处理通知被清除后的相关动作
			text = "通知被清除 :" + message;
		}
//		Toast.makeText(context, "广播接收到通知被点击:" + message.toString(),
//				Toast.LENGTH_SHORT).show();
		// 获取自定义key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP自主处理的过程。。。
	}

	@Override
	public void onRegisterResult(Context context, int errorCode,
                                 XGPushRegisterResult message) {
		// TODO Auto-5 method stub
		if (context == null || message == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = message + "注册成功";
			// 在这里拿token
			String token = message.getToken();
		} else {
			text = message + "注册失败，错误码：" + errorCode;
		}
	}

	// 消息透传
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {

	}

}
