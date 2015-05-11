package com.feifei.study.demo.SlidingMenu;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.animation.Interpolator;

import com.feifei.study.R;

import feifei.library.slidingmenu.SlidingFragmentActivity;
import feifei.library.slidingmenu.SlidingMenu;


/**
 * 作者    裴智飞
 * 时间    15-5-9 下午10:40
 * 文件    Study
 * 描述    SlidingActivity
 * <p/>
 * 布局里面不用添加，直接new，然后set属性，有一个视频教程http://www.jikexueyuan.com/course/61_5.html?ss=1
 * GitHub项目链接：https://github.com/jfeinstein10/SlidingMenu
 * <p/>
 * 代码使用：
 * SlidingMenu menu = new SlidingMenu(this);
 * menu.setMode(SlidingMenu.LEFT);
 * menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
 * menu.setShadowWidthRes(R.dimen.shadow_width);
 * menu.setShadowDrawable(R.drawable.shadow);
 * menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
 * menu.setFadeDegree(0.35f);
 * menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
 * menu.setMenu(R.layout.menu);
 * <p/>
 * xml使用：
 * <com.feifei.library.slidingmenu.SlidingMenu
 * xmlns:sliding="http://schemas.android.com/apk/res-auto"
 * android:id="@+id/slidingmenulayout"
 * android:layout_width="fill_parent"
 * android:layout_height="fill_parent"
 * sliding:viewAbove="@layout/YOUR_ABOVE_VIEW"
 * sliding:viewBehind="@layout/YOUR_BEHIND_BEHIND"
 * sliding:touchModeAbove="margin|fullscreen"
 * sliding:behindOffset="@dimen/YOUR_OFFSET"
 * sliding:behindWidth="@dimen/YOUR_WIDTH"
 * sliding:behindScrollScale="@dimen/YOUR_SCALE"
 * sliding:shadowDrawable="@drawable/YOUR_SHADOW"
 * sliding:shadowWidth="@dimen/YOUR_SHADOW_WIDTH"
 * sliding:fadeEnabled="true|false"
 * sliding:fadeDegree="float"
 * sliding:selectorEnabled="true|false"
 * sliding:selectorDrawable="@drawable/YOUR_SELECTOR"/>
 * <p/>
 * 网上说有多种继承方式，一种是继承他的SlidingFragmentActivity，里面已经集成了一个SlidingMenu，通过getSlidingMenu()就可以获取到
 * 另一种是继承FragmentActivity
 * new出来的需要attachToActivity，get出来的不需要
 * SlidingMenu menu = new SlidingMenu(this);
 * menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
 * menu.setMenu(R.layout.frame_menu);
 * 我们需要自己定义SlidingMenu，然后通过attachToActivity，将SlidingMenu连接到我们的FragmentActivity之上
 * <p/>
 * // 获取到SlidingMenu对象，然后设置一些常见的属性
 * SlidingMenu sm = getSlidingMenu ();
 * // 设置阴影的宽度
 * sm.setShadowWidth (0);
 * // 设置阴影的颜色
 * // sm.setShadowDrawable (R.drawable.shadow);
 * // 设置侧滑栏完全展开之后，距离另外一边的距离，单位px，设置的越大，侧滑栏的宽度越小
 * sm.setBehindOffset (100);
 * // 设置渐变的程度，范围是0-1.0f,设置的越大，则在侧滑栏刚划出的时候，颜色就越暗。1.0f的时候，颜色为全黑
 * sm.setFadeDegree (0.3f);
 * // 设置触摸模式，可以选择全屏划出，或者是边缘划出，或者是不可划出
 * sm.setTouchModeAbove (SlidingMenu.TOUCHMODE_FULLSCREEN);
 * //设置是否左右滑动
 * sm.setMode (SlidingMenu.LEFT);
 * //设置actionBar能否跟随侧滑栏移动，如果没有，则可以去掉
 * setSlidingActionBarEnabled (false);
 * // 设置存放侧滑栏的容器的布局文件
 * setBehindContentView (R.layout.slidingmenu);
 * <p/>
 * 【设置动画】
 * 设置侧滑栏显示动画
 * 通过SlidingMenu.setBehindCanvasTransformer(CanvasTransformer);方法可以设置侧滑栏的显示动画，参数是一个CanvasTransformer对象
 * 下面是几个常见的动画的设置
 * 折叠动画
 * new CanvasTransformer() {
 * public void transformCanvas(Canvas canvas, float percentOpen) {
 * canvas.scale(percentOpen, 1, 0, 0);
 * }
 * }
 * 放缩动画
 * new CanvasTransformer() {
 * public void transformCanvas(Canvas canvas, float percentOpen) {
 * float scale = (float) (percentOpen*0.25 + 0.75);
 * canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
 * }
 * }
 * 上升动画
 * private static Interpolator interp = new Interpolator() {
 * public float getInterpolation(float t) {
 * t -= 1.0f;
 * return t * t * t + 1.0f;
 * }
 * };
 * new CanvasTransformer() {
 * public void transformCanvas(Canvas canvas, float percentOpen) {
 * canvas.translate(0, canvas.getHeight()*(1-interp.getInterpolation(percentOpen)));
 * }
 * }
 */
public class BaseActivity extends SlidingFragmentActivity {

	protected ListFragment mFrag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager()
					.beginTransaction();
			mFrag = new BirdMenuFragment ();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (ListFragment) this.getSupportFragmentManager()
					.findFragmentById(R.id.menu_frame);
		}
		// 继承于SlidingFragmentActivity，可以get出来
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		// actionbar跟着一直走
		setSlidingActionBarEnabled(true);
		sm.setMode(SlidingMenu.LEFT);
		sm.setSecondaryMenu(R.layout.menu_frame_two);
		sm.setSecondaryShadowDrawable(R.drawable.shadowright);
		setSlidingActionBarEnabled(true);
		sm.setBehindScrollScale(0.0f);
		sm.setBehindCanvasTransformer(slide);

	}

	// 设置动画效果
	SlidingMenu.CanvasTransformer scale = new SlidingMenu.CanvasTransformer () {
		@Override
		public void transformCanvas(Canvas canvas, float percentOpen) {
			canvas.scale(percentOpen, 1, 0, 0);
		}
	};

	final Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t + 1.0f;
		}
	};

	SlidingMenu.CanvasTransformer slide = new SlidingMenu.CanvasTransformer () {
		@Override
		public void transformCanvas(Canvas canvas, float percentOpen) {
			canvas.translate(
					0,
					canvas.getHeight()
							* (1 - interp.getInterpolation(percentOpen)));
		}
	};

	SlidingMenu.CanvasTransformer zoom = new SlidingMenu.CanvasTransformer () {
		@Override
		public void transformCanvas(Canvas canvas, float percentOpen) {
			float scale = (float) (percentOpen * 0.25 + 0.75);
			canvas.scale(scale, scale, canvas.getWidth() / 2,
					canvas.getHeight() / 2);
		}
	};
}
