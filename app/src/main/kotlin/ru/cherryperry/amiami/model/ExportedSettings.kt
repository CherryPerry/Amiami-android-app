package ru.cherryperry.amiami.model

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class ExportedSettings {

    @SerializedName("highlight")
    var highlight: ArrayList<String>? = null
}