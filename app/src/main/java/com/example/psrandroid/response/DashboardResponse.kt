package com.example.psrandroid.response

import com.google.gson.annotations.SerializedName

data class DashboardResponse(
    val data: DashboardData,
    val message: String,
    val status: Boolean
)
data class DashboardData(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("created_by")
    val createdBy: String,
    val description: String,
    val id: Int,
    val location: Location,
    @SerializedName("location_id")
    val locationId: String,
    val metals: List<MetalData>,
    val name: String,
    @SerializedName("sheet_date")
    val sheetDate: String,
    @SerializedName("sheet_meta")
    val sheetMeta: List<SheetMeta>,
    @SerializedName("sheet_type")
    val sheetType: String,
    val status: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("urdu_name")
    val urduName: String
)
data class Location(
    @SerializedName("created_at")
    val createdAt: String,
    val id: Int,
    val name: String,
    val slug: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("urdu_name")
    val urduName: String
)

data class MetalData(
    @SerializedName("created_at")
    val createdAt: String,
    val description: String,
    val id: Int,
    @SerializedName("metal_type")
    val metalType: List<MetalType>,
    val name: String,
    val slug: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("urdu_name")
    val urduName: String
)
data class MetalType(
    @SerializedName("created_at")
    val createdAt: String,
    val description: String,
    val id: Int,
    @SerializedName("metal_id")
    val metalId: String,
    val name: String,
    val slug: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("urdu_name")
    val urduName: String
)
data class SheetMeta(
    @SerializedName("buy_price")
    val buyPrice: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("general_price")
    val generalPrice: String,
    val id: Int,
    @SerializedName("metail_type_id")
    val metailTypeId: String,
    @SerializedName("selling_price")
    val sellingPrice: String,
    @SerializedName("sheet_id")
    val sheetId: String,
    @SerializedName("unit_detail")
    val unitDetail: String,
    @SerializedName("updated_at")
    val updatedAt: String
)