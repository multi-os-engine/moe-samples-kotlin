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

package org.moe.kotlinrssreader

import org.moe.natj.general.Pointer
import org.moe.natj.general.ann.Generated
import org.moe.natj.general.ann.Owned
import org.moe.natj.general.ann.RegisterOnStartup
import org.moe.natj.general.ann.Runtime
import org.moe.natj.objc.ObjCRuntime
import org.moe.natj.objc.ann.ObjCClassName
import org.moe.natj.objc.ann.Property
import org.moe.natj.objc.ann.Selector
import ios.uikit.UILabel
import ios.uikit.UITableViewCell
import ios.uikit.UITextView

@Generated
@Runtime(ObjCRuntime::class)
@ObjCClassName("RSSCell")
@RegisterOnStartup
class RSSCell
@Generated
protected constructor(peer: Pointer) : UITableViewCell(peer) {

    @Generated
    @Selector("init")
    external override fun init(): RSSCell

    @Generated
    @Property
    @Selector("rssDate")
    external fun rssDate(): UILabel

    @Generated
    @Property
    @Selector("rssDescription")
    external fun rssDescription(): UITextView

    @Generated
    @Property
    @Selector("rssTitle")
    external fun rssTitle(): UILabel

    @Generated
    @Property
    @Selector("setRssDate:")
    external fun setRssDate(value: UILabel)

    @Generated
    @Property
    @Selector("setRssDescription:")
    external fun setRssDescription(value: UITextView)

    @Generated
    @Property
    @Selector("setRssTitle:")
    external fun setRssTitle(value: UILabel)

    companion object {

        @Generated
        @Owned
        @Selector("alloc")
        @JvmStatic external fun alloc(): RSSCell
    }
}