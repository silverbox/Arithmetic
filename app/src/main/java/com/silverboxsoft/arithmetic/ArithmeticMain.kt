package com.silverboxsoft.arithmetic

import java.util.HashSet
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.os.VibrationEffect
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
// import android.content.DialogInterface
// import android.content.SharedPreferences
// import android.content.SharedPreferences.Editor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

//import android.widget.Toast;

class ArithmeticMain : Activity() {

    private val calcTypeSet = HashSet<String>()
    private var calcType = CALCTYPKEY_EASYADD

    private var initialized = false

    private var tgtA = 0
    private var tgtB = 0
    private var tgtAns = 10
    private var lblQ: TextView? = null
    private var lblAnswer: TextView? = null

    private var btnAnswer: Button? = null
    private var btnClear: Button? = null
    private var btnPass: Button? = null
    private var btn0: Button? = null
    private var btn1: Button? = null
    private var btn2: Button? = null
    private var btn3: Button? = null
    private var btn4: Button? = null
    private var btn5: Button? = null
    private var btn6: Button? = null
    private var btn7: Button? = null
    private var btn8: Button? = null
    private var btn9: Button? = null
    private var btnHint1: Button? = null
    private var btnHint2: Button? = null

    private var hint1A: ImageView? = null
    private var hint1B: ImageView? = null
    private var hint1A2: ImageView? = null
    private var imgScale: ImageView? = null
    //private ImageView hint2;
    private var hintstep = 0
    private var vib: Vibrator? = null//= (Vibrator) getSystemService(VIBRATOR_SERVICE);

    private var correctMsgList: Array<String>? = null
    private var incorrectMsgList: Array<String>? = null

    private val paint = Paint()
    private var mpOk: MediaPlayer? = null
    private var mpFail: MediaPlayer? = null

    private val resultDlgLayout: View
        get() {
            val factory: LayoutInflater = LayoutInflater.from(this)
            val layInfView = factory.inflate(R.layout.resultinfo_dlg, null)
            // val viewGroup = layInfView.findViewById(R.id.layout_resultdlg) as ViewGroup

            // val layout = factory.inflate(R.layout.resultinfo_dlg, viewGroup)
            layInfView.setBackgroundColor(Color.WHITE)

            return layInfView
        }

