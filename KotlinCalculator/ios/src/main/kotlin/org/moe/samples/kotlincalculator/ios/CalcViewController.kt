// Copyright (c) 2015, Intel Corporation
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above
// copyright notice, this list of conditions and the following disclaimer
// in the documentation and/or other materials provided with the
// distribution.
// 3. Neither the name of the copyright holder nor the names of its
// contributors may be used to endorse or promote products derived from
// this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.moe.samples.kotlincalculator.ios

import org.moe.natj.general.Pointer
import org.moe.natj.general.ann.RegisterOnStartup
import org.moe.natj.objc.ObjCRuntime
import org.moe.natj.objc.ann.ObjCClassName
import org.moe.natj.objc.ann.Property
import org.moe.natj.objc.ann.Selector
import org.moe.samples.kotlincalculator.common.CalculatorAdapter
import ios.NSObject
import ios.uikit.UIButton
import ios.uikit.UIColor
import ios.uikit.UILabel
import ios.uikit.UIViewController

@org.moe.natj.general.ann.Runtime(ObjCRuntime::class)
@ObjCClassName("CalcViewController")
@RegisterOnStartup
class CalcViewController protected constructor(peer: Pointer) : UIViewController(peer) {

    @Selector("init")
    external override fun init(): CalcViewController

    private var myCalculatorAdapter: CalculatorAdapter? = null

    val calcNum: UILabel?
        get() = getCalcNumSel()
    val calcBtnC: UIButton?
        get() = getCalcBtnCSel()
    val calcBtnPm: UIButton?
        get() = getCalcBtnPmSel()
    val calcBtnProc: UIButton?
        get() = getCalcBtnProcSel()
    val calcBtnDevide: UIButton?
        get() = getCalcBtnDevideSel()

    val calcBtn7: UIButton?
        get() = getCalcBtn7Sel()
    val calcBtn8: UIButton?
        get() = getCalcBtn8Sel()
    val calcBtn9: UIButton?
        get() = getCalcBtn9Sel()
    val calcBtnUmn: UIButton?
        get() = getCalcBtnUmnSel()

    val calcBtn4: UIButton?
        get() = getCalcBtn4Sel()
    val calcBtn5: UIButton?
        get() = getCalcBtn5Sel()
    val calcBtn6: UIButton?
        get() = getCalcBtn6Sel()
    val calcBtnMin: UIButton?
        get() = getCalcBtnMinSel()

    val calcBtn1: UIButton?
        get() = getCalcBtn1Sel()
    val calcBtn2: UIButton?
        get() = getCalcBtn2Sel()
    val calcBtn3: UIButton?
        get() = getCalcBtn3Sel()
    val calcBtnPlus: UIButton?
        get() = getCalcBtnPlusSel()

    val calcBtn0: UIButton?
        get() = getCalcBtn0Sel()
    val calcBtn0Fake: UIButton?
        get() = getCalcBtn0FakeSel()
    val calcBtnDote: UIButton?
        get() = getCalcBtnDoteSel()
    val calcBtnEquel: UIButton?
        get() = getCalcBtnEquelSel()

