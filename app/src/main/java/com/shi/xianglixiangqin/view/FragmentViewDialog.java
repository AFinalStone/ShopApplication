package com.shi.xianglixiangqin.view;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;

/**
 * 用途更广泛的Dialog，可以自定义布局View,使用initview(View view)可以产生不带取消和确定按钮的对话框
 * 使用initView( View view, String strCancel, String strOk,OnButtonClickListener onButtonClickListener)
 * 可以产生带取消和确定按钮的对话框
 * @author SHI
 * 2016年4月6日 16:03:36
 *
 */
public class FragmentViewDialog extends DialogFragment implements OnKeyListener
{
	//布局视图
	private View view;
	String strCancel = "取消";
	String strOk = "确定";
    private OnButtonClickListener onButtonClickListener;
	private boolean IfClickButton = false;

    public interface OnButtonClickListener
    {
        void OnOkClick();
        void OnCancelClick();
    }

    public void initView( View view){
        this.view = view;
    }

	public void initView( View view, String strCancel, String strOk,OnButtonClickListener onButtonClickListener){
		this.view = view;
		this.strCancel = strCancel;
		this.strOk = strOk;
		this.onButtonClickListener = onButtonClickListener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if(onButtonClickListener != null){
			return onCreateCommonViewDialog();
		}else{
			return onCreateViewDialog();
		}
	}

	/**
	 * 带有取消和确定按钮的ViewDialog
	 * @return
	 */
	private Dialog onCreateCommonViewDialog(){
       	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       	builder.setView(view)
		.setPositiveButton(strOk,
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{
						IfClickButton = true;
						if(onButtonClickListener != null)
							onButtonClickListener.OnOkClick();
					}
				})
		.setNegativeButton(strCancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						IfClickButton = true;
						if(onButtonClickListener != null)
						    onButtonClickListener.OnCancelClick();
					}
				});
       	builder.setOnKeyListener(this);
        return builder.create();
	}

	/**
	 * 只有自定义View的ViewDialog
	 * @return
	 */
	private Dialog onCreateViewDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(view);
		return builder.create();
	}


	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
			if(onButtonClickListener != null)
				 onButtonClickListener.OnCancelClick();
		}
		return false;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if(!IfClickButton){
			if(onButtonClickListener != null)
				onButtonClickListener.OnCancelClick();
		}
	}
}


