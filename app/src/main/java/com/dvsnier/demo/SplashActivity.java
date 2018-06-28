package com.dvsnier.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * the splash activity,as the app launcher page
 *
 * @author dovsnier
 * @version 1.0.3
 * @since JDK 1.7
 */
public class SplashActivity extends Activity {

    public static final int MESSAGE_TEST_EXCEPTION = 100;
    public static final int MESSAGE_TEST_OTHER = 1000;
    private Button btn_exception;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_TEST_EXCEPTION:
                    //test exception example
                    testException();
                    break;
                case MESSAGE_TEST_OTHER:
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_exception:
                    Toast.makeText(SplashActivity.this, "2s 后开始测试", Toast.LENGTH_SHORT).show();
                    handler.sendMessageDelayed(handler.obtainMessage(MESSAGE_TEST_EXCEPTION), 3 * 1000);
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        btn_exception = (Button) findViewById(R.id.btn_exception);
        btn_exception.setOnClickListener(onClickListener);
    }

    private void testException() {
        Integer integer = new Integer("str123");
    }
}
