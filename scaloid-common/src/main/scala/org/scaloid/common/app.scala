/* 
 *
 * 
 *
 *
 * Less painful Android development with Scala
 *
 * http://scaloid.org
 *
 *
 *
 *
 *
 *
 * Copyright 2013 Sung-Ho Lee
 *
 * Sung-Ho Lee licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

/*
 * This file is automatically generated. Any changes on this file will be overwritten!
 */

package org.scaloid.common

import android.app._
import android.content._
import android.os._
import android.view._
import android.view.WindowManager.LayoutParams._
import scala.collection.mutable.ArrayBuffer
import Implicits._


trait TraitActivity[V <: Activity] {

  @inline def contentView_=(p: View) = {
    basis.setContentView(p)
    basis
  }

  @inline def contentView(p: View) = contentView_=(p)

  @noEquivalentGetterExists
  @inline def contentView(implicit no: NoGetterForThisProperty): Nothing = throw new Error("Android does not support the getter for 'contentView'")

  def basis: Activity

  def find[V <: View](id: Int): V = basis.findViewById(id).asInstanceOf[V]

  def runOnUiThread (f: => Unit)  {
    if(uiThread == Thread.currentThread) {
      f
    } else {
      handler.post(new Runnable() {
        def run() {
          f
        }
      })
    }
  }
}

trait SActivity extends Activity with TraitContext[android.content.Context] with TraitActivity[SActivity] with Destroyable with Creatable with Registerable {

  def basis = this
  override implicit val ctx = this

  def onRegister(body: => Any) = onResume(body)
  def onUnregister(body: => Any) = onPause(body)

  val onStartStop = new Registerable {
    def onRegister(body: => Any) = onStart(body)
    def onUnregister(body: => Any) = onStop(body)
  }

  val onCreateDestroy = new Registerable {
    def onRegister(body: => Any) = onCreate(body)
    def onUnregister(body: => Any) = onDestroy(body)
  }

  protected override def onCreate(b: Bundle) {
    super.onCreate(b)
    onCreateBodies.foreach(_ ())
  }

  override def onStart {
    super.onStart()
    onStartBodies.foreach(_ ())
  }

  protected val onStartBodies = new ArrayBuffer[() => Any]

  def onStart(body: => Any) = {
    val el = (() => body)
    onStartBodies += el
    el
  }

  override def onResume {
    super.onResume()
    onResumeBodies.foreach(_ ())
  }

  protected val onResumeBodies = new ArrayBuffer[() => Any]

  def onResume(body: => Any) = {
    val el = (() => body)
    onResumeBodies += el
    el
  }

  override def onPause {
    onPauseBodies.foreach(_ ())
    super.onPause()
  }

  protected val onPauseBodies = new ArrayBuffer[() => Any]

  def onPause(body: => Any) = {
    val el = (() => body)
    onPauseBodies += el
    el
  }

  override def onStop {
    onStopBodies.foreach(_ ())
    super.onStop()
  }

  protected val onStopBodies = new ArrayBuffer[() => Any]

  def onStop(body: => Any) = {
    val el = (() => body)
    onStopBodies += el
    el
  }

  override def onDestroy {
    onDestroyBodies.foreach(_ ())
    super.onDestroy()
  }
}

/**
 * Follows a parent's action of onBackPressed().
 * When an activity is a tab that hosted by TabActivity, you may want a common back-button action for each tab.
 *
 * Please refer http://stackoverflow.com/questions/2796050/key-events-in-tabactivities
 */
trait FollowParentBackButton extends SActivity {
  override def onBackPressed() {
    val p = getParent
    if (p != null) p.onBackPressed()
  }
}

/**
 * Turn screen on and show the activity even if the screen is locked.
 * This is useful when notifying some important information.
 */
trait ScreenOnActivity extends SActivity {
  onCreate {
    getWindow.addFlags(FLAG_DISMISS_KEYGUARD | FLAG_SHOW_WHEN_LOCKED | FLAG_TURN_SCREEN_ON)
  }
}


class RichService[V <: android.app.Service](val basis: V) extends TraitService[V]

trait TraitService[V <: android.app.Service] extends TraitContextWrapper[V] with TraitContext[V] with Destroyable with Creatable with Registerable {


  def onRegister(body: => Any) = onCreate(body)
  def onUnregister(body: => Any) = onDestroy(body)

  def onCreate() {
    super.onCreate()
    onCreateBodies.foreach(_ ())
  }

  def onDestroy() {
    onDestroyBodies.foreach(_ ())
    super.onDestroy()
  }

  @inline def application = basis.getApplication


}



trait LocalService extends TraitService[android.app.Service] {
  private val binder = new ScaloidServiceBinder

  def onBind(intent: Intent): IBinder = binder

  class ScaloidServiceBinder extends Binder {
    def service: LocalService = LocalService.this
  }

}


class AlertDialogBuilder(_title: CharSequence = null, _message: CharSequence = null)(implicit context: Context) extends AlertDialog.Builder(context) {
  if (_title != null) setTitle(_title)
  if (_message != null) setMessage(_message)


  @inline def positiveButton(name: CharSequence = android.R.string.yes, onClick: => Unit = {}): AlertDialogBuilder =
    positiveButton(name, (_, _) => {
      onClick
    })

  @inline def positiveButton(name: CharSequence, onClick: (DialogInterface, Int) => Unit): AlertDialogBuilder = {
    setPositiveButton(name, func2DialogOnClickListener(onClick))
    this
  }

  @inline def neutralButton(name: CharSequence = android.R.string.ok, onClick: => Unit = {}): AlertDialogBuilder =
    neutralButton(name, (_, _) => {
      onClick
    })

  @inline def neutralButton(name: CharSequence, onClick: (DialogInterface, Int) => Unit): AlertDialogBuilder = {
    setNeutralButton(name, func2DialogOnClickListener(onClick))
    this
  }

  @inline def negativeButton(name: CharSequence, onClick: => Unit): AlertDialogBuilder =
    negativeButton(name, (_, _) => {
      onClick
    })

  @inline def negativeButton(name: CharSequence = android.R.string.no, onClick: (DialogInterface, Int) => Unit = (d, _) => {
    d.cancel()
  }): AlertDialogBuilder = {
    setNegativeButton(name, func2DialogOnClickListener(onClick))
    this
  }

  var tit: CharSequence = null

  @inline def title_=(str: CharSequence) = {
    tit = str
    setTitle(str)
  }

  @inline def title = tit

  var msg: CharSequence = null

  @inline def message_=(str: CharSequence) = {
    tit = str
    setMessage(str)
  }

  @inline def message = tit

  override def show():AlertDialog = runOnUiThread(super.show())
}
