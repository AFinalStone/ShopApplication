package com.afinalstone.androidstudy.view.fragment;


import android.app.FragmentTransaction;

import com.shi.xianglixiangqin.R;

/**
 * Fragment切换动画工厂
 * 
 * @author SHI 2016年4月26日 17:33:06
 */
public class FragmentAnimationFactory {

	/** X轴缩放 **/
	public static final int ANIMATION_SCALEX = 0;
	/** Y轴缩放 **/
	public static final int ANIMATION_SCALEY = 1;
	/** XY轴绽放 **/
	public static final int ANIMATION_SCALEXY = 2;
	/** 淡入淡出 **/
	public static final int ANIMATION_FADE = 3;
	/** 水平翻页 **/
	public static final int ANIMATION_FLIP_HORIZONTAL = 4;
	/** 垂直翻页 **/
	public static final int ANIMATION_FLIP_VERTICAL = 5;
	/** 垂直滑动 **/
	public static final int ANIMATION_SLIDE_VERTICAL = 6;
	/** 水平滑动 **/
	public static final int ANIMATION_SLIDE_HORIZONTAL = 7;
	/** 向上推的水平滑动 **/
	public static final int ANIMATION_SLIDE_HORIZONTAL_PUSH_TOP = 8;
	/** 向左推的垂直滑动 **/
	public static final int ANIMATION_SLIDE_VERTICAL_PUSH_LEFT = 9;
	/** 交叉的滑动 **/
	public static final int ANIMATION_GLIDE = 10;
	/** 弹出 **/
	public static final int ANIMATION_STACK = 11;
	/** 魔方 **/
	public static final int ANIMATION_CUBE = 12;
	/** 向下旋转 **/
	public static final int ANIMATION_ROTATE_DOWN = 13;
	/** 向上旋转 **/
	public static final int ANIMATION_ROTATE_UP = 14;
	/** 手风琴 **/
	public static final int ANIMATION_ACCORDION = 15;
	/** 水平翻转 **/
	public static final int ANIMATION_TABLE_HORIZONTAL = 16;
	/** 垂直翻转 **/
	public static final int ANIMATION_TABLE_VERTICAL = 17;
	/** 左角落放大 **/
	public static final int ANIMATION_ZOOM_FROM_LEFT_CORNER = 18;
	/** 右角落放大 **/
	public static final int ANIMATION_ZOOM_FROM_RIGHT_CORNER = 19;

	private static FragmentAnimationFactory mFragmentTransaction;

	public FragmentAnimationFactory() {
		super();
	}

	public static FragmentAnimationFactory getInstance() {
		if (mFragmentTransaction == null) {
			mFragmentTransaction = new FragmentAnimationFactory();
		}
		return mFragmentTransaction;
	}

	/**
	 * 设置Fragment切换时的动画
	 * 
	 * @param mFragmentTransaction
	 *            需要设置切换动画的Fragment事务对象
	 * @param transitionType
	 *            风格类型
	 */
	public void setFragmentAnimation(FragmentTransaction mFragmentTransaction,
			int transitionType) {

		if (mFragmentTransaction == null) {
			return;
		}

		switch (transitionType) {
		case ANIMATION_SCALEX:
			transitionScaleX(mFragmentTransaction);
			break;
		case ANIMATION_SCALEY:
			transitionScaleY(mFragmentTransaction);
			break;
		case ANIMATION_SCALEXY:
			transitionScaleXY(mFragmentTransaction);
			break;
		case ANIMATION_FADE:
			transitionFade(mFragmentTransaction);
			break;
		case ANIMATION_FLIP_HORIZONTAL:
			transitionFlipHorizontal(mFragmentTransaction);
			break;
		case ANIMATION_FLIP_VERTICAL:
			transitionFlipVertical(mFragmentTransaction);
			break;
		case ANIMATION_SLIDE_VERTICAL:
			transitionSlideVertical(mFragmentTransaction);
			break;
		case ANIMATION_SLIDE_HORIZONTAL:
			transitionSlideHorizontal(mFragmentTransaction);
			break;
		case ANIMATION_SLIDE_HORIZONTAL_PUSH_TOP:
			transitionSlideHorizontalPushTop(mFragmentTransaction);
			break;
		case ANIMATION_SLIDE_VERTICAL_PUSH_LEFT:
			transitionSlideVerticalPushLeft(mFragmentTransaction);
			break;
		case ANIMATION_GLIDE:
			transitionGlide(mFragmentTransaction);
			break;
		case ANIMATION_STACK:
			transitionStack(mFragmentTransaction);
			break;
		case ANIMATION_CUBE:
			transitionCube(mFragmentTransaction);
			break;
		case ANIMATION_ROTATE_DOWN:
			transitionRotateDown(mFragmentTransaction);
			break;
		case ANIMATION_ROTATE_UP:
			transitionRotateUp(mFragmentTransaction);
			break;
		case ANIMATION_ACCORDION:
			transitionAccordion(mFragmentTransaction);
			break;
		case ANIMATION_TABLE_HORIZONTAL:
			transitionTableHorizontal(mFragmentTransaction);
			break;
		case ANIMATION_TABLE_VERTICAL:
			transitionTableVertical(mFragmentTransaction);
			break;
		case ANIMATION_ZOOM_FROM_LEFT_CORNER:
			transitionZoomFromLeftCorner(mFragmentTransaction);
			break;
		case ANIMATION_ZOOM_FROM_RIGHT_CORNER:
			transitionZoomFromRightCorner(mFragmentTransaction);
			break;
		}
	}

