package com.silverboxsoft.arithmetic;

import java.util.HashSet;
import java.util.Iterator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
//import android.widget.Toast;

public class ArithmeticMain extends Activity {
	//private static final int CALCTYP_ADD = 0;
	//private static final int CALCTYP_SUB = 1;
	private static final String[] CALCMARK = {" + "," - "};
	
	private static final String PREFKEY_CALCTYPE = "ArithmeticCalcType";
	private static final String CALCTYPKEY_EASYADD = "ctyp_easyadd";
	private static final String CALCTYPKEY_EASYSUB = "ctyp_easysub";
	private static final String CALCTYPKEY_RISEUPADD = "ctyp_riseupadd";
	private static final String CALCTYPKEY_FALLDOWNSUB = "ctyp_falldownsub";
	
	private HashSet<String> calctypSet = new HashSet<String>();
	private String calctyp = CALCTYPKEY_EASYADD;

	boolean initialized = false;

	private int tgt_a = 0;
	private int tgt_b = 0;
	private int tgt_ans = 10;
	private TextView lbl_q;
	private TextView lbl_answer;

	private Button btn_answer;
	private Button btn_clear;
	private Button btn_pass;
	private Button btn_0;
	private Button btn_1;
	private Button btn_2;
	private Button btn_3;
	private Button btn_4;
	private Button btn_5;
	private Button btn_6;
	private Button btn_7;
	private Button btn_8;
	private Button btn_9;
	private Button btn_hint1;
	private Button btn_hint2;

	private ImageView hint1_a;
	private ImageView hint1_b;
	private ImageView hint1_a2;
	private ImageView img_scale;
	//private ImageView hint2;
	private int hintstep = 0;
	private Vibrator vib;//= (Vibrator) getSystemService(VIBRATOR_SERVICE);

	private String[] correct_msgs;
	private String[] incorrect_msgs;
	
