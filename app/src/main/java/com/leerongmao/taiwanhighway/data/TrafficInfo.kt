package com.leerongmao.taiwanhighway.data

import java.io.Serializable

class TrafficInfo : ArrayList<TrafficInfoItem>()

class TrafficInfoItem(
    val UID: String,
    val areaNm: String,
    val comment: String,
    val direction: String,
    val happendate: String,
    val happentime: String,
    val modDttm: String,
    val region: String,
    val road: String,
    val roadtype: String,
    val srcdetail: String,
    val x1: String,
    val y1: String
    ):Serializable

class TrafficResponse(
    val result: TrafficInfo
)
