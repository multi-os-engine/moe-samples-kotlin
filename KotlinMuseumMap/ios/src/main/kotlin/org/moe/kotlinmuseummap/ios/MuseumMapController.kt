package org.moe.kotlinmuseummap.ios

import org.moe.natj.general.Pointer
import org.moe.natj.objc.SEL
import org.moe.natj.objc.ann.Selector
import org.moe.kotlinmuseummap.common.MuseumSearchEngine
import org.moe.kotlinmuseummap.common.model.Museum
import org.moe.kotlinmuseummap.common.model.db.DataSource
import org.moe.kotlinmuseummap.ios.db.SQLiteDatabaseHelper
import ios.c.Globals
import ios.corelocation.struct.CLLocationCoordinate2D
import ios.foundation.NSArray
import ios.foundation.NSDictionary
import ios.foundation.NSMutableArray
import ios.foundation.NSMutableDictionary
import ios.mapkit.MKAnnotationView
import ios.mapkit.MKMapView
import ios.mapkit.MKPinAnnotationView
import ios.mapkit.MKPointAnnotation
import ios.mapkit.protocol.MKAnnotation
import ios.mapkit.protocol.MKMapViewDelegate
import ios.uikit.*
import ios.uikit.enums.*
import ios.uikit.protocol.UITextFieldDelegate

class MuseumMapController protected constructor(peer: Pointer) : UIViewController(peer) {

    private var pinLabelField: UITextField? = null
    private var updaterThread: Thread? = null
    private var mapView: MKMapView? = null
    private var source: DataSource? = null

    private val views :NSMutableDictionary<Any?, Any?> = NSMutableDictionary.alloc().init() as NSMutableDictionary<Any?, Any?>
    private val constraints :NSMutableArray<NSLayoutConstraint> = NSMutableArray.alloc().init() as NSMutableArray<NSLayoutConstraint>

    private val tfdelegate = object : UITextFieldDelegate {

        override fun textFieldShouldReturn(textField: UITextField?): Boolean {
            textField!!.resignFirstResponder()
            return false
        }
    }

    override external fun init(): MuseumMapController

    override fun viewDidLoad() {
        super.viewDidLoad()

        source = DataSource(SQLiteDatabaseHelper("local.db"))
        source!!.open()

        setTitle("Museum Map")

        setEdgesForExtendedLayout(UIRectEdge.None)
        view().setBackgroundColor(UIColor.whiteColor())

        views.put("bottomGuide", this.bottomLayoutGuide())
        views.put("topGuide", this.topLayoutGuide())

        mapView = MKMapView.alloc().init()
        mapView!!.setTranslatesAutoresizingMaskIntoConstraints(false)
        view().addSubview(mapView)
        views.put("map", mapView)

        pinLabelField = UITextField.alloc().init()
        pinLabelField!!.setTranslatesAutoresizingMaskIntoConstraints(false)
        view().addSubview(pinLabelField)
        views.put("pinLabel", pinLabelField)

        mapView!!.setDelegate(object : MKMapViewDelegate {

            override fun mapViewDidSelectAnnotationView(mapView: MKMapView?,
                                                        view: MKAnnotationView?) {
                val ann = view!!.annotation() as MKPointAnnotation
                pinLabelField!!.setText(ann.title())
                val pa = view as MKPinAnnotationView?
                pa!!.setPinTintColor(MKPinAnnotationView.greenPinColor())
            }

            override fun mapViewDidDeselectAnnotationView(mapView: MKMapView?,
                                                          view: MKAnnotationView?) {
                val pa = view as MKPinAnnotationView?
                pinLabelField!!.setText("")
                pa!!.setPinTintColor(MKPinAnnotationView.redPinColor())
            }

            override fun mapViewViewForAnnotation(mapView: MKMapView?,
                                                  annotation: Any?): MKAnnotationView {
                var view = mapView!!.dequeueReusableAnnotationViewWithIdentifier(PointAnnotationViewID) as MKPinAnnotationView?
                if (view == null) {
                    view = MKPinAnnotationView.alloc().initWithAnnotationReuseIdentifier(annotation as MKAnnotation?,
                            PointAnnotationViewID)
                } else {
                    view.setAnnotation(annotation as MKAnnotation?)
                }
                view!!.setAnimatesDrop(true)
                return view
            }

        })

        view().addSubview(mapView)

        pinLabelField!!.setReturnKeyType(UIReturnKeyType.Done)
        pinLabelField!!.setBorderStyle(UITextBorderStyle.RoundedRect)
        pinLabelField!!.setDelegate(tfdelegate)
        view().addSubview(pinLabelField)

        val addPinBtn = UIButton.buttonWithType(UIButtonType.RoundedRect)
        addPinBtn.setTranslatesAutoresizingMaskIntoConstraints(false)
        addPinBtn.setTitleForState("+", UIControlState.Normal)
        views.put("addPinBtn", addPinBtn)
        view().addSubview(addPinBtn)

        addPinBtn.addTargetActionForControlEvents(this, SEL(
                "annotationAction:"), UIControlEvents.TouchUpInside)
        view().addSubview(addPinBtn)

        val remPinBtn = UIButton.buttonWithType(UIButtonType.RoundedRect)
        remPinBtn.setTranslatesAutoresizingMaskIntoConstraints(false)
        remPinBtn.setTitleForState("-", UIControlState.Normal)
        views.put("remPinBtn", remPinBtn)
        view().addSubview(remPinBtn)

        remPinBtn.addTargetActionForControlEvents(this, SEL(
                "annotationAction:"), UIControlEvents.TouchUpInside)
        view().addSubview(remPinBtn)

        val updatePinBtn = UIButton.buttonWithType(UIButtonType.RoundedRect)
        updatePinBtn.setTranslatesAutoresizingMaskIntoConstraints(false)
        updatePinBtn.setTitleForState("Update", UIControlState.Normal)
        views.put("updatePinBtn", updatePinBtn)
        view().addSubview(updatePinBtn)

        updatePinBtn.addTargetActionForControlEvents(this, SEL(
                "annotationAction:"), UIControlEvents.TouchUpInside)
        view().addSubview(updatePinBtn)

        constraints.addObjectsFromArray(NSLayoutConstraint.constraintsWithVisualFormatOptionsMetricsViews("H:|-2-[pinLabel]-2-|",
                0, null, views as NSDictionary<String, Any>) as NSArray<NSLayoutConstraint>)
        constraints.addObjectsFromArray(NSLayoutConstraint.constraintsWithVisualFormatOptionsMetricsViews("H:|-[updatePinBtn]-[addPinBtn]-[remPinBtn]-|",
                0, null, views) as NSArray<NSLayoutConstraint>)
        constraints.addObjectsFromArray(NSLayoutConstraint.constraintsWithVisualFormatOptionsMetricsViews("H:|-0-[map]-0-|",
                0, null, views) as NSArray<NSLayoutConstraint>)
        constraints.addObjectsFromArray(NSLayoutConstraint.constraintsWithVisualFormatOptionsMetricsViews("V:[topGuide]-[pinLabel]-[updatePinBtn]-[map]-0-[bottomGuide]",
                0, null, views) as NSArray<NSLayoutConstraint>)
        constraints.addObjectsFromArray(NSLayoutConstraint.constraintsWithVisualFormatOptionsMetricsViews("V:[topGuide]-[pinLabel]-[addPinBtn]-[map]-0-[bottomGuide]",
                0, null, views) as NSArray<NSLayoutConstraint>)
        constraints.addObjectsFromArray(NSLayoutConstraint.constraintsWithVisualFormatOptionsMetricsViews("V:[topGuide]-[pinLabel]-[remPinBtn]-[map]-0-[bottomGuide]",
                0, null, views) as NSArray<NSLayoutConstraint>)

        view().addConstraints(constraints as NSArray<NSLayoutConstraint>)
        view().layoutSubviews()

        val museums = source!!.allMuseum
        if (museums != null) {
            for (museum in museums!!) {
                val pa = MKPointAnnotation.alloc().init()
                pa.setTitle(museum.name)
                pa.setCoordinate(CLLocationCoordinate2D(museum.latitude, museum.longitude))
                mapView!!.addAnnotation(pa)
            }
        }
    }

