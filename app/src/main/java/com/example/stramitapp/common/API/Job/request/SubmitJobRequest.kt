package com.example.stramitapp.common.API.Job.request

import com.example.stramitapp.model.JobAssetTrackInfo

data class SubmitJobRequest(
    val userId: Int,
    val currentDeviceUdid: String?,
    val jobId: Int,
    val tblJobAssetTrackInfo: List<JobAssetTrackInfo>

    //val JobAssets jobAssets { get; set; }
    //[Serializable]
    // class JobAssets
    //{
    //    [XmlArrayItem(ElementName = "tblJobAssetTrackInfo")]
    //    [JsonProperty("tblJobAssetTrackInfo")]
    //    val tblJobAssetTrackInfo: List<JobAssetTrackInfo>
    //}
)