    private val rRate = 5f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arithmetic_main)
        initCalcTypeSet()
        correctMsgList = resources.getStringArray(R.array.strres_collectmsg)
        incorrectMsgList = resources.getStringArray(R.array.strres_incollectmsg)
        mpOk = MediaPlayer.create(this, R.raw.se_maoudamashii_chime13)
        mpFail = MediaPlayer.create(this, R.raw.se_maoudamashii_onepoint32)
    }

    public override fun onDestroy() {
        val pref = getSharedPreferences(PREFKEY_CALCTYPE, Context.MODE_PRIVATE)
        val edt = pref.edit()
        edt.putBoolean(CALCTYPKEY_EASYADD, calcTypeSet.contains(CALCTYPKEY_EASYADD))
        edt.putBoolean(CALCTYPKEY_EASYSUB, calcTypeSet.contains(CALCTYPKEY_EASYSUB))
        edt.putBoolean(CALCTYPKEY_RISEUPADD, calcTypeSet.contains(CALCTYPKEY_RISEUPADD))
        edt.putBoolean(CALCTYPKEY_FALLDOWNSUB, calcTypeSet.contains(CALCTYPKEY_FALLDOWNSUB))
        edt.apply()
        //Toast toast = Toast.makeText(this, "onDestroy:" + calctypSet.size(), Toast.LENGTH_SHORT);
        //toast.show();
        //stopSCheduleManager();
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // (11)
        item.isChecked = !item.isChecked
        val ret = super.onOptionsItemSelected(item)
        val id = item.itemId
        /*if (id == R.id.menu_calctype_easy_add ||  id ==  R.id.menu_calctype_easy_sub
				|| id == R.id.menu_calctype_riseup_add ||  id ==  R.id.menu_calctype_riseup_sub
		){
			//System.out.println("★★onOptionsMenuSelected" + id);
			openOptionsMenu();
		}*/
        if (id == R.id.menu_calctype_easy_add) {
            if (item.isChecked) {
                calcTypeSet.add(CALCTYPKEY_EASYADD)
            } else {
                calcTypeSet.remove(CALCTYPKEY_EASYADD)
            }
        }
        if (id == R.id.menu_calctype_easy_sub) {
            if (item.isChecked) {
                calcTypeSet.add(CALCTYPKEY_EASYSUB)
            } else {
                calcTypeSet.remove(CALCTYPKEY_EASYSUB)
            }
        }
        if (id == R.id.menu_calctype_riseup_add) {
            if (item.isChecked) {
                calcTypeSet.add(CALCTYPKEY_RISEUPADD)
            } else {
                calcTypeSet.remove(CALCTYPKEY_RISEUPADD)
            }
        }
        if (id == R.id.menu_calctype_riseup_sub) {
            if (item.isChecked) {
                calcTypeSet.add(CALCTYPKEY_FALLDOWNSUB)
            } else {
                calcTypeSet.remove(CALCTYPKEY_FALLDOWNSUB)
            }
        }

        //TODO
        //Toast toast = Toast.makeText(this, "onOptionsMenuClosed:" + calctypSet.size() + ":", Toast.LENGTH_SHORT);
        //toast.show();

        return ret
    }

    /* override fun onOptionsMenuClosed(menu: Menu) {
        calctypSet.clear();
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
		toast.show();
        super.onOptionsMenuClosed(menu)
    } */

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_arithmetic_main, menu)
        menu.findItem(R.id.menu_calctype_easy_add).isChecked = calcTypeSet.contains(CALCTYPKEY_EASYADD)
        menu.findItem(R.id.menu_calctype_easy_sub).isChecked = calcTypeSet.contains(CALCTYPKEY_EASYSUB)
        menu.findItem(R.id.menu_calctype_riseup_add).isChecked = calcTypeSet.contains(CALCTYPKEY_RISEUPADD)
        menu.findItem(R.id.menu_calctype_riseup_sub).isChecked = calcTypeSet.contains(CALCTYPKEY_FALLDOWNSUB)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (!initialized) {
            displayInit()
            makeNewQuestion()
            initialized = true
        }
        //
    }

    private fun initCalcTypeSet() {
        val pref = getSharedPreferences(PREFKEY_CALCTYPE, Context.MODE_PRIVATE)
        calcTypeSet.clear()
        if (pref.getBoolean(CALCTYPKEY_EASYADD, true)) {
            calcTypeSet.add(CALCTYPKEY_EASYADD)
        }
        if (pref.getBoolean(CALCTYPKEY_EASYSUB, false)) {
            calcTypeSet.add(CALCTYPKEY_EASYSUB)
        }
        if (pref.getBoolean(CALCTYPKEY_RISEUPADD, false)) {
            calcTypeSet.add(CALCTYPKEY_RISEUPADD)
        }
        if (pref.getBoolean(CALCTYPKEY_FALLDOWNSUB, false)) {
            calcTypeSet.add(CALCTYPKEY_FALLDOWNSUB)
        }
    }

    private fun displayInit() {
        vib = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        prepareView()
    }

    private fun makeNewQuestion() {
        val wk = Math.random() * calcTypeSet.size
        val wkCalcType = wk.toInt()
        //print("★★wkCalcType1:" + wk + ":" + wkCalcType + ":" + calcTypeSet.size );//TODO
        var idx = 0
        val itr = calcTypeSet.iterator()
        while (itr.hasNext()) {
            calcType = itr.next()
            if (idx == wkCalcType) {
                break
            }
            idx++
        }

        var wkTgtA = (Math.random() * 8).toInt() + 2// 2 - 9
        val wkTgtB: Int
        if (calcTypeIsRiseUp()) {
            wkTgtB = (Math.random() * (10 - wkTgtA)).toInt() + wkTgtA
            wkTgtA += 9// 11 - 18
        } else {
            wkTgtB = (Math.random() * wkTgtA - 1).toInt() + 1//1 - wktgt_a
        }
        //System.out.println("★★wkcalctyp:" + wktgt_a + ":" + wktgt_b + ":" + calctyp);//TODO

        val wkMark: String
        if (calcTypeIsAdd()) {
            tgtAns = wkTgtA
            tgtA = wkTgtB
            tgtB = wkTgtA - wkTgtB
            wkMark = CALCMARK[0]
        } else {
            if (wkTgtA > wkTgtB) {
                tgtAns = wkTgtA - wkTgtB
                tgtA = wkTgtA
                tgtB = wkTgtB
            } else {
                tgtAns = wkTgtB - wkTgtA
                tgtA = wkTgtB
                tgtB = wkTgtA
            }
            wkMark = CALCMARK[1]
        }

        val formula = String.format("%d %s %d = ", tgtA, wkMark, tgtB)
        lblQ!!.text = formula
        onClearBtnClick()
        clearHint()
    }

    private fun calcTypeIsAdd(): Boolean {
        return calcType == CALCTYPKEY_EASYADD || calcType == CALCTYPKEY_RISEUPADD
    }

    private fun calcTypeIsRiseUp(): Boolean {
        return calcType == CALCTYPKEY_RISEUPADD || calcType == CALCTYPKEY_FALLDOWNSUB
    }

    private fun onAnswerBtnClick() {
        val ans1 = tgtAns
        val ansStr = lblAnswer!!.text.toString()
        //System.out.println("★★" + ans1 + ":" + ansStr + ":");
        val layout = resultDlgLayout
        val img = layout.findViewById<View>(R.id.img_face) as ImageView
        val ans2 = Integer.parseInt(ansStr)
        if (ans1 == ans2) {
            img.setImageResource(R.drawable.face_ok)
            img.setBackgroundResource(R.drawable.colorbar_answer)
            mpOk!!.start()
            val vibrationEffect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            vib!!.vibrate(vibrationEffect)
            val msg = correctMsgList!![(Math.random() * correctMsgList!!.size).toInt()]
            AlertDialog.Builder(this).setMessage(msg).setCancelable(true)
                    .setView(layout)
                    .setPositiveButton(resources.getString(R.string.strres_nextquestion)
                    ) { _, _ -> makeNewQuestion() }.show()
        } else {
            img.setImageResource(R.drawable.face_ng)
            img.setBackgroundResource(R.drawable.colorbar_pass)
            mpFail!!.start()
            val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            vib!!.vibrate(vibrationEffect)
            val msg = incorrectMsgList!![(Math.random() * incorrectMsgList!!.size).toInt()]
            AlertDialog.Builder(this).setMessage(msg).setCancelable(true)
                    .setView(layout)
                    .setPositiveButton(resources.getString(R.string.strres_showhint)
                    ) { _, _ ->
                        lblAnswer!!.text = ""
                        hintstep++
                        drawHint(hintstep)
                    }.show()
        }
    }

    private fun onClearBtnClick() {
        lblAnswer!!.text = ""
    }

    private fun onPassBtnClick() {
        makeNewQuestion()
    }

    private fun onHintBtnClick(v: View) {
        if (v == btnHint1) {
            hintstep = 1
        } else {
            hintstep = 2
        }
        drawHint(hintstep)
    }

    private fun drawHint(hintLvl: Int) {
        when (hintLvl) {
            1 -> {
                drawHint1a()
                drawHint1b()
            }
            2 -> drawHint2()
        }
        drawHintScale()
    }

    private fun drawHint1a() {
        if (tgtA > 10) {
            hint1A!!.visibility = View.GONE
            hint1A2!!.visibility = View.VISIBLE
            drawHint1Sub(hint1A2!!, tgtA, Color.BLUE)
        } else {
            hint1A!!.visibility = View.VISIBLE
            hint1A2!!.visibility = View.GONE
            drawHint1Sub(hint1A!!, tgtA, Color.BLUE)
        }
    }

    private fun drawHint1b() {
        hint1B!!.visibility = View.VISIBLE
        drawHint1Sub(hint1B!!, tgtB, Color.GREEN)

    }

    private fun drawHintScale() {
        val bmp = getBitmap(imgScale!!)
        val canvas = Canvas(bmp)
        paint.color = Color.BLACK
        paint.strokeWidth = 2f
        canvas.drawLine(0f, 2f, 0f, 30f, paint)//left
        canvas.drawLine(479f, 2f, 479f, 30f, paint)//right
        canvas.drawLine(240f, 8f, 240f, 24f, paint)//middle
        canvas.drawLine(0f, 16f, 479f, 16f, paint)//holizon
        for (i in 1..9) {
            canvas.drawLine((i * 48).toFloat(), 13f, (i * 48).toFloat(), 19f, paint)
        }
        imgScale!!.setImageBitmap(bmp)
    }

    private fun drawHint1Sub(imgv: ImageView, tgt: Int, color: Int) {
        val bmp = getBitmap(imgv)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        paint.color = color
        paint.strokeWidth = 1f
        for (i in 0 until tgt) {
            canvas.drawRoundRect(getItemRect(i), rRate, rRate, paint)
            //canvas.drawRect(getItemRect(i), paint);
        }

        imgv.setImageBitmap(bmp)
    }

    private fun drawHint2() {
        hint1A!!.visibility = View.GONE
        hint1A2!!.visibility = View.GONE
        hint1B!!.visibility = View.GONE
        //drawHint1Sub(hint1_b, 0, Color.GREEN);
        val wkView = if (calcTypeIsRiseUp()) hint1A2 else hint1A
        wkView!!.visibility = View.VISIBLE

        val bmp = getBitmap(wkView)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        var ttl = 0
        paint.color = Color.BLUE
        paint.strokeWidth = 2f
        for (i in 0 until tgtA) {
            canvas.drawRoundRect(getItemRect(ttl), rRate, rRate, paint)
            //canvas.drawRect(getItemRect(ttl), paint);
            //System.out.println("★★" + r.left + ":" + r.right);
            ttl++
        }

        paint.color = Color.GREEN
        if (calcTypeIsAdd()) {
            for (i in 0 until tgtB) {
                canvas.drawRoundRect(getItemRect(ttl), rRate, rRate, paint)
                //canvas.drawRect(getItemRect(ttl), paint);
                ttl++
            }
            if (calcTypeIsRiseUp()) {
                paint.style = Paint.Style.STROKE
                for (i in 0 until 10 - tgtA) {
                    val r = getItemRect(ttl)
                    canvas.drawRoundRect(r, rRate, rRate, paint)
                    //canvas.drawRect(r, paint);
                    val stx = ((r.left + r.right) / 2).toInt()
                    val sty = ((r.top + r.bottom) / 2).toInt()
                    val r2 = getItemRect(tgtA + i)
                    val edx = ((r2.left + r2.right) / 2).toInt()
                    val edy = ((r2.top + r2.bottom) / 2).toInt()
                    paint.strokeWidth = 2f
                    paint.color = Color.RED
                    canvas.drawLine(stx.toFloat(), sty.toFloat(), edx.toFloat(), edy.toFloat(), paint)
                    paint.strokeWidth = 2f
                    paint.color = Color.GREEN
                    ttl++
                }
                paint.style = Paint.Style.FILL_AND_STROKE
            }
        } else {
            paint.strokeWidth = 5f
            val isRu = calcTypeIsRiseUp()
            val offset = if (isRu) tgtA - 10 else 0
            for (i in tgtA - 1 downTo tgtA - tgtB) {
                val r = getItemRect(i - offset)
                canvas.drawLine(r.left, r.top, r.right, r.bottom, paint)
                canvas.drawLine(r.left, r.bottom, r.right, r.top, paint)
            }
        }

        wkView.setImageBitmap(bmp)
    }

    private fun getItemRect(`val`: Int): RectF {
        val r = RectF()
        val margin = 3
        r.top = (`val` / 10 * 48 + margin).toFloat()
        r.bottom = r.top + 48 - margin * 2
        r.left = (`val` % 10 * 48 + margin).toFloat()
        r.right = r.left + 48 - margin * 2
        return r
    }

    fun clearHint() {
        clearImageView(hint1A)
        clearImageView(hint1A2)
        clearImageView(hint1B)
        clearImageView(imgScale)
        //clearImageView(hint2);
        hintstep = 0
    }

    private fun clearImageView(imgv: ImageView?) {
        val rectBitmap = getBitmap(imgv!!)

        val canvas = Canvas(rectBitmap)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        imgv.setImageBitmap(rectBitmap)
    }

    private fun getBitmap(imgv: ImageView): Bitmap {
        val imageVW = imgv.width
        val imageVH = imgv.height
        return Bitmap.createBitmap(imageVW, imageVH, Bitmap.Config.ARGB_8888)
    }

    private fun prepareView() {
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.style = Paint.Style.FILL_AND_STROKE

        lblQ = findViewById<View>(R.id.txt_fomula) as TextView
        lblAnswer = findViewById<View>(R.id.lbl_answer) as TextView

        btnAnswer = findViewById<View>(R.id.btn_answer) as Button
        val btnAnsListener = OnClickListener { onAnswerBtnClick() }
        btnAnswer!!.setOnClickListener(btnAnsListener)

        btnClear = findViewById<View>(R.id.btn_clear) as Button
        val btnClearListener = OnClickListener { onClearBtnClick() }
        btnClear!!.setOnClickListener(btnClearListener)

        btnPass = findViewById<View>(R.id.btn_pass) as Button
        val btnPassListener = OnClickListener { onPassBtnClick() }
        btnPass!!.setOnClickListener(btnPassListener)

        btn0 = findViewById<View>(R.id.btn_0) as Button
        btn1 = findViewById<View>(R.id.btn_1) as Button
        btn2 = findViewById<View>(R.id.btn_2) as Button
        btn3 = findViewById<View>(R.id.btn_3) as Button
        btn4 = findViewById<View>(R.id.btn_4) as Button
        btn5 = findViewById<View>(R.id.btn_5) as Button
        btn6 = findViewById<View>(R.id.btn_6) as Button
        btn7 = findViewById<View>(R.id.btn_7) as Button
        btn8 = findViewById<View>(R.id.btn_8) as Button
        btn9 = findViewById<View>(R.id.btn_9) as Button
        val btnListener = OnClickListener { arg0 ->
            val wk = lblAnswer!!.text.toString()
            if (wk.length < 2) {
                lblAnswer!!.text = wk + (arg0 as Button).text
            }
        }
        btn0!!.setOnClickListener(btnListener)
        btn1!!.setOnClickListener(btnListener)
        btn2!!.setOnClickListener(btnListener)
        btn3!!.setOnClickListener(btnListener)
        btn4!!.setOnClickListener(btnListener)
        btn5!!.setOnClickListener(btnListener)
        btn6!!.setOnClickListener(btnListener)
        btn7!!.setOnClickListener(btnListener)
        btn8!!.setOnClickListener(btnListener)
        btn9!!.setOnClickListener(btnListener)

        btnHint1 = findViewById<View>(R.id.btn_hint1) as Button
        btnHint2 = findViewById<View>(R.id.btn_hint2) as Button
        val hintBtnListener = OnClickListener { arg0 -> onHintBtnClick(arg0) }
        btnHint1!!.setOnClickListener(hintBtnListener)
        btnHint2!!.setOnClickListener(hintBtnListener)

        hint1A = this.findViewById<View>(R.id.img_hint1_a) as ImageView
        hint1A2 = this.findViewById<View>(R.id.img_hint1_a2) as ImageView
        hint1B = this.findViewById<View>(R.id.img_hint1_b) as ImageView
        //hint2=(ImageView)this.findViewById(R.id.img_hint2);
        imgScale = this.findViewById<View>(R.id.img_scale) as ImageView
    }

    companion object {
        //private static final int CALCTYP_ADD = 0;
        //private static final int CALCTYP_SUB = 1;
        private val CALCMARK = arrayOf(" + ", " - ")

        private val PREFKEY_CALCTYPE = "ArithmeticCalcType"
        private val CALCTYPKEY_EASYADD = "ctyp_easyadd"
        private val CALCTYPKEY_EASYSUB = "ctyp_easysub"
        private val CALCTYPKEY_RISEUPADD = "ctyp_riseupadd"
        private val CALCTYPKEY_FALLDOWNSUB = "ctyp_falldownsub"
    }
}
