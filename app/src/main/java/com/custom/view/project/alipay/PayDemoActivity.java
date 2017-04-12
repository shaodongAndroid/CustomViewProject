package com.custom.view.project.alipay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.custom.view.project.R;
import com.custom.view.project.alipay.util.OrderInfoUtil2_0;

import java.util.Map;

/**
 *  重要说明:
 *  
 *  这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 *  真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 *  防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
 */
public class PayDemoActivity extends FragmentActivity {
	
	/** 支付宝支付业务：入参app_id */
	public static final String APPID = "2016112303146617";
	
	/** 支付宝账户登录授权业务：入参pid值 */
	public static final String PID = "";
	/** 支付宝账户登录授权业务：入参target_id值 */
	public static final String TARGET_ID = "";

	/** 商户私钥，pkcs8格式 */
//	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOjslleSt1Yuu+bBMGJBjsaajyM/pkJW6+XN2ex3K3ZuouUoVi4yGN75BtJMGXLBJ1EVOWXisE5cc3/7Us/LdE2Pfd7TAo8fmDQMGlkCdQZHYcw93jIZyjYGHtF2wMj1p40FqzQyDP6xxBw2gh6CdbDuCvIRzktWlj5oYsxLaLz1AgMBAAECgYBw/OtFGd+jh9IQcuB0mVaYEruinqqY/a8676tp4LkNn0eXoASGLjkQWH6tnsqtdvMKiyAnw1pA+OnySEj4kbodRK3TiHia1yzgLcc482PlQH02/zBdvqw1wYmqXpCtSQ9Pb5JQrt/wYWBWPO9as4dlwC1QTih4W8/otnkXp2V+YQJBAPjHzr238OVR2zxjtfa7139i5aE1YIumQ+v9m1x/8Fswjk8SJZIgGW/wZhXg0fP1LZKN86u0tirgydeiFQSr9L0CQQDvrvuef54kDUqcASZUkYCf7BgXheMPrs8p9S41iw1NOkQxJv12jd907Ehy7k0GTsUeR6xzq5zBBqEZ7Bge9NiZAkAs+6PsNfg+3sAveTALvwdGJ1GyBn7AgX3CjAatgQyq0GrK7VMRyvAOh39N8WrFG83zs+sdXv2ZXy/f55S5VYZZAkEA2Uh0ateecWhrPsn8CaZuSIwruwdxNIJw48SVQ4zvXhd4QmbqWFN9Q+4AL9DAmvtFz5GCUMo+rO5plapmo4mr4QJBAK5vF1yiQbCAARXzaz24AuvtGn79Uw+7PibyeX4p7cRFf2aMl+APjLx658RpQoquu15bn3RXe8zCnM8j2VD6TbY=";
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANHvJ+KUi4j5enMDn/DC4nahLqQ8M6y/d9JDmUsuBWYTmh4wlPyPzRm6e9PD4edj2TSsQoH+xGHzQOCIOw5IDifurGsBDEOAbFWO7Z4wTWOX+nfbb8jHcMJSQxuCzosFbAekHbsh50T0RBtXxlB6PSw6e0B7HV1C+YQoIdWzaEa7AgMBAAECgYBQNrIluB5ssu5fneXx53UCYOniZQJVoj8VPzEGccosS5caq/NxYTy79O41ZuYdNKCcqKks5HyC9KA4arUt6shFNoOlnB9FYDGRE6GGLVRl/4k/0v5mkAVLuLFpLPMu3TJ7oKwq4RpVIuOwT5BE9XbYlkM81QJW7xwsS8SmlBYi8QJBAO9tifx8E926hdteW46Np2Q6onRj0nCimauWn9wYeeYZsrv+5PQloprLy8I3wzgY+1VnDsImsnBVtrXjW3YufpMCQQDgdwNzuL8s2hkse+4bjYvD37yT+b0gJKPHodJPVF2G47+tNL/mSJlPVI7k5LfUpG5DiHuIIBpFm/el/ACi8Ig5AkEAqwqTwr1Y69NuALK9sW7coCuLFp6hHTnrkbr+5JrPqmO9XbPQF+Iy8WaZX1u6qH6gPfhrgKNoYfu5lOKg2iKI5QJBAKYGIV2EtpKcNqZ6e+GRTmFwqXDh9YtCzokoHgBjLFJLPkkEjTPtWsgciRywN3Bmts/osUY8SHJYLFJvFKFwO7ECQBu7n9n5s1n7tuLj0a0ME6G31O94YK50B2iJUi39FbWmxUmhADZYjwHGYzTYzCjk8mDel1qCiVva49/Oz0BsMoA=";

	private static final int SDK_AUTH_FLAG = 2;
	private static final int SDK_PAY_FLAG = 1;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				@SuppressWarnings("unchecked")
				PayResult payResult = new PayResult((Map<String, String>) msg.obj);
				/**
				 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为9000则代表支付成功
				if (TextUtils.equals(resultStatus, "9000")) {
					// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
					Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
				} else {
					// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
					Toast.makeText(PayDemoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case SDK_AUTH_FLAG: {
				@SuppressWarnings("unchecked")
				AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
				String resultStatus = authResult.getResultStatus();

				// 判断resultStatus 为“9000”且result_code
				// 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
				if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
					// 获取alipay_open_id，调支付时作为参数extern_token 的value
					// 传入，则支付账户为该授权账户
					Toast.makeText(PayDemoActivity.this,
							"授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
							.show();
				} else {
					// 其他状态值则为授权失败
					Toast.makeText(PayDemoActivity.this,
							"授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

				}
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_main);
	}
	
	/**
	 * 支付宝支付业务
	 * 
	 * @param v
	 */
	public void payV2(View v) {
		if (TextUtils.isEmpty(APPID) || TextUtils.isEmpty(RSA_PRIVATE)) {
			new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							//
							finish();
						}
					}).show();
			return;
		}
	
		/**
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
		 * 
		 * orderInfo的获取必须来自服务端；
		 */
		Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID);
		String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
		Log.d("info","---------orderParam = "+orderParam);
		String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
		final String orderInfo = orderParam + "&" + sign;
		Log.d("info","---------orderInfo = "+orderInfo);
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(PayDemoActivity.this);
				Map<String, String> result = alipay.payV2(orderInfo, true);
				Log.i("msp", result.toString());
				
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * 支付宝账户授权业务
	 * 
	 * @param v
	 */
	public void authV2(View v) {
		if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID) || TextUtils.isEmpty(RSA_PRIVATE)
				|| TextUtils.isEmpty(TARGET_ID)) {
			new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
						}
					}).show();
			return;
		}

		/**
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
		 * 
		 * authInfo的获取必须来自服务端；
		 */
		Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID);
		String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);
		String sign = OrderInfoUtil2_0.getSign(authInfoMap, RSA_PRIVATE);
		final String authInfo = info + "&" + sign;
		Runnable authRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造AuthTask 对象
				AuthTask authTask = new AuthTask(PayDemoActivity.this);
				// 调用授权接口，获取授权结果
				Map<String, String> result = authTask.authV2(authInfo, true);

				Message msg = new Message();
				msg.what = SDK_AUTH_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread authThread = new Thread(authRunnable);
		authThread.start();
	}
	
	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
	 * 
	 * @param v
	 */
	public void h5Pay(View v) {
		Intent intent = new Intent(this, H5PayDemoActivity.class);
		Bundle extras = new Bundle();
		/**
		 * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
		 * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
		 * 商户可以根据自己的需求来实现
		 */
		String url = "http://m.taobao.com";
		// url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
		extras.putString("url", url);
		intent.putExtras(extras);
		startActivity(intent);
	}

}