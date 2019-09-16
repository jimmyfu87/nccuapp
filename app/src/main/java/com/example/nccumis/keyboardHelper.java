package com.example.nccumis;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class keyboardHelper {


    private Context context;
    private KeyboardView keyboardView;
    private EditText input_amount;
    private Keyboard k1;// 自定义键盘
   private KeyboardCallBack callBack;//按键回调监听

    public keyboardHelper(Context context, KeyboardView keyboardView) {
        this(context, keyboardView, null);
    }


    public keyboardHelper(Context context, KeyboardView keyboardView, Object o) {
        this.context = context;
        k1 = new Keyboard(context, R.xml.keyboard);
        this.keyboardView = keyboardView;
        this.keyboardView.setOnKeyboardActionListener(listener);//设置键盘监听
        this.keyboardView.setKeyboard(k1);//设置默认键盘
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        this.callBack = callBack;
    }
        private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {

            public void swipeUp() {
            }

            @Override
            public void swipeRight() {

            }

            @Override
            public void swipeLeft() {
            }

            @Override
            public void swipeDown() {
            }

            @Override
            public void onText(CharSequence text) {
                //当key中有keyOutputText属性时，点击键盘会触发该方法，回调keyOutputText的值
                Editable editable = input_amount.getText();
                int end = input_amount.getSelectionEnd();
                editable.delete(0, end);
                editable.insert(0, text);
            }

            @Override
            public void onRelease(int primaryCode) {
            }

            @Override
            public void onPress(int primaryCode) {
            }

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                //设置了codes属性后，点击键盘会触发该方法，回调codes的值
                //codes值与ASCLL码对应
                Editable editable = input_amount.getText();
                int start = input_amount.getSelectionStart();
                int end = input_amount.getSelectionEnd();
                switch (primaryCode) {


                    case Keyboard.KEYCODE_DELETE:
                        if (editable != null && editable.length() > 0) {
                            if (start == end) {
                                editable.delete(start - 1, start);
                            } else {
                                editable.delete(start, end);
                            }
                        }
                        break;
                    case Keyboard.KEYCODE_CANCEL:
                        keyboardView.setVisibility(View.GONE);
                        break;




                    default:
                        if (start != end) {
                            editable.delete(start, end);
                        }
                        editable.insert(start, Character.toString((char) primaryCode));
                        break;
                }
                if (callBack != null) {
                    callBack.keyCall(primaryCode);
                }
            }
        };

        //在显示键盘前应调用此方法，指定EditText与KeyboardView绑定
        public void setEditText(EditText editText) {
            this.input_amount = editText;
            //关闭进入该界面获取焦点后弹出的系统键盘
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
            //隐藏该EditText获取焦点而要弹出的系统键盘
            KeyboardUtil.hideSoftInput(input_amount);
        }

        //Activity中获取焦点时调用，显示出键盘
        public void show() {
            int visibility = keyboardView.getVisibility();
            if (visibility == View.GONE || visibility == View.INVISIBLE) {
                keyboardView.setVisibility(View.VISIBLE);
            }
        }

        //隐藏键盘
        public void hide() {
            int visibility = keyboardView.getVisibility();
            if (visibility == View.VISIBLE) {
                keyboardView.setVisibility(View.GONE);
            }
        }

        public boolean isVisibility() {
            if (keyboardView.getVisibility() == View.VISIBLE) {
                return true;
            } else {
                return false;
            }
        }

        public interface KeyboardCallBack {
            void keyCall(int code);
        }

        //设置回调，用于自定义特殊按键在不同界面或EditText的处理
        public void setCallBack(KeyboardCallBack callBack) {
            this.callBack = callBack;
        }
    }
