package com.shuimunianhua.xianglixiangqin.view;

import com.shuimunianhua.xianglixiangqin.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 带有自定义标题和和内容，以及两个按钮的Dialog
 * @author SHI
 * 2016年4月6日 15:19:39
 */
public class FragmentCommonDialog extends android.support.v4.app.DialogFragment implements OnKeyListener
{
	String strTitle = "提示";
	String strMessage = "";
	String strCancel = "取消";
	String strOk = "确定";
    private OnButtonClickListener onButtonClickListener;
	private boolean IfClickButton = false;

    public interface OnButtonClickListener  
    {  
        void OnOkClick(); 
        void OnCancelClick();
    }

	public void initView(String strTitle, String strMessage, String strCancel, String strOk, OnButtonClickListener onButtonClickListener){
		this.strTitle = strTitle;
		this.strMessage = strMessage;
		this.strCancel = strCancel;
		this.strOk = strOk;
		this.onButtonClickListener = onButtonClickListener;
	}


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)  
    {
       	View view = View.inflate(getActivity(), R.layout.dialog_common, null);
       	((TextView)view.findViewById(R.id.tv_title)).setText(strTitle);
       	((TextView)view.findViewById(R.id.tv_message)).setText(strMessage);
       	
       	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  
       	builder.setView(view)
		.setPositiveButton(strOk,new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{
						IfClickButton = true;
						if(onButtonClickListener != null) {
							onButtonClickListener.OnOkClick();
						}
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