    @Selector("annotationAction:")
    fun annotationAction(sender: UIButton) {
        if (sender.titleLabel().text() == "+") {
            val pa = MKPointAnnotation.alloc().init()
            pa.setTitle(pinLabelField!!.text())
            pa.setCoordinate(mapView!!.centerCoordinate())
            mapView!!.addAnnotation(pa)
            source!!.createMuseum(Museum(pa.title(), pa.coordinate().latitude(),
                    pa.coordinate().longitude()))
        } else if (sender.titleLabel().text() == "-") {
            val annotations = mapView!!.selectedAnnotations() as NSArray<MKPointAnnotation>
            for (i in annotations.indices) {
                val annotation = annotations[i]
                val museums = source!!.getMuseumsByAllParameters(annotation.title(),
                        annotation.coordinate().latitude(), annotation.coordinate().longitude())
                for (museum in museums) {
                    source!!.deleteMuseum(museum.id)
                }
            }
            mapView!!.removeAnnotations(annotations)
            pinLabelField!!.setText("")
        } else {
            if (updaterThread != null) {
                return
            }

            updaterThread = Thread(object : Runnable {

                override fun run() {
                    try {
                        val centerCoordinate = mapView!!.centerCoordinate()
                        val museums = MuseumSearchEngine.find(centerCoordinate.latitude(), centerCoordinate.longitude())
                        if (museums == null) {
                            handleError(MuseumSearchEngine.lastError)
                            return
                        }
                        for(i in 0..museums!!.size - 1) {
                            var existMuseums = source!!.getMuseumsByAllParameters(museums!![i].name,
                                    museums!![i].latitude,
                                    museums!![i].longitude)
                            if (existMuseums.size !== 0) {
                                museums!![i].id = existMuseums[0].id
                            }
                        }

                        Globals.dispatch_sync(Globals.dispatch_get_main_queue(), object : Globals.Block_dispatch_sync {

                            override fun call_dispatch_sync() {
                                for (museum in museums!!) {
                                    if (museum.id == -1) {
                                        val pa = MKPointAnnotation.alloc().init()
                                        pa.setTitle(museum.name)
                                        pa.setCoordinate(CLLocationCoordinate2D(museum.latitude, museum.longitude))
                                        mapView!!.addAnnotation(pa)
                                    }
                                    source!!.createMuseum(museum)
                                }
                            }
                        })
                    } finally {
                        updaterThread = null
                    }
                }
            })
            updaterThread!!.start()
        }
    }

    private fun handleError(errorMessage: String?) {
        val alertView = UIAlertView.alloc().init()
        alertView.setMessage("Cannot get museums: " + errorMessage)
        alertView.show()
    }

    companion object {

        private val PointAnnotationViewID = "PointAnnotation"

        @JvmStatic external fun alloc(): MuseumMapController
    }

}