	private Paint paint = new Paint();
	private MediaPlayer mp_ok;
	private MediaPlayer mp_fail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arithmetic_main);
		initCalcTypeSet();
		correct_msgs = getResources().getStringArray(R.array.strres_collectmsg);
		incorrect_msgs = getResources().getStringArray(R.array.strres_incollectmsg);
		mp_ok = MediaPlayer.create(this,R.raw.se_maoudamashii_chime13);
		mp_fail = MediaPlayer.create(this,R.raw.se_maoudamashii_onepoint32);
	}

	@Override
	public void onDestroy() {
		SharedPreferences pref = getSharedPreferences(PREFKEY_CALCTYPE, MODE_PRIVATE);
		Editor edt = pref.edit();
		edt.putBoolean(CALCTYPKEY_EASYADD, calctypSet.contains(CALCTYPKEY_EASYADD));
		edt.putBoolean(CALCTYPKEY_EASYSUB, calctypSet.contains(CALCTYPKEY_EASYSUB));
		edt.putBoolean(CALCTYPKEY_RISEUPADD, calctypSet.contains(CALCTYPKEY_RISEUPADD));
		edt.putBoolean(CALCTYPKEY_FALLDOWNSUB, calctypSet.contains(CALCTYPKEY_FALLDOWNSUB));
		edt.commit();
		//Toast toast = Toast.makeText(this, "onDestroy:" + calctypSet.size(), Toast.LENGTH_SHORT);
		//toast.show();
		//stopSCheduleManager();
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) { // (11)
		item.setChecked(!item.isChecked());
		boolean ret = super.onOptionsItemSelected(item);
		int id = item.getItemId();
		/*if (id == R.id.menu_calctype_easy_add ||  id ==  R.id.menu_calctype_easy_sub
				|| id == R.id.menu_calctype_riseup_add ||  id ==  R.id.menu_calctype_riseup_sub
		){
			//System.out.println("★★onOptionsMenuSelected" + id);
			openOptionsMenu();
		}*/
		if(id == R.id.menu_calctype_easy_add){
			if(item.isChecked()){
				calctypSet.add(CALCTYPKEY_EASYADD);
			}else{
				calctypSet.remove(CALCTYPKEY_EASYADD);
			}
		}
		if(id == R.id.menu_calctype_easy_sub){
			if(item.isChecked()){
				calctypSet.add(CALCTYPKEY_EASYSUB);
			}else{
				calctypSet.remove(CALCTYPKEY_EASYSUB);
			}
		}
		if(id == R.id.menu_calctype_riseup_add){
			if(item.isChecked()){
				calctypSet.add(CALCTYPKEY_RISEUPADD);
			}else{
				calctypSet.remove(CALCTYPKEY_RISEUPADD);
			}
		}
		if(id == R.id.menu_calctype_riseup_sub){
			if(item.isChecked()){
				calctypSet.add(CALCTYPKEY_FALLDOWNSUB);
			}else{
				calctypSet.remove(CALCTYPKEY_FALLDOWNSUB);
			}
		}

		//TODO
		//Toast toast = Toast.makeText(this, "onOptionsMenuClosed:" + calctypSet.size() + ":", Toast.LENGTH_SHORT);
		//toast.show();

		return ret;
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		/*calctypSet.clear();
		if(menu.findItem(R.id.menu_calctype_easy_add).isChecked()){
			calctypSet.add(CALCTYPKEY_EASYADD);
		}
		if(menu.findItem(R.id.menu_calctype_easy_sub).isChecked()){
			calctypSet.add(CALCTYPKEY_EASYSUB);
		}
		if(menu.findItem(R.id.menu_calctype_riseup_add).isChecked()){
			calctypSet.add(CALCTYPKEY_RISEUPADD);
		}
		if(menu.findItem(R.id.menu_calctype_riseup_sub).isChecked()){
			calctypSet.add(CALCTYPKEY_FALLDOWNSUB);
		}
		//TODO
		Toast toast = Toast.makeText(this, "onOptionsMenuClosed:" + calctypSet.size() + ":" + menu.findItem(R.id.menu_calctype_riseup_add).isChecked(), Toast.LENGTH_SHORT);
		toast.show();*/
		super.onOptionsMenuClosed(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_arithmetic_main, menu);
		menu.findItem(R.id.menu_calctype_easy_add).setChecked(calctypSet.contains(CALCTYPKEY_EASYADD));
		menu.findItem(R.id.menu_calctype_easy_sub).setChecked(calctypSet.contains(CALCTYPKEY_EASYSUB));
		menu.findItem(R.id.menu_calctype_riseup_add).setChecked(calctypSet.contains(CALCTYPKEY_RISEUPADD));
		menu.findItem(R.id.menu_calctype_riseup_sub).setChecked(calctypSet.contains(CALCTYPKEY_FALLDOWNSUB));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		if(initialized == false){
			displayInit();
			makeNewQuestion();
			initialized = true;
		}
		//
	}
	
	private void initCalcTypeSet(){
		SharedPreferences pref = getSharedPreferences(PREFKEY_CALCTYPE, MODE_PRIVATE);
		calctypSet.clear();
		if(pref.getBoolean(CALCTYPKEY_EASYADD, true)){
			calctypSet.add(CALCTYPKEY_EASYADD);
		}
		if(pref.getBoolean(CALCTYPKEY_EASYSUB, false)){
			calctypSet.add(CALCTYPKEY_EASYSUB);
		}
		if(pref.getBoolean(CALCTYPKEY_RISEUPADD, false)){
			calctypSet.add(CALCTYPKEY_RISEUPADD);
		}
		if(pref.getBoolean(CALCTYPKEY_FALLDOWNSUB, false)){
			calctypSet.add(CALCTYPKEY_FALLDOWNSUB);
		}
	}
	
	public void displayInit(){
		vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		prepareView();
	}

	private void makeNewQuestion(){
		double wk = Math.random() * calctypSet.size();
		int wkcalctyp = (int) wk;
		//System.out.println("★★wkcalctyp1:" + wk + ":" + wkcalctyp + ":" + calctypSet.size());//TODO
		int idx = 0;
		/*for(String key:calctypSet){
			if(idx == wkcalctyp){
				calctyp = key;
				break;
			}
			idx++;
		}*/
		Iterator<String> itr = calctypSet.iterator();
		while(itr.hasNext()){
			String key = (String)itr.next();
			//System.out.println("★★Iterator:" + key + ":" + idx + ":" + wkcalctyp);//TODO
			if(idx == wkcalctyp){
				calctyp = key;
				break;
			}
			idx++;
		}
		
		int wktgt_a = (int)(Math.random() * 8) + 2;// 2 - 9
		int wktgt_b;
		if(calcTypeIsRiseUp()){
			wktgt_b = (int)(Math.random() * (10 - wktgt_a)) + wktgt_a;
			wktgt_a += 9;// 11 - 18
		}else{
			wktgt_b = (int)(Math.random() * wktgt_a - 1) + 1;//1 - wktgt_a
		}
		//System.out.println("★★wkcalctyp:" + wktgt_a + ":" + wktgt_b + ":" + calctyp);//TODO

		String wkMark;
		if(calcTypeIsAdd()){
			tgt_ans = wktgt_a;
			tgt_a = wktgt_b;
			tgt_b = wktgt_a - wktgt_b;
			wkMark = CALCMARK[0];
		}else{
			if(wktgt_a > wktgt_b){
				tgt_ans = wktgt_a - wktgt_b;
				tgt_a = wktgt_a;
				tgt_b = wktgt_b;
			}else{
				tgt_ans = wktgt_b - wktgt_a;
				tgt_a = wktgt_b;
				tgt_b = wktgt_a;
			}
			wkMark = CALCMARK[1];
		}
		
		String formula = String.format("%d %s %d = ", tgt_a, wkMark, tgt_b);
		lbl_q.setText(formula);
		onClearBtnClick(null);
		clearHint();
	}
	
	private boolean calcTypeIsAdd(){
		return calctyp.equals(CALCTYPKEY_EASYADD) || calctyp.equals(CALCTYPKEY_RISEUPADD);
	}
	
	private boolean calcTypeIsRiseUp(){
		return calctyp.equals(CALCTYPKEY_RISEUPADD) || calctyp.equals(CALCTYPKEY_FALLDOWNSUB);
	}
	
	public void onAnswerBtnClick(View v){
		int ans1 = tgt_ans;
		String ansStr = lbl_answer.getText().toString();
		//System.out.println("★★" + ans1 + ":" + ansStr + ":");
		View layout = getResultDlgLayout();
		final ImageView img= (ImageView) layout.findViewById(R.id.img_face);
		int ans2 = (ansStr != null && !ansStr.equals("")) ? Integer.parseInt(ansStr) : 0;
		if (ans1 == ans2){
			img.setImageResource(R.drawable.face_ok);
			img.setBackgroundResource(R.drawable.colorbar_answer);
			mp_ok.start();
			vib.vibrate(100);
			String msg = correct_msgs[(int)(Math.random() * correct_msgs.length)];
			new AlertDialog.Builder(this).setMessage(msg).setCancelable(true)
			.setView(layout)
			.setPositiveButton(getResources().getString(R.string.strres_nextquestion),
					new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog,int which) {
							makeNewQuestion();
						}
			}).show();
		}else{
			img.setImageResource(R.drawable.face_ng);
			img.setBackgroundResource(R.drawable.colorbar_pass);
			mp_fail.start();
			vib.vibrate(500);
			String msg = incorrect_msgs[(int)(Math.random() * incorrect_msgs.length)];
			new AlertDialog.Builder(this).setMessage(msg).setCancelable(true)
			.setView(layout)
			.setPositiveButton(getResources().getString(R.string.strres_showhint),
					new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog,int which) {
							lbl_answer.setText("");
							hintstep++;
							drawHint(hintstep);
						}
			}).show();
		}
	}
	
	private View getResultDlgLayout(){
		LayoutInflater inflater = LayoutInflater.from(this);
		View layout = inflater.inflate(R.layout.resultinfo_dlg, (ViewGroup) findViewById(R.id.layout_resultdlg));
		layout.setBackgroundColor(Color.WHITE);
		
		return layout;
	}

	public void onClearBtnClick(View v){
		lbl_answer.setText("");
	}
	
	public void onPassBtnClick(View v){
		makeNewQuestion();
	}
	
	public void onHintBtnClick(View v){
		if(v.equals(btn_hint1)){
			hintstep = 1;
		}else{
			hintstep = 2;
		}
		drawHint(hintstep);
	}
	
	public void drawHint(int hintlvl){
		switch(hintlvl){
		case 1:
			drawHint1a();
			drawHint1b();
			break;
		case 2:
			drawHint2();
			break;
		}
		drawHintScale();
	}
	
	private float r_rate = 5;
	
	private void drawHint1a(){
		if(tgt_a > 10){
			hint1_a.setVisibility(View.GONE);
			hint1_a2.setVisibility(View.VISIBLE);
			drawHint1Sub(hint1_a2, tgt_a, Color.BLUE);
		}else{
			hint1_a.setVisibility(View.VISIBLE);
			hint1_a2.setVisibility(View.GONE);
			drawHint1Sub(hint1_a, tgt_a, Color.BLUE);
		}
	}
	private void drawHint1b(){
		hint1_b.setVisibility(View.VISIBLE);
		drawHint1Sub(hint1_b, tgt_b, Color.GREEN);

	}
	private void drawHintScale(){
		Bitmap bmp = getBitmap(img_scale);
		Canvas canvas=new Canvas(bmp);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(2);
		canvas.drawLine(0, 2, 0, 30, paint);//left
		canvas.drawLine(479, 2, 479, 30, paint);//right
		canvas.drawLine(240, 8, 240, 24, paint);//middle
		canvas.drawLine(0, 16, 479, 16, paint);//holizon
		for(int i = 1;i < 10;i++){
			canvas.drawLine(i * 48, 13, i * 48, 19, paint);
		}
		img_scale.setImageBitmap(bmp);
	}
	private void drawHint1Sub(ImageView imgv, int tgt, int color){
		Bitmap bmp = getBitmap(imgv);
		Canvas canvas=new Canvas(bmp);
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		
		paint.setColor(color);
		paint.setStrokeWidth(1);
		for(int i = 0;i < tgt;i++){
			canvas.drawRoundRect(getItemRect(i), r_rate, r_rate, paint);
			//canvas.drawRect(getItemRect(i), paint);
		}
		
		imgv.setImageBitmap(bmp);
	}
	private void drawHint2(){
		hint1_a.setVisibility(View.GONE);
		hint1_a2.setVisibility(View.GONE);
		hint1_b.setVisibility(View.GONE);
		//drawHint1Sub(hint1_b, 0, Color.GREEN);
		ImageView wkView = calcTypeIsRiseUp() ? hint1_a2 : hint1_a;
		wkView.setVisibility(View.VISIBLE);
		
		Bitmap bmp = getBitmap(wkView);
		Canvas canvas=new Canvas(bmp);
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		
		int ttl = 0;
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(2);
		for(int i = 0;i < tgt_a;i++){
			canvas.drawRoundRect(getItemRect(ttl), r_rate, r_rate, paint);
			//canvas.drawRect(getItemRect(ttl), paint);
			//System.out.println("★★" + r.left + ":" + r.right);
			ttl++;
		}

		paint.setColor(Color.GREEN);
		if(calcTypeIsAdd()){
			for(int i = 0;i < tgt_b;i++){
				canvas.drawRoundRect(getItemRect(ttl), r_rate, r_rate, paint);
				//canvas.drawRect(getItemRect(ttl), paint);
				ttl++;
			}
			if(calcTypeIsRiseUp()){
				paint.setStyle(Paint.Style.STROKE);
				for(int i = 0;i < 10 - tgt_a;i++){
					RectF r = getItemRect(ttl);
					canvas.drawRoundRect(r, r_rate, r_rate, paint);
					//canvas.drawRect(r, paint);
					int stx = (int)((r.left + r.right) / 2);
					int sty = (int)((r.top + r.bottom) / 2);
					RectF r2 = getItemRect(tgt_a + i);
					int edx = (int)((r2.left + r2.right) / 2);
					int edy = (int)((r2.top + r2.bottom) / 2);
					paint.setStrokeWidth(2);
					paint.setColor(Color.RED);
					canvas.drawLine(stx, sty, edx, edy, paint);
					paint.setStrokeWidth(2);
					paint.setColor(Color.GREEN);
					ttl++;
				}
				paint.setStyle(Paint.Style.FILL_AND_STROKE);
			}
		}else{
			paint.setStrokeWidth(5);
			boolean isRu = calcTypeIsRiseUp();
			int offset = isRu ? tgt_a - 10: 0;
			for(int i = tgt_a - 1;i >= (tgt_a - tgt_b);i--){
				RectF r = getItemRect(i - offset);
				canvas.drawLine(r.left, r.top, r.right, r.bottom, paint);
				canvas.drawLine(r.left, r.bottom, r.right, r.top, paint);
			}
		}

		wkView.setImageBitmap(bmp);
	}
	
	private RectF getItemRect(int val){
		RectF r = new RectF();
		int margin = 3;
		r.top= (int)(val / 10) * 48 + margin;
		r.bottom= r.top + 48 - (margin * 2);
		r.left = (val % 10) * 48 + margin;
		r.right= r.left + 48 - (margin * 2);
		return r;
	}
	public void clearHint(){
		clearImageView(hint1_a);
		clearImageView(hint1_a2);
		clearImageView(hint1_b);
		clearImageView(img_scale);
		//clearImageView(hint2);
		hintstep = 0;
	}
	
	private void clearImageView(ImageView imgv){
		Bitmap rectBitmap = getBitmap(imgv);

		Canvas canvas=new Canvas(rectBitmap);
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		
		imgv.setImageBitmap(rectBitmap);
	}
	
	private Bitmap getBitmap(ImageView imgv){
		int imageV_w = imgv.getWidth();
		int imageV_h = imgv.getHeight();
		return Bitmap.createBitmap(imageV_w, imageV_h, Bitmap.Config.ARGB_8888);
	}

	private void prepareView(){
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);

		lbl_q = (TextView) findViewById(R.id.txt_fomula);
		lbl_answer = (TextView) findViewById(R.id.lbl_answer);

		btn_answer = (Button) findViewById(R.id.btn_answer);
		OnClickListener btn_anslistener = new OnClickListener(){
			@Override
			public void onClick(View arg0) {onAnswerBtnClick(null);}
		};
		btn_answer.setOnClickListener(btn_anslistener);

		btn_clear = (Button) findViewById(R.id.btn_clear);
		OnClickListener btn_clearlistener = new OnClickListener(){
			@Override
			public void onClick(View arg0) {onClearBtnClick(null);}
		};
		btn_clear.setOnClickListener(btn_clearlistener);

		btn_pass = (Button) findViewById(R.id.btn_pass);
		OnClickListener btn_passistener = new OnClickListener(){
			@Override
			public void onClick(View arg0) {onPassBtnClick(null);}
		};
		btn_pass.setOnClickListener(btn_passistener);

		btn_0 = (Button) findViewById(R.id.btn_0);
		btn_1 = (Button) findViewById(R.id.btn_1);
		btn_2 = (Button) findViewById(R.id.btn_2);
		btn_3 = (Button) findViewById(R.id.btn_3);
		btn_4 = (Button) findViewById(R.id.btn_4);
		btn_5 = (Button) findViewById(R.id.btn_5);
		btn_6 = (Button) findViewById(R.id.btn_6);
		btn_7 = (Button) findViewById(R.id.btn_7);
		btn_8 = (Button) findViewById(R.id.btn_8);
		btn_9 = (Button) findViewById(R.id.btn_9);
		OnClickListener btn_listener = new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String wk = lbl_answer.getText().toString();
				if(wk.length() < 2){
					lbl_answer.setText(wk + ((Button) arg0).getText());
				}
			}
		};
		btn_0.setOnClickListener(btn_listener);
		btn_1.setOnClickListener(btn_listener);
		btn_2.setOnClickListener(btn_listener);
		btn_3.setOnClickListener(btn_listener);
		btn_4.setOnClickListener(btn_listener);
		btn_5.setOnClickListener(btn_listener);
		btn_6.setOnClickListener(btn_listener);
		btn_7.setOnClickListener(btn_listener);
		btn_8.setOnClickListener(btn_listener);
		btn_9.setOnClickListener(btn_listener);

		btn_hint1 = (Button) findViewById(R.id.btn_hint1);
		btn_hint2 = (Button) findViewById(R.id.btn_hint2);
		OnClickListener hintbtn_listener = new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				onHintBtnClick(arg0);
			}
		};
		btn_hint1.setOnClickListener(hintbtn_listener);
		btn_hint2.setOnClickListener(hintbtn_listener);

		hint1_a=(ImageView)this.findViewById(R.id.img_hint1_a);
		hint1_a2=(ImageView)this.findViewById(R.id.img_hint1_a2);
		hint1_b=(ImageView)this.findViewById(R.id.img_hint1_b);
		//hint2=(ImageView)this.findViewById(R.id.img_hint2);
		img_scale=(ImageView)this.findViewById(R.id.img_scale);
	}
}