	private void transitionFade(FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
				android.R.anim.fade_out, android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	private void transitionScaleX(FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(R.anim.scalex_enter,
				R.anim.scalex_exit, R.anim.scalex_enter,
				R.anim.scalex_exit);
	}

	private void transitionScaleY(FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(R.anim.scaley_enter,
				R.anim.scaley_exit, R.anim.scaley_enter,
				R.anim.scaley_exit);
	}

	private void transitionScaleXY(FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(R.anim.scalexy_enter,
				R.anim.scalexy_exit, R.anim.scalexy_enter,
				R.anim.scalexy_exit);
	}

	private void transitionSlideVertical(
			FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(
				R.anim.slide_fragment_vertical_right_in,
				R.anim.slide_fragment_vertical_left_out,
				R.anim.slide_fragment_vertical_left_in,
				R.anim.slide_fragment_vertical_right_out);
	}

	/**
	 * 水平向右
	 * @param mFragmentTransaction
	 */
	private void transitionSlideHorizontal(
			FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(
				R.anim.slide_fragment_horizontal_right_in,
				R.anim.slide_fragment_horizontal_left_out,
				R.anim.slide_fragment_horizontal_left_in,
				R.anim.slide_fragment_horizontal_right_out);
	}
	
//	/**
//	 * 水平向左
//	 * @param mFragmentTransaction
//	 */
//	private void transitionSlideHorizontal(
//			FragmentTransaction mFragmentTransaction) {
//		mFragmentTransaction.setCustomAnimations(
//				R.anim.slide_fragment_horizontal_left_in,
//				R.anim.slide_fragment_horizontal_right_out,
//				R.anim.slide_fragment_horizontal_right_in,
//				R.anim.slide_fragment_horizontal_left_out);
//	}

	private void transitionSlideHorizontalPushTop(
			FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(
				R.anim.slide_fragment_horizontal_right_in,
				R.anim.slide_fragment_vertical_left_out,
				R.anim.slide_fragment_vertical_left_in,
				R.anim.slide_fragment_horizontal_right_out);
	}

	private void transitionSlideVerticalPushLeft(
			FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(
				R.anim.slide_fragment_vertical_right_in,
				R.anim.slide_fragment_horizontal_left_out,
				R.anim.slide_fragment_horizontal_left_in,
				R.anim.slide_fragment_vertical_right_out);
	}

	private void transitionGlide(FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(
				R.anim.glide_fragment_horizontal_in,
				R.anim.glide_fragment_horizontal_out,
				R.anim.glide_fragment_horizontal_in,
				R.anim.glide_fragment_horizontal_out);
	}

	private void transitionStack(FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(R.anim.stack_right_in,
				R.anim.stack_left_out, R.anim.stack_left_in,
				R.anim.stack_right_out);
	}

	private void transitionCube(FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(R.anim.cube_right_in,
				R.anim.cube_left_out, R.anim.cube_left_in,
				R.anim.cube_right_out);
	}

	private void transitionRotateDown(FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(
				R.anim.rotatedown_right_in, R.anim.rotatedown_left_out,
				R.anim.rotatedown_left_in, R.anim.rotatedown_right_out);
	}

	private void transitionRotateUp(FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(R.anim.rotateup_right_in,
				R.anim.rotateup_left_out, R.anim.rotateup_left_in,
				R.anim.rotateup_right_out);
	}

	private void transitionAccordion(FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(R.anim.accordion_right_in,
				R.anim.accordion_left_out, R.anim.accordion_left_in,
				R.anim.accordion_right_out);
	}

	private void transitionTableHorizontal(
			FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(
				R.anim.table_horizontal_right_in,
				R.anim.table_horizontal_left_out,
				R.anim.table_horizontal_left_int,
				R.anim.table_horizontal_right_out);
	}

	private void transitionTableVertical(
			FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(
				R.anim.table_vertical_right_in,
				R.anim.table_vertical_left_out,
				R.anim.table_vertical_left_int,
				R.anim.table_vertical_right_out);
	}

	private void transitionFlipHorizontal(
			FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(
				R.anim.card_flip_horizontal_right_in,
				R.anim.card_flip_horizontal_left_out,
				R.anim.card_flip_horizontal_left_in,
				R.anim.card_flip_horizontal_right_out);
	}

	private void transitionFlipVertical(FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(
				R.anim.card_flip_vertical_right_in,
				R.anim.card_flip_vertical_left_out,
				R.anim.card_flip_vertical_left_in,
				R.anim.card_flip_vertical_right_out);
	}

	private void transitionZoomFromLeftCorner(
			FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(
				R.anim.zoom_from_left_corner_right_in,
				R.anim.zoom_from_left_corner_left_out,
				R.anim.zoom_from_left_corner_left_in,
				R.anim.zoom_from_left_corner_right_out);
	}

	private void transitionZoomFromRightCorner(
			FragmentTransaction mFragmentTransaction) {
		mFragmentTransaction.setCustomAnimations(
				R.anim.zoom_from_right_corner_right_in,
				R.anim.zoom_from_right_corner_left_out,
				R.anim.zoom_from_right_corner_left_in,
				R.anim.zoom_from_right_corner_right_out);
	}

}