    override fun viewDidLoad() {
        myCalculatorAdapter = CalculatorAdapter()

        calcNum!!.setText("0")
        calcBtnC!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtnPm!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtnProc!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtnDevide!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (75 / 255.0).toFloat().toDouble(), (141 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtn7!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtn8!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtn9!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtnUmn!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (75 / 255.0).toFloat().toDouble(), (141 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtn4!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtn5!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtn6!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtnMin!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (75 / 255.0).toFloat().toDouble(), (141 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtn1!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtn2!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtn3!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtnPlus!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (75 / 255.0).toFloat().toDouble(), (141 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtn0!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtn0Fake!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtnDote!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtnEquel!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (75 / 255.0).toFloat().toDouble(), (141 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))

    }

    @Selector("calc_num")
    @Property
    external fun getCalcNumSel(): UILabel

    @Selector("calc_btn_c")
    @Property
    external fun getCalcBtnCSel(): UIButton

    @Selector("calc_btn_pm")
    @Property
    external fun getCalcBtnPmSel(): UIButton

    @Selector("calc_btn_proc")
    @Property
    external fun getCalcBtnProcSel(): UIButton

    @Selector("calc_btn_devide")
    @Property
    external fun getCalcBtnDevideSel(): UIButton

    @Selector("calc_btn_7")
    @Property
    external fun getCalcBtn7Sel(): UIButton

    @Selector("calc_btn_8")
    @Property
    external fun getCalcBtn8Sel(): UIButton

    @Selector("calc_btn_9")
    @Property
    external fun getCalcBtn9Sel(): UIButton

    @Selector("calc_btn_umn")
    @Property
    external fun getCalcBtnUmnSel(): UIButton

    @Selector("calc_btn_4")
    @Property
    external fun getCalcBtn4Sel(): UIButton

    @Selector("calc_btn_5")
    @Property
    external fun getCalcBtn5Sel(): UIButton

    @Selector("calc_btn_6")
    @Property
    external fun getCalcBtn6Sel(): UIButton

    @Selector("calc_btn_min")
    @Property
    external fun getCalcBtnMinSel(): UIButton

    @Selector("calc_btn_1")
    @Property
    external fun getCalcBtn1Sel(): UIButton

    @Selector("calc_btn_2")
    @Property
    external fun getCalcBtn2Sel(): UIButton

    @Selector("calc_btn_3")
    @Property
    external fun getCalcBtn3Sel(): UIButton

    @Selector("calc_btn_plus")
    @Property
    external fun getCalcBtnPlusSel(): UIButton

    @Selector("calc_btn_0")
    @Property
    external fun getCalcBtn0Sel(): UIButton

    @Selector("calc_btn_0_fake")
    @Property
    external fun getCalcBtn0FakeSel(): UIButton

    @Selector("calc_btn_dote")
    @Property
    external fun getCalcBtnDoteSel(): UIButton

    @Selector("calc_btn_equel")
    @Property
    external fun getCalcBtnEquelSel(): UIButton

    @Selector("BtnPressed_c:")
    fun btnPressedC(sender: NSObject) {
        calcBtnC!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))

    }

    @Selector("BtnPressedCancel_c:")
    fun btnPressedCancelC(sender: NSObject) {
        calcBtnC!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("C")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_pm:")
    fun btnPressedPm(sender: NSObject) {
        calcBtnPm!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_pm:")
    fun btnPressedCancelPm(sender: NSObject) {
        calcBtnPm!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("INVERT")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_proc:")
    fun btnPressedProc(sender: NSObject) {
        calcBtnProc!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_proc:")
    fun btnPressedCancelProc(sender: NSObject) {
        calcBtnProc!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("C")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_devide:")
    fun btnPressedDevide(sender: NSObject) {
        calcBtnDevide!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_devide:")
    fun btnPressedCancelDevide(sender: NSObject) {
        calcBtnDevide!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (75 / 255.0).toFloat().toDouble(), (141 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("/")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_7:")
    fun btnPressed7(sender: NSObject) {
        calcBtn7!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_7:")
    fun btnPressedCancel7(sender: NSObject) {
        calcBtn7!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("7")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_8:")
    fun btnPressed8(sender: NSObject) {
        calcBtn8!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_8:")
    fun btnPressedCancel8(sender: NSObject) {
        calcBtn8!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("8")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_9:")
    fun btnPressed9(sender: NSObject) {
        calcBtn9!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_9:")
    fun btnPressedCancel9(sender: NSObject) {
        calcBtn9!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("9")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_umn:")
    fun btnPressedUmn(sender: NSObject) {
        calcBtnUmn!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_umn:")
    fun btnPressedCancelUmn(sender: NSObject) {
        calcBtnUmn!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (75 / 255.0).toFloat().toDouble(), (141 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("*")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_4:")
    fun btnPressed4(sender: NSObject) {
        calcBtn4!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_4:")
    fun btnPressedCancel4(sender: NSObject) {
        calcBtn4!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("4")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_5:")
    fun btnPressed5(sender: NSObject) {
        calcBtn5!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_5:")
    fun btnPressedCancel5(sender: NSObject) {
        calcBtn5!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("5")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_6:")
    fun btnPressed6(sender: NSObject) {
        calcBtn6!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_6:")
    fun btnPressedCancel6(sender: NSObject) {
        calcBtn6!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("6")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_min:")
    fun btnPressedMin(sender: NSObject) {
        calcBtnMin!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_min:")
    fun btnPressedCancelMin(sender: NSObject) {
        calcBtnMin!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (75 / 255.0).toFloat().toDouble(), (141 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("-")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_1:")
    fun btnPressed1(sender: NSObject) {
        calcBtn1!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_1:")
    fun btnPressedCancel1(sender: NSObject) {
        calcBtn1!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("1")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_2:")
    fun btnPressed2(sender: NSObject) {
        calcBtn2!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_2:")
    fun btnPressedCancel2(sender: NSObject) {
        calcBtn2!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("2")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_3:")
    fun btnPressed3(sender: NSObject) {
        calcBtn3!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_3:")
    fun btnPressedCancel3(sender: NSObject) {
        calcBtn3!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("3")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_plus:")
    fun btnPressedPlus(sender: NSObject) {
        calcBtnPlus!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_plus:")
    fun btnPressedCancelPlus(sender: NSObject) {
        calcBtnPlus!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (75 / 255.0).toFloat().toDouble(), (141 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("+")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_0:")
    fun btnPressed0(sender: NSObject) {
        calcBtn0!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtn0Fake!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_0:")
    fun btnPressedCancel0(sender: NSObject) {
        calcBtn0!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        calcBtn0Fake!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("0")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_dote:")
    fun btnPressedDote(sender: NSObject) {
        calcBtnDote!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_dote:")
    fun btnPressedCancelDote(sender: NSObject) {
        calcBtnDote!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (113 / 255.0).toFloat().toDouble(), (186 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol(".")
        calcNum!!.setText(str)
    }

    @Selector("BtnPressed_equel:")
    fun btnPressedEquel(sender: NSObject) {
        calcBtnEquel!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha((253 / 255.0).toFloat().toDouble(), (184 / 255.0).toFloat().toDouble(), (19 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
    }

    @Selector("BtnPressedCancel_equel:")
    fun btnPressedCancelEquel(sender: NSObject) {
        calcBtnEquel!!.setBackgroundColor(UIColor.colorWithRedGreenBlueAlpha(0.0.toFloat().toDouble(), (75 / 255.0).toFloat().toDouble(), (141 / 255.0).toFloat().toDouble(), 1.0.toFloat().toDouble()))
        val str = myCalculatorAdapter!!.sendNewSymbol("=")
        calcNum!!.setText(str)
    }

    companion object {

        @Selector("alloc")
        @JvmStatic external fun alloc(): CalcViewController
    }
}