package com.leerongmao.taiwanhighway.data

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader

object CameraXmlParser {

    fun parse(xml: String): List<CameraInfo> {
        val cameras = mutableListOf<CameraInfo>()

        val parser = Xml.newPullParser()
        parser.setInput(StringReader(xml))

        var eventType = parser.eventType

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_TAG &&
                parser.name == "CCTV"
            ) {
                readCamera(parser)?.let { cameras.add(it) }
            }

            eventType = parser.next()
        }

        return cameras
    }

    private fun readCamera(parser: XmlPullParser): CameraInfo? {

        var id = ""
        var roadName = ""
        var direction = ""
        var locationMile = ""
        var videoStreamUrl = ""
        var latitude: Double? = null
        var longitude: Double? = null

        while (parser.next() != XmlPullParser.END_DOCUMENT) {

            if (parser.eventType == XmlPullParser.END_TAG &&
                parser.name == "CCTV"
            ) {
                break
            }

            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "CCTVID" -> id = parser.nextText()
                "RoadName" -> roadName = parser.nextText()
                "RoadDirection" -> direction = parser.nextText()
                "LocationMile" -> locationMile = parser.nextText()
                "VideoStreamURL" -> videoStreamUrl = parser.nextText()
                "PositionLat" -> latitude =
                    parser.nextText().toDoubleOrNull()
                "PositionLon" -> longitude =
                    parser.nextText().toDoubleOrNull()
            }
        }

        if (id.isBlank() || videoStreamUrl.isBlank()) {
            return null
        }

        return CameraInfo(
            id = id,
            roadName = roadName,
            direction = direction,
            locationMile = locationMile,
            videoStreamUrl = videoStreamUrl,
            latitude = latitude,
            longitude = longitude
        )
    }
}