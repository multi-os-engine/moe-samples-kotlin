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

package org.moe.samples.kotlincalculator.common

import java.math.BigDecimal
import java.math.RoundingMode

class CalculatorAdapter {
    private var isFirst: Boolean = true
    private var isFractPart: Boolean = false
    private var a: Double = 0.toDouble()
    private var b: Double = 0.toDouble()
    private var type: CalcOpsTypes = CalcOpsTypes.NONE
    var showStr: String = "0"

    private var fractPart: Long = 0

    private var numberOfNulls: Long = 0

    fun sendNewSymbol(symbol: String): String {
        val digit = isDigit(symbol)
        if (java.lang.Double.compare(digit, -1.0) != 0) {
            if (java.lang.Double.compare(a, 0.0) == 0) {
                // to avoid making
                // calculations like
                // 0+2
                type = CalcOpsTypes.NONE
            }

            if (type !== CalcOpsTypes.NONE && java.lang.Double.compare(b, 0.0) == 0 && isFirst)
            // to avoid adding extra
            // digits after choice
            // of operation
            {
                isFirst = false
                numberOfNulls = 0
                isFractPart = false
                fractPart = 0
            }
            if (isFractPart) {
                if (fractPart == 0L) {
                    if (digit.toInt() != 0) {
                        fractPart += digit.toInt().toLong()
                    } else {
                        numberOfNulls += 1
                    }
                } else {
                    val prevFractPart = fractPart
                    fractPart = fractPart * 10 + digit.toInt()
                    // Check overflow:
                    if (fractPart < 0) {
                        fractPart = prevFractPart
                    }
                }
            }

            if (isFirst) {
                if (!isFractPart) {
                    a = a * 10 + digit
                    showStr = a.toLong().toString()
                } else {
                    var fractStr = "."
                    for (i in 0..numberOfNulls - 1) {
                        fractStr += "0"
                    }
                    var aLessZero = false
                    if (a < 0)
                        aLessZero = true
                    a = java.lang.Double.parseDouble("${a.toInt()}${fractStr}${fractPart}")
                    if (a > 0 && aLessZero)
                        a = -a
                    showStr = a.toString()
                }
                if (fractPart == 0L && isFractPart) {
                    for (i in 1..numberOfNulls - 1) {
                        showStr += "0"
                    }
                }
                if (fractPart != 0L) {
                    var fractNulls: Long = 0
                    while (fractPart % Math.pow(10.0, (fractNulls + 1).toDouble()).toLong() == 0L) {
                        showStr += "0"
                        fractNulls++
                    }
                }
            } else {
                if (!isFractPart) {
                    b = b * 10 + digit
                    showStr = a.toString() + opToString(type) + b.toLong().toString()
                } else {
                    var fractStr = "."
                    for (i in 0..numberOfNulls - 1) {
                        fractStr += "0"
                    }
                    b = java.lang.Double.parseDouble("${b.toInt()}${fractStr}${fractPart}")
                    showStr = a.toString() + opToString(type) + b.toString()
                }

                if (fractPart == 0L && isFractPart) {
                    for (i in 1..numberOfNulls - 1) {
                        showStr += "0"
                    }
                }

                if (fractPart != 0L) {
                    var fractNulls: Long = 0
                    while (fractPart % Math.pow(10.0, (fractNulls + 1).toDouble()).toLong() == 0L) {
                        showStr += "0"
                        fractNulls++
                    }
                }
            }

        } else {
            val opType = isCalcOperation(symbol)
            if (opType !== CalcOpsTypes.NONE) {
                if (java.lang.Double.compare(a, 0.0) != 0) {
                    if (java.lang.Double.compare(b, 0.0) != 0) {
                        calculateAndPrepare(opType)
                    } else {
                        type = opType
                        showStr = a.toString() + opToString(type) + "0"
                        fractPart = 0
                        isFractPart = false
                    }
                }
            } else {
                if (symbol == "=") {
                    //everything all right
                    if (!isFirst) {
                        calculateAndPrepare(CalcOpsTypes.NONE)
                    }
                } else if (symbol == "C") {
                    //everything all right
                    fractPart = 0
                    a = 0.0
                    b = 0.0
                    numberOfNulls = 0
                    isFractPart = false
                    isFirst = true
                    showStr = "0"
                } else if (symbol == ".") {
                    if (!isFractPart)
                        showStr += "."
                    isFractPart = true
                } else if (symbol == "INVERT" && isFirst) {
                    a = -a
                    showStr = a.toString()
                    if (fractPart == 0L && isFractPart) {
                        for (i in 1..numberOfNulls - 1) {
                            showStr += "0"
                        }
                    }
                    if (fractPart != 0L) {
                        var fractNulls: Long = 0
                        while (fractPart % Math.pow(10.0, (fractNulls + 1).toDouble()).toLong() == 0L) {
                            showStr += "0"
                            fractNulls++
                        }
                    }
                }

            }
        }
        return showStr
    }

    fun isDigit(ss: String): Double {
        var digit = -1.0
        try {
            digit = ss.toDouble()
        } catch (nfe: NumberFormatException) {
            return digit
        }

        return digit
    }

    fun isCalcOperation(ss: String): CalcOpsTypes {
        var myType = CalcOpsTypes.NONE
        if (ss == "+") {
            myType = CalcOpsTypes.SUM
        } else if (ss == "-") {
            myType = CalcOpsTypes.DIFF
        } else if (ss == "*") {
            myType = CalcOpsTypes.MULT
        } else if (ss == "/") {
            myType = CalcOpsTypes.DIVIDE
        }

        return myType
    }

    fun opToString(myType: CalcOpsTypes): String {
        var ss = ""
        when (myType) {
            CalcOpsTypes.DIFF -> ss = "-"
            CalcOpsTypes.DIVIDE -> ss = "/"
            CalcOpsTypes.MULT -> ss = "*"
            CalcOpsTypes.NONE -> {
            }
            CalcOpsTypes.SUM -> ss = "+"
        }
        return ss
    }

    private fun calculateAndPrepare(afterOperation: CalcOpsTypes) {
        var result = CalcOperations.calculate(a, b, type)
        try {
            result = BigDecimal(result).setScale(4, RoundingMode.HALF_UP).toDouble()
            showStr = result.toString()
            when (afterOperation) {
                CalcOpsTypes.SUM -> showStr += "+"
                CalcOpsTypes.DIFF -> showStr += "-"
                CalcOpsTypes.DIVIDE -> showStr += "/"
                CalcOpsTypes.MULT -> showStr += "*"
                CalcOpsTypes.NONE -> {
                }
                else -> {
                }
            }
        } catch (e: NumberFormatException) {
            println("The result is NAN!")
            showStr = result.toString()
        }

        a = result
        b = 0.0
        numberOfNulls = 0
        isFirst = true
        fractPart = (Math.abs(a - a.toInt()) * Math.pow(10.0, (Math.abs(a).toString().length - 2).toDouble())).toInt().toLong()
        type = afterOperation
    }
}